package com.mashreq.oa.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.Buildings;

@Repository
public class BuildingsDaoImpl implements BuildingsDao {

	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	Logger logger=LoggerFactory.getLogger(BuildingsDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Buildings> getBuildingNames() {
		
		logger.info("Inside getBuildingNames in BuildingsDaoImpl ");
		
		try{
		
		String query = "SELECT BUILDING_ID , BUILDING_NAME , IS_ACTIVE FROM "+DBAPPEND+"OA_BUILDINGS ORDER BY BUILDING_NAME ASC";
		
		List<Buildings> buildings = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Buildings.class));
		return buildings;
		
		}
		catch(Exception e)
		{
			logger.info("Exception in getBuildingNames() in BuildingsDaoImpl "+e.getCause());
			return null;
		}
	}
}
