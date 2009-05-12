package com.nhn.bullshtml;

import java.util.ArrayList;

public class SrcDir extends Src {
	private static String template;
	private static String dirTemplate;
	private static String subDirTemplate;
	private static String subFileTemplate;
	static {
		template = BullsUtil.loadResourceContent("html/SrcDir.html");
		dirTemplate = BullsUtil.loadResourceContent("html/SrcDir_DirList.html");
		subDirTemplate = BullsUtil.loadResourceContent("html/SrcDir_SubDirList.html");
		subFileTemplate = BullsUtil.loadResourceContent("html/SrcDir_SubFileList.html");
	}
	public SrcDir(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public String path;
	public int fileCount;
	public ArrayList<Src> child = new ArrayList<Src>();
	@Override
	public String getHtml(String path) {
		
		//output.append(b)
		String dir = String.format(dirTemplate, genCurrentHtml());
		
		StringBuffer subDirBuffer = new StringBuffer();
		StringBuffer subSrcBuffer = new StringBuffer();
		
		for (Src subDir : child) {
			if (subDir instanceof SrcDir) {
				subDirBuffer.append(subDir.genCurrentHtml());
			} else {
				subSrcBuffer.append(subDir.genCurrentHtml());
			}
		}
		String subDir = new String();
		if (subDirBuffer.length() != 0) {
			subDir = String.format(SrcDir.subDirTemplate, subDirBuffer);
		}
		
		String subSrc = new String();
		if (subSrcBuffer.length() != 0) {
			subSrc = String.format(SrcDir.subFileTemplate, subSrcBuffer);
		}
		StringBuffer output = new StringBuffer(String.format(template, name, dir, subDir, subSrc));
		return output.toString();	
	}
	
	@Override
	public String genCurrentHtml() {
		return String.format("<tr><td><a href='%s.html'>%s</a></td><td class='value'>%d</td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width:%spx'><span class='text'>%d/%d</span></div></div></td></tr></table></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width:%spx'><span class='text'>%d/%d</span></div></div></td></tr></table></td></tr>", 
				path, name, fileCount, getFunctionCoverage(),  getFunctionCoverage(), coveredFunctionCount, functionCount,  getBranchCoverage(),  getBranchCoverage(), coveredBranchCount, branchCount);

	}

}
