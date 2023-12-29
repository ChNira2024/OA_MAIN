package com.mashreq.oa.dao;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.NotificationDetails;





@Repository
public class NotificationDaoImpl implements NotificationDao {
	
	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	@Value("${OA-DBFLAG}")
	private String DBFLAG;
	
	public static Logger LOGGER = LoggerFactory.getLogger(NotificationDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public NotificationDetails saveMollakDetails(NotificationDetails mollakDetails) {
		String seq =null;
		LOGGER.info("Executing saveMollakDetails Dao Method");
		try {
			if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
				seq = "NEXT VALUE FOR " +DBAPPEND+"BUDGET_SEQUENCE";
			}else {
				seq ="BUDGET_SEQUENCE.NEXTVAL FROM DUAL";
			}
			jdbcTemplate.update(
					"INSERT INTO "+DBAPPEND+"OA_MOLLAK_BUDGET_INFO (BUDGET_INFO_ID,PROPERTY_GROUP_ID,MC_ID,PERIOD_CODE,STATUS) VALUES "
					+ "(("+seq+"),?,?,?,?)",
				    mollakDetails.getPropertyGroupId(),mollakDetails.getMcId(),mollakDetails.getPeriodCode(), mollakDetails.getStatus());

		} catch (Exception e) {
			LOGGER.error("Exception raised while inserting data to Mollak_Budget_Info" + e.getMessage());

		}
		return mollakDetails;
	}

	

}
