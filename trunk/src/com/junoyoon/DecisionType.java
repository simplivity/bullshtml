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
