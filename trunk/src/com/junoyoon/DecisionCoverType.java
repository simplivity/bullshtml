package com.junoyoon;

public enum DecisionCoverType {
	NONE("none", "<span class='uncoveredmark'>TF</span>", "uncovered"), 
	TRUE("true", "<span class='coveredmark'>T</span><span class='uncoveredmark'>F</span>", "halfcovered"), 
	ONLY_TRUE("true", "<span class='coveredmark'>T</span>", "covered"),
	ONLY_FALSE("none", "<span class='uncoveredmark'>T</span>", "uncovered"),
	FALSE("false", "<span class='uncoveredmark'>T</span><span class='coveredmark'>F</span>", "halfcovered"), 
	FULL("full", "<span class='coveredmark'>TF</span>", "covered"), 
	FUNCTION_CALLED("true", "<img src='images/check_icon.png'>", "covered"), 
	FUNCTION_UNCALLED("true", "<img src='images/uncheck_icon.png'>", "uncovered");

	public final String name;
	private final String html;
	private final String lineCss;

	private DecisionCoverType(String name, String html, String lineCss) {
		this.name = name;
		this.html = html;
		this.lineCss = lineCss;
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
