package com.junoyoon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * File coverage information
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class SrcFile extends Src {

	private String fileName;
	public String path;
	public SrcFile(String[] lines) throws IOException {
		fileName = new File(lines[0]).getCanonicalFile().toString();		
		path = BullsUtil.normalizePath(fileName);
		super.coveredFunctionCount = Integer.parseInt(lines[1]);
		super.functionCount = Integer.parseInt(lines[2]);
		super.coveredBranchCount = Integer.parseInt(lines[4]);
		super.branchCount = Integer.parseInt(lines[5]);
		List<String> paths = new ArrayList<String>(Arrays.asList(fileName.split("\\"+ File.separator)));
		if (fileName.startsWith("/")) {
			paths.add("/");
		}
		name = paths.remove(paths.size()-1);
		
		registerParent(paths, this);
	}
	
	/** 
	 * 
	 * @param paths
	 * @param file
	 */
	public void registerParent(List<String> paths, SrcFile file) {
		String pathComp = new String();
		SrcDir curSrcDir = null; 
		int i = 0;
		for (String path : paths) {
			if (i++ == 0) {
				pathComp = path;
			} else {
				pathComp = pathComp + File.separator + path;
			}
			SrcDir src = (SrcDir)BullsHtml.srcMap.get(pathComp);
			if (src == null) {
				src = new SrcDir(path, BullsUtil.normalizePath(pathComp));
				BullsHtml.srcMap.put(pathComp, src);
				src.parentDir = curSrcDir;
				if (curSrcDir == null){
					BullsHtml.baseList.add(src);
				} else {
					curSrcDir.child.add(src);
				}

			}
			curSrcDir = src;
		}
		file.parentDir = curSrcDir;
		curSrcDir.child.add(file);
		incrementParent();
	}
	
	public void incrementParent() {
		SrcDir currentParent = parentDir;
		while(currentParent != null) {
			currentParent.coveredBranchCount += coveredBranchCount;
			currentParent.branchCount += branchCount;
			currentParent.functionCount += functionCount;
			currentParent.coveredFunctionCount += coveredFunctionCount;
			currentParent.fileCount++;
			currentParent = currentParent.parentDir;
		}
	}
	@Override
	protected String genCurrentHtml() {
		return String.format("<tr><td><a href='%s.html'>%s</a></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width:%spx'><span class='text'>%d/%d</span></div></div></td></tr></table></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div class='greenbar' style='width:%spx'><span class='text'>%d/%d</span></div></div></td></tr></table></td></tr>", 
				path, name, getFunctionCoverage(),  getFunctionCoverage(), coveredFunctionCount, functionCount,  getBranchCoverage(),  getBranchCoverage(), coveredBranchCount, branchCount);

	}
	@Override
	protected String getHtml(String path) {		
		return	BullsUtil.getCmdOutput("covbr --html --no-banner \"" + fileName + "\"");
	}
	
}
