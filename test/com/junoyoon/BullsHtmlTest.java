package com.junoyoon;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.antlr.stringtemplate.StringTemplate;
import org.jdom.JDOMException;
import org.junit.Test;

public class BullsHtmlTest {
	@Test
	public void testProcessInternal() throws JDOMException, IOException {
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
		a.setAttribute("b", new ArrayList<Integer>() {{ add(3); add(22);}});
		System.out.println(a.toString());;
	
	}
}
