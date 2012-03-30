/**
 * Copyright (C) 2009 JunHo Yoon
 *
 * bullshtml is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * bullshtml is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 */

package com.junoyoon;

import java.io.File;

import com.uwyn.jhighlight.tools.StringUtils;

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
		if (this.functionCount == 0) {
			return "N/A";
		}
		return String.format(Src.format, getFunctionCoverage());
	}

	private float getFunctionCoverage() {
		return (float) this.coveredFunctionCount / this.functionCount * 100;
	}

	public String getName() {
		return this.path.getName();
	}

	public String getNormalizedName() {
		return BullsUtil.normalizePath(this.path.getName());
	}

	public String getBranchCoverageString() {
		if (this.branchCount == 0) {
			return "N/A";
		}
		return String.format(Src.format, getBranchCoverage());
	}

	private float getBranchCoverage() {
		return (float) this.coveredBranchCount / this.branchCount * 100;
	}

	public String getXmlEncodedNormalizedPath() {
		return StringUtils.encodeHtml(getNormalizedPath());
	}

	public String getXmlEncodedNormalizedName() {
		return StringUtils.encodeHtml(getNormalizedName());
	}

	public String getXmlEncodedName() {
		return StringUtils.encodeHtml(getName());
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
		File nPath = new File(targetPath, this.normalizedPath + ".html");
		if (isWorthToPrint()) {
			BullsUtil.writeToFile(nPath, getHtml());
		}
		return nPath;
	}

	public void incrementParent() {
		SrcDir currentParent = this.parentDir;
		while (currentParent != null) {
			currentParent.coveredBranchCount += this.coveredBranchCount;
			currentParent.branchCount += this.branchCount;
			currentParent.functionCount += this.functionCount;
			currentParent.coveredFunctionCount += this.coveredFunctionCount;
			currentParent.fileCount++;
			currentParent = currentParent.parentDir;
		}
	}

	public Src getWorthyParent() {
		Src eachDir = this.parentDir;
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
		return this.normalizedPath;
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
