/**
 Copyright 2008 JunHo Yoon

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.junoyoon;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Main class
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class BullsHtml {
	/** System default encoding*/
	public static String enc = new java.io.OutputStreamWriter(System.out).getEncoding();
	/** Map b/w path and src */
	public static Map<String, SrcDir> srcMap = new HashMap<String, SrcDir>();
	/** top most dir list */
	public static ArrayList<SrcDir> baseList = new ArrayList<SrcDir>();
	/** src file list */
	public static ArrayList<SrcFile> srcFileList = new ArrayList<SrcFile>();

	/**
	 * Contrcut Src and Dir List.
	 * After calling the method, the static variable 
	 * {@link BullsHtml.srcMap}, {@link BullsHtml.baseList}, 
	 * {@link BullsHtml.srcFileList}
	 * are constructed.
	 */
	public void process() {
		try {
			Pattern rootPathPattern = Pattern.compile("^([a-z]\\:|\\/)");
			// Get test.cov Dir
			Process covxmlprocess = Runtime.getRuntime().exec("covxml --no-banner");
			InputSource covXmlInputStream = new InputSource(covxmlprocess.getInputStream());
			covXmlInputStream.setEncoding(enc);
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(covXmlInputStream);
			String testcovDir = document.getDocumentElement().getAttribute("dir");
			
			// Get Files
			Process process = Runtime.getRuntime().exec("covsrc --csv --no-banner --decision");
			InputStreamReader reader = new InputStreamReader(process.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(reader);
			CSVReader csvReader = new CSVReader(bufferedReader);
			csvReader.readNext(); // Pass Header
			String[] lines = null;
			while ((lines = csvReader.readNext()) != null) {
				if (lines[0].equals("Total")) continue;
				String fileName = lines[0];
				// If the each file is relative path, make it absolute path using testcovdir
				if (!rootPathPattern.matcher(fileName).find()) {
					lines[0] = testcovDir + fileName;
				}
				srcFileList.add(new SrcFile(lines));
			}
		} catch (Exception e) {
			BullsHtml.printErrorAndExit(e.getMessage());	
		}
	}

	/** 
	 * Copy static resources
	 * @param outputFolder the target folder 
	 */
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
	 * Show usage
	 */
	private static void usage() {
		String file = "com/junoyoon/usage_win32.txt";
		if (!System.getProperty("os.name").contains("Windows")) {
			file = "com/junoyoon/usage_linux.txt";
		}
		String output = BullsUtil.loadResourceContent(file);
		System.out.println(output);
		System.exit(0);
	}

	/**
	 * Print Error Message and Exit
	 * @param message message to print
	 */
	public static void printErrorAndExit(String message) {
		System.err.println(message);
		System.exit(-1);
	}
	

	/**
	 * generate html
	 * @param path output dir
	 */
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
	
	/**
	 * generate main html page
	 * @param path   output dir
	 */
	public void generateMainHtml(String path) {
		String template = BullsUtil.loadResourceContent("html/frame_summary.html");
		String nPath = path + File.separator + "frame_summary.html";

		ArrayList<String> dirList = new ArrayList<String>(srcMap.keySet());
		Collections.sort(dirList);

		Collections.sort(srcFileList, new Comparator<SrcFile>() {
			public int compare(SrcFile o1, SrcFile o2) {
				//System.out.println(o2.name + o2.risk + o1.name + o1.risk + (o1.risk -o2.risk ));
				return (o2.risk - o1.risk) ;
			}
		});
		StringBuilder buffer = new StringBuilder();
		int i =0;
		for (SrcFile src : srcFileList) {
			if (i++ >= 10) break;
			String content = String
					.format(
							"<tr><td><a href='%s.html'>%s</a></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div %s><span class='text'>%d/%d</span></div></div></td></tr></table></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div %s><span class='text'>%d/%d</span></div></div></td></tr></table></td></tr>",
							src.path, src.name, src.getFunctionCoverage(), src.getFunctionCoverageStyle(),
							src.coveredFunctionCount, src.functionCount, src.getBranchCoverage(), src
									.getBranchCoverageStyle(), src.coveredBranchCount, src.branchCount);
			buffer.append(content).append("\n");
		}
		
		
		StringBuilder buffer2 = new StringBuilder();

		for (String key : dirList) {
			SrcDir src = srcMap.get(key);
			String content = String
					.format(
							"<tr><td><a href='%s.html'>%s</a></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div %s><span class='text'>%d/%d</span></div></div></td></tr></table></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div %s><span class='text'>%d/%d</span></div></div></td></tr></table></td></tr>",
							src.path, key, src.getFunctionCoverage(), src.getFunctionCoverageStyle(),
							src.coveredFunctionCount, src.functionCount, src.getBranchCoverage(), src
									.getBranchCoverageStyle(), src.coveredBranchCount, src.branchCount);
			buffer2.append(content).append("\n");

		}
		BullsUtil.writeToFile(nPath, String.format(template, buffer.toString(), buffer2.toString()));
	}

	/**
	 * generate upper left dir html page
	 * @param path  output dir
	 */
	public void generateDirListHtml(String path) {
		String template = BullsUtil.loadResourceContent("html/frame_dirs.html");
		ArrayList<String> dirList = new ArrayList<String>(srcMap.keySet());
		Collections.sort(dirList);
		StringBuilder buffer = new StringBuilder();
		for (String src : dirList) {
			buffer.append(String.format(
					"<tr><td nowrap='nowrap'><a target='summary' href='%s.html'>%s</a> <i>%s%%</i></td></tr>",
					BullsUtil.normalizePath(src), src, srcMap.get(src).getFunctionCoverage()));
		}

		String nPath = path + File.separator + "frame_dirs.html";
		BullsUtil.writeToFile(nPath, String.format(template, buffer.toString()));
	}

	/**
	 * generate down left src html page
	 * @param path  output dir
	 */
	public void generateFileListHtml(String path) {
		String template = BullsUtil.loadResourceContent("html/frame_files.html");
		StringBuilder buffer = new StringBuilder();

		for (SrcFile src : srcFileList) {
			buffer.append(String.format(
					"<tr><td nowrap='nowrap'><a target='summary' href='%s.html'>%s</a> <i>%s%%</i></td></tr>",
					src.path, src.name, src.getFunctionCoverage()));
		}

		String nPath = path + File.separator + "frame_files.html";
		BullsUtil.writeToFile(nPath, String.format(template, buffer.toString()));
	}

	/**
	 * generate each dir/file html page
	 * @param path  output dir
	 */
	public void generateChildHtml(String path, SrcDir dir, String baseName) {
		for (Src src : dir.child) {
			String normalizedPath = baseName + "_" + src.name;
			if (src instanceof SrcDir) {
				src.generateHtml(path, normalizedPath);
				generateChildHtml(path, (SrcDir) src, normalizedPath);
			} else {
				src.generateHtml(path, normalizedPath);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String outputPath ="";
		if (args.length == 1 && args[0].equals("-h")) {
			usage();
		}
		if (args.length == 1) {
			outputPath = args[0];
			File o = new File(outputPath);
			if (!o.exists()) {
				if (!o.mkdir()) {
					printErrorAndExit(outputPath + " directory can be not created.");
				}
			} else if (!o.isDirectory()){
				printErrorAndExit(outputPath + " is not directory.");
			} else if (!o.canWrite()) {
				printErrorAndExit(outputPath + " is not writable.");
			}
		}
		BullsHtml bullshtml = new BullsHtml();
		bullshtml.process();
		bullshtml.copyResources(outputPath);
		bullshtml.generateHtml(outputPath);
	}


}
