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
		return group.getInstanceOf("template/" + templateName);
	}

	/**
	 * Make path to file form.
	 * 
	 * @param path
	 * @return normalized path
	 */
	public static String normalizePath(File path) {
		return path.getAbsolutePath().replace(" ", "_").replace(":", "_").replace("\\", "_").replace("/", "_");
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
		System.out.println(fileName);
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
	 */
	public static String getCmdOutput(List<String> cmd) {
		ProcessBuilder builder = new ProcessBuilder();
		builder.command().addAll(cmd);
		builder.redirectErrorStream(true);
		StringBuilder result = new StringBuilder(1024);
		try {
			Process proc = builder.start();
			InputStreamReader inputStream = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(inputStream);
			String buffer;
			int i = 0;
			while ((buffer = br.readLine()) != null) {
				if (++i < 6)
					buffer = buffer.replace("charset=us-ascii", "charset=euc-kr");
				result.append(buffer).append("\n");
			}
			br.close();
			if (i < 11) {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
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
