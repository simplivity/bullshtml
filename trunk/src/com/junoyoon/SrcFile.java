/**
 Copyright 2008 JunHo Yoon

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.junoyoon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.jdom.Element;

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
		return functions.size();
	}
	
	public boolean isModified() {
		return this.timestamp < this.path.lastModified();
	}
	
	public String getUnixStylePath() throws IOException {
		return path.getCanonicalPath().replace("\\", "/");
	}
	
	public SrcFile init(File dir, Element element) {
		String name = element.getAttributeValue("name");
		try {
			this.path = new File(dir, name).getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setNormalizedPath(BullsUtil.normalizePath(this.path));
		super.coveredFunctionCount = Integer.parseInt(element.getAttributeValue("fn_cov"));
		super.functionCount = Integer.parseInt(element.getAttributeValue("fn_total"));
		super.coveredBranchCount = Integer.parseInt(element.getAttributeValue("d_cov"));
		super.branchCount = Integer.parseInt(element.getAttributeValue("d_total"));
		this.timestamp = Long.parseLong(element.getAttributeValue("mtime"));
		risk = branchCount - coveredBranchCount;
		registerParent(dir, this);
		for (Object elementObject : element.getChildren("fn")) {
			Element fnElement = (Element) elementObject;
			SrcFunction srcFunction = new SrcFunction();
			functions.add(srcFunction.init(fnElement));
			decisionPoints.add(new SrcFunctionDecisionPoint(srcFunction.line, srcFunction.covered ? DecisionCoverType.FUNCTION_CALLED
					: DecisionCoverType.FUNCTION_UNCALLED, DecisionType.FUNCTION, srcFunction.name));
			decisionPoints.addAll(srcFunction.decisionPoints);
		}
		return this;
	}

	/**
	 * Regster parent;
	 * 
	 * @param paths
	 * @param srcFile
	 */
	public void registerParent(File path, Src srcFile) {
		Src src = srcFile;
		while (path != null) {
			SrcDir srcDir = (SrcDir) BullsHtml.srcMap.get(path);
			// If not, create one.
			if (srcDir == null) {

				srcDir = new SrcDir(path);
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
			return new SourcePainter().paint(path, decisionPoints, BullsHtml.sourceEncoding);
		} catch (IOException e) {
			return String.format("%s is not available", this.path);
		}
	}

	@Override
	protected String getHtml() {
		StringTemplate template = BullsUtil.getTemplate("SrcFilePage");
		template.setAttribute("srcFile", this);
		return template.toString();
	}

	@Override
	public boolean isWorthToPrint() {
		return true;
	}

	public int compareTo(SrcFile o) {
		return this.path.getName().compareTo(o.path.getName());
	}

	public boolean isSrcFile() {
		return true;
	}

}
