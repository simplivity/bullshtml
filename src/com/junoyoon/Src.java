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

import org.apache.commons.io.FilenameUtils;

/**
 * Base class for src file and directory
 * 
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public abstract class Src {

	public File path;
	public String normalizedPath;
	public int functionCount;
	public int coveredFunctionCount;
	public int branchCount;
	public int coveredBranchCount;
	public SrcDir parentDir;
	public static String format = new String("%.1f");

	public String getFunctionCoverage() {
		if (functionCount == 0) {
			return "N/A";
		}
		return String.format(format, ((float) coveredFunctionCount / functionCount) * 100);
	}

	public String getName() {
		return path.getPath();
	}

	public String getBranchCoverage() {
		if (branchCount == 0) {
			return "N/A";
		}
		return String.format(format, (((float) coveredBranchCount / branchCount) * 100));
	}

	public String getBranchCoverageStyle() {
		String bc = getBranchCoverage();
		return bc.equals("N/A") ? "class='na' style='width:100px'" : "class='greenbar' style='width:" + bc + "px'";
	}

	public String getFunctionCoverageStyle() {
		String fc = getFunctionCoverage();
		return fc.endsWith("N/A") ? "class='na' style='width:100px'" : "class='greenbar' style='width:" + fc + "px'";
	}

	/*
	 * /** Generate html
	 * 
	 * @param target
	 * 
	 * @param path
	 * 
	 * @return
	 */
	public File generateHtml(File targetPath) {
		File nPath = new File(targetPath, BullsUtil.normalizePath(path) + ".html");
		if (isWorthToPrint()) {
			BullsUtil.writeToFile(nPath, getHtml());
		}
		return nPath;
	}

	abstract protected boolean isWorthToPrint();

	/**
	 * Get whole src html.
	 * 
	 * @return
	 */
	abstract protected String getHtml();

}
