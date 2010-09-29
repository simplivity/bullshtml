package com.junoyoon;


public class SrcFunctionDecisionPoint extends SrcDecisionPoint {
	public String name;
	
	public SrcFunctionDecisionPoint() {
	}

	public SrcFunctionDecisionPoint(int line, DecisionCoverType decisionCoverType, DecisionType decisionType, String name) {
		super(line, decisionCoverType, decisionType);
		this.name = name;
	}

}
