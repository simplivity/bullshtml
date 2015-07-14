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
import java.util.ArrayList;
import java.util.List;

import org.stringtemplate.v4.ST;

/**
 * Directory coverage information
 * 
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class SrcDir extends Src implements Comparable<SrcDir> {

	public SrcDir() {
	}

	public SrcDir init(File path) {
		this.path = path;
		this.setNormalizedPath(BullsUtil.normalizePath(path));
		return this;
	}

	public int fileCount;
	public ArrayList<Src> child = new ArrayList<Src>();

	public List<Src> getChildSrcDir() {
		List<Src> srcDirList = new ArrayList<Src>();
		for (Src subDir : this.child) {
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
		for (Src sub : this.child) {
			if (sub instanceof SrcFile) {
				srcList.add(sub);
			}
		}
		return srcList;
	}

	@Override
	protected String getHtml() {
		ST template = BullsUtil.getTemplate("SrcDirPage");
		template.add("srcDir", this);
		return template.render();
	}

	public void generateChildHtml(File outputPath) {
		for (Src src : this.child) {
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
		for (Src each : this.child) {
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
		this.child.addAll(baseList);
		int fileCount = 0;
		for (SrcDir eachChild : baseList) {
			eachChild.parentDir = this;
			eachChild.incrementParent();
			fileCount += eachChild.fileCount;
		}
		this.fileCount = fileCount;
	}
}
