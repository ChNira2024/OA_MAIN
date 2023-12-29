package com.mashreq.oa.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mashreq.oa.entity.Buildings;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;
import com.mashreq.oa.entity.PropertyGroups;
import com.mashreq.oa.service.PaymentDataService;
import com.mashreq.oa.service.TokenService;

@RestController
@RequestMapping("/payment")
@CrossOrigin
public class PaymentDataController {
	
	@Autowired
	public TokenService tokenService; 
	
	@Value("${payment.extension}")
	private String paymentExtension;

	private static final Logger logger = LoggerFactory.getLogger(PaymentDataController.class);

	@Autowired
	private PaymentDataService paymentDataService;

	/*
	 * upload excel file to database
	 */
	@PostMapping("/excelUpload/{username}/{mgmtCompId}")
	public String uploadExcel(@PathVariable String username, @PathVariable Integer mgmtCompId, @RequestParam("file") MultipartFile file
			,@RequestHeader Map<String,String> headers)
			throws IOException {
		
		logger.info("uploadExcel() in PaymentController");


		logger.info("UserName from React js is: " + username+"=="+paymentExtension+":::"+
				FilenameUtils.getExtension(file.getOriginalFilename()));
		logger.info("MGMT_COMP_ID from react js is: "+mgmtCompId);
	/*if(!tokenService.validateToken(headers)){
			return null;
		}*/

		try {
            boolean extFlag=false;
            String fileExt=FilenameUtils.getExtension(file.getOriginalFilename());
            String[] extention=paymentExtension.split(",");
            for(String ext: extention){
                  if(ext.equalsIgnoreCase(fileExt)){
                         extFlag=true;
                  }
            }
            
                  if(extFlag){
                  List<Long> list = paymentDataService.uploadExcel(file, username, mgmtCompId);
                  if(list.size()==0)
                  {
                         return "Data Successfully Inserted";
                  }      
                  
                  else {
                         return "Ignoring  Matrix Ref Nos " + list.toString() + " as these are already present, uploaded remaining Payment Requests Successfully";
                  
                  }
            }else{
                  return "please provide valied file!";
            }

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("upload Excel() failed in PaymentController::"+e.getCause());
			return "Error: Data not inserted ! please try again after sometime! ";
		}

	}

	/*
	 * Get List of PaymentData
	 */
	@GetMapping("/getPaymentData")
	public List<PaymentData> getPaymentData(@RequestHeader Map<String,String> headers) {
		
		logger.info("Calling getPaymentData() in PaymentController ");
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
		List<PaymentData> list = paymentDataService.getListData();

		return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Exception in getPaymentData() in PaymentController "+e.getCause());
			return null;
		}
	}
	
	
	@PostMapping("/searchPaymentData")
	public List<PaymentData> searchPaymentData(@RequestBody PaymentSearchInput searchInput,@RequestHeader Map<String,String> headers) {
		
		logger.info("Calling searchPaymentData() in PaymentController ");
		if(!tokenService.validateToken(headers)){
			return null;
		}
		try{
		List<PaymentData> searchList = paymentDataService.getSearchList(searchInput);
		if(searchList!=null && searchList.size()>0) {
		return searchList;
		}else {
			logger.info("No Records for  getListData() ::: "+searchList.size());
			return searchList;
		}
		}
		catch(Exception e)
		{
			logger.info("Exception in searchPaymentData() in PaymentController "+e.getCause());
			return null;
		}
	}
	
	//This method used to get Building Id and BuildingName Based on MCName
	@GetMapping("/getBuildings/{mgmtCompId}")
		public List<Buildings> getBuildings(@PathVariable Integer mgmtCompId){
			
			logger.info("Calling getBuildings() in PaymentController ");
			
			try{
				
				List<Buildings> bd = paymentDataService.getBuildings(mgmtCompId);
				return bd;
			}
			catch(Exception e)
			{
				logger.info("Exception in getBuildings() in PaymentController "+e.getCause());
				return null;
			}
				
		}

		@GetMapping("/getPropertyGroups/{mgmtCompId}")
		public List<PropertyGroups> getProperties(@PathVariable Integer mgmtCompId){
			
			logger.info("Calling getProperties() in PaymentController ");
			
			try{
				
				List<PropertyGroups> bd = paymentDataService.getProperties(mgmtCompId);
				return bd;
			}
			catch(Exception e)
			{
				logger.info("Exception in getBuildings() in PaymentController "+e.getCause());
				return null;
			}
				
		}
}
