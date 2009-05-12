package com.nhn.bullshtml;

import java.io.File;

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

	
	public String generateHtml(String target, String path) {
		
		String nPath = target + File.separator + BullsUtil.normalizePath(path) + ".html";
		BullsUtil.writeToFile(nPath, getHtml(path));
		return nPath;
	}
	
	abstract public String getHtml(String path);
	abstract public String genCurrentHtml();
}
