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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.jdom2.Element;

import com.uwyn.jhighlight.tools.StringUtils;

/**
 * File coverage information
 * 
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class SrcFile extends Src implements Comparable<SrcFile> {
	public int risk;
	public List<SrcFunction> functions = new ArrayList<SrcFunction>();
	public List<SrcDecisionPoint> decisionPoints = new ArrayList<SrcDecisionPoint>();
	public long timestamp;

	public SrcFile() {
	}

	public int getFunctionCount() {
		return this.functions.size();
	}

	public boolean isModified() {
		return this.timestamp < this.path.lastModified() / 1000;
	}

	public String getUnixStylePath() throws IOException {
		return this.path.getCanonicalPath().replace("\\", "/");
	}

	public String getXmlEncodedUnixStylePath() throws IOException {
		return StringUtils.encodeHtml(getUnixStylePath());
	}

	public SrcFile init(File dir, Element element) {
		String name = element.getAttributeValue("name");
		try {
			this.path = new File(dir, name).getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setNormalizedPath(BullsUtil.normalizePath(this.path.getParentFile()) + "/" + BullsUtil.normalizePath(this.path.getName()));
		this.coveredFunctionCount = Integer.parseInt(element.getAttributeValue("fn_cov"));
		this.functionCount = Integer.parseInt(element.getAttributeValue("fn_total"));
		this.coveredBranchCount = Integer.parseInt(element.getAttributeValue("d_cov"));
		this.branchCount = Integer.parseInt(element.getAttributeValue("d_total"));
		this.timestamp = Long.parseLong(element.getAttributeValue("mtime"));
		this.risk = this.branchCount - this.coveredBranchCount;
		registerParent(dir);

		for (Object elementObject : element.getChildren()) {

			Element fnElement = (Element) elementObject;
			if (!fnElement.getName().equals("fn")) {
				continue;
			}
			SrcFunction srcFunction = new SrcFunction();
			this.functions.add(srcFunction.init(fnElement));

			this.decisionPoints.add(new SrcFunctionDecisionPoint(srcFunction.line, srcFunction.covered ? DecisionCoverType.FUNCTION_CALLED
				: DecisionCoverType.FUNCTION_UNCALLED, DecisionType.FUNCTION, srcFunction.name));
			this.decisionPoints.addAll(srcFunction.decisionPoints);
		}
		
		// Sort the decisions in line order as the code output formatter needs it that way
		Collections.sort(this.decisionPoints);
		
		return this;
	}

	/**
	 * Regster parent;
	 * 
	 * @param paths
	 * @param srcFile
	 */
	private void registerParent(File path) {
		Src src = this;
		while (path != null) {
			SrcDir srcDir = BullsHtml.srcMap.get(path);
			// If not, create one.
			if (srcDir == null) {
				srcDir = new SrcDir();
				srcDir.init(path);
				BullsHtml.srcMap.put(path, srcDir);
				srcDir.child.add(src);
				src.parentDir = srcDir;
				src = srcDir;
				if (path.getParentFile() == null) {
					BullsHtml.baseList.add(srcDir);
					break;
				}
				path = path.getParentFile();
			} else {
				srcDir.child.add(src);
				src.parentDir = srcDir;
				break;
			}
		}
		incrementParent();
	}

	public String getContent() {

		try {
			return new SourcePainter().paint(this.path, this.decisionPoints, BullsHtml.sourceEncoding);
		} catch (IOException e) {
			return String.format("<div class='box'>%s is not available.</div>", this.path);
		}
	}

	@Override
	protected String getHtml() {
		ST template = BullsUtil.getTemplate("SrcFilePage");
		template.add("srcFile", this);
		return template.render();
	}

	@Override
	public boolean isWorthToPrint() {
		return true;
	}

	public int compareTo(SrcFile o) {
		return this.path.getName().compareTo(o.path.getName());
	}

	@Override
	public boolean isSrcFile() {
		return true;
	}

}
