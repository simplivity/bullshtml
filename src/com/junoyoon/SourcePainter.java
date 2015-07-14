/**
 * Copyright (C) 2009 JunHo Yoon
 *
 * bullshtml is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * bullshtml is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 */
package com.junoyoon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.uwyn.jhighlight.highlighter.ExplicitStateHighlighter;
import com.uwyn.jhighlight.renderer.CppXhtmlRenderer;
import com.uwyn.jhighlight.tools.StringUtils;

/**
 * 
 * @author iceize at NHN Corporation
 */
public class SourcePainter {

	/**
	 * Constructor
	 */
	public SourcePainter() {

	}

	public String getColumnString(String a, int startIndex, int endIndex) {
		StringBuffer buffer = new StringBuffer();
		int rawIndex = 0;
		for (int i = 0; i < a.length(); i++) {
			char charAt = a.charAt(i);
			if (charAt != ' ') {
				rawIndex++;
				if (endIndex != 0 && rawIndex > endIndex) {
					break;
				} else if (rawIndex <= startIndex) {
					buffer.append(' ');
				} else {
					buffer.append(charAt);
				}
			} else if (charAt == ' ') {
				buffer.append(charAt);
			}
		}
		return buffer.toString();
	}

	/**
	 * Custom Renderer which enables line separation.
	 */
	class CustomCppXhtmlRenderer extends CppXhtmlRenderer {
		String token;
		int length;
		int style;
		String css_class;
		int previous_style = 0;
		boolean newline = false;

		public StringBuffer highlight(List<?> lines, PeekingIterator<SrcDecisionPoint> peekingIterator) throws IOException {
			ExplicitStateHighlighter highlighter = getHighlighter();
			StringBuffer w = new StringBuffer();
			String line;
			this.length = 0;
			this.style = 0;
			this.css_class = null;
			this.previous_style = 0;
			this.newline = false;
			SrcDecisionPoint decisionPoint = peekingIterator.next();
			w.append("<div style=\"overflow-x:scroll;\">").append("\n");
			w.append("<table class=\"source\">").append("\n");
			w.append("<colgroup>").append("\n");
			w.append("<col width=\"40\"/>").append("\n");
			w.append("<col width=\"40\"/>").append("\n");
			w.append("<col width=\"\"/>").append("\n");

			w.append("</colgroup>").append("\n");

			int lineCount = 0;
			for (Object eachLineObject : lines) {
				line = (String) eachLineObject;
				// line += "\n";
				line = StringUtils.convertTabsToSpaces(line, 4);
				lineCount++;
				if (lineCount == decisionPoint.line) {
					char count = 'a';
					int curColumn = 0;
					int nextColumn = 0;
					do {
						String remainedLine = line;
						w.append("<tr class=\"").append(decisionPoint.decisionCoverType.getLineCss()).append("\">").append("\n");
						w.append("<td class=\"line\">").append(lineCount);
						if (decisionPoint.sequence) {
							w.append("-").append(count++);
						}
						if (decisionPoint.decisionType == DecisionType.FUNCTION) {
							w.append("<a name='").append(lineCount).append("'/>");
						}
						w.append("</td>").append("\n");
						if (decisionPoint.sequence) {
							if (peekingIterator.hasNext()) {
								nextColumn = peekingIterator.peek().column;
							} else {
								nextColumn = -1;
							}
							remainedLine = getColumnString(line, curColumn, nextColumn);
							curColumn = nextColumn;
						}
						w.append("<td class=\"line\">").append(decisionPoint.decisionCoverType.getHtml()).append("</td>").append("\n");
						w.append("<td class=\"code\">");
						renderALine(remainedLine.toString(), highlighter, w);
						w.append("</td>").append("\n");
					} while (peekingIterator.hasNext() && lineCount == (decisionPoint = peekingIterator.next()).line);
				} else {
					w.append("<tr class=\"kwnone\">").append("\n");
					w.append("<td class=\"line\">")
					    .append(lineCount)
 						.append("<a name=\"").append(lineCount).append("\"/>")
						.append("</td>")
						.append("\n");
					w.append("<td class=\"line\">").append("</td>").append("\n");
					w.append("<td class=\"code\">");
					renderALine(line, highlighter, w);
					w.append("</td>").append("\n");
				}

				w.append("</tr>").append("\n");
				this.newline = true;
			}
			w.append("</table>").append("\n");
			w.append("</div>");
			return w;
		}

		public void renderALine(String line, ExplicitStateHighlighter highlighter, StringBuffer output) throws IOException {
			Reader lineReader = new StringReader(line);
			highlighter.setReader(lineReader);
			int index = 0;
			while (index < line.length()) {
				//
				this.style = highlighter.getNextToken();
				this.length = highlighter.getTokenLength();
				this.token = line.substring(index, index + this.length);

				if (this.style != this.previous_style || this.newline) {
					this.css_class = getCssClass(this.style);

					if (this.css_class != null) {
						if (this.previous_style != 0 && !this.newline) {
							output.append("</span>");
						}
						output.append("<span class=\"" + this.css_class + "\">");

						this.previous_style = this.style;
					}
				}
				this.newline = false;
				output.append(StringUtils.replace(StringUtils.encodeHtml(StringUtils.replace(this.token, "\n", "")), " ", "&nbsp;"));
				index += this.length;
			}
			output.append("</span>\n");

		}
	}

	private final CustomCppXhtmlRenderer renderer = new CustomCppXhtmlRenderer();

	/**
	 * paint source code with decision point
	 **/
	public String paint(File file, List<SrcDecisionPoint> decisionPoints, Encoding encoding) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		PeekingIterator<SrcDecisionPoint> peekingIterator = BullsUtil.peekingIterator(decisionPoints.iterator());
		StringBuffer sb = this.renderer.highlight(IOUtils.readLines(inputStream, encoding.getEncodingKey()), peekingIterator);
		IOUtils.closeQuietly(inputStream);
		return sb.toString();
	}
}
