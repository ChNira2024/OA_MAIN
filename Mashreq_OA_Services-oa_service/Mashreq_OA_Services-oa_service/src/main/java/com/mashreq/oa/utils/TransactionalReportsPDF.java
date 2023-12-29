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
import com.mashreq.oa.entity.TransactionalReportOutput;

public class TransactionalReportsPDF {
	
private List<TransactionalReportOutput> reports;
	
	//@Value("${transactionalreport.file.pdfpath}")
	private String file="/app/ownersassociation/documents/reports/OATransactions.pdf";
	
	
	Logger logger=org.slf4j.LoggerFactory.getLogger(TransactionalReportsPDF.class);

	public TransactionalReportsPDF(List<TransactionalReportOutput> reports) {
		this.reports = reports;
	}
	
	private void writeTableHeader(PdfPTable table)
	{
		PdfPCell cell=new PdfPCell();
		cell.setPadding(5);
		cell.setBackgroundColor(BaseColor.DARK_GRAY);
		
		Font font=FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		
		
		cell.setPhrase(new Phrase("Total Transaction",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("In-Progress",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Exception",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Approved",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Rejected",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("User Name",font));
		table.addCell(cell);
		
	}
	
	private void writeTableData(PdfPTable table)
	{
		for(TransactionalReportOutput report:reports)
		{
			table.addCell(String.valueOf(report.getTotalTransaction()));
			table.addCell(String.valueOf(report.getInProgress()));
			table.addCell(String.valueOf(report.getException()));
			table.addCell(String.valueOf(report.getApproved()));
			table.addCell(String.valueOf(report.getRejected()));
			table.addCell(String.valueOf(report.getUserName()));
			
		}
	}
	
	public String export() throws DocumentException, IOException
	{
		Document document= new Document(PageSize.A4);
		File file1=File.createTempFile(file, null);
		PdfWriter.getInstance(document, new FileOutputStream(file1));	
		
		document.open();
		
		PdfPTable table =new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setSpacingBefore(15);
		table.setWidths(new float[] {3.0f,3.0f,3.0f,3.0f,3.0f,5.0f});
		
		writeTableHeader(table);
		writeTableData(table);
		
		document.add(table);
		
		logger.info("Pdf created");
		
		document.close();
		
		
		
		byte[] base64=Files.readAllBytes(file1.toPath());
		
		String base64Str=Base64.getEncoder().encodeToString(base64);
		
		return base64Str;
	}

	
	
}
