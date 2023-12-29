package com.mashreq.oa;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mashreq.oa.dao.ReserveFundDetailsDao;
import com.mashreq.oa.entity.ReserveFundAccountUpdateResponseBO;
import com.mashreq.oa.entity.ReserveFundUpdateBO;
import com.mashreq.oa.entity.ReserveFundUpdateBoForIslamicAccount;
import com.mashreq.oa.entity.ReservefundDetails;
import com.mashreq.oa.entity.ReserveseFundData;
import com.mashreq.oa.entity.TokenResponse;
import com.mashreq.oa.service.ReserveFundDetailsService;
import com.mashreq.oa.service.ReserveFundDetailsServiceImpl;
import com.mashreq.oa.utility.HttpUtil;

@RestController
public class UpdatePercentage {

	private static Logger logger = LoggerFactory.getLogger(UpdatePercentage.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpUtil httpUtil;

	@Value("${accountUpdate.url}")
	private String accountUpdateURL;
	
	@Value("${IslamicAccountUpdate.url}")
	private String IslamicaccountUpdateURL;
	
	@Value("${tokenForRes.url}")
	private String tokenURL;

	@Value("${client_id}")
	private String client_id;

	@Value("${client_secret}")
	private String client_secret;

	@Value("${scopeForToken}")
	private String scopeForToken;

	@Value("${grant_type}")
	private String grant_type;

	@Value("${X-MSG-ID}")
	private String X_MSG_ID;

	@Value("${X-ORG-ID}")
	private String X_ORG_ID;

	@Value("${X-USER-ID}")
	private String X_USER_ID;
	
	@Value("${ISLAMIC_BRANCH_CODE}")
	private String ISLAMIC_BRANCH_CODE;

	private TokenResponse tokenResponse;

	@Autowired
	private JdbcTemplate template;
	@Autowired
	private ReserveFundDetailsDao reserveFundDao;
	@Autowired
	private ReserveFundDetailsServiceImpl serviceImpl;

	@Value("${oa-mollakdb-package}")
	private String mollakDBPackage;

	@GetMapping("/updateresfunddata")
	public void updatePercentage(@RequestParam("year") String year) {
		// get management comp ids where percentage = null
		// start for loop
		// get propid based on building id

		List<ReserveseFundData> resFromDB = getRecordsHavingNull();

		for (ReserveseFundData reserveseFundData : resFromDB) {
			logger.info("Entered inside loop::");
			logger.info("Starting process of Reserve Fund::" + reserveseFundData.getReserveFundId());
			logger.info("Management ID is :::" + reserveseFundData.getMgmntCompId());
			logger.info("Building ID is:::" + reserveseFundData.getBuildingId());

			Integer propID = getPropId(reserveseFundData.getBuildingId());
			logger.info("Prop ID is ::" + propID);

			if (propID != null) {

				logger.info("PropID is ::" + propID);

				Double reserveFund = reserveFundDao.getReserveFund(reserveseFundData.getMgmntCompId(), propID, year);
				logger.info("Reserve Fund value is :::" + reserveFund);

				Double generalFund = reserveFundDao.getGeneralFund(reserveseFundData.getMgmntCompId(), propID, year);
				logger.info("General fund value is :::" + generalFund);

				if (reserveFund != null && generalFund != null && !reserveFund.isNaN() && !generalFund.isNaN()) {

					Double percentage = (reserveFund) / (generalFund + reserveFund)*100; //raghuram
					String convertedValue = null;

					logger.info("Caluclated percentage Value is ::" + percentage);
					DecimalFormat df = new DecimalFormat("#.#####");
					convertedValue = df.format(percentage).toString();
					logger.info("percentage Value is  converted into String::" + convertedValue);

					logger.info("Upadating Percentage.");
					int response = reserveFundDao.UpdateStatus(reserveseFundData.getMgmntCompId(), convertedValue,
							reserveseFundData.getBuildingId());
					if (response > 0) {
						logger.info("Percentage has updated successfully!");

					} else {
						logger.error("Percentage has not updated successfully!!");
					}

				}

			}

			// update percentage & current date
//			logger.info(" updateReserveFundData sucess");
//			int response = reserveFundDao.UpdateStatus(mgmtCompId, convertedValue, buildingId);
//			logger.info(buildingId + ",,After updating response::" + response);
//			logger.info("Completed::" + response);

		}

//		Double reserveFund = reserveFundDao.getReserveFund(mgmtCompId, propId, periodCode);
//
//		Double generalFund = reserveFundDao.getGeneralFund(mgmtCompId, propId, periodCode);
//		LOGGER.info("Reserve Fund & General Fund method completed");
//		LOGGER.info("Reserve Fund  Value is ::" + reserveFund);
//		LOGGER.info("General Fund  Value is ::" + generalFund);
//
//		Double percentage = (reserveFund) / (generalFund + reserveFund);
//		String convertedValue = null;
//
//		LOGGER.info("Caluclated percentage Value is ::" + percentage);
//		DecimalFormat df = new DecimalFormat("#.#####");
//		convertedValue = df.format(percentage).toString();
//		LOGGER.info("percentage Value is  converted into String::" + convertedValue);
//
//		// update percentage & current date
//		LOGGER.info(" updateReserveFundData sucess");
//		int response = reserveFundDao.UpdateStatus(mgmtCompId, convertedValue, buildingId);
//		LOGGER.info(buildingId + ",,After updating response::" + response);
//		LOGGER.info("Completed::" + response);

		// end loop

	}

	public List<ReserveseFundData> getRecordsHavingNull() {
		String query = "select * from " + mollakDBPackage
				+ " OA_RESERVE_FUND_DETAILS where [RESERVE_FUND_PERCENTAGE] is  NULL order by [RESERVE_FUND_ID]";
		logger.info("Query For getting Null Records ::" + query);
		try {
			List<ReserveseFundData> listOfData = template.query(query,
					BeanPropertyRowMapper.newInstance(ReserveseFundData.class));
			logger.info("Response after Excuting query::" + listOfData.toString());
			if (listOfData != null) {
				return listOfData;
			} else {
				logger.error("Failed to get response from DB:::" + listOfData);
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Raised while getting records from DB::" + e.getMessage());
			return null;
		}

	}

	public Integer getPropId(Integer buildingID) {
		try {
			logger.info("Calling getPropId()");
			String getBuildingQuery = "";
			List<Integer> getId = template.queryForList("SELECT PROP_ID FROM " + mollakDBPackage
					+ " OA_BUILDING_PROPGROUP_MAPPING WHERE BUILDING_ID=" + buildingID + " ", Integer.class);
			logger.info("list of Porp Id is:::" + getId);

			if (getId != null && getId.size() > 0) {
				return getId.get(0);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Cause of Exception in getBuildingId()::" + e.getCause());
			return null;
		}

	}

	public List<ReserveseFundData> getRecordsToUpdateFlex(String id) {
		String query = "select * from " + mollakDBPackage + "OA_RESERVE_FUND_DETAILS where [RESERVE_FUND_ID] in (" + id
				+ ")";
		logger.info("Query For getting Null Records ::" + query);
		try {
			List<ReserveseFundData> listOfData = template.query(query,
					BeanPropertyRowMapper.newInstance(ReserveseFundData.class));
			logger.info("Response after Excuting query::" + listOfData.toString());
			if (listOfData != null) {
				return listOfData;
			} else {
				logger.error("Failed to get response from DB:::" + listOfData);
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Raised while getting records from DB::" + e.getMessage());
			return null;
		}

	}

	@GetMapping(value = "/updateToFlex")
	public void flexPercentage(@RequestParam("year") String year, @RequestParam("id") String id) {
		String error = "";

		List<ReserveseFundData> records = getRecordsToUpdateFlex(id);
		for (ReserveseFundData reserveseFundData : records) {
			String statusFromAPI = "";

			try {
				logger.info("Entered inside loop::");
				logger.info("Starting process of Reserve Fund::" + reserveseFundData.getReserveFundId());
				logger.info("Management ID is :::" + reserveseFundData.getMgmntCompId());
				logger.info("Building ID is:::" + reserveseFundData.getBuildingId());
				Integer propID = getPropId(reserveseFundData.getBuildingId());

				logger.info("Prop ID is ::" + propID);

				if (propID != null) {

					logger.info("PropID is ::" + propID);

					Double reserveFund = reserveFundDao.getReserveFund(reserveseFundData.getMgmntCompId(), propID,
							year);
					logger.info("Reserve Fund value is :::" + reserveFund);

					Double generalFund = reserveFundDao.getGeneralFund(reserveseFundData.getMgmntCompId(), propID,
							year);
					logger.info("General fund value is :::" + generalFund);

					if (reserveFund != null && generalFund != null && !reserveFund.isNaN() && !generalFund.isNaN()) {

						Double percentage = (reserveFund) / (generalFund + reserveFund)*100;  //raghuram
						String convertedValue = null;

						logger.info("Caluclated percentage Value is ::" + percentage);
						DecimalFormat df = new DecimalFormat("#.#####");
						convertedValue = df.format(percentage).toString();
						logger.info("percentage Value is  converted into String::" + convertedValue);

//						logger.info("Upadating Percentage.");
//						int response = reserveFundDao.UpdateStatus(reserveseFundData.getMgmntCompId(), convertedValue,
//								reserveseFundData.getBuildingId());

						ReservefundDetails details = reserveFundDao.getAccountDetails(
								reserveseFundData.getMgmntCompId(), reserveseFundData.getBuildingId());
						details.setEscapeRowPercentage(convertedValue);
//							details.setEscapeRowPercentage(convertedValue);
						logger.info("response from getAccountDetails::" + details.toString());

						// Calling Flex Call
						// statusFromAPI = updateConventionalAccountPercentage(details);
						// logger.info("Status from flex API::" + statusFromAPI);

						/*
						 * Before updating into Flex, first we are updating the Percentage details in
						 * database
						 */
						int isResponse = reserveFundDao.UpdateStatus(reserveseFundData.getMgmntCompId(), convertedValue,
								reserveseFundData.getBuildingId());

						logger.info(" updateReserveFundData success");
						logger.info("After updating response::" + isResponse);
						logger.info("Completed::" + isResponse);
						logger.info("Branch Code is::"+details.getBranch());

						//String ISLAMIC_BRANCH_CODE = "091";

						String statusFromIslamicAPI = null;
						String statusFromConventionalAPI = null;

						if (isResponse > 0 && details.getBranch() != null && details.getBranch().equals(ISLAMIC_BRANCH_CODE)) {
							// update percentage & current date
							String statusAPI = "Success";
							// call Islamic API here
							statusFromIslamicAPI = updateIslamicAccountPercentage(details);

							int result = saveErrorInDB(reserveseFundData.getReserveFundId(), statusFromIslamicAPI,
									statusAPI);
							if (result > 0) {

								logger.info("Success has been Updated in DB For Islamic Account.");

							}
						} else if (isResponse > 0) {
							String statusAPI = "Success";
							statusFromConventionalAPI = updateConventionalAccountPercentage(details);
							int result = saveErrorInDB(reserveseFundData.getReserveFundId(), statusFromConventionalAPI,
									statusAPI);
							if (result > 0) {
								logger.info("Status has been Updated in DB For Conventional Account.");
							}

						} else {
							String statusAPI = "Failed due to ERROR";
							statusFromAPI="Flex call is not invoked due to Exception";
							int result = saveErrorInDB(reserveseFundData.getReserveFundId(), statusFromAPI, statusAPI);
							// save method
							if (result > 0) {

								logger.info("Error has Updated in DB.");
								logger.info("Flex call is not invoked due to Exception");
							}

						}
					} else {
						logger.info("Reserve Fund  is NULL.");
						String statusAPI = "Failed due to ERROR";
						String status = "Flex call is not invoked due to No Reserve Fund";
						int result = saveErrorInDB(reserveseFundData.getReserveFundId(), status,statusAPI);
						// save method
						if (result > 0) {

							logger.info("Error has been Updated in DB.");
						}
					}

				}

			} catch (Exception e) {
				logger.info("Exception Occured :: " + e.getMessage());
				logger.info("Cause of Exception is:: " + e.getCause());

			}
		}
	}

	public int saveErrorInDB(int id, String errorMsg, String statusAPI) {

		String query = " INSERT INTO  " + mollakDBPackage
				+ "[RESERVE_FUND_ERROR](ID,ERROR,STATUS,UPDATED_DATE)  values(?,?,?,?)";
		logger.info("Query for saving Error in DB::" + query);
		/*SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		dateFormat.format(date);
		logger.info("Date and Time is :::" + dateFormat.format(date));*/
		System.out.println(new java.sql.Date(System.currentTimeMillis()));

		Object[] args = { id, errorMsg, statusAPI,new java.sql.Date(System.currentTimeMillis()) };

		logger.info("Error MSG::" + errorMsg);
		try {
			int status = template.update(query, args);
			if (status > 0) {

				return status;
			} else {
				logger.error("Failed to insert Error MSG in DB.Status is::" + status);
				return 0;
			}

		} catch (Exception e) {
			logger.info("Exception occured while INSERTING Error MSG into DB!OOPS::" + e.getMessage());
			return 0;

		}

	}

	private Object Date(long currentTimeMillis) {
		// TODO Auto-generated method stub
		return null;
	}

//earlier it was update updatePercentage 
	private String updateConventionalAccountPercentage(ReservefundDetails details) {
		String ConventionalErrorAPI = "";

		try {
//			TokenResponse tokenResponse = httpUtil.generateToken("CORE");
			logger.info("Entered into Conventional Account Percentage API Call ");
			TokenResponse tokenResponse = serviceImpl.generateToken();
			String x_msg_id=generateX_MSG_ID();
			ResponseEntity<ReserveFundAccountUpdateResponseBO> apiResponse = null;
			logger.info("Access Token::" + tokenResponse.getAccess_token());
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set("Accept", "application/json");
			headers.set("clientid", client_id);
			headers.set("Authorization", "Bearer " + tokenResponse.getAccess_token());
			headers.set("scope", "CORE");
			headers.set("X-MSG-ID", x_msg_id);
			headers.set("X-ORG-ID", X_ORG_ID);
			headers.set("X-USER-ID", X_USER_ID);
			logger.info("Conventinal API Headers::" + headers);
			logger.info("Conventinal API details::" + details);
			ReserveFundUpdateBO reserveFundUpdateBO = new ReserveFundUpdateBO();
			reserveFundUpdateBO.setAccount(details.getAccount());
			reserveFundUpdateBO.setEscrowtrn("Y");
			reserveFundUpdateBO.setCifId(details.getCifId());
			reserveFundUpdateBO.setBranch(details.getBranch());
			reserveFundUpdateBO.setEscapeRowAccount(details.getEscapeRowAccount());
			reserveFundUpdateBO.setEscapeRowBranchCode(details.getBranch());
			reserveFundUpdateBO.setEscapeRowPercentage(details.getEscapeRowPercentage());

			logger.info("Request Body::" + reserveFundUpdateBO);

			HttpEntity<ReserveFundUpdateBO> request = new HttpEntity<ReserveFundUpdateBO>(reserveFundUpdateBO, headers);
			logger.info("Conventinal API url::" + accountUpdateURL);
			logger.info("Entity::" + request);

			apiResponse = restTemplate.postForEntity(accountUpdateURL, request,
					ReserveFundAccountUpdateResponseBO.class);

			logger.info("apiResponse : " + apiResponse.toString());
			logger.info("Response Size is ::" + apiResponse.toString().length());
			if (apiResponse.getBody().getStatus().equals("Success")) {
				return apiResponse.getBody().getStatus();
			} else {
				ConventionalErrorAPI = apiResponse.getBody().toString();
				logger.info("ConventionalErrorAPI :::" + ConventionalErrorAPI);
				return ConventionalErrorAPI;
			}

		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Cause of Exception in updatePercentage()::" + e.getMessage());
			ConventionalErrorAPI = e.getMessage();
			return ConventionalErrorAPI;

		}
	}

	// method to call Islamic percentage update to Flex --->Raghuram
	private String updateIslamicAccountPercentage(ReservefundDetails details) {
		String IslamicErrorAPI = "";

		try {

			logger.info("Entered into updateIslamicAccountPercentage API Call Method()");
			TokenResponse tokenResponse = serviceImpl.generateToken();
			String x_msg_id=generateX_MSG_ID();
			ResponseEntity<ReserveFundAccountUpdateResponseBO> islamicApiResponse = null;
			logger.info("Access Token::" + tokenResponse.getAccess_token());
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set("Accept", "application/json");
			headers.set("clientid", client_id);
			headers.set("Authorization", "Bearer " + tokenResponse.getAccess_token());
			//headers.set("scope", "CORE"); no need to add scope
			headers.set("X-MSG-ID", x_msg_id);
			headers.set("X-ORG-ID", X_ORG_ID);
			headers.set("X-USER-ID", X_USER_ID);
			logger.info("Islamic API Call Headers::" + headers);
			logger.info("Islamic API details::" + details);

			ReserveFundUpdateBoForIslamicAccount islamicAccountDetailsRequest = new ReserveFundUpdateBoForIslamicAccount();
			islamicAccountDetailsRequest.setBranch(details.getBranch());
			islamicAccountDetailsRequest.setAccount(details.getAccount());
			islamicAccountDetailsRequest.setCustno(details.getCifId());
			islamicAccountDetailsRequest.setEscrowtrn("Y");
			islamicAccountDetailsRequest.setEscapeRowPercentage(details.getEscapeRowPercentage());
			islamicAccountDetailsRequest.setEscapeRowBranchCode(details.getBranch());
			islamicAccountDetailsRequest.setEscapeRowAccount(details.getEscapeRowAccount());

			logger.info("Request Body::" + islamicAccountDetailsRequest);

			HttpEntity<ReserveFundUpdateBoForIslamicAccount> request = new HttpEntity<ReserveFundUpdateBoForIslamicAccount>(islamicAccountDetailsRequest, headers);
			logger.info("Islamic API url::" + IslamicaccountUpdateURL);
			logger.info("Islamic Account Entity::" + request);

			islamicApiResponse = restTemplate.postForEntity(IslamicaccountUpdateURL, request,
					ReserveFundAccountUpdateResponseBO.class);

			logger.info("Islamic Account-apiResponse : " + islamicApiResponse.toString());
			logger.info("Islamic Account Response Size is ::" + islamicApiResponse.toString().length());
			if (islamicApiResponse.getBody().getStatus().equals("Success")) {
				return islamicApiResponse.getBody().getStatus();
			} else {
				IslamicErrorAPI = islamicApiResponse.getBody().toString();
				logger.info("IslamicErrorAPI :::" + IslamicErrorAPI);
				return IslamicErrorAPI;
			}

		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Cause of Exception in Islamic Account Percentage Update:" + e.getMessage());
			IslamicErrorAPI = e.getMessage();
			return IslamicErrorAPI;

		}
	}
	// to generate random X_MSG_ID
		public String generateX_MSG_ID() {
			String digit = "";
			Random r = new Random();
			for (int i = 0; i < 10; i++)
				digit = digit + r.nextInt(10);
			logger.info("Digit:" + digit);
			return digit;

		}

}

