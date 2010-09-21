package com.junoyoon;

import java.io.File;

public class CloverXml {
	private static String template;
	static {
		template = BullsUtil.loadResourceContent("html/clover.xml");
	}

	public int coveredConditionals = 0;
	public int conditionals = 0;
	public int coveredMethods = 0;
	public int methods = 0;

	public void generateXml(File targetPath, StringBuffer xmlContent) {
		File nPath = new File(targetPath, "clover.xml");
		BullsUtil.writeToFile(nPath, getXmlContent(xmlContent));
	}

	private String getXmlContent(StringBuffer xmlContent) {
		return String.format(template, xmlContent, System.currentTimeMillis(), System.currentTimeMillis(), coveredConditionals, conditionals,
				coveredConditionals, conditionals, coveredMethods, methods);
	}
}
