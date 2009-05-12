package com.junoyoon;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BullsUtil {
	public static String normalizePath(String path) {
		return path.replace(" ", "_").replace(":", "_").replace("\\", "_").replace("/", "_");
	}

	public static void writeToFile(String path, String content) {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(path);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

			// 버퍼를 통한 스트림 쓰기
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.close();
			inputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getCmdOutput(String cmd) {
		StringBuffer result = new StringBuffer(1024);
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader inputStream = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(inputStream);
			String buffer;
			int i = 0;
			while ((buffer = br.readLine()) != null) {
				if (++i < 10)
					buffer = buffer.replace("charset=us-ascii", "charset=euc-kr");
				result.append(buffer).append("\n");
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();

	}

	public static String loadResourceContent(String resourceLocation) {
		StringBuffer result = new StringBuffer(1024);
		try {
			InputStream is = BullsUtil.class.getClassLoader().getResourceAsStream(resourceLocation);
			InputStreamReader inputStream = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(inputStream);
			// 버퍼를 통한 스트림 쓰기
			String buffer;
			while ((buffer = br.readLine()) != null) {
				result.append(buffer).append("\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
