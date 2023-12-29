package com.mashreq.oa.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashreq.oa.dao.BuildingsDao;
import com.mashreq.oa.entity.Buildings;

@Service
public class BuildingsServiceImpl implements BuildingsService {

Logger logger=LoggerFactory.getLogger(BuildingsServiceImpl.class);
	
	@Autowired
	private BuildingsDao buildingDao;
	
	@Override
	public List<Buildings> getBuildingNames() {
		
		try{
		logger.info("Inside BuildingsService");
		return buildingDao.getBuildingNames();
		}
		catch(Exception e)
		{
			logger.info("Exception in getBuildingNames() in BuildingsService "+e.getCause());
			return null;
		}
	}

}
