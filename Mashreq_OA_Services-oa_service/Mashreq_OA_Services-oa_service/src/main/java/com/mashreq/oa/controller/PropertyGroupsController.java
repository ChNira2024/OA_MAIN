package com.mashreq.oa.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.entity.PropertyGroups;
import com.mashreq.oa.service.PropertyGroupsService;

@RestController
@CrossOrigin
@RequestMapping("/properties")
public class PropertyGroupsController {
	
	Logger logger=LoggerFactory.getLogger(PropertyGroupsController.class);

	@Autowired
	public PropertyGroupsService propertyService;
	
	@GetMapping("/groupNames")
	public List<PropertyGroups> getPropertyGroupName(){
		
		logger.info("Inside PropertyGroupsController");

		try{
		List<PropertyGroups>  propGroups = propertyService.getPropertyGroupNames();
		return propGroups;
		}
		
		catch(Exception e)
		{
			logger.info("Exception in getPropertyGroupName() in PropertyGroupsController "+e.getCause());
			return null;
		}
	}


}
