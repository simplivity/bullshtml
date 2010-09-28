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
import java.util.ArrayList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;

/**
 * Directory coverage information
 * 
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class SrcDir extends Src implements Comparable<SrcDir> {
	private static String cloverXmlTemplate;

	public SrcDir(File path) {
		this.path = path;
		this.setNormalizedPath(BullsUtil.normalizePath(path));
	}

	public int fileCount;
	public ArrayList<Src> child = new ArrayList<Src>();

	public List<Src> getChildSrcDir() {
		List<Src> srcDirList = new ArrayList<Src>();
		for (Src subDir : child) {
			if (subDir instanceof SrcDir) {
				Src eachDir = subDir;
				while (!eachDir.isWorthToPrint()) {
					eachDir = ((SrcDir) eachDir).child.get(0);
				}
				eachDir.parentDir = this;
				srcDirList.add(eachDir);
			}
		}
		return srcDirList;
	}

	public List<Src> getChildSrcFile() {
		List<Src> srcList = new ArrayList<Src>();
		for (Src sub : child) {
			if (sub instanceof SrcFile) {
				srcList.add(sub);
			}
		}
		return srcList;
	}

	@Override
	protected String getHtml() {
		StringTemplate template = BullsUtil.getTemplate("SrcDirPage");
		template.setAttribute("srcDir", this);
		return template.toString();
	}

	public void generateChildHtml(File outputPath) {
		for (Src src : child) {
			src.generateHtml(outputPath);
			if (src instanceof SrcDir) {
				((SrcDir) src).generateChildHtml(outputPath);
			}
		}
	}

	@Override
	public boolean isWorthToPrint() {
		return !BullsHtml.isSingleElement(this);
	}

	public boolean containFiles() {
		for (Src each : child) {
			if (each instanceof SrcFile) {
				return true;
			}
		}
		return false;
	}


	public int compareTo(SrcDir o) {
		return this.path.compareTo(o.path);
	}

	public void addChildren(ArrayList<SrcDir> baseList) {
		child.addAll(baseList);
		int fileCount = 0;
		for (SrcDir eachChild : baseList) {
			eachChild.parentDir = this;
			eachChild.incrementParent();
			fileCount += eachChild.fileCount;
		}
		this.fileCount = fileCount;

	}
}
