package com.mashreq.oa.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.CompletedRequestInput;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.service.CompletedRequestService;
import com.mashreq.oa.service.TokenService;
import com.mashreq.oa.utils.CompletedRequestExcel;
import com.mashreq.oa.utils.CompletedRequestPdf;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/completedrequest")
@CrossOrigin
public class CompletedRequestController {
	
	@Autowired
	private CompletedRequestService completedRequestService;
	@Autowired
	public TokenService tokenService; 
	private static final Logger logger = (Logger) LoggerFactory.getLogger(CompletedRequestController.class);
	
	/*
	 * Get List of Completed Requests
	 */
	@GetMapping("/getCompletedRequests")
	public List<PaymentData> getCompletedRequests(@RequestHeader Map<String,String> headers)
	{
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
		logger.info("Calling getCompleteRequests() in CompletedReqcontroller");
		List<PaymentData> completedReqList= completedRequestService.getCompletedRequests();
		return completedReqList;
		}
		catch(Exception e)
		{
			logger.info("Exception in getCompleteRequests() in CompletedReqcontroller "+e.getCause());
			return null;
		}
	}
	
	/*
	 * Get CompletedRequest Data based on pymtReqId
	 */
	@GetMapping("/pymtReqId/{pymtReqId}")
	public List<AttachmentData>   getDetails(@PathVariable Integer pymtReqId,@RequestHeader Map<String,String> headers)
	{	
//		if(!tokenService.validateToken(headers)){
//			return null;
//		}
		try{
		logger.info("Calling getDetails() in CompletedReqcontroller");
		List<AttachmentData> compDetails = completedRequestService.getDetails(pymtReqId);
		return compDetails;
		}
		catch(Exception e){
			logger.info("Exception in getDetails() in CompletedReqcontroller "+e.getCause());
			return null;
		}
		
	}
	

	@PostMapping("/completedRequests")
	public List<PaymentData> completedRequests(@RequestBody CompletedRequestInput compInput,@RequestHeader Map<String,String> headers)
	{
//		if(!tokenService.validateToken(headers)){
//			return null;
//		}
		try {
			logger.info("Calling completedRequests() in CompletedRequestController");
			List<PaymentData> completedList = completedRequestService.completedRequests(compInput);
			
			logger.info("Returned to Controller");
			return completedList;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Exception raised in completedRequests() in CompletedRequestController");
			return null;
		}
	}
	
	@PostMapping("/excelGenerate")
	public ResponseEntity<String> generateExcel(@RequestBody CompletedRequestInput compInput,@RequestHeader Map<String,String> headers) throws  IOException {
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try {
		logger.info("Calling generateExcel() in CompletedReqController");	
		List<PaymentData> completedRequests = completedRequestService.generateExcel(compInput);
		CompletedRequestExcel excel=new CompletedRequestExcel();
		String result = excel.generateRepotsxlsFile(completedRequests);

		return new ResponseEntity<String>(result, null, HttpStatus.OK);
		}
		catch(Exception e)
		{
			logger.info("Exception raised in generateExcel() in CompletedRequestController");
			return null;
		}
	}
	
	@PostMapping("/pdfGenerate")
	public ResponseEntity<String> generatePdf(@RequestBody CompletedRequestInput compInput,@RequestHeader Map<String,String> headers) throws  IOException, DocumentException {
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try {
		logger.info("Calling generatePdf() in CompletedReqController");	
		List<PaymentData> completedRequests = completedRequestService.generatedPdf(compInput);

		logger.info("List of completedRequests>.>." + completedRequests);

		CompletedRequestPdf completedRequestPdf = new CompletedRequestPdf(completedRequests);
		String result = completedRequestPdf.export();

		logger.info("result>>>" + result); 
		

		return new ResponseEntity<String>(result, null, HttpStatus.OK);
		}
		catch(Exception e)
		{
			logger.info("Exception raised in generatePdf() in CompletedRequestController");
			return null;
		}

	}

}
