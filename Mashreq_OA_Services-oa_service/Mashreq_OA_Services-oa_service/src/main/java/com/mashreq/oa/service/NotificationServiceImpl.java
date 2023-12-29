package com.mashreq.oa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashreq.oa.dao.NotificationDao;
import com.mashreq.oa.entity.NotificationDetails;


@Service
public class NotificationServiceImpl implements NotificationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);
	
	@Autowired
	private NotificationDao notificationDao;
	
	

	/*@Override
	public NotificationDetails saveMollakDetails(NotificationDetails mollakDetails, String requestId) {
		LOGGER.info("Executing saveDetails Service Method");
		
		return notificationDao.saveMollakDetails(mollakDetails);
		 
	}*/

	public NotificationDetails saveMollakDetails(NotificationDetails mollakDetails) {
		LOGGER.info("Executing saveDetails Service Method");
		
		return notificationDao.saveMollakDetails(mollakDetails);
		 
	}


	


}
