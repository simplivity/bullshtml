package com.junoyoon;

import org.jdom.Element;

public class SrcDecisionPoint {
	public int line;
	public DecisionCoverType decisionCoverType;
	public DecisionType decisionType;
	public int column;
	public boolean sequence;
	public String name;

	public SrcDecisionPoint() {

	}

	public SrcDecisionPoint(int line, DecisionCoverType decisionCoverType, DecisionType decisionType, String name) {
		this(line, decisionCoverType, decisionType);
		this.name = name;
	}
	
	
	public SrcDecisionPoint(int line, DecisionCoverType decisionCoverType, DecisionType decisionType) {
		super();
		this.line = line;
		this.decisionCoverType = decisionCoverType;
		this.decisionType = decisionType;
	}

	public static SrcDecisionPoint createDecisionPoint(Element element) {
		SrcDecisionPoint point = null;
		DecisionType decisionType = DecisionType.getDecisionCoverType(element.getAttributeValue("kind"));
		if (decisionType != null) {
			point = new SrcDecisionPoint();

			point.decisionType = decisionType;
			point.decisionCoverType = DecisionCoverType.getDecisionCoverType(element.getAttributeValue("event"));
			if (point.decisionType == DecisionType.CASE) {
				point.decisionCoverType = point.decisionCoverType == DecisionCoverType.NONE ? DecisionCoverType.ONLY_FALSE
						: DecisionCoverType.ONLY_TRUE;
			}
			point.line = Integer.parseInt(element.getAttributeValue("line"));
			String columnValue = element.getAttributeValue("column");
			point.column = columnValue != null ? Integer.parseInt(columnValue) : 0;
			point.sequence = element.getAttributeValue("seq") != null;
		}
		return point;
	}
}
