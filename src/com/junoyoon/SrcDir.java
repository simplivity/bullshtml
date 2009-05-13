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

import java.util.ArrayList;

/**
 * Directory coverage information
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class SrcDir extends Src {
	private static String template;
	private static String dirTemplate;
	private static String subDirTemplate;
	private static String subFileTemplate;
	static {
		template = BullsUtil.loadResourceContent("html/SrcDir.html");
		dirTemplate = BullsUtil.loadResourceContent("html/SrcDir_DirList.html");
		subDirTemplate = BullsUtil.loadResourceContent("html/SrcDir_SubDirList.html");
		subFileTemplate = BullsUtil.loadResourceContent("html/SrcDir_SubFileList.html");
	}

	public SrcDir(String name, String path) {

		this.name = name;
		this.path = path;

	}

	public String path;
	public int fileCount;
	public ArrayList<Src> child = new ArrayList<Src>();

	@Override
	protected String getHtml(String path) {

		//output.append(b)
		String dir = String.format(dirTemplate, genCurrentHtml());

		StringBuilder subDirBuffer = new StringBuilder();
		StringBuilder subSrcBuffer = new StringBuilder();

		for (Src subDir : child) {
			if (subDir instanceof SrcDir) {
				subDirBuffer.append(subDir.genCurrentHtml());
			} else {
				subSrcBuffer.append(subDir.genCurrentHtml());
			}
		}
		String subDir = new String();
		if (subDirBuffer.length() != 0) {
			subDir = String.format(SrcDir.subDirTemplate, subDirBuffer);
		}

		String subSrc = new String();
		if (subSrcBuffer.length() != 0) {
			subSrc = String.format(SrcDir.subFileTemplate, subSrcBuffer);
		}
		StringBuilder output = new StringBuilder(String.format(template, name, dir, subDir, subSrc));

		return output.toString();

	}

	@Override
	protected String genCurrentHtml() {

		return String
				.format(
						"<tr><td><a href='%s.html'>%s</a></td><td class='value'>%d</td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div %s><span class='text'>%d/%d</span></div></div></td></tr></table></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div %s><span class='text'>%d/%d</span></div></div></td></tr></table></td></tr>",
						path, name, fileCount, getFunctionCoverage(), getFunctionCoverageStyle(), coveredFunctionCount,
						functionCount, getBranchCoverage(), getBranchCoverageStyle(), coveredBranchCount, branchCount);

	}

}
