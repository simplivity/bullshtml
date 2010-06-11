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

	public void generateHtml(String targetPath) {
		String nPath = targetPath + File.separator + "clover.xml";
		BullsUtil.writeToFile(nPath, getHtml());
	}

	private String getHtml() {
		
		return String.format(template, System.currentTimeMillis(), System.currentTimeMillis(), coveredConditionals, conditionals,coveredConditionals, conditionals, coveredMethods, methods);
	}	
}
