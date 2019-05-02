package com.junoyoon;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import org.jdom2.JDOMException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class BullsHtmlV4Test {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testProcessInternal() throws JDOMException, IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("coverage_v4.xml");
		InputStreamReader reader = new InputStreamReader(is);
		BullsHtml bullsHtml = new BullsHtml();
		bullsHtml.processInternal(reader);
        File o = folder.newFolder("output");
        assertThat(o.exists(), is(true));
		bullsHtml.generateCloverXml(o);
	}
}
