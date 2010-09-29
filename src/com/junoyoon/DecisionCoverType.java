package com.junoyoon;

public enum DecisionCoverType {
	NONE("none", "<span class='uncoveredmark'>TF</span>", "uncovered", 0, 0), 
	TRUE("true", "<span class='coveredmark'>T</span><span class='uncoveredmark'>F</span>", "halfcovered", 1, 0), 
	ONLY_TRUE("true", "<span class='coveredmark'>T</span>", "covered", 1, 0),
	ONLY_FALSE("none", "<span class='uncoveredmark'>T</span>", "uncovered", 0, 0),
	FALSE("false", "<span class='uncoveredmark'>T</span><span class='coveredmark'>F</span>", "halfcovered", 0, 1), 
	FULL("full", "<span class='coveredmark'>TF</span>", "covered", 1, 1), 
	FUNCTION_CALLED("true", "<img src='../images/check_icon.png'>", "covered", 1, 0), 
	FUNCTION_UNCALLED("true", "<img src='../images/uncheck_icon.png'>", "uncovered", 0, 1);

	public final String name;
	private final String html;
	private final String lineCss;
	public final int trueCount;
	public final int falseCount;

	private DecisionCoverType(String name, String html, String lineCss, int trueCount, int falseCount) {
		this.name = name;
		this.html = html;
		this.lineCss = lineCss;
		this.trueCount = trueCount;
		this.falseCount = falseCount;

	}

	public static DecisionCoverType getDecisionCoverType(String value) {
		for (DecisionCoverType eachType : values()) {
			if (eachType.name.equals(value)) {
				return eachType;
			}
		}
		return DecisionCoverType.NONE;
	}

	public String getHtml() {
		return this.html;
	}

	public String getLineCss() {
		return this.lineCss;
	}

}
