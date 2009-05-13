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
	
	public String getFunctionCoverage() {
		if (functionCount == 0) {
			return "N/A";
		}
		return String.valueOf(((float)coveredFunctionCount / functionCount) *100);
	}
	
	public String getBranchCoverage() {
		if (branchCount == 0) {
			return "N/A";
		}
		return String.valueOf(((float)coveredBranchCount / branchCount) *100);
	}

	/**
	 * Generate html
	 * @param target
	 * @param path
	 * @return
	 */
	public String generateHtml(String target, String path) {
		String nPath = target + File.separator + BullsUtil.normalizePath(path) + ".html";
		BullsUtil.writeToFile(nPath, getHtml(path));
		return nPath;
	}
	/**
	 * Get src html fragment.
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
