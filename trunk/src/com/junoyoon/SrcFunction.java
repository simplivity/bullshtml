package com.junoyoon;

import org.jdom.Element;

public class SrcFunction {
	public SrcFunction() {
	}

	public SrcFunction init(Element element) {
		this.name = element.getAttributeValue("name");
		this.branchCount = Integer.parseInt(element.getAttributeValue("d_total"));
		this.coveredBranchCount = Integer.parseInt(element.getAttributeValue("d_cov"));
		this.covered = "1".equals(element.getAttributeValue("fn_cov"));
		return this;
	}

	public String name;
	public boolean covered;
	public int branchCount;
	public int coveredBranchCount;
	public static String format = new String("%.1f");

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

}
