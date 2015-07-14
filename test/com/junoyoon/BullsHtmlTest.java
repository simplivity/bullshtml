package com.junoyoon;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.jdom2.JDOMException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;

import static org.junit.Assert.assertThat;

public class BullsHtmlTest {
	@Test
	public void testProcessInternal() throws JDOMException, IOException {
		File file = new File("test/coverage.xml");
		FileReader reader = new FileReader(file);
		BullsHtml bullsHtml = new BullsHtml();
		bullsHtml.processInternal(reader);
		File o = new File("output");
		o.mkdir();
		bullsHtml.generateHtml(o);
		assertThat(o.exists(), is(true));
		assertThat(new File(o, "index.html").exists(), is(true));
		bullsHtml.copyResources("output");
		assertThat(new File(o, "js").exists(), is(true));
		assertThat(new File(o, "js/popup.js").exists(), is(true));
		bullsHtml.generateCloverXml(o);
		assertThat(new File(o, "clover.xml").exists(), is(true));
		assertThat(new File(o, "clover.xml").length(), not(0L));

	}

	@Test
	public void testProcessInternal2() throws JDOMException, IOException {
		File file = new File("test/output.xml");
		FileReader reader = new FileReader(file);
		BullsHtml bullsHtml = new BullsHtml();
		bullsHtml.processInternal(reader);
		new File("output").mkdir();
		for (SrcDir srcDir : BullsHtml.baseList) {
			assertThat(srcDir.path, notNullValue());
		}
	}

	@Test
	public void testColumnString() throws URISyntaxException {
		SourcePainter painter = new SourcePainter();
		assertThat(painter.getColumnString("  hello world", 0, 5), is("  hello "));
		assertThat(painter.getColumnString(" test world", 4, 0), is("      world"));
	}

	@Test
	public void testSystemDefaultEncoding() {
		System.out.println(java.nio.charset.Charset.defaultCharset().name());
	}

	@Test
	public void testEncodedName() {
		SrcFunction function = new SrcFunction();
		function.name = "&hello<world>";
		assertThat(function.getXmlEncodedName(), is("&amp;hello&lt;world&gt;"));
	}
}
