package com.mashreq.oa.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;
import com.mashreq.oa.service.IndividualPayementsService;
import com.mashreq.oa.service.TokenService;

import ch.qos.logback.classic.Logger;

@RestController
@CrossOrigin
@RequestMapping("/exception/queue/")
public class IndividualPaymentsController {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(IndividualPaymentsController.class);
	
	@Autowired
	private IndividualPayementsService individualPayementsService;
	
	@Autowired
	public TokenService tokenService; 

	@GetMapping("individualpayments/list")

	public List<PaymentData> getList() {
		try{
			logger.info("calling getList() in IndividualPaymentscontroller");
			List<PaymentData> individualDetails = individualPayementsService.getIndividualPayementsList();
			return individualDetails;
		}
		catch(Exception e)
		{
			logger.info("Exception in getList() in IndividualPaymentscontroller "+e.getCause());
			return null;
		}
	}

	/*
	 * To show attachment related details in individual/bulk pymt screen
	 */
	@GetMapping("/pymtReqId/{pymtReqId}")
	public List<AttachmentData>   getDetails(@PathVariable Integer pymtReqId,@RequestHeader Map<String,String> headers)
	{
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
		logger.info("Calling getDetails() in IndividualPaymentsController");
		return individualPayementsService.getDetails(pymtReqId);
		}
		catch(Exception e)
		{
			logger.info("Exception in getDetails() in IndividualPaymentscontroller "+e.getCause());
			return null;
		}

	}	

	// this method belongs to exception update screen
//	@PostMapping("/updateData")
//	public void updateAttachment(@RequestBody AttachmentData updateData,@RequestHeader Map<String,String> headers) {
//		if(!tokenService.validateToken(headers)){
//			
//		}
//		try{
//		logger.info("calling updateAttachmentData() in IndividualPaymentController");
//		individualPayementsService.updateAttachment(updateData);
//		}
//		catch(Exception e)
//		{
//			logger.info("Exception in updateAttachment() in IndividualPaymentscontroller "+e.getCause());
//			
//		}
//		
//	}
	
	@PostMapping("/updateData")
	public String updateAttachment(@RequestBody AttachmentData updateData,@RequestHeader Map<String,String> headers) {
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
		logger.info("calling updateAttachmentData() in IndividualPaymentController");
		String specialChar = "<>&%[]{}!~'?\"";
		logger.info("Special characters::"+specialChar);
		boolean returnFlag = false;
		
		if(updateData.getServiceCode() != null && !"".equals(updateData.getServiceCode())){
			logger.info("Service code if");
			for(int i=0;i<specialChar.length();i++)
			{
				if(updateData.getServiceCode().contains(Character.toString(specialChar.charAt(i))))
				{
					logger.info("Service code contains spl chars");
					returnFlag = true;
					break;
				}
			}
		}
		if(updateData.getBifurcation() != null && !"".equals(updateData.getBifurcation()) ){
			logger.info("bifurcation if");
			for(int i=0;i<specialChar.length();i++)
			{
				if(updateData.getBifurcation().contains(Character.toString(specialChar.charAt(i))))
				{
					logger.info("bifurcation contains spl chars");
					returnFlag = true;
					break;
				}
			}
		}
		if(updateData.getRemarks() != null && !"".equals(updateData.getRemarks())){
			logger.info("remarks if");
			for(int i=0;i<specialChar.length();i++)
			{
				if(updateData.getRemarks().contains(Character.toString(specialChar.charAt(i))))
				{
					logger.info("Remarks contains spl chars");
					returnFlag = true;
					break;
				}
			}
		}
		if(returnFlag)
		{
			return "Invalid input. Fields can not contain special characters like <>&%[]{}!~'?\" ";
		}else{			
			individualPayementsService.updateAttachment(updateData);
			return "Data updated successfully";
			}
		
		}
		catch(Exception e)
		{
			logger.info("Exception in updateAttachment() in IndividualPaymentscontroller "+e.getCause());
			return "Error: Data not updated";
		}
		
	}



	@PostMapping("/individualpayments")
	public List<PaymentData> getIndividualExceptions(@RequestBody PaymentSearchInput pymtSerachInput,@RequestHeader Map<String,String> headers)
	{
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
			logger.info("Calling getIndividualException() in IndividualPaymentsController");
			
			List<PaymentData> individualList = individualPayementsService.
					getIndividualExceptions(pymtSerachInput);
			logger.info("Returned to controller");
			return individualList;
		}
		catch(Exception e)
		{
			logger.info("Exception raised in getIndividualExceptions() in IndividualPaymentsController:: " +e.getCause());
			return null;
		}
	}


}
