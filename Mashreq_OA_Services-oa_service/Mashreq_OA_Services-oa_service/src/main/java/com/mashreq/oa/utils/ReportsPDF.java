package com.mashreq.oa.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mashreq.oa.entity.ReportsOutput;

public class ReportsPDF {
	
private List<ReportsOutput> reports;
	
	//@Value("${report.file.pdfpath}")
	String file="/app/ownersassociation/documents/reports/OAReports.pdf";
	
	Logger logger=org.slf4j.LoggerFactory.getLogger(ReportsPDF.class);

	public ReportsPDF(List<ReportsOutput> reports) {
		this.reports = reports;
	}
	
	public ReportsPDF() {
		super();
	}
	
	private void writeTableHeader(PdfPTable table)
	{
		PdfPCell cell=new PdfPCell();
		cell.setPadding(5);
		cell.setBackgroundColor(BaseColor.DARK_GRAY);
		
		Font font=FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		
		
		cell.setPhrase(new Phrase("Management Company",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Supplier",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Budget Year",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Invoice Year",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Total Budget",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Consumed Amount",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Balance Amount",font));
		table.addCell(cell);
		
	}
	
	private void writeTableData(PdfPTable table)
	{
		for(ReportsOutput report:reports)
		{
			table.addCell(report.getMcNameEn());
			table.addCell(report.getSupplierName());
			table.addCell(String.valueOf(report.getBudgetYear()));
			table.addCell(String.valueOf(report.getInvoiceYear()));
			table.addCell(String.valueOf(report.getTotalBudget()));
			table.addCell(String.valueOf(report.getConsumedAmount()));
			table.addCell(String.valueOf(report.getBalanceAmount()));
			
		}
		
	}
	
	public String export() throws DocumentException, IOException
	{
		Document document= new Document(PageSize.A4);
		
		File file1=File.createTempFile(file, null);
	//	PdfWriter.getInstance(document, response.getOutputStream());
		PdfWriter.getInstance(document, new FileOutputStream(file1));
		
		//File file1=new File(file);
		
		
		
		document.open();
		
		PdfPTable table =new PdfPTable(7);
		table.setWidthPercentage(100);
		table.setSpacingBefore(15);
		table.setWidths(new float[] {4.5f,4.5f,2.0f,2.0f,2.0f,3.0f,2.5f});
		
		writeTableHeader(table);
		writeTableData(table);
		
		document.add(table);
		
		logger.info("Pdf created");
		
		document.close();
		
		
		
		byte[] base64=Files.readAllBytes(file1.toPath());
		
		String base64Str=Base64.getEncoder().encodeToString(base64);
		
		return base64Str;
	}

	public String getPDF(String base64Path) throws DocumentException, IOException
	{
		String actualPath = decrypt(base64Path);
		
		byte[] base64=Files.readAllBytes((new File(actualPath)).toPath());
		
		String base64Str=Base64.getEncoder().encodeToString(base64);
		
		return base64Str;
	}
	public String decrypt(String base64URL) {
		
		String encodePath = base64URL.substring(base64URL.lastIndexOf("?") + 10);
		System.out.println("Encoded Path  is::" + encodePath);
		byte[] decodedByte = org.apache.commons.codec.binary.Base64.decodeBase64(encodePath.getBytes());
		System.out.println("Original Path  is::" + new String(decodedByte));
		return new String(decodedByte);
	}
	
}
