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
 * 
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public abstract class Src {
	public File path;
	private String normalizedPath;
	public int functionCount;
	public int coveredFunctionCount;
	public int branchCount;
	public int coveredBranchCount;
	public SrcDir parentDir;
	public static String format = new String("%.1f");

	public String getFunctionCoverageString() {
		if (functionCount == 0) {
			return "N/A";
		}
		return String.format(format, getFunctionCoverage());
	}

	private float getFunctionCoverage() {
		return ((float) coveredFunctionCount / functionCount) * 100;
	}

	public String getName() {
		return path.getName();
	}
	


	public String getBranchCoverageString() {
		if (branchCount == 0) {
			return "N/A";
		}
		return String.format(format, (getBranchCoverage()));
	}

	private float getBranchCoverage() {
		return ((float) coveredBranchCount / branchCount) * 100;
	}

	public String getBranchCoverageStyle() {
		String bc = getBranchCoverageString();
		return bc.equals("N/A") ? "class='na' style='width:100px'" : "class='greenbar' style='width:" + Math.round(getBranchCoverage()) + "px'";
	}

	public String getFunctionCoverageStyle() {
		String fc = getFunctionCoverageString();
		return fc.endsWith("N/A") ? "class='na' style='width:100px'" : "class='greenbar' style='width:" + Math.round(getFunctionCoverage()) + "px'";
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
		File nPath = new File(targetPath, normalizedPath + ".html");
		if (isWorthToPrint()) {
			BullsUtil.writeToFile(nPath, getHtml());
		}
		return nPath;
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
	
	public Src getWorthyParent() {
		Src eachDir = parentDir;
		while (eachDir != null && !eachDir.isWorthToPrint()) {
			eachDir = ((SrcDir) eachDir).parentDir;
		}
		return eachDir;
	}

	abstract public boolean isWorthToPrint();

	/**
	 * Get whole src html.
	 * 
	 * @return
	 */
	abstract protected String getHtml();

	public void setNormalizedPath(String normalizedPath) {
		this.normalizedPath = normalizedPath;
	}

	public String getNormalizedPath() {
		return normalizedPath;
	}
	
	public boolean isSrcFile() {
		return false;
	}
	
	public int getCoveredElementCount() {
		return this.coveredBranchCount + this.coveredFunctionCount;
	}
	
	public int getElementCount() {
		return this.branchCount + this.functionCount;
	}
}
