package com.mashreq.oa.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mashreq.oa.dto.SchLockStatusDetails;
import com.mashreq.oa.entity.ServiceChargeGroupDetails;
import com.mashreq.oa.utility.AppNameAndReqTForManageSchLockStatus;
import com.mashreq.oa.utility.DBQueries;
import com.mashreq.oa.utility.SchProcessStatus;




@Repository
public class ManageSchedulerLockDAO {
	
	private static final Logger log = LoggerFactory.getLogger(ManageSchedulerLockDAO.class);
	private JdbcTemplate jdbcTemplateJIE;
	
	@Value("${oa-mollakdb-package}")
	private String mollakDBPackage;
	
	@Value("${OA-DBFLAG}")
	private String mollakDBFlag;
	
	@Autowired
	public ManageSchedulerLockDAO(@Qualifier("jieOracleJdbcTemplate") JdbcTemplate jdbcTemplateJIE
			) {
		this.jdbcTemplateJIE = jdbcTemplateJIE;
		
	}
	public SchLockStatusDetails getSchLockStatusforUpdateBudgetDetailsService() {
		//log.info("Executing getSchLockStatusforUpdateBudgetDetailsService Method");
		String query=null;
		if(mollakDBFlag.equalsIgnoreCase("ORACLE")) {
		query="select log_id,LAST_UPDATED_TIMESTAMP from "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS where app_name=? and Request_type=? "
				+ "and ((status=?) or ((((TIMESTAMP_DIFF(systimestamp,LAST_UPDATED_TIMESTAMP))/60) > 120)))";
		}else {
			query="select log_id,LAST_UPDATED_TIMESTAMP from "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS where app_name=? and Request_type=? "
					+ "and ((status=?) or ((((DATEDIFF(SECOND,CURRENT_TIMESTAMP,LAST_UPDATED_TIMESTAMP))/60) > 120)))";
		}
	//	log.info("query"+query);
		try {
			Object [] params = {AppNameAndReqTForManageSchLockStatus.OABUDGETSCHEDULER.getValue(),
					AppNameAndReqTForManageSchLockStatus.OA_UPDATE_BUDGET_DETAILS_SERVICE.getValue(),
					SchProcessStatus.NOT_RUNNING.getValue()};
			return jdbcTemplateJIE.queryForObject(query,params, (rs,i) ->{
				return new SchLockStatusDetails(rs.getLong("LOG_ID"), rs.getString("LAST_UPDATED_TIMESTAMP"));
			});
		} catch (DataAccessException e) {
			log.info("error "+e.getCause());
			log.error("DataAccessException :"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	public SchLockStatusDetails getSchLockStatusforProcessPaymentRequestService() {
		//log.info("Executing getSchLockStatusforProcessPaymentRequestService Method");
		String query=null;
		if(mollakDBFlag.equalsIgnoreCase("ORACLE")) {
		query="select log_id,LAST_UPDATED_TIMESTAMP from "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS where app_name=? and Request_type=? "
				+ "and ((status=?) or ((((TIMESTAMP_DIFF(systimestamp,LAST_UPDATED_TIMESTAMP))/60) > 120)))";
		}else {
			query="select log_id,LAST_UPDATED_TIMESTAMP from "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS where app_name=? and Request_type=? "
					+ "and ((status=?) or ((((DATEDIFF(SECOND,CURRENT_TIMESTAMP,LAST_UPDATED_TIMESTAMP))/60) > 120)))";
		}
	//	log.info("query"+query);
		try {
			Object [] params = {AppNameAndReqTForManageSchLockStatus.OABUDGETSCHEDULER.getValue(),
					AppNameAndReqTForManageSchLockStatus.OA_PROCESS_PAYMENT_REQUEST_SERVICE.getValue(),
					SchProcessStatus.NOT_RUNNING.getValue()};
			return jdbcTemplateJIE.queryForObject(query,params, (rs,i) ->{
				return new SchLockStatusDetails(rs.getLong("LOG_ID"), rs.getString("LAST_UPDATED_TIMESTAMP"));
			});
		} catch (DataAccessException e) {
			log.info("error "+e.getCause());
			log.error("DataAccessException :"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	public SchLockStatusDetails getSchLockStatusforSendingStatusMailService() {
	//	log.info("Executing getSchLockStatusforSendingStatusMailService Method");
		String query=null;
		if(mollakDBFlag.equalsIgnoreCase("ORACLE")) {
		query="select log_id,LAST_UPDATED_TIMESTAMP from "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS where app_name=? and Request_type=? "
				+ "and ((status=?) or ((((TIMESTAMP_DIFF(systimestamp,LAST_UPDATED_TIMESTAMP))/60) > 120)))";
		}else {
			query="select log_id,LAST_UPDATED_TIMESTAMP from "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS where app_name=? and Request_type=? "
					+ "and ((status=?) or ((((DATEDIFF(SECOND,CURRENT_TIMESTAMP,LAST_UPDATED_TIMESTAMP))/60) > 120)))";
		}
	//	log.info("query"+query);
		try {
			Object [] params = {AppNameAndReqTForManageSchLockStatus.OABUDGETSCHEDULER.getValue(),
					AppNameAndReqTForManageSchLockStatus.OA_SENDING_STATUS_MAIL_SERVICE.getValue(),
					SchProcessStatus.NOT_RUNNING.getValue()};
			return jdbcTemplateJIE.queryForObject(query,params, (rs,i) ->{
				return new SchLockStatusDetails(rs.getLong("LOG_ID"), rs.getString("LAST_UPDATED_TIMESTAMP"));
			});
		} catch (DataAccessException e) {
			log.info("error "+e.getCause());
			log.error("DataAccessException :"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Transactional(value = "jieOracleTranscationManager" ,propagation = Propagation.REQUIRES_NEW)
	public int updateSchLock(String lastupdatesTime, long logId) {
		log.info("lastupdatesTime :"+lastupdatesTime,logId);
		Object [] params = {SchProcessStatus.RUNNING.getValue(),lastupdatesTime,logId};
		String query=null;
		if(mollakDBFlag.equalsIgnoreCase("ORACLE")) {
		 query="update "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS set LAST_UPDATED_TIMESTAMP=systimestamp ,status=? where LAST_UPDATED_TIMESTAMP=TO_TIMESTAMP(?,'YYYY-MM-DD HH24: MI:SS:FF') and log_id=?";
		}else {
			query="update "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS set LAST_UPDATED_TIMESTAMP=CURRENT_TIMESTAMP ,status=? where LAST_UPDATED_TIMESTAMP=? and log_id=?";
		}
		 return jdbcTemplateJIE.update(query,params);
	}
	
	
	@Transactional(value = "jieOracleTranscationManager" ,propagation = Propagation.REQUIRES_NEW)
	public int updateSchLockAFCJ(long id) {
		log.info("LOG ID :"+id);
		Object [] params = {SchProcessStatus.NOT_RUNNING.getValue(),id};String query=null;
		if(mollakDBFlag.equalsIgnoreCase("ORACLE")) {
		query="update "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS set LAST_UPDATED_TIMESTAMP=systimestamp ,status=? where log_id=?";
		}else {
			query="update "+mollakDBPackage+"MANAGE_SCH_LOCK_STATUS set LAST_UPDATED_TIMESTAMP=CURRENT_TIMESTAMP ,status=? where log_id=?";
		}
		return jdbcTemplateJIE.update(query,params);
	}
	public static void main(String[] args) {
		ServiceChargeGroupDetails sc1=new ServiceChargeGroupDetails();
		sc1.setServiceChargeGroupId("1234");
		ServiceChargeGroupDetails sc2=new ServiceChargeGroupDetails();
		sc2.setServiceChargeGroupId("456");
		ServiceChargeGroupDetails sc3=new ServiceChargeGroupDetails();
		sc3.setServiceChargeGroupId("457");
		List<ServiceChargeGroupDetails> l1=new ArrayList<ServiceChargeGroupDetails>();
		l1.add(sc1);
		l1.add(sc2);
		List<ServiceChargeGroupDetails> l2=new ArrayList<ServiceChargeGroupDetails>();
		l2.add(sc1);
		l2.add(sc2);
		l2.add(sc3);
		System.out.println(l1);
		for (ServiceChargeGroupDetails oldData : l2) {
			int updateCount = 0;boolean duplicateFalg=false;
			for (ServiceChargeGroupDetails newData : l1) {
				if (newData.getServiceChargeGroupId().equalsIgnoreCase(oldData.getServiceChargeGroupId())) {

					updateCount++;
					duplicateFalg=true;
					break;
				}
				
			}
			if (!duplicateFalg) {
			//	LOGGER.info("Service Charge Group Id does not Exists:: " + oldData.getServiceChargeGroupId());
				//budgetDetailsDao.updateActiveFlagForBudgetDetails(budgetId, oldData);
				System.out.println("Service Charge Group Id does not Exists:: " + oldData.getServiceChargeGroupId());

			} else {
				//LOGGER.info("Service Charge Group Id  Exists:: ");
				System.out.println("Service Charge Group Id  Exists:: "+oldData.getServiceChargeGroupId());
			}
		}

		
		
		
		
	}
	
}
