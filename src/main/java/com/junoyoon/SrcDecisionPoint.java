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

import org.jdom2.Element;

public class SrcDecisionPoint implements Comparable<SrcDecisionPoint>
{
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
		this.column = 0;
		this.name = name;
	}

	public SrcDecisionPoint(int line, DecisionCoverType decisionCoverType, DecisionType decisionType) {
		super();
		this.column = 0;
		this.decisionCoverType = decisionCoverType;
		this.decisionType = decisionType;
		this.line = line;
	}

	public int compareTo(SrcDecisionPoint other) {
		return (line == other.line) ? (column - other.column) : (line - other.line);  
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

	@Override
	public String toString() {
		return String.format("%d %s %s %d %s", this.line, this.decisionCoverType.name, this.decisionType.name, this.column, this.name);
	}
}
