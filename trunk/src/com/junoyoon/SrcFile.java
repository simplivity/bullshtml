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

import org.jdom.Element;

/**
 * File coverage information
 * 
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class SrcFile extends Src {
	public int risk;

	// private static String fileNotFoundTemplate;
	// static {
	// fileNotFoundTemplate =
	// BullsUtil.loadResourceContent("html/SrcFileNotFound.html");
	// }

	public SrcFile(File dir, Element element) {
		String name = element.getAttributeValue("name");
		try {
			this.path = new File(dir, name).getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.normalizedPath = BullsUtil.normalizePath(this.path);
		super.coveredFunctionCount = Integer.parseInt(element.getAttributeValue("fn_cov"));
		super.functionCount = Integer.parseInt(element.getAttributeValue("fn_total"));
		super.coveredBranchCount = Integer.parseInt(element.getAttributeValue("d_cov"));
		super.branchCount = Integer.parseInt(element.getAttributeValue("d_total"));
		risk = branchCount - coveredBranchCount;
		registerParent(dir, this);
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

	public void incrementParent() {
		SrcDir currentParent = parentDir;
		while (currentParent != null) {
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
		return String
				.format(
						"<tr><td><a href='%s.html'>%s</a></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div %s><span class='text'>%d/%d</span></div></div></td></tr></table></td><td><table cellpadding='0px' cellspacing='0px' class='percentgraph'><tr class='percentgraph'><td align='right' class='percentgraph' width='40'>%s%%</td><td class='percentgraph'><div class='percentgraph'><div %s><span class='text'>%d/%d</span></div></div></td></tr></table></td></tr>",
						this.normalizedPath, path.getName(), getFunctionCoverage(), getFunctionCoverageStyle(), coveredFunctionCount, functionCount,
						getBranchCoverage(), getBranchCoverageStyle(), coveredBranchCount, branchCount);
	}

	@Override
	protected String getHtml() {
		try {
			return new SourcePainter().paint(path, Encoding.UTF_8);
		} catch (IOException e) {
			return "";
		}
	}

	@Override
	protected boolean isWorthToPrint() {
		return true;
	}
}
