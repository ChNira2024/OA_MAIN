package com.mashreq.oa.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.mashreq.oa.entity.ReportsInput;
import com.mashreq.oa.entity.ReportsOutput;
import com.mashreq.oa.entity.Supplier;
import com.mashreq.oa.service.ReportsService;
import com.mashreq.oa.service.TokenService;
import com.mashreq.oa.utils.ReportsExcel;
import com.mashreq.oa.utils.ReportsPDF;

@RestController
@RequestMapping("/reports")
@CrossOrigin
public class ReportsController {
	
	@Autowired
	private ReportsService reportService;
	@Autowired
	public TokenService tokenService; 
	ReportsExcel excel=new ReportsExcel();
	
	Logger logger=LoggerFactory.getLogger(ReportsService.class);
	
	@PostMapping("/getReportList")
	public  List<ReportsOutput> generateReport(@RequestBody ReportsInput reports,@RequestHeader Map<String,String> headers)
	{
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
		logger.info("calling getReportlist in  ReportsController");
		return reportService.generateReport(reports);
		}
		
		catch(Exception e)
		{
			logger.info("Exception in generateReport() in ReportsController "+e.getCause());
			return null;
		}
	}
	

	@GetMapping("/supplierList")
	public List<Supplier> supplierList()
	{
		try{
		logger.info("calling supplierList() in ReportsController");
		return reportService.supplierList();
		
	}
		catch(Exception e)
		{
			logger.info("Exception in supplierList() in ReportsController "+e.getCause());
			return null;
		}
	}

	@PostMapping("/excelGenerate")
	public ResponseEntity<String> generateExcel(@RequestBody ReportsInput data,@RequestHeader Map<String,String> headers) throws  IOException {
		
		logger.info("Calling generateExcel() in Reports Controller");
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
		List<ReportsOutput> reports = reportService.generatedReportForExcel(data);

		String result = excel.generateRepotsxlsFile(reports);

		return new ResponseEntity<String>(result, null, HttpStatus.OK);
		}
		catch(Exception e)
		{
			logger.info("Exception in generateExcel() in ReportsController "+e.getCause());
			return null;
		}
		
	}

	@PostMapping("/pdfGenerate")
	public ResponseEntity<String> generatePdf(@RequestBody ReportsInput data,@RequestHeader Map<String,String> headers) throws  IOException, DocumentException {
		
		logger.info("Calling generatePdf in ReportsController");
		logger.info("Report Data is>>" + data);
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
		DateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY_HH:mm:ss");
		String currentDateTime = dateFormat.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=OA_Reports " + currentDateTime + ".pdf";

		List<ReportsOutput> reports = reportService.generatedReportsForPdf(data);

		logger.info("List of reports>.>." + reports);

		ReportsPDF reportPdf = new ReportsPDF(reports);
		String result = reportPdf.export();

		logger.info("result>>>" + result); 
		

		return new ResponseEntity<String>(result, null, HttpStatus.OK);
	}
		catch(Exception e)
		{
			logger.info("Exception in generatePdf() in ReportsController "+e.getCause());
			return null;
		}

	}
	

	@GetMapping("/downloadPDF")
	public ResponseEntity<String> downloadPDF(@RequestParam Integer documentId,@RequestHeader Map<String,String> headers) throws  IOException, DocumentException {
		
		logger.info("Calling generatePdf in ReportsController");
		logger.info("Report Data is>>" + documentId);
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{


		String base64Path = reportService.getDocumentPathByDocumentId(documentId);

		logger.info("List of Documents>.>." + base64Path);

		ReportsPDF reportPdf = new ReportsPDF();
		String result = reportPdf.getPDF(base64Path);

		logger.info("result>>>" + result); 
		

		return new ResponseEntity<String>(result, null, HttpStatus.OK);
	}
		catch(Exception e)
		{
			logger.info("Exception in generatePdf() in ReportsController "+e.getCause());
			return null;
		}

	}

}
