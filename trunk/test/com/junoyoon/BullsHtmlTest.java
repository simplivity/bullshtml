package com.junoyoon;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.jdom.JDOMException;
import org.junit.Test;

import com.uwyn.jhighlight.tools.StringUtils;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

public class BullsHtmlTest {
	@Test
	public void testProcessInternal() throws JDOMException, IOException {
		File file = new File("test/coverage.xml");
		FileReader reader = new FileReader(file);
		BullsHtml bullsHtml = new BullsHtml();
		bullsHtml.processInternal(reader);
		new File("output").mkdir();
		bullsHtml.generateHtml(new File("output"));
		bullsHtml.copyResources("output");
		bullsHtml.generateCloverXml(new File("output"));
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
	public void testEncodedName( ) {
		SrcFunction function = new SrcFunction();
		function.name = "&hello<world>";
		assertThat(function.getXmlEncodedName(), is("&amp;hello&lt;world&gt;"));
	}
}
