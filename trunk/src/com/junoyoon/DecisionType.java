package com.junoyoon;

public enum DecisionType {
	TRY("try"), CATCH("catch"), CASE("switch-label"), DECISION("decision"), FUNCTION("function");

	public final String name;

	private DecisionType(String name) {
		this.name = name;
	}

	public static DecisionType getDecisionCoverType(String value) {
		for (DecisionType eachType : values()) {
			if (eachType.name.equals(value)) {
				return eachType;
			}
		}
		return null;
	}
}
