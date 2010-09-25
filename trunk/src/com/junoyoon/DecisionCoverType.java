package com.junoyoon;

public enum DecisionCoverType {
	NONE("none"), TRUE("true"), FALSE("false"), FULL("full");

	public final String name;

	private DecisionCoverType(String name) {
		this.name = name;
	}

	public static DecisionCoverType getDecisionCoverType(String value) {
		for (DecisionCoverType eachType : values()) {
			if (eachType.name.equals(value)) {
				return eachType;
			}
		}
		return DecisionCoverType.NONE;
	}
}
