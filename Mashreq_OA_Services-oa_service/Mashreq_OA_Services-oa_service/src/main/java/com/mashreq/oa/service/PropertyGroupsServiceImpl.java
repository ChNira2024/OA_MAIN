package com.mashreq.oa.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashreq.oa.dao.PropertyGroupsDao;
import com.mashreq.oa.entity.PropertyGroups;

@Service
public class PropertyGroupsServiceImpl implements PropertyGroupsService {

	Logger logger=LoggerFactory.getLogger(PropertyGroupsServiceImpl.class);
	
	@Autowired
	public PropertyGroupsDao propertyDao;
	
	@Override
	public List<PropertyGroups> getPropertyGroupNames() {
		logger.info("Inside getPropertyGroupNames() Service");
		return propertyDao.getPropertyGroupNames();
	}

}
