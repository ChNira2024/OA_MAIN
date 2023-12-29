package com.mashreq.oa.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;

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
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.ReportsOutput;

public class CompletedRequestPdf {

	
private List<PaymentData> completedRequests;
	
	//@Value("${}")
	String file="/app/ownersassociation/documents/reports/CompletedPayments.pdf";
	
	Logger logger=org.slf4j.LoggerFactory.getLogger(CompletedRequestPdf.class);

	public CompletedRequestPdf(List<PaymentData> completedRequests) {
		this.completedRequests = completedRequests;
	}
	
	private void writeTableHeader(PdfPTable table)
	{
		PdfPCell cell=new PdfPCell();
		cell.setPadding(5);
		cell.setBackgroundColor(BaseColor.DARK_GRAY);
		
		Font font=FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(BaseColor.WHITE);
		logger.info("Setting headers");
		
		cell.setPhrase(new Phrase("Matrix Ref No",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("SubProduct",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("DebitAccountNoDesc",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Beneficiary Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Initiator Date",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Payment Currency",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Amount",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Customer Reference",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Initiator Name/Date & time",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Status",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Remarks",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Username",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Service Code",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Budget Year",font));
		table.addCell(cell);
		logger.info("Completed Setting headers");
	}
	
	private void writeTableData(PdfPTable table)
	{
		for(PaymentData compReq:completedRequests)
		{
			logger.info("Writing Data ");
			table.addCell(String.valueOf(compReq.getMatrixRefNo()));
			table.addCell(String.valueOf(compReq.getSubProduct()));
			table.addCell(String.valueOf(compReq.getDebitAccountNumberDesc()));
			table.addCell(String.valueOf(compReq.getBeneficiaryName()));
			table.addCell(String.valueOf(compReq.getDisplayInitiatorDate()));
			table.addCell(String.valueOf(compReq.getPaymentCurrency()));
			table.addCell(String.valueOf(compReq.getInvoiceAmount()));
			table.addCell(String.valueOf(compReq.getCustomerReference()));
			table.addCell(String.valueOf(compReq.getInitiatorNameDateTime()));
			table.addCell(String.valueOf(compReq.getStatus()));
			table.addCell(String.valueOf(compReq.getRemarks()));
			table.addCell(String.valueOf(compReq.getUploadedBy()));
			table.addCell(String.valueOf(compReq.getServiceCode()));
			table.addCell(String.format("%.0f",compReq.getBudgetYear()));
			logger.info("Writing Data Completed"+String.format("%.0f",compReq.getBudgetYear()));
		}
		
	}
	
	public String export() throws DocumentException, IOException
	{
		Document document= new Document(PageSize.A1);
		
		File file1=File.createTempFile(file, null);
	//	PdfWriter.getInstance(document, response.getOutputStream());
		PdfWriter.getInstance(document, new FileOutputStream(file1));
		
		//File file1=new File(file);
		
		
		
		document.open();
		
		PdfPTable table =new PdfPTable(14);
		
		table.setWidthPercentage(100);
		table.setSpacingBefore(100);
		
		table.setWidths(new float[] {50f,50f,50f,50f,50f,50f,50f,50f,50f,50f,50f,50f,50f,50f});
		
		writeTableHeader(table);
		writeTableData(table);
		logger.info("PDF Data adding");
		document.add(table);
		
		logger.info("Pdf created");
		
		document.close();
		
		
		
		byte[] base64=Files.readAllBytes(file1.toPath());
		
		String base64Str=Base64.getEncoder().encodeToString(base64);
		
		return base64Str;
	}

}
