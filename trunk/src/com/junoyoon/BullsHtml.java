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

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Main class
 * 
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class BullsHtml {

	private static final Options OPTS = new Options();
	static {
		OPTS.addOption("e", "encoding", true, "source code encoding.");
		OPTS.addOption("h", "help", false, "print help");
	}

	/** System default encoding */
	public static String enc = new java.io.OutputStreamWriter(System.out).getEncoding();
	/** Map b/w path and src */
	public static Map<File, SrcDir> srcMap = new HashMap<File, SrcDir>();
	/** top most dir list */
	public static ArrayList<SrcDir> baseList = new ArrayList<SrcDir>();
	/** src file list */
	public static ArrayList<SrcFile> srcFileList = new ArrayList<SrcFile>();
	public static Encoding sourceEncoding = Encoding.UTF_8;

	/**
	 * Contrcut Src and Dir List. After calling the method, the static variable
	 * {@link BullsHtml.srcMap}, {@link BullsHtml.baseList},
	 * {@link BullsHtml.srcFileList} are constructed.
	 */

	public void process() {
		InputStreamReader reader = null;
		try {
			Process process = Runtime.getRuntime().exec("covxml --no-banner");
			reader = new InputStreamReader(process.getInputStream());
			processInternal(reader);
		} catch (Exception e) {
			BullsHtml.printErrorAndExit(e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	public void processInternal(InputStreamReader reader) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder(false);
		Document build = builder.build(reader);
		Element root = build.getRootElement();
		File rootDir = new File(root.getAttributeValue("dir"));
		buildSrcFileList(srcFileList, root, rootDir);
		if (baseList.size() > 1) {
			SrcDir dir = new SrcDir(new File("/")) {
				@Override
				public String getNormalizedPath() {
					return "_";
				}
				
				public File generateHtml(File targetPath) {
					File nPath = new File(targetPath, "_.html");
					BullsUtil.writeToFile(nPath, getHtml());
					return nPath;
				}
			};
			dir.addChildren(baseList);
			baseList.clear();
			baseList.add(dir);
		}
	}

	Pattern rootPathPattern = Pattern.compile("^[a-zA-Z]:");

	private void buildSrcFileList(ArrayList<SrcFile> srcFileList, Element eachFolder, File baseDir) {
		try {
			baseDir = baseDir.getCanonicalFile();
		} catch (IOException e) {
			BullsHtml.printErrorAndExit(e);
		}
		for (Object elementObject : eachFolder.getChildren()) {
			Element element = (Element) elementObject;
			String name = element.getName();
			if ("folder".equals(name)) {
				String newFolderName = element.getAttributeValue("name");
				File newFile = rootPathPattern.matcher(newFolderName).find() ? new File(newFolderName) : new File(baseDir, newFolderName);
				buildSrcFileList(srcFileList, element, newFile);
			} else if ("src".equals(name)) {
				srcFileList.add(new SrcFile().init(baseDir, element));
			}
		}
	}

	public void generateCloverXml(File o) {
		Set<SrcDir> parentDirList = new HashSet<SrcDir>();
		for (SrcFile srcFile : srcFileList) {
			parentDirList.add(srcFile.parentDir);
		}
		StringTemplate template = BullsUtil.getTemplate("clover");
		template.setAttribute("mtime", System.currentTimeMillis());
		template.setAttribute("summary", baseList.get(0));
		template.setAttribute("parentDirList", parentDirList);
		BullsUtil.writeToFile(new File(o, "clover.xml"), template.toString());
	}

	
	/**
	 * Copy static resources
	 * 
	 * @param outputFolder
	 *            the target folder
	 * @throws IOException
	 */
	public void copyResources(String outputFolder) throws IOException {
		BullsUtil.copyResource(outputFolder + "/js/popup.js", "js/popup.js");
		BullsUtil.copyResource(outputFolder + "/js/sortabletable.js", "js/sortabletable.js");
		BullsUtil.copyResource(outputFolder + "/js/customsorttypes.js", "js/customsorttypes.js");
		BullsUtil.copyResource(outputFolder + "/js/stringbuilder.js", "js/stringbuilder.js");
		BullsUtil.copyResource(outputFolder + "/css/help.css", "css/help.css");
		BullsUtil.copyResource(outputFolder + "/css/main.css", "css/main.css");
		BullsUtil.copyResource(outputFolder + "/css/highlight.css", "css/highlight.css");

		BullsUtil.copyResource(outputFolder + "/css/sortabletable.css", "css/sortabletable.css");
		BullsUtil.copyResource(outputFolder + "/css/source-viewer.css", "css/source-viewer.css");
		BullsUtil.copyResource(outputFolder + "/css/tooltip.css", "css/tooltip.css");
		BullsUtil.copyResource(outputFolder + "/images/upper.png", "images/upper.png");
		BullsUtil.copyResource(outputFolder + "/images/check_icon.png", "images/check_icon.png");
		BullsUtil.copyResource(outputFolder + "/images/uncheck_icon.png", "images/uncheck_icon.png");

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
		printMessage(output);
		System.exit(0);
	}

	/**
	 * Print Error Message and Exit
	 * 
	 * @param message
	 *            message to print
	 */
	public static void printMessage(String message) {
		System.out.println(message);
	}

	/**
	 * Print Error Message and Exit
	 * 
	 * @param message
	 *            message to print
	 */
	public static void printErrorAndExit(String message) {
		System.err.println(message);
		System.exit(-1);
	}

	/**
	 * Print Error Message and Exit
	 * 
	 * @param message
	 *            message to print
	 */
	public static void printErrorAndExit(Exception e) {
		e.printStackTrace(System.err);
		System.exit(-1);
	}

	/**
	 * generate html
	 * 
	 * @param targetPath
	 *            output dir
	 */
	public void generateHtml(File targetPath) {

		for (SrcDir srcDir : baseList) {
			srcDir.generateHtml(targetPath);
			generateChildHtml(targetPath, srcDir);
		}
		generateDirListHtml(targetPath);
		generateFileListHtml(targetPath);
		generateMainHtml(targetPath);
	}

	// private void generateCloverXmlFully(File outputPath) {
	// CloverXml cloverXml = new CloverXml();
	// StringBuffer buffer = new StringBuffer();
	// List<SrcDir> folderList = new ArrayList<SrcDir>(srcMap.values());
	// Collections.sort(folderList, new Comparator<SrcDir>() {
	// public int compare(SrcDir arg0, SrcDir arg1) {
	// return
	// arg0.path.getAbsolutePath().compareTo(arg1.path.getAbsolutePath());
	// }
	// });
	// for (SrcDir src : folderList) {
	// src.appendCloverXml(buffer);
	// }
	// cloverXml.generateXml(outputPath, buffer);
	// }

	public static boolean isSingleElement(SrcDir dir) {
		return (dir.child.size() == 1 && dir.child.get(0) instanceof SrcDir);
	}

	/**
	 * generate main html page
	 * 
	 * @param path
	 *            output dir
	 */
	public void generateMainHtml(File path) {
		StringTemplate template = BullsUtil.getTemplate("frame_summary");
		File nPath = new File(path, "frame_summary.html");

		template.setAttribute("baseDir", baseList);
		List<SrcFile> localSrcFileList = new ArrayList<SrcFile>(srcFileList);

		// Sort By Risk
		Collections.sort(localSrcFileList, new Comparator<SrcFile>() {
			public int compare(SrcFile o1, SrcFile o2) {
				// System.out.println(o2.name + o2.risk + o1.name + o1.risk +
				// (o1.risk -o2.risk ));
				return (o2.risk - o1.risk);
			}
		});

		template.setAttribute("srcFileList", localSrcFileList.subList(0, Math.min(10, srcFileList.size())));

		List<SrcDir> dirFileList = getSrcDirList();

		template.setAttribute("dirList", dirFileList.subList(0, Math.min(10, dirFileList.size())));
		BullsUtil.writeToFile(nPath, template.toString());
	}

	public List<SrcDir> getSrcDirList() {
		ArrayList<SrcDir> list = new ArrayList<SrcDir>();
		for (SrcDir srcDir : srcMap.values()) {
			if (srcDir.isWorthToPrint()) {
				list.add(srcDir);
			}
		}
		Collections.sort(list, new Comparator<SrcDir>() {
			public int compare(SrcDir o1, SrcDir o2) {
				// System.out.println(o2.name + o2.risk + o1.name + o1.risk +
				// (o1.risk -o2.risk ));
				return o1.path.compareTo(o2.path);
			}
		});

		return list;
	}

	/**
	 * generate upper left dir html page
	 * 
	 * @param path
	 *            output dir
	 */
	public void generateDirListHtml(File path) {
		StringTemplate template = BullsUtil.getTemplate("frame_dirs");

		template.setAttribute("srcDirList", getSrcDirList());
		File nPath = new File(path, "frame_dirs.html");
		BullsUtil.writeToFile(nPath, template.toString());
	}

	/**
	 * generate down left src html page
	 * 
	 * @param path
	 *            output dir
	 */
	public void generateFileListHtml(File path) {
		StringTemplate template = BullsUtil.getTemplate("frame_files");
		Collections.sort(srcFileList);
		template.setAttribute("srcFileList", srcFileList);
		BullsUtil.writeToFile(new File(path, "frame_files.html"), template.toString());
	}

	/**
	 * generate each dir/file html page
	 * 
	 * @param outputPath
	 * @param dir
	 * @param baseNormalizedName
	 */
	public void generateChildHtml(File outputPath, SrcDir dir) {
		for (Src src : dir.child) {
			src.generateHtml(outputPath);
			if (src instanceof SrcDir) {
				generateChildHtml(outputPath, (SrcDir) src);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final CommandLineParser clp = new PosixParser();
		CommandLine line = null;

		// 선언한 옵션의 포맷에 맞게 인자가 넘어왔는지 확인한다.
		try {
			line = clp.parse(OPTS, args);
		} catch (ParseException e) {
			usage();
			return;
		}
		String sourceEncoding = enc;
		// p 옵션이 사용되었으면 값을 가져와 출력한다.
		if (line.hasOption("e")) {
			sourceEncoding = line.getOptionValue("e");
		}
		// h 옵션이 사용되었으면 usage를 출력한다.
		if (line.hasOption("h")) {
			usage();
		}

		String outputPath = ".";

		if (line.getArgs().length != 1) {
			printErrorAndExit("please provide the html output directory");
		}
		outputPath = line.getArgs()[0];
		File o = new File(outputPath);
		if (!o.exists()) {
			if (!o.mkdirs()) {
				printErrorAndExit(outputPath + " directory can be not created.");
			}
		} else if (!o.isDirectory()) {
			printErrorAndExit(outputPath + " is not directory.");
		} else if (!o.canWrite()) {
			printErrorAndExit(outputPath + " is not writable.");
		}
		BullsHtml bullshtml = new BullsHtml();
		bullshtml.process();
		try {
			bullshtml.copyResources(outputPath);
		} catch (Exception e) {
			printErrorAndExit("The output " + outputPath + " is not writable." + e.toString());
		}
		BullsHtml.sourceEncoding = Encoding.getEncoding(sourceEncoding);
		bullshtml.generateHtml(o);
		bullshtml.generateCloverXml(o);
	}


}
