package com.mashreq.oa.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
import com.mashreq.oa.entity.ManagementCompany;
import com.mashreq.oa.service.AttachmentDataService;
import com.mashreq.oa.service.TokenService;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/attachment")
@CrossOrigin
public class AttachmentDataController {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(AttachmentDataController.class);
	
	@Autowired
	public AttachmentDataService attachmentService;
	
	@Autowired
	public TokenService tokenService; 
	
	/*
	 * Save Attachment Data based
	 */
//	@PostMapping("/saveAttachmentData")
//	public Integer saveAttachmentData(@RequestBody AttachmentData data,@RequestHeader Map<String,String> headers)
//	{
//		if(!tokenService.validateToken(headers)){
//			return 0;
//		}
//		
//		try{	
//		logger.info("Inside saveData() AttachmentController");
//		Integer id= attachmentService.saveAttachmentData(data);	
//		return id;
//		
//		}
//		
//		catch(Exception e)
//		{
//			logger.info("Exception in saveData()  in AttachementController "+e.getCause());
//			return 0;
//		}
//	}
	@PostMapping("/saveAttachmentData")
	public String saveAttachmentData(@RequestBody AttachmentData data,@RequestHeader Map<String,String> headers)
	{
		if(!tokenService.validateToken(headers)){
			return null;
		}
		
		try{
			
		logger.info("Inside saveData() AttachmentController");
		
		String specialChar = "<>&%[]{}!~'?\"";
		logger.info("Special characters::"+specialChar);
		boolean returnFlag = false;
		
		if(data.getServiceCode() != null && !"".equals(data.getServiceCode())){
			logger.info("Service code if");
			for(int i=0;i<specialChar.length();i++)
			{
				if(data.getServiceCode().contains(Character.toString(specialChar.charAt(i))))
				{
					logger.info("Service code contains spl chars");
					returnFlag = true;
					break;
				}
			}
		}
		if(data.getBifurcation() != null && !"".equals(data.getBifurcation()) ){
			logger.info("bifurcation if");
			for(int i=0;i<specialChar.length();i++)
			{
				if(data.getBifurcation().contains(Character.toString(specialChar.charAt(i))))
				{
					logger.info("bifurcation contains spl chars");
					returnFlag = true;
					break;
				}
			}
		}
		if(data.getRemarks() != null && !"".equals(data.getRemarks())){
			logger.info("remarks if");
			for(int i=0;i<specialChar.length();i++)
			{
				if(data.getRemarks().contains(Character.toString(specialChar.charAt(i))))
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
			Integer id= attachmentService.saveAttachmentData(data);	
				if(id !=0){
					return "Data Saved Successfully";
				}else{
					return "Error: Data not saved";
				}
			}
			
		
		}catch(Exception e){
			logger.error("Exception in saveData()  in AttachementController "+e.getCause());
			return "Error: Data not saved. Please try again after sometime";
		}
	}
	
	/*
	 * Get List of ManagemnetCompanies and their Ids
	 */
	@GetMapping("/managementCompany")
	public List<ManagementCompany> getManagementCompany(){
		
		try{
			
		logger.info("Inside getManagementCompany() AttachmentController");	
		List<ManagementCompany> list=attachmentService.getManagementCompany();
		return list;
		
		}
		catch(Exception e)
		{
			logger.info("Exception in getManagementCompany() in AttachementController "+e.getCause());
			return null;
		}
	}
	
	/*
	 * Fetch MatrixRefNo from pymtReqId
	 */
	@GetMapping("/getMatrixNo/{pymtReqId}")
	public AttachmentData getMatrixRefNo(@PathVariable Integer pymtReqId,@RequestHeader Map<String,String> headers)
	{
		if(!tokenService.validateToken(headers)){
			return null;
		}
		
		try{
		logger.info("Calling getMatrixRefNo"+pymtReqId);
		AttachmentData data = attachmentService.getMatrixRefNo(pymtReqId);
		return data;
		}
		catch(Exception e)
		{
			logger.info("Exception in getMatrixRefNo()  in AttachementController "+e.getCause());
			return null;
		}
	}
	
	/*
	 * Fetch List of AttachmentData
	 */
	@GetMapping("/getAttachmentData")
	public List<AttachmentData> getAttachmentData()
	{
		
		try{
		logger.info("Calling getAttachmentData() AttachmentController");
		List<AttachmentData> attchList = attachmentService.getAttachmentData();
		return attchList;
		}
		catch(Exception e)
		{
			logger.info("Exception in getAttachmentData()  in AttachementController "+e.getCause());
			return null;
		}
	}
	
	
	@GetMapping("/getExpiryDates/{splyer_id}/{mgmt_cpny_id}")
	public AttachmentData getExpiryDates(@PathVariable Integer splyer_id,@PathVariable Integer mgmt_cpny_id) throws ParseException 
	{
		logger.info("Calling getExpiryDates() in AttachmentController");
		
		try{	
		logger.info(mgmt_cpny_id+"::"+splyer_id);
		
		AttachmentData attachmentData=attachmentService.getExpiryDates(splyer_id,mgmt_cpny_id);
				
		return attachmentData;
		}
		catch(Exception e)
		{
			logger.info("Exception in getExpiryDates() in AttachementController "+e.getCause());
			return null;
		}
		
	}
}
