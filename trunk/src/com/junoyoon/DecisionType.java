package com.junoyoon;

public enum DecisionType {
	TRY("try", 0), CATCH("catch", 1), CASE("switch-label", 1), DECISION("decision", 1), FUNCTION("function", 0);

	public final String name;
	public final int complexity;

	private DecisionType(String name, int complexity) {
		this.name = name;
		this.complexity = complexity;
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
