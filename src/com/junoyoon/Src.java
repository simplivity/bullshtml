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

/**
 * Base class for src file and directory
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public abstract class Src {
	
	public String name;
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
		return String.format(format,((float)coveredFunctionCount / functionCount) *100);
	}
	
	public String getBranchCoverage() {
		if (branchCount == 0) {
			return "N/A";
		}
		return String.format(format,(((float)coveredBranchCount / branchCount) *100));
	}
	
	public String getBranchCoverageStyle() {
		String bc = getBranchCoverage();
		return bc.equals("N/A") ? "class='na' style='width:100px'" : "class='greenbar' style='width:"+bc+"px'";
	}
	
	public String getFunctionCoverageStyle() {
		String fc = getFunctionCoverage();
		return fc.endsWith("N/A") ? "class='na' style='width:100px'" : "class='greenbar' style='width:"+fc+"px'";
	}
	/*
	/**
	 * Generate html
	 * @param target
	 * @param path
	 * @return
	 */
	public String generateHtml(String target, String path) {
		System.out.println(path);
		String nPath = target + File.separator + BullsUtil.normalizePath(path) + ".html";
		BullsUtil.writeToFile(nPath, getHtml(path));
		return nPath;
	}
	/**
	 * Get whole src html.
	 * @param path
	 * @return
	 */
	abstract protected String getHtml(String path);
	/**
	 * Get src specific html fragment
	 * @param path
	 * @return
	 */
	abstract protected String genCurrentHtml();
}
