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
	 * Custom Renderer which enables line speration.
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
			length = 0;
			style = 0;
			css_class = null;
			previous_style = 0;
			newline = false;
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
						if (decisionPoint.decisionType == DecisionType.FUNCTION ) {
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
					w.append("<td class=\"line\">").append(lineCount).append("</td>").append("\n");
					w.append("<td class=\"line\">").append("</td>").append("\n");
					w.append("<td class=\"code\">");
					renderALine(line, highlighter, w);
					w.append("</td>").append("\n");
				}

				w.append("</tr>").append("\n");
				newline = true;
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
				style = highlighter.getNextToken();
				length = highlighter.getTokenLength();
				token = line.substring(index, index + length);

				if (style != previous_style || newline) {
					css_class = getCssClass(style);

					if (css_class != null) {
						if (previous_style != 0 && !newline) {
							output.append("</span>");
						}
						output.append("<span class=\"" + css_class + "\">");

						previous_style = style;
					}
				}
				newline = false;
				output.append(StringUtils.replace(StringUtils.encodeHtml(StringUtils.replace(token, "\n", "")), " ", "&nbsp;"));
				index += length;
			}
			output.append("</span>\n");

		}
	}

	CustomCppXhtmlRenderer renderer = new CustomCppXhtmlRenderer();

	/**
	 * paint source code which decision point 
	 **/
	public String paint(File file, List<SrcDecisionPoint> decisionPoints, Encoding encoding) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		PeekingIterator<SrcDecisionPoint> peekingIterator = BullsUtil.peekingIterator(decisionPoints.iterator());
		StringBuffer sb = renderer.highlight(IOUtils.readLines(inputStream, encoding.getEncodingKey()), peekingIterator);
		IOUtils.closeQuietly(inputStream);
		return sb.toString();
	}
}
