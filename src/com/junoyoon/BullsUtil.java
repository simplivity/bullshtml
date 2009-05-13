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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class
 * 
 * @author JunHo Yoon (junoyoon@gmail.com)
 */
public class BullsUtil {
	/**
	 * Make path to file form.
	 * 
	 * @param path
	 * @return normalized path
	 */
	public static String normalizePath(String path) {

		return path.replace(" ", "_").replace(":", "_").replace("\\", "_").replace("/", "_");

	}

	/**
	 * Write file
	 * 
	 * @param path
	 *            the path
	 * @param content
	 *            content
	 */
	public static void writeToFile(String path, String content) {

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(path);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			BullsHtml.printErrorAndExit(e.getMessage());
		}

	}

	/**
	 * Copy resource from jar to destination
	 * 
	 * @param toDir
	 * @param fileName
	 */
	public static void copyResource(String toDir, String fileName) {

		File file = new File(toDir);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		}

		try {
			InputStream is = BullsUtil.class.getClassLoader().getResourceAsStream(fileName);
			BufferedInputStream inputStream = new BufferedInputStream(is);
			FileOutputStream fs = new FileOutputStream(toDir);
			BufferedOutputStream outputStream = new BufferedOutputStream(fs);

			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.close();
			inputStream.close();

		} catch (Exception e) {
			BullsHtml.printErrorAndExit(e.getMessage());

		}

	}

	/**
	 * Run command and
	 * 
	 * @param cmd
	 * @return
	 */
	public static String getCmdOutput(String cmd) {

		StringBuilder result = new StringBuilder(1024);
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
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
		} catch (IOException e) {

			return null;

			// BullsHtml.printErrorAndExit("covbr command in bullseye coverage is not available. please check the path.");
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
			BullsHtml.printErrorAndExit(e.getMessage());
		}

		return result.toString();

	}
}
