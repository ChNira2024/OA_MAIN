package com.mashreq.oa.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.entity.AuditTrailLog;
import com.mashreq.oa.entity.BudgetDetailsInput;
import com.mashreq.oa.entity.BudgetDetailsOutput;
import com.mashreq.oa.entity.Reversal;
import com.mashreq.oa.service.BudgetDetailsService;
import com.mashreq.oa.service.TokenService;

@RestController
@RequestMapping("/budget")
@CrossOrigin
public class BudgetDetailsController {
	
	@Autowired
	private BudgetDetailsService budgetDetailsService;
	@Autowired
	public TokenService tokenService; 
	private static final Logger logger = (Logger) LoggerFactory.getLogger(BudgetDetailsController.class);
	
	@PostMapping("/budgetDetails")
	public List<BudgetDetailsOutput> getBudgetDetails(@RequestBody BudgetDetailsInput bdInput,@RequestHeader Map<String,String> headers)
	{
		/*
		if(!tokenService.validateToken(headers))
		{
			return null;
		}
		*/
		try {
			
			logger.info("Calling getBudgetDetails() in BudgetDetailsController");
			
			List<BudgetDetailsOutput> bdDetails = budgetDetailsService.getBudgetDetails(bdInput);
			for(BudgetDetailsOutput bean:bdDetails) 
			{
				if(bean.getTotalCost()!=null && bean.getTotalCost()!=0) {
					//bean.setTotalCost(Math.round(bean.getTotalCost()*100.0)/100.0);
					bean.setTotalCost(decimalTruncate(bean.getTotalCost()));
				}
				if(bean.getVatAmount()!=null && bean.getVatAmount()!=0) {
					//bean.setVatAmount(Math.round(bean.getVatAmount()*100.0)/100.0);
					bean.setVatAmount(decimalTruncate(bean.getVatAmount()));
				}
				if(bean.getTotalBudget()!=null && bean.getTotalBudget()!=0) {
					//bean.setTotalBudget(Math.round(bean.getTotalBudget()*100.0)/100.0);
					bean.setTotalBudget(decimalTruncate(bean.getTotalBudget()));
				}
				if(bean.getConsumedAmount()!=null && bean.getConsumedAmount()!=0) {
					//bean.setConsumedAmount(Math.round(bean.getConsumedAmount()*100.0)/100.0);
					bean.setConsumedAmount(decimalTruncate(bean.getConsumedAmount()));
				}
				if(bean.getBalanceAmount()!=null && bean.getBalanceAmount()!=0) {
					//bean.setBalanceAmount(Math.round(bean.getBalanceAmount()*100.0)/100.0);
					bean.setBalanceAmount(decimalTruncate(bean.getBalanceAmount()));
				}
			}
			return bdDetails;
		}
		catch(Exception e)
		{
			logger.info("Exception in getBudgetDetails() in BudgetDetailsController :: "+e.getCause());
			return null;
		}
	}
	
	//from screen level changes 
	@PutMapping("/updateBudget")
	public void updateBudgetDetails(@RequestBody BudgetDetailsOutput bdOutput,@RequestHeader Map<String,String> headers)
	{
		/*
		if(!tokenService.validateToken(headers)){
			
		}*/
		try {
			logger.info("Calling updateBudgetDetails() in BudgetDetailsController");
			String username=headers.get("username");
			budgetDetailsService.updateBudgetDetails(bdOutput,username);
		}
		catch(Exception e)
		{
			logger.info("Exception in updateBudgetDetails() in BudgetDetailsController :: "+e.getCause());
		}
	}
	//for reversal
		@PostMapping("/updateBudgetItems")
	    public ResponseEntity<BudgetDetailsOutput> updateBudgetItems(@RequestBody Reversal reversal,@RequestHeader Map<String,String> headers) 
		{
			/*
			if(!tokenService.validateToken(headers))
			{
				
			}*/
			logger.info("BudgetDetailsUpdateController class updateBudgetItems()-Reversal Data: "+reversal);
	       
	        try 
	        {
	        	String username=headers.get("username");
	        	BudgetDetailsOutput data = budgetDetailsService.updateBudgetItems(reversal,username);

	            if (data != null) 
	            {
	                return new ResponseEntity<>(data, HttpStatus.OK);
	            } 
	            else 
	            {
	                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	            }
	        } 
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
		
		//for fetchAuditTrailLog
		@PostMapping("/fetchAuditTrailLog")
	    public ResponseEntity<List<AuditTrailLog>> fetchAuditTrailLog2(@RequestParam String serviceCode,@RequestParam String userName,@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date updatedFrom,@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date updatedTo) 
		{
			List<AuditTrailLog> data = null;
			logger.info("BudgetDetailsUpdateController class fetchAuditTrailLog()-Audit Data: ");
			System.out.println("serviceCode:"+serviceCode);
			System.out.println("userName:"+userName);
			System.out.println("updatedFrom:"+updatedFrom);
			System.out.println("updatedTo:"+updatedTo);
			
	        try 
	        {

	            data = budgetDetailsService.getDataFromAuditTrailLog(serviceCode, userName, updatedFrom,updatedTo);
	            
	            if (data != null && !data.isEmpty())
	            {
	                return new ResponseEntity<>(data, HttpStatus.OK);
	            } 
	            else 
	            {
	                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // or HttpStatus.NOT_FOUND 
	            }
	        } 
	        catch (Exception e) 
	        {
	            // handle Exception
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	@GetMapping("/budgetYears")
	public List<Integer> getBudgetYears(){
		try {
			logger.info("Calling getBudgetYears() in BudgetDetailsController");
			List<Integer> budgetYears=budgetDetailsService.getBudgetYears();
			return budgetYears;
		}
		catch(Exception e)
		{
			logger.info("Exception in getBudgetYears() in BudgetDetailsController :: "+e.getCause());
			return null;
		}
	}
	public static Double decimalTruncate(Double value) {
		
		return Math.floor(value*100)/100;
		
	}
	
	}
	
	


