package com.mashreq.oa.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.PropertyGroups;

@Repository
public class PropertyGroupsDaoImpl implements PropertyGroupsDao {

	Logger logger=LoggerFactory.getLogger(PropertyGroupsDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	@Override
	public List<PropertyGroups> getPropertyGroupNames() {
		
		String query="SELECT PROPERTY_GROUP_ID,PROPERTY_GROUP_NAME_EN FROM "+DBAPPEND+"OA_PROPERTY_GROUPS";
		
		List<PropertyGroups> groupNames=jdbcTemplate.query(query,
				BeanPropertyRowMapper.newInstance(PropertyGroups.class));
		
		return groupNames;
	}

}
