package com.nhn.bullshtml;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import au.com.bytecode.opencsv.CSVReader;

public class BullsHtml {
	public static String enc = new java.io.OutputStreamWriter(System.out).getEncoding();
	public static Map<String, SrcDir> srcMap = new HashMap<String, SrcDir>();
	public static ArrayList<SrcDir> baseList = new ArrayList<SrcDir>();
	public static ArrayList<SrcFile> srcFileList = new ArrayList<SrcFile>();

	public void process() {
		try {
			Pattern rootPathPattern = Pattern.compile("^[a-z]\\:");
			Process covxmlprocess = Runtime.getRuntime().exec("covxml --no-banner");
			InputSource covXmlInputStream = new InputSource(covxmlprocess.getInputStream());
			covXmlInputStream.setEncoding(enc);
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(covXmlInputStream);

			String testcovdir = document.getDocumentElement().getAttribute("dir");
			Process process = Runtime.getRuntime().exec("covsrc --csv --no-banner");
			InputStreamReader reader = new InputStreamReader(process.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(reader);
			CSVReader csvReader = new CSVReader(bufferedReader);
			csvReader.readNext(); // 헤더를 넘긴다.
			String[] lines = null;
			while ((lines = csvReader.readNext()) != null) {
				if (lines[0].equals("Total"))
					continue;
				String fileName = lines[0];
				if (!rootPathPattern.matcher(fileName).find() && !fileName.equals("/")) {
					lines[0] = testcovdir + fileName;
				}
				srcFileList.add(new SrcFile(lines));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void copyResources(String outputFolder) {
		BullsUtil.copyResource(outputFolder + "/js/popup.js", "js/popup.js");
		BullsUtil.copyResource(outputFolder + "/js/sortabletable.js", "js/sortabletable.js");
		BullsUtil.copyResource(outputFolder + "/js/customsorttypes.js", "js/customsorttypes.js");
		BullsUtil.copyResource(outputFolder + "/js/stringbuilder.js", "js/stringbuilder.js");
		BullsUtil.copyResource(outputFolder + "/css/help.css", "css/help.css");
		BullsUtil.copyResource(outputFolder + "/css/main.css", "css/main.css");
		BullsUtil.copyResource(outputFolder + "/css/sortabletable.css", "css/sortabletable.css");
		BullsUtil.copyResource(outputFolder + "/css/source-viewer.css", "css/source-viewer.css");
		BullsUtil.copyResource(outputFolder + "/css/tooltip.css", "css/tooltip.css");
		BullsUtil.copyResource(outputFolder + "/images/blank.png", "images/blank.png");
		BullsUtil.copyResource(outputFolder + "/images/downsimple.png", "images/downsimple.png");
		BullsUtil.copyResource(outputFolder + "/images/upsimple.png", "images/upsimple.png");
		BullsUtil.copyResource(outputFolder + "/index.html", "html/index.html");
		BullsUtil.copyResource(outputFolder + "/help.html", "html/help.html");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BullsHtml bullshtml = new BullsHtml();
		bullshtml.process();
		String path = "test";
		bullshtml.copyResources(path);
		bullshtml.generateHtml(path);
	}

	public static void registerBase(SrcDir src) {
		baseList.add(src);
	}

	public void generateHtml(String path) {
		String folderName;
		for (SrcDir srcDir : baseList) {
			folderName = srcDir.name;
			srcDir.generateHtml(path, folderName);
			generateChildHtml(path, srcDir, folderName);
		}

		generateDirListHtml(path);
		generateFileListHtml(path);
		generateMainHtml(path);
	}

	public void generateMainHtml(String path) {
		String template = BullsUtil.loadResourceContent("html/frame_summary.html");
		ArrayList<String> dirList = new ArrayList<String>(srcMap.keySet());
		Collections.sort(dirList);
		StringBuffer buffer = new StringBuffer();

		for (String key : dirList) {
			SrcDir src = srcMap.get(key);
			String content = String.format("<tr><td><a href='%s.html'>%s</a></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width:%spx'><span class='text'>%d/%d</span></div></div></td></tr></table></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width:%spx'><span class='text'>%d/%d</span></div></div></td></tr></table></td></tr>", 
					src.path, key, src.getFunctionCoverage(),  src.getFunctionCoverage(), src.coveredFunctionCount, src.functionCount,  src.getBranchCoverage(),  src.getBranchCoverage(), src.coveredBranchCount, src.branchCount);
			buffer.append(content).append("\n");
			
		}
		String nPath = path + File.separator + "frame_summary.html";
		BullsUtil.writeToFile(nPath, String.format(template, buffer.toString()));
	}

	
	public void generateDirListHtml(String path) {
		String template = BullsUtil.loadResourceContent("html/frame_dirs.html");
		ArrayList<String> dirList = new ArrayList<String>(srcMap.keySet());
		Collections.sort(dirList);
		StringBuffer buffer = new StringBuffer();
		for (String src : dirList) {
			buffer.append(String.format(
					"<tr><td nowrap='nowrap'><a target='summary' href='%s.html'>%s</a> <i>%s%%</i></td></tr>",
					BullsUtil.normalizePath(src), src, srcMap.get(src).getFunctionCoverage()));
		}

		String nPath = path + File.separator + "frame_dirs.html";
		BullsUtil.writeToFile(nPath, String.format(template, buffer.toString()));
	}

	public void generateFileListHtml(String path) {
		String template = BullsUtil.loadResourceContent("html/frame_files.html");
		StringBuffer buffer = new StringBuffer();
		
		for (SrcFile src : srcFileList) {
			buffer.append(String.format(
					"<tr><td nowrap='nowrap'><a target='summary' href='%s.html'>%s</a> <i>%s%%</i></td></tr>",
					src.path, src.name, src.getFunctionCoverage()));
		}

		String nPath = path + File.separator + "frame_files.html";
		BullsUtil.writeToFile(nPath, String.format(template, buffer.toString()));
	}

	public void generateChildHtml(String path, SrcDir dir, String baseName) {

		for (Src src : dir.child) {
			String path2 = baseName + "_" + src.name;
			if (src instanceof SrcDir) {
				src.generateHtml(path, path2);
				generateChildHtml(path, (SrcDir) src, path2);
			} else {
				src.generateHtml(path, path2);
			}
		}
	}

}
