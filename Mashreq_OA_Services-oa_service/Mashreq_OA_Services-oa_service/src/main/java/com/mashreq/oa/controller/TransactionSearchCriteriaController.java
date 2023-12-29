package com.mashreq.oa.controller;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.SearchResults;
import com.mashreq.oa.service.TokenService;
import com.mashreq.oa.service.TransactionSearchCriteriaService;


@CrossOrigin
@RestController
public class TransactionSearchCriteriaController {
	private static final Logger logger=LoggerFactory.getLogger(TransactionSearchCriteriaController.class);
	
	@Autowired
	private TransactionSearchCriteriaService transactionService;
	@Autowired
	public TokenService tokenService; 
	
	@PostMapping("/search/getPaymentData") 
	public SearchResults getTransactionList(@RequestBody PaymentData pd,@RequestHeader Map<String,String> headers)
	{
		logger.info("Calling getTransactionList() in TransactionSearchCriteriaController");

		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
		logger.info(pd+"");
		return transactionService.gettransactionList(pd);
		}
		catch(Exception e)
		{
			logger.info("Exception in getTransactionList() in TransactionSearchCriteriaController "+e.getCause());
			return null;
		}
	}
	
	

}
