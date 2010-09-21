package com.junoyoon;

import com.uwyn.jhighlight.tools.FileUtils;

public enum FileExtension {
	groovy, java, sh, cpp, c, h, cc, cxx, html, xml, xhtml, NONE;

	/**
	 * 파일 이름에서 확장자를 가져온다.
	 * 
	 * @param filename
	 *            파일 이름
	 * @return 확장자
	 */
	public static FileExtension getExtension(String filename) {
		String ext = FileUtils.getExtension(filename);

		if (ext == null) {
			return FileExtension.NONE;
		}

		for (FileExtension extension : FileExtension.values()) {
			if (ext.equalsIgnoreCase(extension.toString())) {
				return extension;
			}
		}

		return FileExtension.NONE;
	}
}
