package com.junoyoon;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class SrcFunction {
	public SrcFunction() {
	}

	public SrcFunction init(Element element) {
		String name = element.getAttributeValue("name");
		this.name = name.contains(")") ? name : name + "()";
		this.branchCount = Integer.parseInt(element.getAttributeValue("d_total"));
		this.coveredBranchCount = Integer.parseInt(element.getAttributeValue("d_cov"));
		this.covered = "1".equals(element.getAttributeValue("fn_cov"));
		boolean isFirst = true;
		for (Object elementObject : element.getChildren("probe")) {
			Element eachProbe = (Element) elementObject;
			if (isFirst) {
				isFirst = false;
				line = Integer.parseInt(eachProbe.getAttributeValue("line"));
				continue;
			}
			SrcDecisionPoint decisionPoint = SrcDecisionPoint.createDecisionPoint(eachProbe);
			if (decisionPoint != null) {
				decisionPoints.add(decisionPoint);
			}
		}
		return this;
	}

	public String name;
	public boolean covered;
	public int branchCount;
	public int coveredBranchCount;
	public int line;
	public static String format = new String("%.1f");
	public List<SrcDecisionPoint> decisionPoints = new ArrayList<SrcDecisionPoint>();

	public int getCoveredCount() {
		return covered ? 1 : 0;
	}
	
	public int getComplexity() {
		int count = 1;
		for (SrcDecisionPoint decisionPoint : decisionPoints) {
			count += decisionPoint.decisionType.complexity;
		}
		return count;
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

}
