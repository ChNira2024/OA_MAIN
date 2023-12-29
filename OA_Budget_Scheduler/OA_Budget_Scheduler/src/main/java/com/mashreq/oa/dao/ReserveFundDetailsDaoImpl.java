package com.mashreq.oa.dao;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.ApiCallStatus;
import com.mashreq.oa.entity.ReservefundDetails;
import com.mashreq.oa.entity.ReserveseFundData;
import com.mashreq.oa.service.BudgetDetailsServiceImpl;

@Repository
public class ReserveFundDetailsDaoImpl implements ReserveFundDetailsDao {
	private static Logger LOGGER = LoggerFactory.getLogger(ReserveFundDetailsDaoImpl.class);

	@Autowired
	private JdbcTemplate template;

	@Value("${client_secret}")
	private String client_secret;

	@Value("${oa-mollakdb-package}")
	private String mollakDBPackage;

	@Value("${service_code}")
	private String serviceCode;

	@Override
	public ReservefundDetails getAccountDetails(Integer mgmtCompId, Integer propId) {
		try {

			LOGGER.info("Calling getAccountDetails.....");
			String getAccountQuery = "select ACCOUNT_NUMBER as account  , BRANCH_CODE as branch ,CIF_NUMBER as cifId, RESERVE_ACCOUNT_NUMBER as escapeRowAccount "
					+ "from " + mollakDBPackage
					+ "OA_RESERVE_FUND_DETAILS  where MGMNT_COMP_ID=? and BUILDING_ID=? and  IS_ACTIVE=? ";
			String isActive = "Y";
			List<ReservefundDetails> resData = null;
			Object[] args = { mgmtCompId, propId, isActive };
			resData = template.query(getAccountQuery, args,
					BeanPropertyRowMapper.newInstance(ReservefundDetails.class));
			LOGGER.info("Query for getAccounts Details::" + getAccountQuery);
			LOGGER.info("Responce data::" + resData);
			if (!resData.isEmpty()) {
				return resData.get(0);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause of Exception in getAccountDetails()::" + e.getCause());
			return null;

		}
	}

	@Override
	public Integer getBuildingId(Integer propId) {
		try {
			LOGGER.info("Calling getBuildingId()");
			String getBuildingQuery = "";
			List<Integer> getId = template.queryForList("SELECT BUILDING_ID FROM  " + mollakDBPackage
					+ "OA_BUILDING_PROPGROUP_MAPPING WHERE PROP_ID=" + propId + " ", Integer.class);
			LOGGER.info("list of Id>>" + getId);

			if (getId != null && getId.size() > 0) {
				return getId.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause of Exception in getBuildingId()::" + e.getCause());
			return null;
		}
		return null;

	}
	//GET RESERVE_FUND_ID 
	@Override
	public Integer getReserveFundId(Integer mgmtId,Integer buildingId) {
		try {
			LOGGER.info("Calling getReserveFundId()");
			
			List<Integer> getReserveFundId = template.queryForList("SELECT RESERVE_FUND_ID FROM  " + mollakDBPackage
					+ "OA_RESERVE_FUND_DETAILS WHERE MGMNT_COMP_ID=" + mgmtId + " AND BUILDING_ID="+ buildingId +" ", Integer.class);
			LOGGER.info("list of Id>>" + getReserveFundId);

			if (getReserveFundId != null && getReserveFundId.size() > 0) {
				return getReserveFundId.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause of Exception in getReserveFundId()::" + e.getCause());
			return null;
		}
		return null;

	}


	@Override
	public Double getReserveFund(Integer mgmtCompId, Integer propId, String periodCode) {

		try {
			LOGGER.info("Calling getReserveFund()");

//			select SUM(bi.TOTAL_BUDGET) from OA_BUDGET_ITEMS bi, OA_BUDGET_DETAILS bd 
//			  where bi.BUDGET_ID=bd.BUDGET_ID and bd.MGMT_COMP_ID=335 AND bd.PROP_ID=2319  and CATEGORY_NAME_EN != 'Reserved Fund'

			/*List<Double> getReserveFundValue = template.queryForList(
					"SELECT SUM(bi.TOTAL_BUDGET) FROM  " + mollakDBPackage + "OA_BUDGET_ITEMS bi, " + mollakDBPackage
							+ "OA_BUDGET_DETAILS bd WHERE bi.BUDGET_ID=bd.BUDGET_ID and bd.MGMT_COMP_ID=" + mgmtCompId
							+ "AND bd.PROP_ID=" + propId + " AND BUDGET_PERIOD_CODE like '%" + periodCode
							+ "%' AND  CATEGORY_NAME_EN = 'Reserved Fund'",
					Double.class);*/
			
			List<Double> getReserveFundValue = template.queryForList(
					"SELECT SUM(bi.TOTAL_BUDGET) FROM  " + mollakDBPackage + "OA_BUDGET_ITEMS bi, " + mollakDBPackage
					+ "OA_BUDGET_DETAILS bd WHERE bi.BUDGET_ID=bd.BUDGET_ID and bd.MGMT_COMP_ID=" + mgmtCompId
					+ "AND bd.PROP_ID=" + propId + " AND BUDGET_PERIOD_CODE not like 'P%' AND BUDGET_PERIOD_CODE like '%"+periodCode+"%' AND (CATEGORY_NAME_EN = 'Reserved Fund' OR SUB_CATEGORY_NAME_EN='Reserved Fund')",
					Double.class);
			LOGGER.info("% for getReserveFundValue:::" + getReserveFundValue);

			if (getReserveFundValue != null && getReserveFundValue.size() > 0) {
				return getReserveFundValue.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause of Exception in getReserveFund()::" + e.getCause());
			return null;
		}
		return null;

	}

	@Override
	public Double getGeneralFund(Integer mgmtCompId, Integer propId, String periodCode) {
		try {
			LOGGER.info("Calling getGeneralFund()");

//			  select SUM(bi.TOTAL_BUDGET) from OA_BUDGET_ITEMS bi, OA_BUDGET_DETAILS bd 
//			  where bi.BUDGET_ID=bd.BUDGET_ID and bd.MGMT_COMP_ID=335 AND bd.PROP_ID=2319 
//			  and CATEGORY_NAME_EN = 'Reserved Fund';

			/*List<Double> getGeneralFundValue = template.queryForList(
					"SELECT SUM(bi.TOTAL_BUDGET)  FROM  " + mollakDBPackage + "OA_BUDGET_ITEMS bi," + mollakDBPackage
							+ "OA_BUDGET_DETAILS bd WHERE bi.BUDGET_ID=bd.BUDGET_ID and bd.MGMT_COMP_ID=" + mgmtCompId
							+ "AND bd.PROP_ID=" + propId + " AND BUDGET_PERIOD_CODE like '%" + periodCode
							+ "%' AND CATEGORY_NAME_EN != 'Reserved Fund'" + " AND SERVICE_CODE not in " + serviceCode,
					Double.class);*/
			/*List<Double> getGeneralFundValue = template.queryForList(
					"SELECT SUM(bi.TOTAL_BUDGET)  FROM  " + mollakDBPackage + "OA_BUDGET_ITEMS bi," + mollakDBPackage
					+ "OA_BUDGET_DETAILS bd WHERE bi.BUDGET_ID=bd.BUDGET_ID and bd.MGMT_COMP_ID=" + mgmtCompId
					+ "AND bd.PROP_ID=" + propId + " AND BUDGET_PERIOD_CODE not like 'P%' AND BUDGET_PERIOD_CODE like '%"+periodCode+"%' AND CATEGORY_NAME_EN != 'Reserved Fund'"
					+ " AND SERVICE_CODE not in "+serviceCode,
					Double.class);*/
			List<Double> getGeneralFundValue = template.queryForList(
					"SELECT SUM(bi.TOTAL_BUDGET)  FROM  " + mollakDBPackage + "OA_BUDGET_ITEMS bi," + mollakDBPackage
					+ "OA_BUDGET_DETAILS bd WHERE bi.BUDGET_ID=bd.BUDGET_ID and bd.MGMT_COMP_ID=" + mgmtCompId
					+ "AND bd.PROP_ID=" + propId + " AND BUDGET_PERIOD_CODE not like 'P%' AND BUDGET_PERIOD_CODE like '%"+periodCode+"%' AND CATEGORY_NAME_EN != 'Reserved Fund'"
					+ " AND SUB_CATEGORY_NAME_EN != 'Reserved Fund' AND SERVICE_CODE not in "+serviceCode,
					Double.class);
			LOGGER.info("% For getGeneralFundValue " + getGeneralFundValue);

			if (getGeneralFundValue != null && getGeneralFundValue.size() > 0) {
				return getGeneralFundValue.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause of Exception in getGeneralFund()::" + e.getCause());
			return null;
		}
		return null;
	}

//	[RESERVE_FUND_PERCENTAGE]
//		      ,[LAST_CALCULATED_ON]

	@Override
	public int UpdateStatus(Integer mgmtCompId, String percentage, Integer buildingId) {
		try {
			LOGGER.info("Calling UpdateStatus() .....");
			String response = "Successfully upadted";

			String updateQuery = "update " + mollakDBPackage + "OA_RESERVE_FUND_DETAILS set RESERVE_FUND_PERCENTAGE=?"
					+ ", LAST_CALCULATED_ON=? where MGMNT_COMP_ID=? and BUILDING_ID=? ";

//			long timeInSec = System.currentTimeMillis() / 1000;

			System.out.println(new java.sql.Date(System.currentTimeMillis()));

			Object[] args = { percentage, new java.sql.Date(System.currentTimeMillis()), mgmtCompId, buildingId };

			int resData = template.update(updateQuery, args);
			LOGGER.info("Calling UpdateStatus() resData:: " + resData);
			return resData;

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause of Exception in else UpdateStatus()::" + e.getCause());
			return (Integer) null;

		}
	}
	@Override
	public int UpdateRetryStatus(Integer reserveFundId,String apiStatus, String flexStatus) {
		try {
			LOGGER.info("Calling UpdateStatus() .....");
			//String response = "Successfully upadted";

			String updateQuery = "update " + mollakDBPackage + "RESERVE_FUND_ERROR set STATUS=?,ERROR=?"
					+ ", UPDATED_DATE=? where ID=? ";

			System.out.println(new java.sql.Date(System.currentTimeMillis()));

			Object[] args = { flexStatus,apiStatus, new java.sql.Date(System.currentTimeMillis()),reserveFundId };

			int resData = template.update(updateQuery, args);
			LOGGER.info("Calling UpdateRetryStatus() resData:: " + resData);
			return resData;

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause of Exception in else UpdateRetryStatus()::" + e.getCause());
			return (Integer) null;

		}
	}

	/* Added Method for Api error msg storage */
	public int saveAPICallStatusInDB(int id, String errorMsg, String statusAPI) {

		String query = " INSERT INTO  " + mollakDBPackage
				+ "[RESERVE_FUND_ERROR](ID,ERROR,STATUS,UPDATED_DATE)  values(?,?,?,?)";
		LOGGER.info("Query for saving Error in DB::" + query);
		/*SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		dateFormat.format(date);
		LOGGER.info("Date and Time is :::" + dateFormat.format(date));*/
		
		System.out.println(new java.sql.Date(System.currentTimeMillis()));

		Object[] args = { id, errorMsg, statusAPI, new java.sql.Date(System.currentTimeMillis())};

		LOGGER.info("Error MSG::" + errorMsg);
		try {
			int status = template.update(query, args);
			if (status > 0) {

				return status;
			} else {
				LOGGER.error("Failed to insert Error MSG in DB.Status is::" + status);
				return 0;
			}

		} catch (Exception e) {
			LOGGER.info("Exception occured while INSERTING Error MSG into DB!OOPS::" + e.getMessage());
			return 0;

		}

	}

	/*public List<ReserveseFundData> getRecordsHavingNullForRetry() {
		String query = "select * from " + mollakDBPackage
				+ " OA_RESERVE_FUND_DETAILS where [RESERVE_FUND_PERCENTAGE] is  NULL order by [RESERVE_FUND_ID]";
		LOGGER.info("Query For getting Null Records ::" + query);
		try {
			List<ReserveseFundData> listOfData = template.query(query,
					BeanPropertyRowMapper.newInstance(ReserveseFundData.class));
			LOGGER.info("Response after Excuting query::" + listOfData.toString());
			if (listOfData != null) {
				return listOfData;
			} else {
				LOGGER.error("Failed to get response from DB:::" + listOfData);
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception Raised while getting records from DB::" + e.getMessage());
			return null;
		}

	}*/
	
	public List<ReserveseFundData> getRecordsHavingNullForRetry(Integer id) {
		String query= "select * from " + mollakDBPackage + "OA_RESERVE_FUND_DETAILS where RESERVE_FUND_ID= " + id + "";
		
		LOGGER.info("Query For getting Null Records ::" + query);
		try {
			List<ReserveseFundData> listOfData = template.query(query,
					BeanPropertyRowMapper.newInstance(ReserveseFundData.class));
			LOGGER.info("Response after Excuting query::" + listOfData.toString());
			if (listOfData != null) {
				return listOfData;
			} else {
				LOGGER.error("Failed to get response from DB:::" + listOfData);
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception Raised while getting records from DB::" + e.getMessage());
			return null;
		}

	}

	/*public List<ApiCallStatus> getPercentageStatusRecords(Integer id) {
		String query = "SELECT " + mollakDBPackage + "OA_RESERVE_FUND_DETAILS.RESERVE_FUND_PERCENTAGE, "
				+ mollakDBPackage + "RESERVE_FUND_ERROR.STATUS FROM " + mollakDBPackage
				+ "OA_RESERVE_FUND_DETAILS "  +" INNER JOIN "+  mollakDBPackage + "RESERVE_FUND_ERROR ON "
				+ mollakDBPackage + "OA_RESERVE_FUND_DETAILS.RESERVE_FUND_ID=" + mollakDBPackage
				+ "RESERVE_FUND_ERROR.ID where " + mollakDBPackage + "RESERVE_FUND_ERROR.ID= " + id + " ";
		LOGGER.info("Query For getting Failed/Success Records ::" + query);
		try {
			List<ApiCallStatus> listOfData = template.query(query,
					BeanPropertyRowMapper.newInstance(ApiCallStatus.class));
			LOGGER.info("Response after Excuting query::" + listOfData.toString());
			if (listOfData != null) {
				return listOfData;
			} else {
				LOGGER.error("Failed to get response from DB:::" + listOfData);
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception Raised while getting records from DB::" + e.getMessage());
			return null;
		}

	}*/
	public List<String> getStatus(Integer fundId) {
		try {
			LOGGER.info("Calling getStatus()");
			List<String> getStatus = template.queryForList("SELECT STATUS FROM " + mollakDBPackage
					+ "RESERVE_FUND_ERROR WHERE ID=" + fundId + " ", String.class);
			//LOGGER.info("list of Status is:" + getStatus);
			LOGGER.info("list of Status fetched from RESERVE_FUND_ERROR Successfully");

			/*if (getStatus != null && getStatus.size() > 0) {
				return getStatus.get(0);
			} else {
				return null;
			}*/
			return getStatus;

		} catch (Exception e) {
			//e.printStackTrace();
			LOGGER.error("Cause of Exception in getStatus():" + e.getMessage());
			LOGGER.error("Cause of Exception in getStatus():" + e.getCause());
			return null;
		}

	}
	

	public Integer getPropId(Integer buildingID) {
		try {
			LOGGER.info("Calling getPropId()");
			String getBuildingQuery = "";
			List<Integer> getId = template.queryForList("SELECT PROP_ID FROM " + mollakDBPackage
					+ " OA_BUILDING_PROPGROUP_MAPPING WHERE BUILDING_ID=" + buildingID + " ", Integer.class);
			LOGGER.info("list of Porp Id is:::" + getId);

			if (getId != null && getId.size() > 0) {
				return getId.get(0);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause of Exception in getBuildingId()::" + e.getCause());
			return null;
		}

	}
}
