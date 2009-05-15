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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * File coverage information
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class SrcFile extends Src {

	private String fileName;
	public String path;
	public int risk;
	private static String fileNotFoundTemplate;
	static {
		fileNotFoundTemplate = BullsUtil.loadResourceContent("html/SrcFileNotFound.html");
	}

	public SrcFile(String[] lines) throws IOException {

		fileName = new File(lines[0]).getCanonicalFile().toString();
		path = BullsUtil.normalizePath(fileName);
		super.coveredFunctionCount = Integer.parseInt(lines[1]);
		super.functionCount = Integer.parseInt(lines[2]);
		super.coveredBranchCount = Integer.parseInt(lines[4]);
		super.branchCount = Integer.parseInt(lines[5]);
		risk = branchCount - coveredBranchCount;
		List<String> paths = new ArrayList<String>(Arrays.asList(fileName.split("\\" + File.separator)));
 
                if (paths.get(0).equals("")) {
			paths.set(0, File.separator);
		}

		name = paths.remove(paths.size() - 1);
		registerParent(paths, this);

	}

	/** 
	 * Regster parent;
	 * @param paths
	 * @param file
	 */
	public void registerParent(List<String> paths, SrcFile file) {

		String pathComp = new String();
		SrcDir curSrcDir = null;
		int i = 0;
		for (String path : paths) {
			if (i++ == 0) {
				pathComp = path;
			} else {
				if (pathComp.equals("/")) {
					pathComp = pathComp + path;
				} else {
					pathComp = pathComp + File.separator + path;
				}
			}
			SrcDir src = (SrcDir) BullsHtml.srcMap.get(pathComp);
			if (src == null) {
				src = new SrcDir(path, BullsUtil.normalizePath(pathComp));
				BullsHtml.srcMap.put(pathComp, src);
				src.parentDir = curSrcDir;
				if (curSrcDir == null) {
					BullsHtml.baseList.add(src);
				} else {
					curSrcDir.child.add(src);
				}
			}
			curSrcDir = src;
		}
		file.parentDir = curSrcDir;

		curSrcDir.child.add(file);
		incrementParent();

	}

	public void incrementParent() {

		SrcDir currentParent = parentDir;
		while (currentParent != null) {
			currentParent.coveredBranchCount += coveredBranchCount;
			currentParent.branchCount += branchCount;
			currentParent.functionCount += functionCount;
			currentParent.coveredFunctionCount += coveredFunctionCount;
			currentParent.fileCount++;
			currentParent = currentParent.parentDir;
		}

	}

	@Override
	protected String genCurrentHtml() {
		return String
				.format( 
						"<tr><td><a href='%s.html'>%s</a></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div %s><span class='text'>%d/%d</span></div></div></td></tr></table></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div %s><span class='text'>%d/%d</span></div></div></td></tr></table></td></tr>",
						path, name, getFunctionCoverage(), getFunctionCoverageStyle(), coveredFunctionCount,
						functionCount, getBranchCoverage(), getBranchCoverageStyle(), coveredBranchCount, branchCount);
	}

	@Override
	protected String getHtml(String path) {
		List<String> command = new ArrayList<String>();
		command.add("covbr");
		command.add("--html");
		command.add("--no-banner");
		command.add(fileName);
		String out = BullsUtil.getCmdOutput(command);
		if (out == null) {
			out = String.format(fileNotFoundTemplate, name, name);
		}
		return out;

	}

}
