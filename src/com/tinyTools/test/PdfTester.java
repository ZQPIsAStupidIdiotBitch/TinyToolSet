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
 * 
 * @author Administrator
 *
 */
public class PdfTester {

	private static final String[] HEAD_ARR = { "_A", "_B", "_C" };

	public static void main(String[] args) throws IOException {
		try {
			// 创建文档
			Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
			// PDF操作器
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("E:\\ITextTest.pdf"));
			document.open();

			// 汉字字体
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			Font boldFontChinese = new Font(bfChinese, 12, Font.BOLD, BaseColor.BLACK);

			// 表单
			PdfPTable t = createTbl(boldFontChinese);

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

			for (int i = 0; i < 1000; i++) {
				if (i % 27 == 0) {
					document.add(t);
					document.newPage();
					t = createTbl(boldFontChinese);
				}
				for (int j = 0; j < 3; j++) {
					cell = new PdfPCell(new Phrase("单元格" + i + "行" + j + "列", boldFontChinese));
					t.addCell(cell);
				}
			}
			
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

	private static PdfPTable createTbl(Font boldFontChinese) {
		PdfPTable tbl = new PdfPTable(3);
		tbl.setSpacingBefore(25);
		tbl.setSpacingAfter(25);
		tbl.setWidthPercentage(100);

		for (int i = 0; i < 3; i++) {
			PdfPCell cell = new PdfPCell(new Phrase("表头_" + (i + 1) + HEAD_ARR[i], boldFontChinese));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tbl.addCell(cell);
		}

		return tbl;
	}

}
