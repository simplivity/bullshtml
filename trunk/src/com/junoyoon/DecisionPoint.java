package com.junoyoon;

import org.jdom.Element;

public class DecisionPoint {
	public int line;
	public DecisionCoverType decisionCoverType;

	public static DecisionPoint createDecisionPoint(Element element) {
		DecisionPoint point = new DecisionPoint();
		point.line = Integer.parseInt(element.getAttributeValue("line"));
		point.decisionCoverType = DecisionCoverType.getDecisionCoverType(element.getAttributeValue("event"));
		return point;
	}
}
