package com.junoyoon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.uwyn.jhighlight.renderer.CppXhtmlRenderer;
import com.uwyn.jhighlight.renderer.GroovyXhtmlRenderer;
import com.uwyn.jhighlight.renderer.JavaXhtmlRenderer;
import com.uwyn.jhighlight.renderer.Renderer;
import com.uwyn.jhighlight.renderer.XmlXhtmlRenderer;

/**
 * 
 * @author iceize at NHN Corporation
 */
public class SourcePainter {

	/**
	 * 생성자
	 * 
	 * @param build
	 *            {@link Build} 인스턴스
	 * @param problems
	 *            Klocwork 결과
	 */
	public SourcePainter() {

	}

	/**
	 * 파일 이름을 이용하여 Renderer를 리턴한다.
	 * 
	 * @param filename
	 *            파일 이름
	 * @return {@link Renderer}
	 */
	private Renderer getRenderer(File filePath) {
		FileExtension extension = FileExtension.getExtension(FilenameUtils.getExtension(filePath.getAbsolutePath()));

		switch (extension) {
		case groovy:
			return new GroovyXhtmlRenderer();
		case cpp:
		case c:
		case h:
		case cc:
		case cxx:
			return new CppXhtmlRenderer();
		case html:
		case xml:
		case xhtml:
			return new XmlXhtmlRenderer();
		default:
			return new JavaXhtmlRenderer();
		}
	}

	/**
	 * 소스 파일을 읽어 Klocwork 결과를 포함한 HTML로 변환하여 리턴한다.
	 * 
	 * @param encoding
	 * 
	 * @param filename
	 *            소스 파일 이름
	 * @param problemLines
	 *            라인 정보
	 * @param encoding
	 *            파일 인코딩
	 * @return HTML
	 * @throws IOException
	 *             파일에 저장할 수 없는 경우 발생하는 예외
	 */
	public String paint(File file, Encoding encoding) throws IOException {

		InputStream inputStream = new FileInputStream(file);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		Renderer renderer = getRenderer(file);
		renderer.highlight(file.getAbsolutePath(), inputStream, outputStream, encoding.getEncodingKey(), true);
		StringBuffer sb = new StringBuffer();
		String[] splitted = outputStream.toString(encoding.getEncodingKey()).split("\n");
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);

		sb.append("<div style=\"overflow-x:scroll;\">").append("\n");
		sb.append("<table class=\"source\">").append("\n");
		sb.append("<colgroup>").append("\n");
		sb.append("<col width=\"40\"/>").append("\n");
		sb.append("<col width=\"\"/>").append("\n");
		sb.append("</colgroup>").append("\n");

		for (int line = 1; line < splitted.length; line++) {
			// if (line > Constant.MAXIMUM_SOURCE_LINE) {
			// break;
			// }
			sb.append("<tr class=\"kwnone\">").append("\n");
			sb.append("<td class=\"line\">" + line + "</td>").append("\n");
			sb.append("<td class=\"code\">" + splitted[line] + "</td>").append("\n");

			sb.append("</tr>").append("\n");
		}

		sb.append("</table>").append("\n");
		sb.append("</div>");

		return sb.toString();
	}
}
