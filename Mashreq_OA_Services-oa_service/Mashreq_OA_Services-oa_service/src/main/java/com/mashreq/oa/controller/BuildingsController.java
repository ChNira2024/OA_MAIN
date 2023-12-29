package com.mashreq.oa.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.entity.Buildings;
import com.mashreq.oa.service.BuildingsService;

@RestController
@CrossOrigin
@RequestMapping("/buildings")
public class BuildingsController {
	
	Logger logger=LoggerFactory.getLogger(BuildingsController.class);
	
	@Autowired
	private BuildingsService buildingService;
	
	@GetMapping("/buildingNames")
	public List<Buildings> getBuildingNames()
	{
		logger.info("Inside BuildingsController");
		try{
			
			List<Buildings> buidings =  buildingService.getBuildingNames();
			return buidings;
		}
		catch(Exception e)
		{
			logger.info("Exception in getBuildingNames in BuildingsController "+e.getCause());
			return null;
		}
		
	}

}
