package com.junoyoon;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jdom2.JDOMException;
import org.junit.Test;

public class BullsHtmlV4Test {
	@Test
	public void testProcessInternal() throws JDOMException, IOException {
		File file = new File("test/coverage_v4.xml");
		FileReader reader = new FileReader(file);
		BullsHtml bullsHtml = new BullsHtml();
		bullsHtml.processInternal(reader);
		File o = new File("output");
		o.mkdir();
		bullsHtml.generateCloverXml(o);
	}
}
