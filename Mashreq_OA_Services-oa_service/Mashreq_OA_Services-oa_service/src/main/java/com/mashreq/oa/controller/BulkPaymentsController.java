package com.mashreq.oa.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.entity.BulkPayements;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;
import com.mashreq.oa.service.BulkPayementsService;
import com.mashreq.oa.service.TokenService;

import ch.qos.logback.classic.Logger;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/exception/queue/")
public class BulkPaymentsController {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(BulkPaymentsController.class);

	@Autowired
	public TokenService tokenService; 
	
	@Autowired
	private BulkPayementsService bulkPayementsService;

	@GetMapping("bulkpayments/list")
	public List<PaymentData> getBulkPayementsList() {
		
		logger.info("calling getBulkPayementsList controller");
		try{
			List<PaymentData> bulkPayments = bulkPayementsService.getBulkPayementsList();
			return bulkPayments;
		}
		catch(Exception e)
		{
			logger.info("Exception in getBulkPayementsList() in BulkPayments Controller "+e.getCause());
			return null;
		}
	}



	@PostMapping("/bulkpayments")
	public List<PaymentData> getBulkExceptions(@RequestBody PaymentSearchInput pymtSerachInput,@RequestHeader Map<String,String> headers)
	{
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
			logger.info("Calling getBulkExceptions() in BulkPaymentsController");
			
			List<PaymentData> bulkList = bulkPayementsService.
					getBulkExceptions(pymtSerachInput);
			logger.info("Returned to controller");
			return bulkList;
		}
		catch(Exception e)
		{
			logger.info("Exception raised in getBulkExceptions() in BulkPaymentsController:: " +e.getCause());
			return null;
		}
	}

}