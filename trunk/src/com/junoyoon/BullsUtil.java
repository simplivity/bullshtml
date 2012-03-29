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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Utility class
 * 
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class BullsUtil {
	public static StringTemplateGroup group = new StringTemplateGroup("mygroup");

	public static StringTemplate getTemplate(String templateName) {
		return BullsUtil.group.getInstanceOf("template/" + templateName);
	}

	/**
	 * Make path to file form.
	 * 
	 * @param path
	 * @return normalized path
	 */
	public static String normalizePath(File path) {
		try {
			return normalizePath(path.getCanonicalPath());
		} catch (IOException e) {
			return normalizePath(path.getAbsolutePath());
		}
	}

	/**
	 * Make path to file form.
	 * 
	 * @param path
	 * @return normalized path
	 */
	public static String normalizePath(String path) {
		return path.replace(" ", "_").replace(":", "_").replace("\\", "_").replace("/", "_").replace(".", "_");
	}

	/**
	 * Write file
	 * 
	 * @param path
	 *            the path
	 * @param content
	 *            content
	 */
	public static void writeToFile(File path, String content) {
		try {
			FileUtils.writeStringToFile(path, content, "UTF-8");
		} catch (IOException e) {
			BullsHtml.printErrorAndExit(e);
		}
	}

	public static void copyResource(String toDir, String fileName) throws IOException {
		File file = new File(toDir);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			InputStream is = BullsUtil.class.getClassLoader().getResourceAsStream(fileName);
			FileOutputStream fs = new FileOutputStream(toDir);
			IOUtils.copy(is, fs);
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fs);
		} catch (Exception e) {
			BullsHtml.printErrorAndExit(e);
		}
	}

	/**
	 * Run command and
	 * 
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public static String getCmdOutput(List<String> cmd) throws IOException {
		ProcessBuilder builder = new ProcessBuilder();
		builder.command().addAll(cmd);
		builder.redirectErrorStream(true);
		StringBuilder result = new StringBuilder(1024);
		Process proc = builder.start();
		boolean firstLine = true;
		for (Object eachLineObject : IOUtils.readLines(proc.getInputStream())) {
			String eachLine = (String) eachLineObject;
			if (firstLine) {
				eachLine = eachLine.replace("charset=us-ascii", "charset=" + Constant.DEFAULT_ENCODING);
				firstLine = false;
			}
			result.append(eachLine).append("\n");
		}
		return result.toString();
	}

	/**
	 * Load Resource
	 * 
	 * @param resourceLocation
	 * @return
	 */
	public static String loadResourceContent(String resourceLocation) {
		StringBuilder result = new StringBuilder(1024);
		try {
			InputStream is = BullsUtil.class.getClassLoader().getResourceAsStream(resourceLocation);
			InputStreamReader inputStream = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(inputStream);
			String buffer;
			while ((buffer = br.readLine()) != null) {
				result.append(buffer).append("\n");
			}
			br.close();
		} catch (Exception e) {
			BullsHtml.printErrorAndExit(e);
		}
		return result.toString();
	}

	public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
		if (iterator instanceof PeekingIterator<?>) {
			@SuppressWarnings("unchecked")
			PeekingIterator<T> peeking = (PeekingIterator<T>) iterator;
			return peeking;
		}
		return new PeekingIterator<T>(iterator);
	}
}
