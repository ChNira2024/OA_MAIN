package com.mashreq.oa.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.ApprovedBudgetItems;
import com.mashreq.oa.entity.AuditTrail;
import com.mashreq.oa.entity.BeneficiaryListDetails;
import com.mashreq.oa.entity.BudgetItemDetails;
import com.mashreq.oa.entity.BudgetItems;
import com.mashreq.oa.entity.Language;
import com.mashreq.oa.entity.ManagementCompanyDetails;
import com.mashreq.oa.entity.NotificationDetails;
import com.mashreq.oa.entity.PaymentRequestData;
import com.mashreq.oa.entity.PropertyGroupDetails;
import com.mashreq.oa.entity.ServiceChargeGroupDetails;
import com.mashreq.oa.entity.ServiceStatusDetails;
import com.mashreq.oa.entity.TokenResponse;
import com.mashreq.oa.exceptions.OAServiceException;
import com.mashreq.oa.model.BudgetDetailsInfo;

@Repository
public class BudgetDetailsDaoImpl implements BudgetDetailsDao {

	private static Logger LOGGER = LoggerFactory.getLogger(BudgetDetailsDaoImpl.class);
	
	@Value("${oa-mollakdb-package}")
	private String mollakDBPackage;
	
	@Value("${OA-DBFLAG}")
	private String DBFLAG;
	
	
	@Value("${spring.budgetfailedStatus}")
	private String budgetFailedStatus;
	
	@Value("${spring.budgetDetailsPending}")
	private String budgetDetailsPending;
	
	@Value("${budgetDetails.retryCount}")
	private int budgetRetryCount;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void generateToken(TokenResponse tokenResponse) {
		
		try {
			LOGGER.info("calling generateToken dao method");

			String tokenQuery = "INSERT INTO oa_token(token_type,access_token,scope,expires_on,consented_on) values(?,?,?,?,?) ";

			jdbcTemplate.update(tokenQuery,tokenResponse.getToken_type(),tokenResponse.getAccess_token(),tokenResponse.getScope(),
					tokenResponse.getExpires_in(),tokenResponse.getConsented_on());
			
			LOGGER.info("generateToken is updated in table");
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("cause of Exception in generateToken::"+e.getCause());
		}

	}

	@Override
	public NotificationDetails getNotificationDetails(String status, String compStatus,String source) { //String source

		NotificationDetails notificationDetails = new NotificationDetails();
		//TODO add source in select & where condition
		

		try {
			/*String sql = "SELECT BUDGET_INFO_ID,MOLLAK_MC_ID,MOLLAK_PROP_GRP_ID,PERIOD_CODE,SOURCE,PYMT_REQ_ID,RETRY_COUNT  FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO WHERE STATUS='"
					+ status + "' OR STATUS='FAILED' AND BUDGET_INFO_ID=(SELECT MIN(BUDGET_INFO_ID) FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO"
							+ " WHERE STATUS='"+status+"' OR STATUS='FAILED' AND RETRY_COUNT<"+budgetRetryCount+" ) ";
			*/
			String sql = "SELECT BUDGET_INFO_ID,MOLLAK_MC_ID,MOLLAK_PROP_GRP_ID,PERIOD_CODE,SOURCE,PYMT_REQ_ID,RETRY_COUNT FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO WHERE STATUS='"
					+ status + "' AND BUDGET_INFO_ID=(SELECT MIN(BUDGET_INFO_ID) FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO WHERE STATUS='"+status+"')";

			
			List<NotificationDetails> details = jdbcTemplate.query(sql,
					BeanPropertyRowMapper.newInstance(NotificationDetails.class));
			if (details.size() > 0) {
				notificationDetails = details.get(0);
				LOGGER.info("MC_ID >>>>>" + notificationDetails.getMollakMcId());
				LOGGER.info("property_group_id >>>>>" + notificationDetails.getMollakPropGrpId());
				LOGGER.info("BUDGET_INFO_ID >>>>>" + notificationDetails.getBudgetInfoId());
				LOGGER.info("PERIOD_CODE >>>>>" + notificationDetails.getPeriodCode());
				LOGGER.info("SOURCE >>>>>" + notificationDetails.getSource());
				LOGGER.info("Payment req id >>>>>" + notificationDetails.getPymtReqId());
			} else {

				LOGGER.info("No record found with status pending");
			}

			if (details.size() > 0 && compStatus!=null) {
				
				LOGGER.info("Checking for Duplicate cases"+notificationDetails.getMollakMcId()+notificationDetails.getMollakPropGrpId()
				+notificationDetails.getPeriodCode()+compStatus);String queryCount =null;
			//	String queryCount = "SELECT COUNT(BUDGET_INFO_ID) FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO WHERE MOLLAK_MC_ID=? AND "
				//		+ " MOLLAK_PROP_GRP_ID=? AND PERIOD_CODE=? AND STATUS=?";
				if(notificationDetails.getSource().equals("OA")) {
					queryCount= "SELECT COUNT(BUDGET_INFO_ID) FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO WHERE MOLLAK_MC_ID=? AND "
							+ " MOLLAK_PROP_GRP_ID=?  AND STATUS=?";
					Object[] args = { notificationDetails.getMollakMcId(), notificationDetails.getMollakPropGrpId(),
							 compStatus };

					int count = jdbcTemplate.queryForObject(queryCount, args, Integer.class);
					LOGGER.info("Checking for Duplicate cases"+count+queryCount);
					notificationDetails.setCount(count);
				}else {
					queryCount = "SELECT COUNT(BUDGET_INFO_ID) FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO WHERE MOLLAK_MC_ID=? AND "
								+ " MOLLAK_PROP_GRP_ID=? AND PERIOD_CODE=? AND STATUS=?";
					Object[] args = { notificationDetails.getMollakMcId(), notificationDetails.getMollakPropGrpId(), notificationDetails.getPeriodCode(),
							 compStatus };

					int count = jdbcTemplate.queryForObject(queryCount, args, Integer.class);
					LOGGER.info("Checking for Duplicate cases"+count+compStatus);
					notificationDetails.setCount(count);
				}
				

				

			}
			// if details of size > 0 then execute another query
			// get notification details list
			// select count(budgetinfoId) from noification table where
			// (mgmtId=?,propId=?,periodcode=?)) and status in (pending,completd)
			// set count to notification object
			return notificationDetails;

		}catch(Exception e) {
			e.printStackTrace();LOGGER.error("cause of Exception in getNotificationDetails::"+e.getCause());
			return null;
		}
		
	}
	@Override
	public NotificationDetails getProvisionNotificationDetails(String status, String compStatus,String source) { //String source

		NotificationDetails notificationDetails = new NotificationDetails();
			try {
			
			String sql = "SELECT BUDGET_INFO_ID,MOLLAK_MC_ID,MOLLAK_PROP_GRP_ID,PERIOD_CODE,SOURCE,PYMT_REQ_ID,RETRY_COUNT FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO WHERE STATUS='"
					+ status + "' AND BUDGET_INFO_ID=(SELECT MIN(BUDGET_INFO_ID) FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO WHERE STATUS='"+status+"')";

			
			List<NotificationDetails> details = jdbcTemplate.query(sql,
					BeanPropertyRowMapper.newInstance(NotificationDetails.class));
			if (details.size() > 0) {
				notificationDetails = details.get(0);
				LOGGER.info("MC_ID >>>>>" + notificationDetails.getMollakMcId());
				LOGGER.info("property_group_id >>>>>" + notificationDetails.getMollakPropGrpId());
				LOGGER.info("BUDGET_INFO_ID >>>>>" + notificationDetails.getBudgetInfoId());
				LOGGER.info("PERIOD_CODE >>>>>" + notificationDetails.getPeriodCode());
				LOGGER.info("SOURCE >>>>>" + notificationDetails.getSource());
				LOGGER.info("Payment req id >>>>>" + notificationDetails.getPymtReqId());
			} else {

				LOGGER.info("No record found with ProvisionNotificationDetails pending");
			}

			if (details.size() > 0 && compStatus!=null) {
				
				LOGGER.info("Checking for Duplicate cases"+notificationDetails.getMollakMcId()+notificationDetails.getMollakPropGrpId()
				+notificationDetails.getPeriodCode()+compStatus);String queryCount =null;
			
				if(notificationDetails.getSource().equals("MOLLAK")) {
					
					String provisionalCode="P-";
					queryCount = "SELECT COUNT(BUDGET_INFO_ID) FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO WHERE MOLLAK_MC_ID=? AND "
							+ " MOLLAK_PROP_GRP_ID=? AND PERIOD_CODE= '"+provisionalCode+notificationDetails.getPeriodCode()+"'   AND STATUS=?";
					
				Object[] args = { notificationDetails.getMollakMcId(), notificationDetails.getMollakPropGrpId(), compStatus };

				int count = jdbcTemplate.queryForObject(queryCount, args, Integer.class);
				LOGGER.info("Checking for Duplicate cases"+count+compStatus);
				notificationDetails.setCount(count);
				}
				if(notificationDetails.getCount()==1) {
					queryCount = "SELECT COUNT(BUDGET_INFO_ID) FROM "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO WHERE MOLLAK_MC_ID=? AND "
							+ " MOLLAK_PROP_GRP_ID=? AND PERIOD_CODE=? AND STATUS=?";
				Object[] args = { notificationDetails.getMollakMcId(), notificationDetails.getMollakPropGrpId(), notificationDetails.getPeriodCode(),
						 compStatus };

				int count = jdbcTemplate.queryForObject(queryCount, args, Integer.class);
				LOGGER.info("Checking for  cases"+count+compStatus);
					if(count>0) {
						notificationDetails.setCount(2);
					}
				
				}
				
				
				
			}
		
			return notificationDetails;

		}catch(Exception e) {
			e.printStackTrace();LOGGER.error("cause of Exception in getProvisionNotificationDetails::"+e.getCause());
			return null;
		}
		
	}
	@Override
	public List<Integer> ValidateManagementCompanyId(Integer mcId) {
		try {
			LOGGER.info("calling validateManagementComapnyId Dao method with " + mcId);
			String sql = "SELECT MGMT_COMP_ID FROM "+mollakDBPackage+"OA_MANAGEMENT_COMPANIES WHERE MOLLAK_MC_ID=" + mcId + " ";
			List<Integer> mgmtCompIds = jdbcTemplate.queryForList(sql, Integer.class);
		//	LOGGER.info("Getting list of Row =" + mgmtCompIds);
			// TODO: return list of MC objects
			return mgmtCompIds;
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in ValidateManagementCompanyId::"+e.getCause());
				return null;
			}
	
	}

