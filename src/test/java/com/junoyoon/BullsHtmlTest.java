package com.junoyoon;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.jdom2.JDOMException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class BullsHtmlTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testProcessInternal() throws JDOMException, IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("coverage.xml");
		InputStreamReader reader = new InputStreamReader(is);
		BullsHtml bullsHtml = new BullsHtml();
		bullsHtml.processInternal(reader);
		File o = folder.newFolder("output");
        assertThat(o.exists(), is(true));
        bullsHtml.generateHtml(o);
        assertThat(new File(o, "index.html").exists(), is(false));
		bullsHtml.copyResources(o.getAbsolutePath());
        assertThat(new File(o, "index.html").exists(), is(true));
		assertThat(new File(o, "js").exists(), is(true));
		assertThat(new File(o, "js/popup.js").exists(), is(true));
		bullsHtml.generateCloverXml(o);
		assertThat(new File(o, "clover.xml").exists(), is(true));
		assertThat(new File(o, "clover.xml").length(), not(0L));
	}

	@Test
	public void testProcessInternal2() throws JDOMException, IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("output.xml");
		InputStreamReader reader = new InputStreamReader(is);
		BullsHtml bullsHtml = new BullsHtml();
		bullsHtml.processInternal(reader);
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
