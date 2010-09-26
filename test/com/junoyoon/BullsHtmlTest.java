package com.junoyoon;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.antlr.stringtemplate.StringTemplate;
import org.jdom.JDOMException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

public class BullsHtmlTest {
	@Test
	public void testProcessInternal() throws JDOMException, IOException {
		
		System.out.println(new File("c:").getCanonicalFile().getPath());;
		
		File file = new File("test/coverage.xml");
		FileReader reader = new FileReader(file);
		try {
			BullsHtml bullsHtml = new BullsHtml();
			bullsHtml.processInternal(reader);
			for (SrcDir src : bullsHtml.srcMap.values()) {
				System.out.println(src.path);
				for (Src child : src.child) {
					System.out.println("--" + child.path);
				}
			}
			System.out.println("---");
			for (Src src : bullsHtml.srcFileList) {
				System.out.println(src.path);
			}
			System.out.println("---");
			for (Src src : bullsHtml.baseList) {
				System.out.println(src.path);
			}
			new File("output").mkdir();
			bullsHtml.generateHtml(new File("output"));
			bullsHtml.copyResources("output");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStringTemplate() {
		StringTemplate a = new StringTemplate("$b:{a|$a$, }$");
		a.setAttribute("b", new ArrayList<Integer>() {{ add(1); add(2);}});
		System.out.println(a.toString());;
		a.reset();
		a.setAttribute("", new ArrayList<Integer>() {{ add(3); add(22);}});
		System.out.println(a.toString());;
		
		StringTemplate tem = new StringTemplate("$a.size$");
		tem.setAttribute("a", new ArrayList<Integer>() {{ add(3); add(22);}});
		
		System.out.println(tem.toString());
		
	}
	
	@Test
	public void testWow() {
		StringTemplate a = new StringTemplate("$b.size()");
	}

	@Test
	public void testColumnString() throws URISyntaxException {
		SourcePainter painter = new SourcePainter();
		assertThat(painter.getColumnString("  hello world", 0, 5), is("  hello "));
		assertThat(painter.getColumnString(" test world", 4, 0), is("      world"));
	}
}