	@Override
	public Integer saveManagementCompany(ManagementCompanyDetails companyDetails) {

		
		
		try {
			LOGGER.info("calling saveManagementCompany Dao method");
			String sequence=null;
			if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
				sequence="SELECT NEXT VALUE FOR " +mollakDBPackage+"MGMT_COMP_ID";
			}else {
				sequence="SELECT MGMT_COMP_ID.NEXTVAL FROM DUAL";
			}
			int sequenceValue=jdbcTemplate.queryForObject(sequence, Integer.class); //15Feb
			
			LOGGER.info("Sequence Value is::"+sequenceValue);
			String query = "INSERT INTO "+mollakDBPackage+"OA_MANAGEMENT_COMPANIES(MGMT_COMP_ID,MOLLAK_MC_ID,MC_NAME_EN,MC_NAME_AR,CONTACT_NUMBER,EMAIL,TRN,ADDRESS,MERCHANT_CODE,DATA_ID,SERVICE_ID)"
					+ " VALUES("+sequenceValue+",?,?,?,?,?,?,?,?,?,?)";

		//	KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setInt(1, companyDetails.getId());
				ps.setString(2, companyDetails.getName().getEnglishName());
				ps.setString(3, companyDetails.getName().getArabicName());
				ps.setString(4, companyDetails.getContactNumber());
				ps.setString(5, companyDetails.getEmail());
				ps.setString(6, companyDetails.getTrn());
				ps.setString(7, companyDetails.getAddress());
				ps.setString(8, companyDetails.getMerchantCode());
				if(companyDetails.getDataId() == null){
				ps.setNull(9, Types.INTEGER);
				}else {
					ps.setInt(9, companyDetails.getDataId());
				}
				ps.setInt(10, companyDetails.getServiceId());
				return ps;
			});
			LOGGER.info("Insert Management Company Details Data Successfully");

		//	Integer mgmt_Id = keyHolder.getKey().intValue();

			//LOGGER.info("MGMT_COMP_ID : " + mgmt_Id);
			return sequenceValue;
		} catch (Exception e) {
			LOGGER.info("Exception raise while saving managment Company"+e.getCause());
			e.printStackTrace();
			return 0;
		}

		
	}

	@Override
	public Integer saveAdditionalData(String data) {
		try {
			String sequence =null;
			if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
				
				sequence = "SELECT NEXT VALUE FOR " +mollakDBPackage+"DATA_ID_SEQUENCE";
			}else {
				sequence ="select DATA_ID_SEQUENCE.NEXTVAL from  dual";
			}
			int sequenceValue=jdbcTemplate.queryForObject(sequence, Integer.class);
			LOGGER.info("Sequence Value is::"+sequenceValue);
			
			String query = "INSERT INTO "+mollakDBPackage+"OA_ADDITIONAL_DATA (DATA_ID,ADDITIONAL_DATA) VALUES ("+sequenceValue+",?)";

		//	KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1, data);
				return ps;
			});
			LOGGER.info("Inserted Additional Data Successfully");

			//Integer dataId = keyHolder.getKey().intValue();

			//LOGGER.info("Additonal data id : " + dataId);

			return sequenceValue;
		}catch(Exception e) {
			LOGGER.info("Cause of Exception while saveAdditionalData::"+e.getCause());
			return 0;
		}
		
	}
	
	public Integer saveServiceStatus(ServiceStatusDetails serviceStatusDetails) throws ParseException {
		
		try {
		LOGGER.info("Calling saveServiceStatus Data Successfully");String sequence =null;
		if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
			sequence ="SELECT NEXT VALUE FOR " +mollakDBPackage+"SERVICE_ID_SEQUENCE";
		}
		else {
			sequence ="SELECT SERVICE_ID_SEQUENCE.NEXTVAL FROM DUAL";
		}
		int sequenceValue=jdbcTemplate.queryForObject(sequence, Integer.class);
		LOGGER.info("Sequence Value is::"+sequenceValue);
		String query = "INSERT INTO "+mollakDBPackage+"OA_SERVICE_STATUS (SERVICE_ID,SERVICE_NAME,RESPONSE_CODE,TIMESTAMP) "
				+ "VALUES ("+sequenceValue+",?,?,?)";
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date=sdf.parse(serviceStatusDetails.getTimestamp());
		
		SimpleDateFormat sdf2=new SimpleDateFormat("dd-MMMM-yy");
		String serviceDate=sdf2.format(date);
	//	KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1, serviceStatusDetails.getService_Name());
				ps.setString(2, serviceStatusDetails.getResponse_Code());
				ps.setString(3, serviceDate);
			//	ps.setClob(4, serviceStatusDetails.getValidation_Errors());
				return ps;
			});
			LOGGER.info("Inserted saveServiceStatus Data Successfully");

		//	dataId= keyHolder.getKey().intValue();

			//LOGGER.info("Additonal data id : " + dataId);
		}catch(Exception e) {
			e.printStackTrace();
		}
		

		return sequenceValue;
		}catch(Exception e) {
			LOGGER.info("Cause of Exception is saveServiceStatus::"+e.getCause());
			return 0;
		}
	}

	@Override
	public List<Integer> validatePropertyGroupId(Integer propertyGroupId) {
		try {
			LOGGER.info("calling validatePropertyGroupId Dao method with " + propertyGroupId);
			String sql = "SELECT PROP_ID FROM "+mollakDBPackage+"OA_PROPERTY_GROUPS WHERE MOLLAK_PROP_GRP_ID='" + propertyGroupId + "' ";
			List<Integer> proIds = jdbcTemplate.queryForList(sql, Integer.class);
			LOGGER.info("Getting List of Property Table Row =" + proIds);
			return proIds;
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in validatePropertyGroupId::"+e.getCause());
				return null;
			}
		
	}
	
	@Override
	public List<Integer> validatePropertyGroupIdForExistingData(Integer propertyGroupId,Integer mgmtGrpId) {
		try {
			LOGGER.info("calling validatePropertyGroupIdForExistingData Dao method with " + propertyGroupId);
			String sql = "SELECT PROP_ID FROM "+mollakDBPackage+"OA_PROPERTY_GROUPS WHERE MOLLAK_PROP_GRP_ID='" + propertyGroupId + "' AND MGMT_COMP_ID='"+ mgmtGrpId+"' " ;
			List<Integer> proIds = jdbcTemplate.queryForList(sql, Integer.class);
			//LOGGER.info("Getting List of Property Table Row =" + proIds);
			return proIds;
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in validatePropertyGroupIdForExistingData::"+e.getCause());
				return null;
			}
		
	}
	
	@Override
	public List<Integer> validateBudgetIdForExistingData(Integer propertyGroupId,Integer mgmtGrpId,String periodCode) {
		try {
			LOGGER.info("calling validateBudgetIdForExistingData Dao method with " + propertyGroupId);
			String sql = "SELECT BUDGET_ID FROM "+mollakDBPackage+"OA_BUDGET_DETAILS WHERE PROP_ID='" + propertyGroupId + "' AND MGMT_COMP_ID='"+ mgmtGrpId+"' "
					+ "AND IS_ACTIVE='Y' AND BUDGET_PERIOD_CODE='"+periodCode+"' " ;
			List<Integer> budgetIds = jdbcTemplate.query(sql, new RowMapper<Integer>() {

				@Override
				public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
					// TODO Auto-generated method stub
					return rs.getInt(1);
				}
				
			});
		//	LOGGER.info("Getting List of Budget Table Row =" + budgetIds);
			return budgetIds;
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in validateBudgetIdForExistingData::"+e.getCause());
				return null;
			}
		
	}
	
	
	@Override
	public Map<Integer,String> validateBudgetIdForExistingDataMap(Integer propertyGroupId,Integer mgmtGrpId,String periodCode) {
		try {
			LOGGER.info("calling validateBudgetIdForExistingDataMap Dao method with " + propertyGroupId);
			Map<Integer,String> mapBudgetId=new HashMap<Integer, String>();
			String sql = "SELECT BUDGET_ID,SERVICE_CHARGE_GRP_ID FROM "+mollakDBPackage+"OA_BUDGET_DETAILS WHERE PROP_ID='" + propertyGroupId + "' AND MGMT_COMP_ID='"+ mgmtGrpId+"' "
					+ " AND IS_ACTIVE='Y' AND BUDGET_PERIOD_CODE='"+periodCode+"'" ;
			  List<ServiceChargeGroupDetails> listofBugetItemDetails=new ArrayList<ServiceChargeGroupDetails>();
		    	try {
					RowMapper<ServiceChargeGroupDetails> rowmapper = new RowMapper<ServiceChargeGroupDetails>() {
						@Override
						public ServiceChargeGroupDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
							ServiceChargeGroupDetails bean=new ServiceChargeGroupDetails();
							bean.setBudgetId(rs.getInt(1));
							bean.setServiceChargeGroupId(rs.getString(2));
							
							return bean;
						}
					};
					listofBugetItemDetails=jdbcTemplate.query(sql,rowmapper);
				//	LOGGER.info("listofBugetItemDetails Size  is"+listofBugetItemDetails.size());
					for(ServiceChargeGroupDetails bean:listofBugetItemDetails) {
						//put Key as budgetId and Value as serviceChargeGrpID
						mapBudgetId.put(bean.getBudgetId(), bean.getServiceChargeGroupId());
					}
				}catch(Exception e) {
					LOGGER.error("Excpetion "+e.getMessage());
				}
				return mapBudgetId;
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in validateBudgetIdForExistingDataMap::"+e.getCause());
				return null;
			}
		
	}
	@Override
	public Integer savePropertyGroups(PropertyGroupDetails propertyDetails) {
		try {
	
		String sequence =null;
		if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
		 sequence = "SELECT NEXT VALUE FOR " +mollakDBPackage+"PROP_ID_SEQUENCE";
		}else {
			 sequence ="select PROP_ID_SEQUENCE.NEXTVAL FROM DUAL";
		}
		int sequenceValue=jdbcTemplate.queryForObject(sequence, Integer.class);
		LOGGER.info("Sequence Value is::"+sequenceValue);
		String query = "INSERT INTO "+mollakDBPackage+"OA_PROPERTY_GROUPS(PROP_ID,MOLLAK_PROP_GRP_ID,PROPERTY_GROUP_NAME_EN,"
				+ "PROPERTY_GROUP_NAME_AR,MASTER_COMMUNITY_NAME_EN,MASTER_COMMUNITY_NAME_AR,PROJECT_NAME_EN,PROJECT_NAME_AR,STATUS,"
				+ "MERCHANT_CODE,DATA_ID,MGMT_COMP_ID,SERVICE_ID) VALUES ("+sequenceValue+",?,?,?,?,?,?,?,?,?,?,?,?)";

	//	KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, propertyDetails.getPropertyGroupId());
			ps.setString(2, propertyDetails.getPropertyGroupName().getEnglishName());
			ps.setString(3, propertyDetails.getPropertyGroupName().getArabicName());
			ps.setString(4, propertyDetails.getMasterCommunityName().getEnglishName());
			ps.setString(5, propertyDetails.getMasterCommunityName().getArabicName());
			ps.setString(6, propertyDetails.getProjectName().getEnglishName());
			ps.setString(7, propertyDetails.getProjectName().getArabicName());
			ps.setString(8, propertyDetails.getStatus());
			ps.setString(9, propertyDetails.getMerchantCode());
			
			if(propertyDetails.getDataId()==null) {
				ps.setNull(10, Types.INTEGER);
			}else {
			ps.setInt(10, propertyDetails.getDataId());
			}
			ps.setInt(11, propertyDetails.getMgmtCompId());
			ps.setInt(12,propertyDetails.getServiceId());
			return ps;
		});
		LOGGER.info("Insert Property Groups Details Data Successfully");

	//	Integer prop_id = keyHolder.getKey().intValue();

	

		return sequenceValue;}
		catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("Excpetion in savePropertyGroups::"+e.getCause());
			return 0;
		}
	}

	// TODO: mgmt_comop_id
	// impelemnet same as save managment company, don't execute 2nd query
