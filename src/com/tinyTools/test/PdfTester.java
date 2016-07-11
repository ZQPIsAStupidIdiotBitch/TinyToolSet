package com.tinyTools.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 默认的iText字体设置不支持中文字体，需要下载远东字体包iTextAsian.jar
 * @author Administrator
 *
 */
public class PdfTester {

	public static void main(String[] args) throws IOException {
		try {
			// 创建文档
			Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
			// PDF操作器
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("E:\\ITextTest.pdf"));
			document.open();
			
			// 汉字字体
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",  
                    "UniGB-UCS2-H", false);  
            Font boldFontChinese = new Font(bfChinese, 12, Font.BOLD,  
                    BaseColor.BLACK);

			// 表单
			PdfPTable t = new PdfPTable(3);
			t.setSpacingBefore(25);
			t.setSpacingAfter(25);
			t.setWidthPercentage(100);

			PdfPCell c1 = new PdfPCell(new Phrase("表头 1A", boldFontChinese));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t.addCell(c1);

			PdfPCell c2 = new PdfPCell(new Phrase("表头 2B", boldFontChinese));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t.addCell(c2);

			PdfPCell c3 = new PdfPCell(new Phrase("表头 3C", boldFontChinese));
			c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t.addCell(c3);

			t.addCell(new PdfPCell(new Phrase("1.1")));
			t.addCell(new PdfPCell(new Phrase("1.2")));
			PdfPCell cell = new PdfPCell(new Phrase("1.3"));
			cell.setRowspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			t.addCell(cell);
			
			cell = new PdfPCell(new Phrase("1.4"));
			cell.setColspan(2);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			t.addCell(cell);

			document.add(t);
			// TODO

			document.close();
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		System.out.println("PDF writed!");

	}

}
