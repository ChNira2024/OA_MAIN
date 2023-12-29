package com.mashreq.oa.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.mashreq.oa.entity.TransactionalReportInput;
import com.mashreq.oa.entity.TransactionalReportOutput;
import com.mashreq.oa.service.TokenService;
import com.mashreq.oa.service.TransactionalReportService;
import com.mashreq.oa.utils.TransactionalReportsExcel;
import com.mashreq.oa.utils.TransactionalReportsPDF;

@RestController
@RequestMapping("/transactional")
@CrossOrigin
public class TransactionalReportController {
	
	Logger logger = LoggerFactory.getLogger(TransactionalReportController.class);
	
	@Autowired
	public TokenService tokenService; 
	@Autowired
	private TransactionalReportService trReportService;
	
	@PostMapping("/getReportsList")
	public List<TransactionalReportOutput> generateReport(@RequestBody TransactionalReportInput reportInput,@RequestHeader Map<String,String> headers)
	{	
		if(!tokenService.validateToken(headers)){
			return null;
		}
		
		try{
		logger.info("Inside TransactionalReportController");
		return  trReportService.generateReport(reportInput);
		}
		catch(Exception e)
		{
			logger.info("Exception in generateReport() in TransactionalReportController");
			return null;

		}
	}
	
	@PostMapping("/excelGenerate")
	public ResponseEntity<String> generateExcel(@RequestBody TransactionalReportInput data,@RequestHeader Map<String,String> headers) throws  IOException {
		
		logger.info("Calling generateExcel() in TransactionalReportController");
		if(!tokenService.validateToken(headers)){
			return null;
		}
		
		try{
		List<TransactionalReportOutput> reports = trReportService.generatedReportForExcel(data);

		TransactionalReportsExcel excel = new TransactionalReportsExcel();
		String result = excel.generateRepotsxlsFile(reports);

		return new ResponseEntity<String>(result, null, HttpStatus.OK);
		}
		catch(Exception e)
		{
			logger.info("Exception in generateExcel() in TransactionalReportController");
			return null;
		}
	}

	@PostMapping("/pdfGenerate")
	public ResponseEntity<String> generatePdf(@RequestBody TransactionalReportInput data,@RequestHeader Map<String,String> headers) throws  IOException, DocumentException {
		
		logger.info("Calling generateExcel() in TransactionalReportController");
		logger.info("Report Data is>>" + data);
		if(!tokenService.validateToken(headers)){
			return null;
		}
		
		try{
		DateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY_HH:mm:ss");
		String currentDateTime = dateFormat.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=OA_Reports " + currentDateTime + ".pdf";

		List<TransactionalReportOutput> reports = trReportService.generatedReportsForPdf(data);

		logger.info("List of reports>.>." + reports);

		TransactionalReportsPDF reportPdf = new TransactionalReportsPDF(reports);
		String result = reportPdf.export();

		logger.info("result>>>" + result); 
		
		
		return new ResponseEntity<String>(result, null, HttpStatus.OK);
		}
		catch(Exception e)
		{
			logger.info("Exception in generatePdf() in TransactionalReportController");
			return null;
		}
		
	}
	

}