//		String sql1="SELECT PROP_ID FROM OA_PROPERTY_GROUPS WHERE PROPERTY_GROUP_ID ='"+propertyDetails.getPropertyGroupId()+"'";
//		
//		Integer propId = jdbcTemplate.queryForObject(sql1, Integer.class);
//		return propId;

	@Override
	public void saveBeneficiaryList(BeneficiaryListDetails beneList) {
		try {
			LOGGER.info("calling saveBeneficiaryDetails Dao method");String sequence =null;
			if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
			 sequence = "NEXT VALUE FOR " +mollakDBPackage+"BENEFICIARY_ID_SEQUENCE";
			
			}else {
				 sequence ="BENEFICIARY_ID_SEQUENCE.NEXTVAL";
			}
			
			String sql = "INSERT INTO  "+mollakDBPackage+"OA_BENEFICIARY_DETAILS  VALUES " + "(("+sequence+"),?,?,?)";
			jdbcTemplate.update(sql, beneList.getBeneficiaryCode(), beneList.getAccountType(), beneList.getPropId());

			LOGGER.info("Beneficiary Data inserted successfully");
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in saveBeneficiaryList::"+e.getCause());
				
			}
		

	}

	@Override
	public Integer getMgmtCompId(Integer mollakmgmtCompId) {
		try {
			LOGGER.info("Calling getMgmtCompId()");
			List<Integer> listMgmtId = jdbcTemplate.queryForList("SELECT MGMT_COMP_ID FROM  "+mollakDBPackage+"OA_MANAGEMENT_COMPANIES WHERE MOLLAK_MC_ID="+mollakmgmtCompId+" ",
					Integer.class);
			LOGGER.info("list of Id>>" + listMgmtId);
			return listMgmtId.get(0);
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in getMgmtCompId()::"+e.getCause());
				return null;
			}
		
	}

	@Override
	public Integer getPropId(Integer mollakPropGrpId) {
		try {
			LOGGER.info("Calling getPropId()");
			List<Integer> listPropId = jdbcTemplate.queryForList("SELECT PROP_ID FROM "+mollakDBPackage+"OA_PROPERTY_GROUPS WHERE MOLLAK_PROP_GRP_ID="+mollakPropGrpId+" " , Integer.class);
			LOGGER.info("list of PropId>>" + listPropId);
			return listPropId.get(0);
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in getPropId::"+e.getCause());
				return null;
			}
		
	}

	@Override
	public Integer saveBudgetDetails(ServiceChargeGroupDetails serviceDetails) {
		try {
		LOGGER.info("calling saveBudgetDetails Dao method");
		String is_Active="Y";String sequence =null;
		if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
			 sequence = "SELECT NEXT VALUE FOR " +mollakDBPackage+"BUDGET_ID_SEQUENCE";
		
		}else {
			 sequence ="SELECT BUDGET_ID_SEQUENCE.NEXTVAL FROM DUAL";
		}
		Integer budgetId = jdbcTemplate.queryForObject(sequence, Integer.class);
		String sql = "INSERT INTO "+mollakDBPackage+"OA_BUDGET_DETAILS (BUDGET_ID,PROPERTY_MANAGER_EMAIL,SERVICE_CHARGE_GRP_ID,SERVICE_CHARGE_GRP_NAME_EN,"
				+ "SERVICE_CHARGE_GRP_NAME_AR,USAGE_EN,USAGE_AR,BUDGET_PERIOD_CODE,BUDGET_PERIOD_TITLE,BUDGET_PERIOD_FROM,"
				+ "BUDGET_PERIOD_TO,PROP_ID,MGMT_COMP_ID,DATA_ID,SERVICE_ID,IS_ACTIVE) VALUES (("+budgetId+"),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		jdbcTemplate.update(sql, serviceDetails.getEmail(), serviceDetails.getServiceChargeGroupId(),
				serviceDetails.getServiceChargeGroupName().getEnglishName(),
				serviceDetails.getServiceChargeGroupName().getArabicName(), serviceDetails.getUsage().getEnglishName(),
				serviceDetails.getUsage().getArabicName(), serviceDetails.getBudgetPeriodCode(),
				serviceDetails.getBudgetPeriodTitle(), serviceDetails.getBudgetPeriodFrom(),
				serviceDetails.getBudgetPeriodTo(), serviceDetails.getPropId(), serviceDetails.getMgmtId(),
				serviceDetails.getDataId(),serviceDetails.getServiceId(),is_Active);

	/*	String sql1 = "SELECT BUDGET_ID FROM "+mollakDBPackage+"OA_BUDGET_DETAILS WHERE SERVICE_CHARGE_GRP_ID='"
				+ serviceDetails.getServiceChargeGroupId() + "' AND USAGE_EN='"+serviceDetails.getUsage().getEnglishName()+"' AND IS_ACTIVE='Y'";

		*/

		return budgetId;
		}catch(Exception e) {
			LOGGER.info("Cause of Exeption saveBudgetDetails is::"+e.getCause());
			return 0;
		}
	}

	@Override
	public void saveBudgetItems(BudgetItemDetails budgetList) {
		try {
			LOGGER.info("calling saveBudgetItems Dao method");String sequence =null;
			if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
			 sequence = "NEXT VALUE FOR " +mollakDBPackage+"BUDGET_ITEM_ID_SEQUENCE";
			}else {
				 sequence ="BUDGET_ITEM_ID_SEQUENCE.NEXTVAL";
			}
			
			String sql = "INSERT INTO "+mollakDBPackage+"OA_BUDGET_ITEMS(BUDGET_ITEM_ID,CATEGORY_CODE,CATEGORY_NAME_EN,CATEGORY_NAME_AR,"
					+ "SUB_CATEGORY_CODE,SUB_CATEGORY_NAME_EN,SUB_CATEGORY_NAME_AR,SERVICE_CODE,SERVICE_NAME_EN,"
					+ "SERVICE_NAME_AR,TOTAL_COST,VAT_AMOUNT,BUDGET_ID,TOTAL_BUDGET,CONSUMED_AMOUNT,BALANCE_AMOUNT,IS_ACTIVE,SERVICE_ID)"
					+ " VALUES (("+sequence+"),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if(budgetList.getBudgetId()!=null && budgetList.getBudgetId()>0) {
			jdbcTemplate.update(sql, budgetList.getCategoryCode(), budgetList.getCategoryName().getEnglishName(),
					budgetList.getCategoryName().getArabicName(), budgetList.getSubCategoryCode(),
					budgetList.getSubCategoryName().getEnglishName(), budgetList.getSubCategoryName().getArabicName(),
					budgetList.getServiceCode(), budgetList.getServiceName().getEnglishName(),
					budgetList.getServiceName().getArabicName(), budgetList.getTotalCost(), budgetList.getVatAmount(),
					budgetList.getBudgetId(), budgetList.getTotalBudget(), budgetList.getConsumedAmount(), budgetList.getBalanceAmount(),
					budgetList.getIsActive(),budgetList.getServiceId());
		}
			LOGGER.info("Insert data in Budget Details and Budget Items Successfully");
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in saveBudgetItems::"+e.getCause());
				return;
			}
		

	}

	Integer prop_Id = null;

	@Override
	public Integer getSinglePropId(Integer propGrpId) {
		try {
			String query = "SELECT PROP_ID FROM "+mollakDBPackage+"OA_PROPERTY_GROUPS WHERE MOLLAK_PROP_GRP_ID='" + propGrpId + "'";
			prop_Id = jdbcTemplate.queryForObject(query, Integer.class);
			LOGGER.info("check prop_id:>>" + prop_Id);
		} catch (Exception e) {

			prop_Id = null;
			LOGGER.info("prop_Id in catch:>>>>" + prop_Id);
		}

		return prop_Id;

	}

	Integer mc_Id = null;

	@Override
	public Integer getSingleMcId(Integer mcId) {
		try {
			String query = "SELECT MGMT_COMP_ID FROM "+mollakDBPackage+"OA_MANAGEMENT_COMPANIES WHERE MOLLAK_MC_ID='" + mcId + "'";
			mc_Id = jdbcTemplate.queryForObject(query, Integer.class);
			LOGGER.info("Check mc_id:>>" + mc_Id);
		} catch (Exception e) {

			mc_Id = null;
			LOGGER.info("mc_Id in catch:>>>>" + mc_Id);
		}

		return mc_Id;
	}

	@Override
	public List<ManagementCompanyDetails> getCompaniesId() {

		LOGGER.info("calling getCompaniedId Dao Method");

		String query = "SELECT MGMT_COMP_ID,MOLLAK_MC_ID FROM "+mollakDBPackage+"OA_MANAGEMENT_COMPANIES";

		List<ManagementCompanyDetails> listofMcId = jdbcTemplate.query(query,
				BeanPropertyRowMapper.newInstance(ManagementCompanyDetails.class));

		return listofMcId;
	}

	@Override
	public List<Integer> getListOfPropId() {

		LOGGER.info("calling getListOfPropId dao method");

		String query = "SELECT MOLLAK_PROP_GRP_ID FROM "+mollakDBPackage+"OA_PROPERTY_GROUPS";

		List<Integer> listOfPropId = jdbcTemplate.queryForList(query, Integer.class);

		return listOfPropId;
	}

	@Override
	public void validateBudgetDetails(Map<Integer, Integer> mgmtCompIds, Map<Integer, Integer> propGrpIds,
			String periodCode) {

		LOGGER.info("Calling validateBudgetDetails Dao method");
		Integer mgmtCompId = null;
		Integer propId = null;

		for (Map.Entry<Integer, Integer> entry : mgmtCompIds.entrySet()) {
			Integer key = entry.getKey();
			mgmtCompId = entry.getValue();

		}

		LOGGER.info(">>>>>>>>>>>" + propGrpIds);
		for (Map.Entry<Integer, Integer> entry : propGrpIds.entrySet()) {
			Integer key = entry.getKey();
			propId = entry.getValue();

		}

		String query = "SELECT PROP_ID,MGMT_COMP_ID,BUDGET_PERIOD_CODE FROM "+mollakDBPackage+"OA_BUDGET_DETAILS";

		List<BudgetDetailsInfo> budgetDetails = jdbcTemplate.query(query,
				BeanPropertyRowMapper.newInstance(BudgetDetailsInfo.class));

		for (int i = 0; i < budgetDetails.size(); i++) {

			if (budgetDetails.get(i).getMgmtCompId() == mgmtCompId && budgetDetails.get(i).getPropId() == propId
					&& budgetDetails.get(i).getBudgetPeriodCode() == periodCode) {

				LOGGER.info("Update Functionality");
			} else {
				LOGGER.info("Insert Functionality");
			}
		}

	}

	public int updateNotification(String status, Integer budgetInfoId) {
		try {
			LOGGER.info("Calling updateNotification >>>BUDGET_INFO_ID: " + budgetInfoId);
			LOGGER.info("Status is: " + status);

			String query = "UPDATE "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO SET STATUS=? WHERE BUDGET_INFO_ID=?";

			Object[] inputs = new Object[] { status, budgetInfoId };
			LOGGER.info("Updated status in dbo.OA_MOLLAK_BUDGET_INFO Table for budgetInfoId");
			return jdbcTemplate.update(query, inputs);
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in updateNotification::"+e.getCause());
				return 0;
			}
		
	}
	@Override
	public int updateNotificationForFailedrecords(String status, Integer budgetInfoId, Integer retryCount) {
		try {
			LOGGER.info("Calling updateNotificationForFailedrecords >>>BUDGET_INFO_ID: " + budgetInfoId+"retryCount=="+retryCount);
			LOGGER.info("Status is: " + status);

			String query = "UPDATE "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO SET STATUS=?,RETRY_COUNT=? WHERE BUDGET_INFO_ID=?";

			Object[] inputs = new Object[] { status, retryCount, budgetInfoId };
			LOGGER.info("Updated status in dbo.OA_MOLLAK_BUDGET_INFO Table for budgetInfoId");
			return jdbcTemplate.update(query, inputs);
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in updateNotification::"+e.getCause());
				return 0;
			}
	}

	@Override
	public List<BudgetItemDetails> getOldBudgetItemDetails(List<Integer> budgetId) {
		// TODO Auto-generated method stub
		try {
			Object[] screeName =budgetId.toArray();
			String res = StringUtils.join(screeName, "','");
			LOGGER.info("Calling getOldBudgetItemDetails:: " + res);
			String query = "SELECT CATEGORY_CODE,SUB_CATEGORY_CODE,"
					+ "SERVICE_CODE,TOTAL_COST,VAT_AMOUNT,TOTAL_BUDGET,CONSUMED_AMOUNT,BALANCE_AMOUNT,"
					+ "IS_ACTIVE,SERVICE_ID,BUDGET_ID,BUDGET_ITEM_ID FROM "+mollakDBPackage+"OA_BUDGET_ITEMS WHERE IS_ACTIVE='Y' AND BUDGET_ID IN('"+ res+"')";
			  
	        List<BudgetItemDetails> listofBugetItemDetails=new ArrayList<BudgetItemDetails>();
	    	try {
				RowMapper<BudgetItemDetails> rowmapper = new RowMapper<BudgetItemDetails>() {
					@Override
					public BudgetItemDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
						BudgetItemDetails bean=new BudgetItemDetails();
						
						bean.setCategoryCode(rs.getString(1));
						
						bean.setSubCategoryCode(rs.getString(2));
						
						bean.setServiceCode(rs.getString(3));
						
						bean.setTotalCost(rs.getDouble(4));
						bean.setVatAmount(rs.getDouble(5));
						bean.setTotalBudget(rs.getDouble(6));
						bean.setConsumedAmount(rs.getDouble(7));
						bean.setBalanceAmount(rs.getDouble(8));
						bean.setIsActive(rs.getString(9));
						bean.setServiceId(rs.getInt(10));
						bean.setBudgetId(rs.getInt(11));
						bean.setBudget_Item_Id(rs.getInt(12));
						return bean;
					}
				};
				listofBugetItemDetails=jdbcTemplate.query(query,rowmapper);
				
			//	LOGGER.info("Old Budget Items Size is"+listofBugetItemDetails.size());
			}catch(Exception e) {
				LOGGER.error("Excpetion "+e.getMessage());
			}
			

			return listofBugetItemDetails;
			
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in getOldBudgetItemDetails::"+e.getCause());
				return null;
			}
	
	}

	@Override
	public int updateBudgetItemDetails(Integer budgetId, Double totalCost, Double vatAmount,
			Double modifiedTotalBudget, Double modifiedBalanceAmount,String serviceCode) {
		try {

			// TODO Auto-generated method stub
			LOGGER.info("Calling updateBudgetItemDetails: " + budgetId);
			

			String query = "UPDATE "+mollakDBPackage+"OA_BUDGET_ITEMS SET TOTAL_COST=?,VAT_AMOUNT=?,TOTAL_BUDGET=?,BALANCE_AMOUNT=? WHERE BUDGET_ID=? AND SERVICE_CODE=?";
			totalCost=totalCost-vatAmount;
			Object[] inputs = new Object[] { totalCost, vatAmount, modifiedTotalBudget, modifiedBalanceAmount, budgetId, serviceCode };
			LOGGER.info("Updated Item Details in _OA_BUDGET_ITEM_DETAILS Table for budgetId"+budgetId);
			return jdbcTemplate.update(query, inputs);
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in updateBudgetItemDetails::"+e.getCause());
				return 0;
			}
	}

	@Override
	public int updateActiveFlag(Integer budgetId, String serviceCode) {
		try {
			// TODO Auto-generated method stub
			LOGGER.info("Calling updateActiveFlag: " + budgetId+"Service Code::==>"+serviceCode);
			

			String query = "UPDATE "+mollakDBPackage+"OA_BUDGET_ITEMS SET IS_ACTIVE='N' WHERE BUDGET_ID=? AND SERVICE_CODE=?";

			Object[] inputs = new Object[] {  budgetId, serviceCode };
			LOGGER.info("Updated serviceCode in _OA_BUDGET_ITEM_DETAILS Table for budgetId"+budgetId);
			return jdbcTemplate.update(query, inputs);
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in updateActiveFlag::"+e.getCause());
				return 321;
			}
		
	}

	@Override
	public void insertAuditDetails(Integer id, String fieldName, String oldValue, String newValue, String updatedBy) {
		try {
			// TODO Auto-generated method stub
			LOGGER.info("Calling insertAuditDetails: " +id+"FiledName:"+fieldName+"New Value:"+newValue+"Old Value:"+oldValue+"Updated By:"+updatedBy);
			String query = "INSERT INTO "+mollakDBPackage+"OA_AUDIT_TRAIL (ID,FIELDNAME,OLDVALUE,NEWVALUE,UPDATEDBY) VALUES (?,?,?,?,?)";
			jdbcTemplate.update(query, id,fieldName,oldValue,newValue,updatedBy);
			LOGGER.info("Inserted Audit Data Successfully");
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in insertAuditDetails::"+e.getCause());
			
			}
	}

	@Override
	public List<ServiceChargeGroupDetails> getOldBudgetDetails(List<Integer> budgetId) {
		try {
			LOGGER.info("Calling getOldBudgetDetails:: " + budgetId);
			Object[] screeName =budgetId.toArray();
			String res = StringUtils.join(screeName, "','");
			String query = "SELECT SERVICE_CHARGE_GRP_ID,SERVICE_CHARGE_GRP_NAME_EN,"
					+ "USAGE_EN,BUDGET_PERIOD_CODE,BUDGET_PERIOD_TITLE,BUDGET_PERIOD_FROM,BUDGET_PERIOD_TO,BUDGET_ID"
					+ " FROM "+mollakDBPackage+"OA_BUDGET_DETAILS WHERE BUDGET_ID IN('"+res+"') AND IS_ACTIVE='Y' ";
			  
	        List<ServiceChargeGroupDetails> listofBugetItemDetails=new ArrayList<ServiceChargeGroupDetails>();
	    	try {
				RowMapper<ServiceChargeGroupDetails> rowmapper = new RowMapper<ServiceChargeGroupDetails>() {
					@Override
					public ServiceChargeGroupDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
						ServiceChargeGroupDetails bean=new ServiceChargeGroupDetails();
						
						bean.setServiceChargeGroupId(rs.getString(1));
						Language srvChrgEngNamee=new Language();
						srvChrgEngNamee.setEnglishName(rs.getString(2));
						bean.setServiceChargeGroupName(srvChrgEngNamee);
						Language usgEngName=new Language();
						usgEngName.setEnglishName(rs.getString(3));
						bean.setUsage(usgEngName);;
						bean.setBudgetPeriodCode(rs.getString(4));
						bean.setBudgetPeriodTitle(rs.getString(5));
						bean.setBudgetPeriodFrom(rs.getString(6));
						bean.setBudgetPeriodTo(rs.getString(7));
						bean.setBudgetId(rs.getInt(8));
						
						return bean;
					}
				};
				listofBugetItemDetails=jdbcTemplate.query(query,rowmapper);
				
				
			}catch(Exception e) {
				LOGGER.error("Excpetion "+e.getMessage());
			}
			

			return listofBugetItemDetails;
		
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in getOldBudgetDetails::"+e.getCause());
				return null;
			}
		}

	@Override
	public void updateExistingBudgetList(ServiceChargeGroupDetails serviceChargeGroupDetails, Map<Integer,String> budgetId) {
		try {
			LOGGER.info("Calling  updateExistingBudgetList(): " );
			Integer budgetMapId=null;
			for (Map.Entry<Integer, String> entry : budgetId.entrySet()) {
				Integer key = entry.getKey();//budgetId
				String value = entry.getValue();//srvChrGrpId
				if(value.equalsIgnoreCase(serviceChargeGroupDetails.getServiceChargeGroupId())) {
					budgetMapId=key;
					
				}
				
			}LOGGER.info("Key>>>>>>>>>>>" +budgetMapId );
			LOGGER.info("BUDGET_INFO_ID: " + budgetMapId+"serviceChargeGroupDetails.getUsage().getEnglishName()"+serviceChargeGroupDetails.getUsage().getEnglishName());
			
			try {
				String query = "UPDATE "+mollakDBPackage+"OA_BUDGET_DETAILS SET BUDGET_PERIOD_CODE=?,BUDGET_PERIOD_TITLE=?,BUDGET_PERIOD_FROM=?,BUDGET_PERIOD_TO=? "
						+ " WHERE BUDGET_ID=? AND SERVICE_CHARGE_GRP_ID=? AND USAGE_EN=? ";

				Object[] inputs = new Object[] { serviceChargeGroupDetails.getBudgetPeriodCode(), serviceChargeGroupDetails.getBudgetPeriodTitle(),
						serviceChargeGroupDetails.getBudgetPeriodFrom(), serviceChargeGroupDetails.getBudgetPeriodTo(), budgetMapId, serviceChargeGroupDetails.getServiceChargeGroupId(),
						serviceChargeGroupDetails.getUsage().getEnglishName()};
				LOGGER.info("Updated status in OA_BUDGET_DETAILS Table for budgetInfoId");
				 jdbcTemplate.update(query, inputs);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in updateExistingBudgetList::"+e.getCause());
			
			}
		
	}


	@Override
	public void updateActiveFlagForBudgetDetails(Map<Integer, String> mapbudgetId, ServiceChargeGroupDetails serviceChargeGroupDetails) {
		try {
			Integer budgetId=null;
			LOGGER.info("Map Budget  Primary Key is::" + mapbudgetId.size());
			for (Map.Entry<Integer, String> entry : mapbudgetId.entrySet()) {
				Integer key = entry.getKey();

				String value = entry.getValue();
				if (value.equalsIgnoreCase(serviceChargeGroupDetails.getServiceChargeGroupId())) {
					budgetId=key;
				}
			//	LOGGER.info("Key is::" + key + "Value::" + value);
			}
			try {
				String query = "UPDATE "+mollakDBPackage+"OA_BUDGET_DETAILS SET IS_ACTIVE='N' "
						+ " WHERE BUDGET_ID=? AND SERVICE_CHARGE_GRP_ID=? AND USAGE_EN=?";

				Object[] inputs = new Object[] {budgetId, serviceChargeGroupDetails.getServiceChargeGroupId(), serviceChargeGroupDetails.getUsage().getEnglishName() };
				
				 jdbcTemplate.update(query, inputs);
				 LOGGER.info("Updated status in OA_BUDGET_DETAILS Table for budgetInfoId,ServiceChargeGroupId And  UsageenglishName");
				 
				 String budgetItemsQuery="UPDATE "+mollakDBPackage+"OA_BUDGET_ITEMS SET IS_ACTIVE='N' WHERE BUDGET_ID=?";
				 Object[] budgetItemInputs = new Object[] { budgetId };
				 jdbcTemplate.update(budgetItemsQuery, budgetItemInputs);
				 LOGGER.info("Updated status in OA_BUDGET_DETAILS Table for budgetId");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in getNotificationDetails::"+e.getCause());
				
			}
		
	}
	/******************** for validation*********************/
	
	public List<PaymentRequestData> getPaymentRequests(String inproStatus,String failedStatus) {

		LOGGER.info("calling getPaymentRequests in DAO");

	
		
		String query = "SELECT payment.PYMT_REQ_ID,payment.PAYMENT_TYPE, attachment.AUTO_RENEWAL, attachment.NO_PROPER_DOCUMENTS,  attachment.ATTACHMENT_DATA_ID, attachment.ISSUANCE_AUTHORITY,"
				+ " attachment.TRADE_LICENSE_EXP_DATE, attachment.AGREEMENT_EXP_DATE ,  payment.BUDGET_YEAR, attachment.INVOICE_DATE_YEAR, payment.INVOICE_AMOUNT,  payment.SUPPLIER_ID, "
				+ " payment.MGMT_COMP_ID,  mapping.PROP_ID, payment.BUILDING_ID, payment.SERVICE_CODE, attachment.IS_GOVERNMENT_ENTITY, attachment.IS_INSURANCE_COMPANY, attachment.IS_EXCEPTIONAL_APPROVAL, "
				+ " payment.BIFURCATION, payment.REMARKS "
				+ " FROM " + mollakDBPackage + "oa_attachments_data attachment, " + mollakDBPackage
				+ "oa_payment_requests payment "
				// +" LEFT OUTER JOIN "+mollakDBPackage+"OA_AGREEMENT_DETAILS
				// agreement ON agreement.SUPPLIER_ID = payment.SUPPLIER_ID AND
				// agreement.MGMT_COMP_ID=payment.MGMT_COMP_ID "
				// +" LEFT OUTER JOIN "+mollakDBPackage+"OA_SUPPLIERS suppliers
				// ON suppliers.SUPPLIER_ID=payment.SUPPLIER_ID "
				+ " LEFT OUTER JOIN " + mollakDBPackage
				+ "OA_BUILDING_PROPGROUP_MAPPING mapping ON mapping.BUILDING_ID=payment.BUILDING_ID "

				+ " WHERE payment.attachment_data_id=attachment.attachment_data_id AND payment.STATUS IN ('"+ inproStatus +  "', '"+failedStatus+ "')";
				 
		
		
		
			LOGGER.info("query of inprogress::"+query);
		List<PaymentRequestData> inProgressList = jdbcTemplate.query(query,
				BeanPropertyRowMapper.newInstance(PaymentRequestData.class));
		

		return inProgressList;
	}
	@Override
	public void updateAmounts(List<BudgetItems> budgetItems, Integer pymtReqId, String remarks, List<AuditTrail> auditLogs) {
		LOGGER.info("updateAmounts dao is calling..");
		String paymentRequestId = String.valueOf(pymtReqId);
		LOGGER.info("Payemnt request id is:"+ paymentRequestId);
		for (int i = 0; i < budgetItems.size(); i++) {
			String query = "UPDATE " + mollakDBPackage
					+ "oa_budget_items  SET CONSUMED_AMOUNT=?, balance_amount=? where budget_item_id=?";

			Object[] args = { budgetItems.get(i).getConsumedAmount(), budgetItems.get(i).getBalanceAmount(),
					budgetItems.get(i).getBudgetItemId() };

			jdbcTemplate.update(query, args);
		}
		for (int i = 0; i < auditLogs.size(); i++) {
			String query = "insert into "+ mollakDBPackage+"oa_audit_trail(fieldname,oldvalue,newvalue,updatedby,id,pymt_req_id) values(?,?,?,'OA Scheduler',?,?)";

			Object[] args = { auditLogs.get(i).getFieldName(), auditLogs.get(i).getOldValue(),auditLogs.get(i).getNewValue(), auditLogs.get(i).getId(),paymentRequestId };

			jdbcTemplate.update(query, args);
		}
		
		//for loof for auditLogs

		LOGGER.info("Amounts is updated.");

		String status = "APPROVED";

		//String remarks = "Payment Request is APPROVED.";

		Object[] arg = { status, remarks };

		String statusQuery = "UPDATE " + mollakDBPackage
				+ "OA_PAYMENT_REQUESTS SET STATUS=?, REMARKS=? WHERE PYMT_REQ_ID='" + pymtReqId + "' ";

		jdbcTemplate.update(statusQuery, arg);

		LOGGER.info("status is APPROVED.");

	}


	@Override
	public List<BudgetItems> getMollakData(Integer mgmtCompId, String propertyGroupId, Integer budgetYear,
			String serviceCode) {
		
		LOGGER.info("calling getMollakData in DAO");
		String query = "select bi.budget_item_id,bi.total_budget,bi.consumed_amount,bi.balance_amount, bi.service_code "
				+ "from "+mollakDBPackage+"oa_budget_items bi, "+mollakDBPackage+"oa_budget_details bd "
				+ "where bi.budget_id=bd.budget_id  and bd.mgmt_comp_id="+mgmtCompId+" and bd.prop_id="+propertyGroupId+" and bd.budget_period_code like '%"+budgetYear+"%' AND bi.is_active='Y' and bi.service_code IN('"+serviceCode.replaceAll("," , "','")+"')";

		List<BudgetItems> mollakData = jdbcTemplate.query(query,
				BeanPropertyRowMapper.newInstance(BudgetItems.class));
		return mollakData;
	}
	
	@Override
	public void insertMollakBudgetInfo(Integer mollakMcId, Integer mollakPropGrpId, String mollaknodatastatus2,
			String mollaksource2, Integer pymtReqId) {
		try {
		LOGGER.info("calling insertMollakBudgetInfo Dao method");
		String sequence=null;
		if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
			sequence="NEXT VALUE FOR " +mollakDBPackage+"BUDEGET_SEQUENCE";
		}else {
			sequence="BUDGET_INFO_ID.nextval";
		}
		//String sequence = "NEXT VALUE FOR " +mollakDBPackage+"BUDEGET_SEQUENCE";
		String sql = "insert into "+mollakDBPackage+"OA_MOLLAK_BUDGET_INFO (BUDGET_INFO_ID,MOLLAK_PROP_GRP_ID, MOLLAK_MC_ID, STATUS, SOURCE, PYMT_REQ_ID, RETRY_COUNT) "
				+ "VALUES ("+sequence+",?, ?, ?, ?, ?, 0)";
		Object[] arg = { mollakPropGrpId, mollakMcId,  mollaknodatastatus2, mollaksource2,  pymtReqId };
		jdbcTemplate.update(sql,arg );

		LOGGER.info("Insert data in OA_MOLLAK_BUDGET_INFO Successfully");
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Exception Cause:"+e.getMessage());
		}
		
	}
	
	@Override
	public boolean getBudgetDetailsCount(Integer mgmtCompId, String propertyGroupId, Integer budgetYear) {
		LOGGER.info("getBudgetDetailsCount method");
	      String BudgetDetailsCountQuery="select count(BUDGET_ID) from "+mollakDBPackage+"OA_BUDGET_DETAILS where MGMT_COMP_ID=? and PROP_ID=? and BUDGET_PERIOD_CODE like '%"+budgetYear+"%'";
	      Object[] arg = { mgmtCompId,propertyGroupId };
	      
			@SuppressWarnings("deprecation")
			int BudgetDetailsCount = jdbcTemplate.queryForObject(BudgetDetailsCountQuery, arg, Integer.class);
				//System.out.println("BudgetDetailsCountQuery::"+BudgetDetailsCountQuery);
			LOGGER.info("BudgetDetailsCount from databse:::" + BudgetDetailsCount);
			if(BudgetDetailsCount>0) {
				return true;
			}else {
		
				return false;
			}
	}
	
	

	@Override
	public List<String> getServiceCode(Integer mgmtCompId, Integer budgetYear, String propertyGroupId,
			String inputServiceCode) {
		
		LOGGER.info("execute getServiceCode() in DAO");

		
		LOGGER.info("Input MGMT COMP ID dao: "+mgmtCompId);
		LOGGER.info("Input BUDGET YEAR dao: "+budgetYear);
		LOGGER.info("Input PROPERTY GROUP ID dao: "+propertyGroupId);
		LOGGER.info("Input service code dao: "+inputServiceCode);
		

		String query = "SELECT  budget.SERVICE_CODE FROM  "+mollakDBPackage+"oa_budget_items budget, "
				+ "  "+mollakDBPackage+"oa_budget_details bd  WHERE "
				+ " bd.PROP_ID='" + propertyGroupId +  "' AND bd.budget_period_CODE LIKE '%"
				+ budgetYear + "%' AND bd.MGMT_COMP_ID='" + mgmtCompId + "' AND "
				+ "  budget.budget_id=bd.budget_id AND budget.service_code IN('"+inputServiceCode+"')";
		LOGGER.info("Service cods in dao query: "+query);

		List<String> serviceCodes = jdbcTemplate.queryForList(query, String.class);
		
		LOGGER.info("Service cods in dao: "+serviceCodes);

		return serviceCodes;
	}
	
	/*
	 * updating status and Remarks
	 */
	@Override
	public void updatePaymentStatus(Integer pymtId, String status, String remarks) {
		LOGGER.info("Calling updatePaymentStatus Dao method, payment req id:"+ pymtId);

		Object[] args = { status, remarks };

		String query = "UPDATE "+mollakDBPackage+"OA_PAYMENT_REQUESTS SET STATUS=?,REMARKS=? WHERE PYMT_REQ_ID='" + pymtId + "'";
		jdbcTemplate.update(query, args);

		LOGGER.info("Status and Remarks Successfully updated");
	}
	
	
	
	@Override
	public List<PaymentRequestData> getPaymentRequestsIndividualMail(String approvedStatus, String rejectStatus,
			String excpStatus) {
		LOGGER.info("calling getPaymentRequestsIndividualMail in DAO");
		String query="select PYMT_REQ_ID, SUB_PRODUCT, MATRIX_REF_NO, DEBIT_ACCOUNT_NUMBER_DESC, BENEFICIARY_NAME, INITIATOR_DATE, INVOICE_AMOUNT, STATUS, UPLOADED_BY, MATRIX_FILE_REF_NO, REMARKS "
				+ " from "+mollakDBPackage+"oa_payment_requests where status IN('"+approvedStatus+"','"+rejectStatus+"','"+excpStatus+"') AND PAYMENT_TYPE='INDIVIDUAL' AND ISMAILSENT='N' ";
		List<PaymentRequestData> requestsMail = jdbcTemplate.query(query,
				BeanPropertyRowMapper.newInstance(PaymentRequestData.class));
		//LOGGER.info("getPaymentRequestsForMail::"+requestsMail);
		return requestsMail;
	}
	
	@Override
	public void isMailSentPymtReqId(Integer pymtReqId) {
		
		
		LOGGER.info("calling isMailSentPymtReqId method");

		String statusQuery = "UPDATE "+mollakDBPackage+"OA_PAYMENT_REQUESTS SET ISMAILSENT='Y' WHERE PYMT_REQ_ID='" + pymtReqId + "' ";

		jdbcTemplate.update(statusQuery);
		
		LOGGER.info("ismailsent is updated.");
	}
	@Override
	public List<Integer> getPaymentRequestsBulkMailCount(String approvedStatus, String rejectStatus,
			String excpStatus) {
		
		Integer matrixFileCount=null;
				LOGGER.info("calling getPaymentRequestsBulkMailCount in DAO");
				String Query="select distinct(MATRIX_FILE_REF_NO) as MATRIX_FILE_REF_NO from "+mollakDBPackage+"oa_payment_requests where ismailsent='N' and payment_type='BULK'  AND STATUS NOT IN ('APPROVED','EXCEPTION','REJECTED')";
		
		List<Integer> matrixno=jdbcTemplate.query(Query,BeanPropertyRowMapper.newInstance(Integer.class));
					for(int i=0;i<matrixno.size();i++){
						matrixFileCount= jdbcTemplate.queryForObject("select  count(PYMT_REQ_ID) from "+mollakDBPackage+"oa_payment_requests where ismailsent='N'  AND payment_type='BULK' AND STATUS NOT IN ('APPROVED','EXCEPTION','REJECTED') AND MATRIX_FILE_REF_NO='"+matrixno.get(i)+"'",  Integer.class);	
						if(matrixFileCount!=0){
							
						matrixno.remove(i);
						} 
						
					}
					
			return matrixno; 
		
		
	}

	@Override
	public PaymentRequestData getPaymentRequestsBulklMail(Integer matrixFileRefNo) {
		LOGGER.info("calling getPaymentRequestsBulklMail in DAO");
		String query="select PYMT_REQ_ID, SUB_PRODUCT, MATRIX_REF_NO, DEBIT_ACCOUNT_NUMBER_DESC, BENEFICIARY_NAME, INITIATOR_DATE, INVOICE_AMOUNT, STATUS, UPLOADED_BY, MATRIX_FILE_REF_NO, REMARKS "
				+ " from "+mollakDBPackage+"oa_payment_requests where status IN('APPROVED','EXCEPTION','REJECTED') AND PAYMENT_TYPE='BULK' AND ISMAILSENT='N' AND MATRIX_FILE_REF_NO='"+matrixFileRefNo+"'  and rownum=1 ";
		PaymentRequestData requestsMail = jdbcTemplate.queryForObject(query,
				BeanPropertyRowMapper.newInstance(PaymentRequestData.class));
		
		return requestsMail;
		
		
	}

	@Override
	public void isMailSentMatrixFileRefNo(Integer matrixFileRefNo) {

		LOGGER.info("calling isMailSentMatrixFileRefNo method");

		String statusQuery = "UPDATE "+mollakDBPackage+"OA_PAYMENT_REQUESTS SET ISMAILSENT='Y' WHERE PYMT_REQ_ID='" + matrixFileRefNo + "' ";

		jdbcTemplate.update(statusQuery);
		
		LOGGER.info("ismailsent is updated.");
		
	}

	@Override
	public Integer checkForBuildingId(PropertyGroupDetails details) {
		// TODO Auto-generated method stub
		
		try {
			LOGGER.info("calling checkForBuildingId Dao method with " + details.getPropertyGroupName().getEnglishName());
			String sql = "SELECT BUILDING_ID FROM "+mollakDBPackage+"OA_BUILDINGS WHERE upper(BUILDING_NAME)=upper('" +details.getPropertyGroupName().getEnglishName() +"' )";
			
			List<Integer> propertyData = 
			jdbcTemplate.query(sql, new RowMapper<Integer>() {

				@Override
				public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
					// TODO Auto-generated method stub
					return rs.getInt(1);
				}
				
			});
			
			if(propertyData.isEmpty()) {
				return null;
			}
			else if(propertyData!=null && propertyData.size()>0) {
				LOGGER.info("Getting buildingId  =" + propertyData);
				return propertyData.get(0);
			}
			// TODO: return list of MC objects
			
		}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Cause of Exception in checkForBuildingId::"+e.getCause());
				return null;
			}
		return null;
		
	}

	@Override
	public void insertBuildingMapping(Integer buildingId, Integer propId, Integer mgmtId) {
		// TODO Auto-generated method stub
		try {
			LOGGER.info("calling insertBuildingMapping  method");

			String query = "INSERT INTO "+mollakDBPackage+"OA_BUILDING_PROPGROUP_MAPPING(BUILDING_ID, PROP_ID,MGMT_COMP_ID) values(?,?,?) "; //raghuram

			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setInt(1, buildingId);
				ps.setInt(2, propId);  
				ps.setInt(3, mgmtId);
				return ps;
			});
			
			LOGGER.info("Inserted in Mapping Table");
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("cause of Exception in Mapping Table::"+e.getCause());
		}
	}
	
	@Override
	public boolean checkBuildingMapping(Integer buildingId, Integer propId) {
		// TODO Auto-generated method stub
		try {
			LOGGER.info("calling check BuildingMapping  method");

			String query = "SELECT BUILDING_ID FROM "+mollakDBPackage+"OA_BUILDING_PROPGROUP_MAPPING WHERE BUILDING_ID=? ";

			 Object[] arg = { buildingId };
             List<Integer> bdgIds = jdbcTemplate.queryForList(query, arg,Integer.class);
        
	        if(bdgIds != null && bdgIds.size()>0) {
	              LOGGER.info("Mapping available for  BUILDING_ID=" + buildingId );
	              return true;
	        }      
		
			
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("cause of Exception while checking Mapping::"+e.getCause());
		}
		return false;
	}
	
	@Override
    public Integer getMollakMcId(Integer mgmtCompId) throws OAServiceException {
           Integer mcid=null;
           try{
                  LOGGER.info("calling getMollakMcId method in DAO");
                 String mcidquery="select MOLLAK_MC_ID FROM  "+mollakDBPackage+"oa_management_companies  WHERE  MGMT_COMP_ID=?";
                 Object[] arg = { mgmtCompId };
                  List<String> propgrpid = jdbcTemplate.queryForList(mcidquery, arg,String.class);
             
             if(propgrpid!=null && propgrpid.size()>0 && propgrpid.get(0)!="") {
                   LOGGER.info("Getting propgrpid  =" + propgrpid);
                   mcid= Integer.parseInt(propgrpid.get(0));
             }
                        
           }catch (NumberFormatException ex) {
                  LOGGER.info("NumberFormatException :"+ex.getMessage());
                  throw new OAServiceException(ex.getMessage());
           }catch (DataAccessException ex) {
                  LOGGER.info("DataAccessException :"+ex.getMessage());
                  throw new OAServiceException(ex.getMessage());
           }catch (Exception e) {
                  LOGGER.info("Exception :"+e.getMessage());
                  throw new OAServiceException(e.getMessage());
           }
           return mcid;
    }

    @Override
    public Integer getMollakPropGrpId(Integer propId) {
           Integer mollakPropGrpId=null;
           try{
                  LOGGER.info("calling getMollakPropGrpId method in DAO");
                 String propquery="SELECT MOLLAK_PROP_GRP_ID FROM "+mollakDBPackage+"oa_property_groups WHERE prop_id=?";
                 Object[] arg = { propId };
                 
                        
                 
                 
                 List<Integer> propgrpid = jdbcTemplate.queryForList(propquery, arg,Integer.class);
      
      if(propgrpid!=null && propgrpid.size()>0) {
            LOGGER.info("Getting propgrpid  =" + propgrpid);
            mollakPropGrpId= propgrpid.get(0);
      }

           }catch (DataAccessException ex) {
                  LOGGER.info("DataAccessException while fectching mollak prop grp id :"+ex.getMessage());
                  throw new OAServiceException(ex.getMessage());
           }catch (Exception e) {
                  LOGGER.info("Exception while fectching mollak prop grp id:"+e.getMessage());
                  throw new OAServiceException(e.getMessage());
           }
           return mollakPropGrpId;
           
    }
    @Override
	public void insertApprovedBudgetItems(Integer mgmtCompId, String propId, Integer budgetYear, String serviceCode,
			Double invoiceAmount, Integer supplierId, String bifurcation) {
		try {
			LOGGER.info("calling insertApprovedBudgetItems Dao method");
			String sequence=null;
			if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
				sequence="NEXT VALUE FOR " +mollakDBPackage+"OA_APPROVED_BUDGET_ITEMS_SEQ";
			}else {
				sequence="OA_APPROVED_BUDGET_ITEMS_SEQ.nextval";
			}
			//String sequence = "NEXT VALUE FOR " +mollakDBPackage+"BUDEGET_SEQUENCE";
			String sql = "insert into "+mollakDBPackage+"oa_approved_budget_items(DATA_ID, MGMT_COMP_ID, PROP_ID, BUDGET_YEAR, SERVICE_CODE, APPROVED_AMOUNT,"
					+ "  SUPPLIER_ID, STATUS, BIFURCATION) "
					+ "VALUES ("+sequence+",?, ?, ?, ?, ?, ?, 'PENDING', ?)";
			Object[] arg = {mgmtCompId, propId, budgetYear, serviceCode, invoiceAmount, supplierId, bifurcation};
			jdbcTemplate.update(sql,arg );

			LOGGER.info("Insert data in oa_approved_budget_items Successfully");
			}catch (Exception e) {
				e.printStackTrace();
				LOGGER.info("Exception Cause:"+e.getMessage());
			}
		
		
	}

	@Override
	public List<ApprovedBudgetItems> getApprovedBudgetItems(String status) {
		List<ApprovedBudgetItems> approvedBudgetItems = null;
		try{
		LOGGER.info("calling getApprovedBudgetItems in DAO");
		String query="select DATA_ID, MGMT_COMP_ID, PROP_ID, BUDGET_YEAR, SERVICE_CODE, APPROVED_AMOUNT, SUPPLIER_ID, STATUS, BIFURCATION FROM "+mollakDBPackage+"oa_approved_budget_items WHERE STATUS='"+status+"' ";
				
		LOGGER.info("query of getApprovedBudgetItems:"+query);
		 approvedBudgetItems =  jdbcTemplate.query(query,
				BeanPropertyRowMapper.newInstance(ApprovedBudgetItems.class));

				
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Exception Cause:"+e.getMessage());
		}
		return approvedBudgetItems;
	}

	@Override
	public List<BudgetItems> fetchNewBudgetItemList(Integer mgmtCompId, Integer propId, String serviceCodes,
			String budgetYear) {
		List<BudgetItems> budgetItemDetails = null;
		try{
			LOGGER.info("calling fetchNewBudgetItemList dao method");
		//String idsroCommaseperated=StringUtils.join(budgetIdList,",");
		// TODO Auto-generated method stub
		String query="select bi.budget_item_id,bi.total_budget,bi.consumed_amount,bi.balance_amount, bi.service_code "
				+ "from "+mollakDBPackage+"oa_budget_items bi, "+mollakDBPackage+"oa_budget_details bd "
				+ "where bi.budget_id=bd.budget_id  and bd.mgmt_comp_id="+mgmtCompId+" and bd.prop_id="+propId+" and bd.budget_period_code like '%"+budgetYear+"%' AND bi.is_active='Y' and bi.service_code IN('"+serviceCodes+"')";
		budgetItemDetails =  jdbcTemplate.query(query,
					BeanPropertyRowMapper.newInstance(BudgetItems.class));
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Exception Cause:"+e.getMessage());
		}
		return budgetItemDetails;
	}

	@Override
	public void updateApprovedAmounts(List<BudgetItems> newBudgetItemdetails, Integer dataId,
			List<AuditTrail> auditLogs) {
		
		String paymentRequestStatus = "Deducted For Provisional Amount";
		try{
			LOGGER.info("updateAmounts dao is calling..");
			for (int i = 0; i < newBudgetItemdetails.size(); i++) {
				String query = "UPDATE " + mollakDBPackage
						+ "oa_budget_items  SET CONSUMED_AMOUNT=?, balance_amount=? where budget_item_id=?";

				Object[] args = { newBudgetItemdetails.get(i).getConsumedAmount(), newBudgetItemdetails.get(i).getBalanceAmount(),
						newBudgetItemdetails.get(i).getBudgetItemId() };

				jdbcTemplate.update(query, args);
			}
			for (int i = 0; i < auditLogs.size(); i++) {
				String query = "insert into "+ mollakDBPackage+"oa_audit_trail(fieldname,oldvalue,newvalue,updatedby,id,pymt_req_id) values(?,?,?,'OA Scheduler',?,?)";

				Object[] args = { auditLogs.get(i).getFieldName(), auditLogs.get(i).getOldValue(),auditLogs.get(i).getNewValue(), auditLogs.get(i).getId(),paymentRequestStatus };

				jdbcTemplate.update(query, args);
			}
			
			LOGGER.info("added in audit logs table.");

			LOGGER.info("Amounts is updated.");

			String status = "COMPLETED";


			Object[] arg = { status };

			String statusQuery = "UPDATE " + mollakDBPackage
					+ "oa_approved_budget_items SET STATUS=? WHERE DATA_ID='" + dataId + "' ";

			jdbcTemplate.update(statusQuery, arg);

			LOGGER.info("amounts are deducted successfully, status is compleated");
			
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Exception Cause:"+e.getMessage());
		}
		
	}

	@Override
	public boolean checkforPropertyGroupId(Integer propertyGroupId) {
		// TODO Auto-generated method stub
		boolean flag=false;
		try {
			LOGGER.info("checkforPropertyGroupId is calling..");
			
			String sql = "SELECT PROP_ID FROM "+mollakDBPackage+"OA_PROPERTY_GROUPS WHERE MOLLAK_PROP_GRP_ID='" + propertyGroupId + "' ";

			
			List<Integer> details = jdbcTemplate.query(sql,
					BeanPropertyRowMapper.newInstance(Integer.class));
			if (details.size() > 0) {
				flag=true;
			} 
		}catch(Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
