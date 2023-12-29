package com.mashreq.oa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.entity.NotificationDetails;
import com.mashreq.oa.service.NotificationService;

@RestController
@RequestMapping("/mashreq/budget")
public class NotificationController {
	
	@Autowired
	private NotificationService notificationService;
	
	
	private static Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
	
	
	@PostMapping("/notification")
	public ResponseEntity<String> saveMollakDetails(@RequestBody NotificationDetails mollakDetails){
		//	@RequestHeader String requestId){
			

		String status = "";
		HttpHeaders headerResponse=new HttpHeaders();
		
		
		try {
			LOGGER.info("In try block");
			
			//headerResponse.add("requestId", requestId);
			
			//if(requestId.isEmpty()) throw new InvalidInputException("");
			status = "Sent Notification to pending";
			mollakDetails.setStatus("pending");
			//mollakDetails.setRequestId(requestId);
			//this.notificationService.saveMollakDetails(mollakDetails,requestId);
			this.notificationService.saveMollakDetails(mollakDetails);
			
				
		} catch (Exception e) {
			LOGGER.info("In catch block");
			status  = "Notification failed for request id:";
		}
		
		
		return new  ResponseEntity<String>(status,headerResponse,HttpStatus.OK);
		
		
	}
	
	
		
}
		
	


