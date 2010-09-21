package com.junoyoon;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;


public class FileTest {
	@Test
	public void testFile() throws IOException {
		File file = new File("c:\\hello\\wewe", "..");
		System.out.println(file.getCanonicalFile().getAbsolutePath());
		File root = new File("c:\\");
		System.out.println(root.getParent());
		assertThat(root.isAbsolute(), is(true));
		File wow = new File("/hello/wow");
	System.out.println(wow.getAbsolutePath());
	System.out.println(FilenameUtils.getPath("/ewe/wewe"));
//		assertThat(wow.isAbsolute(), is(true));
	}
}
