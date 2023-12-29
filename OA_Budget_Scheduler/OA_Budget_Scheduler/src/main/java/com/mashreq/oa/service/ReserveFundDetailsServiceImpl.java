package com.mashreq.oa.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.annotation.JsonValue;

import com.mashreq.oa.dao.ReserveFundDetailsDao;
import com.mashreq.oa.entity.ApiCallStatus;
import com.mashreq.oa.entity.ReserveFundAccountUpdateResponseBO;
import com.mashreq.oa.entity.ReserveFundUpdateBO;
import com.mashreq.oa.entity.ReserveFundUpdateBoForIslamicAccount;
import com.mashreq.oa.entity.ReservefundDetails;
import com.mashreq.oa.entity.ReserveseFundData;
import com.mashreq.oa.entity.TokenResponse;
import com.mashreq.oa.utility.HttpUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Service
public class ReserveFundDetailsServiceImpl implements ReserveFundDetailsService {

	private static Logger LOGGER = LoggerFactory.getLogger(ReserveFundDetailsServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ReserveFundDetailsDao reserveFundDao;
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

	@Value("${X-ORG-ID}")
	private String X_ORG_ID;

	@Value("${X-USER-ID}")
	private String X_USER_ID;
	
	@Value("${ISLAMIC_BRANCH_CODE}")
	private String ISLAMIC_BRANCH_CODE;

	private TokenResponse tokenResponse;


	@Override
	public void updateReserveFundData(Integer mgmtCompId, Integer propId, String periodCode) {
		LOGGER.info("calling updateReserveFundData");
		ReservefundDetails details = null;
		String statusFromAPI = "";

		Integer buildingId = reserveFundDao.getBuildingId(propId);
		LOGGER.info("Building Id is::" + buildingId);
		Integer reserveFundId = reserveFundDao.getReserveFundId(mgmtCompId, buildingId);
		LOGGER.info("Reserver_Fund Id is::" + reserveFundId);

		if (periodCode == null || periodCode == "") {
			Date tempDate = new Date();
			periodCode = tempDate.getYear() + "";
			LOGGER.info("periodCode::" + periodCode);
		}

		Double reserveFund = reserveFundDao.getReserveFund(mgmtCompId, propId, periodCode);

		Double generalFund = reserveFundDao.getGeneralFund(mgmtCompId, propId, periodCode);
		LOGGER.info("Reserve Fund & General Fund method completed");
		LOGGER.info("Reserve Fund  Value is ::" + reserveFund);
		LOGGER.info("General Fund  Value is ::" + generalFund);

		Double percentage = (reserveFund) / (generalFund + reserveFund) * 100; // raghuram
		String convertedValue = null;

		LOGGER.info("Caluclated percentage Value is ::" + percentage);
		DecimalFormat df = new DecimalFormat("#.#####");
		convertedValue = df.format(percentage).toString();
		LOGGER.info("percentage Value is  converted into String::" + convertedValue);
		details = reserveFundDao.getAccountDetails(mgmtCompId, buildingId);
		details.setEscapeRowPercentage(convertedValue);
		LOGGER.info("Response from getAccountDetails::" + details);

		// call update service -to update
		LOGGER.info(" updating ReserveFundData Details");
		int insertresponse = reserveFundDao.UpdateStatus(mgmtCompId, convertedValue, buildingId);
		LOGGER.info("After updating response::" + insertresponse);
		LOGGER.info("Completed::" + insertresponse);
		LOGGER.info("Branch Code From Account Details::" +details.getBranch());

	//	String ISLAMIC_BRANCH_CODE = "091";
		String statusFromIslamicAPI = null;
		String statusFromConventionalAPI = null;
		if (insertresponse > 0 && details.getBranch() != null && details.getBranch().equals(ISLAMIC_BRANCH_CODE)) {
			// update percentage & current date
			String statusAPI = "Success";
			// call Islamic API here
			try {
				statusFromIslamicAPI = updateIslamicAccountPercentage(details);

				int result = reserveFundDao.saveAPICallStatusInDB(reserveFundId, statusFromIslamicAPI, statusAPI);
				if (result > 0) {

					LOGGER.info("Success Status has been Updated in DB For Islamic Account.");

				}

			} catch (Exception ex) {
				String apiError = ex.getMessage();
				LOGGER.info("Exception Occured While Making a Call to updateIslamicAccountPercentage API" + apiError);
				reserveFundDao.saveAPICallStatusInDB(reserveFundId, apiError, "Failed");

			}
		} else if (insertresponse > 0) {
			String statusAPI = "Success";
			try {
				statusFromConventionalAPI = updateConventionalAccountPercentage(details);

				int result = reserveFundDao.saveAPICallStatusInDB(reserveFundId, statusFromConventionalAPI, statusAPI);
				if (result > 0) {
					LOGGER.info("Success Status has been Updated in DB For Conventional Account.");
				}

			} catch (Exception ex) {
				String apiError = ex.getMessage();
				LOGGER.info("Exception Occured While Making a Call to updateConventionalAccountPercentage API:"+apiError);
				reserveFundDao.saveAPICallStatusInDB(reserveFundId, apiError, "Failed");
			}

		} else {
			String statusAPI = "Failed due to ERROR";
			String status = "Flex call is not invoked due to Exception";

			int result = reserveFundDao.saveAPICallStatusInDB(reserveFundId, status, statusAPI);
			
			if (result > 0) {

				LOGGER.info("Error has been Updated in DB.");

			}

		}

	}

	private String updateConventionalAccountPercentage(ReservefundDetails details) {
		String conventinalErrorAPI = "";
		try {
//			TokenResponse tokenResponse = httpUtil.generateToken("CORE");
			TokenResponse tokenResponse = generateToken();
			String x_Msg_Id = generateX_MSG_ID();
			ResponseEntity<ReserveFundAccountUpdateResponseBO> apiResponse = null;
			LOGGER.info("Access Token::" + tokenResponse.getAccess_token());
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set("Accept", "application/json");
			headers.set("clientid", client_id);
			headers.set("Authorization", "Bearer " + tokenResponse.getAccess_token());
			headers.set("scope", "CORE");
			headers.set("X-MSG-ID", x_Msg_Id); // need to generate random message id conventional & Islamic
			headers.set("X-ORG-ID", X_ORG_ID);
			headers.set("X-USER-ID", X_USER_ID);
			LOGGER.info("Headers::" + headers);
			LOGGER.info("Conventional Account Details::" + details);
			ReserveFundUpdateBO reserveFundUpdateBO = new ReserveFundUpdateBO();
			reserveFundUpdateBO.setAccount(details.getAccount());
			reserveFundUpdateBO.setEscrowtrn("Y");
			reserveFundUpdateBO.setCifId(details.getCifId());
			reserveFundUpdateBO.setBranch(details.getBranch());
			reserveFundUpdateBO.setEscapeRowAccount(details.getEscapeRowAccount());
			reserveFundUpdateBO.setEscapeRowBranchCode(details.getBranch());
			reserveFundUpdateBO.setEscapeRowPercentage(details.getEscapeRowPercentage());

			LOGGER.info("Request Body::" + reserveFundUpdateBO);

							
				HttpEntity<ReserveFundUpdateBO> request = new HttpEntity<ReserveFundUpdateBO>(reserveFundUpdateBO,
						headers);
				LOGGER.info("Conventioanl API url::" + accountUpdateURL);
				LOGGER.info("Entity::" + request);

				apiResponse = restTemplate.postForEntity(accountUpdateURL, request,
						ReserveFundAccountUpdateResponseBO.class);
			LOGGER.info("apiResponse : " + apiResponse);
			if (apiResponse.getBody().getStatus().equals("Success")) {
				return apiResponse.getBody().getStatus();
			} else {
				conventinalErrorAPI = apiResponse.getBody().toString();
				LOGGER.info("ConventionalErrorAPI :::" + conventinalErrorAPI);
				return conventinalErrorAPI;
			}

		} catch (Exception e) {
			conventinalErrorAPI = e.getMessage();
			//LOGGER.error("Cause of Exception in updatePercentage()::" + e.getCause());
			LOGGER.error("Cause of Exception in Conventinal-Account-update::" + e.getMessage());
			return conventinalErrorAPI;

		}

	}

	private String updateIslamicAccountPercentage(ReservefundDetails details) {
		String IslamicErrorAPI = "";

		try {
			LOGGER.info("Entered into updateIslamicAccountPercentage API Call Method()");
			TokenResponse tokenResponse = generateToken();
			String x_Msg_Id = generateX_MSG_ID();
			ResponseEntity<ReserveFundAccountUpdateResponseBO> IslamicApiResponse = null;
			LOGGER.info("Access Token::" + tokenResponse.getAccess_token());
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set("Accept", "application/json");
			headers.set("clientid", client_id);
			headers.set("Authorization", "Bearer " + tokenResponse.getAccess_token());
			// headers.set("scope", "CORE"); no need scope
			headers.set("X-MSG-ID", x_Msg_Id);
			headers.set("X-ORG-ID", X_ORG_ID);
			headers.set("X-USER-ID", X_USER_ID);
			LOGGER.info("Headers::" + headers);
			LOGGER.info("Islamic API details::" + details);

			ReserveFundUpdateBoForIslamicAccount islamicAccountDetailsRequest = new ReserveFundUpdateBoForIslamicAccount();
			islamicAccountDetailsRequest.setBranch(details.getBranch());
			islamicAccountDetailsRequest.setAccount(details.getAccount());
			islamicAccountDetailsRequest.setCustno(details.getCifId());
			islamicAccountDetailsRequest.setEscrowtrn("Y");
			islamicAccountDetailsRequest.setEscapeRowPercentage(details.getEscapeRowPercentage());
			islamicAccountDetailsRequest.setEscapeRowBranchCode(details.getBranch());
			islamicAccountDetailsRequest.setEscapeRowAccount(details.getEscapeRowAccount());

			LOGGER.info("Request Body::" + islamicAccountDetailsRequest);

			HttpEntity<ReserveFundUpdateBoForIslamicAccount> request = new HttpEntity<ReserveFundUpdateBoForIslamicAccount>(
					islamicAccountDetailsRequest, headers);
			LOGGER.info("Islamic API url::" + IslamicaccountUpdateURL);
			LOGGER.info("Islamic Account Entity Request::" + request);

			IslamicApiResponse = restTemplate.postForEntity(IslamicaccountUpdateURL, request,
					ReserveFundAccountUpdateResponseBO.class);

			LOGGER.info("Islamic Account-apiResponse : " + IslamicApiResponse.toString());
			LOGGER.info("Islamic Account Response Size is ::" + IslamicApiResponse.toString().length());
			if (IslamicApiResponse.getBody().getStatus().equals("Success")) {
				return IslamicApiResponse.getBody().getStatus();
			} else {
				IslamicErrorAPI = IslamicApiResponse.getBody().toString();
				LOGGER.info("IslamicErrorAPI :::" + IslamicErrorAPI);
				return IslamicErrorAPI;
			}

		} catch (Exception e) {
			IslamicErrorAPI = e.getMessage();
			LOGGER.error("Cause of Exception in updatePercentage() For Islamic Account::" + e.getMessage());
			LOGGER.error("Cause of Exception is::" + e.getCause());
			return IslamicErrorAPI;

		}
	}

	/* Generate Token Method */
	public TokenResponse generateToken() {

		long timeInSec = System.currentTimeMillis() / 1000;

		String status = "Success";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> urlVariables = new LinkedMultiValueMap<String, String>();
			urlVariables.add("client_id", client_id);
			urlVariables.add("client_secret", client_secret);
			urlVariables.add("grant_type", grant_type);
			urlVariables.add("scope", scopeForToken);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
					urlVariables, headers);
			ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenURL, request, TokenResponse.class);

			LOGGER.info("Response from the REquest:::" + response.toString());

			if (response != null) {
				this.tokenResponse = response.getBody();
				LOGGER.info("tokenResponse:::" + tokenResponse);

				return tokenResponse;
			} else {
				LOGGER.error("Failed to get Response from API Connect URL");
				return null;
			}

		} catch (Exception ex) {
			status = ex.getMessage();
			LOGGER.error("testToken+status:" + status);
			ex.printStackTrace();
			return null;
		}

	}

	/* Retry Option Logic implementation for Screen Level */
	public void retryToUpdatePercentage(Integer reserveFund_Id) {

		LOGGER.info("Entered into retryToUpdatePercentage() method");

		String responseForNullPercentageAfterRetry = null;

		//List<ApiCallStatus> flexStatus = null;
		List<String> flexStatus = null;  // need to change for retry mechanism

		String apiCallStatusForReservfundId = null;

		// 1st scenario check if % is calculated or not?
		// DB Call to fecth percentage value if it is null call below method
		//try {
		List<ReserveseFundData> records = reserveFundDao.getRecordsHavingNullForRetry(reserveFund_Id);

		for (ReserveseFundData reserveseFundData : records) {

			LOGGER.info("Percentage of ReserveFund_Id is:" + reserveseFundData.getReserveFundPercentage());

			String percentageValue = reserveseFundData.getReserveFundPercentage();

			flexStatus = reserveFundDao.getStatus(reserveFund_Id);
			LOGGER.info("Status of given RESERVE_FUND_ID:"+reserveFund_Id+ " from RESERVE_FUND_ERROR Table is:"+flexStatus);

			/*for (ApiCallStatus status : flexStatus) {
				apiCallStatusForReservfundId = status.getApiStatus();
				LOGGER.info("Status of percentage from Error_Table: " + apiCallStatusForReservfundId);

			}*/
			//LOGGER.info("Status of percentage from Error_Table: " + flexStatus);

			if (percentageValue == null) {
				LOGGER.info("First If block");

				responseForNullPercentageAfterRetry = calculatePercentageWithCurrentYear(reserveseFundData.mgmntCompId,
						reserveseFundData.getBuildingId(), reserveseFundData.getReserveFundId());
				
				/*responseForNullPercentageAfterRetry = calculatePercentageForNullRecords(reserveseFundData.mgmntCompId,
						reserveseFundData.buildingId, reserveseFundData.reserveFundId);*/

				LOGGER.info("Status for retryNull-Value Percentage:" + responseForNullPercentageAfterRetry);

			} else if (percentageValue != null && flexStatus.contains("Failed")) {
				// call API to retry
				// 2nd scenario check if % is not null and status is failed then call API
				LOGGER.info("entered into else-if part to update flex failed records");
				String apiCallStatusForFaildRecords = apiCallForRetryMechanism(reserveseFundData.mgmntCompId,
						reserveseFundData.buildingId, percentageValue, reserveseFundData.reserveFundId);

				LOGGER.info("API Call Status for failed records: " + apiCallStatusForFaildRecords);
			} else if (percentageValue != null && flexStatus.contains("Success")) {
				// call calculate % here and calculate with current year
				// cal API method
				LOGGER.info("Entered into calculate Percentage with current year Block");

				String apiCallStatusForSuccessRecords = calculatePercentageWithCurrentYear(
						reserveseFundData.mgmntCompId, reserveseFundData.buildingId, reserveseFundData.reserveFundId);
				LOGGER.info("Status of API call is:" + apiCallStatusForSuccessRecords);
			}else {
				LOGGER.info("No Records Found with Success or Failed Status for Given Reserve_Fund_Id:"+reserveFund_Id);
			}
		}

	}


	// API Call And % Calulation with current year in which Reserver_Fund_id is
	// having % and Success in API
	public String calculatePercentageWithCurrentYear(Integer mgmtCompId, Integer BuildingId, Integer reserve_FundId) {

		String apiResponse = null;

		LOGGER.info("Entered inside loop of calculate Percentage With Current Year::");
		LOGGER.info("Starting process of Reserve Fund::" + reserve_FundId);
		LOGGER.info("Management ID is :::" + mgmtCompId);
		LOGGER.info("Building ID is:::" + BuildingId);

		Integer propID = reserveFundDao.getPropId(BuildingId);
		LOGGER.info("Prop ID is ::" + propID);

		// getting current year
		Year getCurrentYear = Year.now();
		String currentYear = String.valueOf(getCurrentYear);
		LOGGER.info(">>>Current Year is<<< " + currentYear);

		if (propID != null) {

			LOGGER.info("PropID is ::" + propID);
			LOGGER.info("Calculating the Percentage with Current Year:"+currentYear);

			Double reserveFund = reserveFundDao.getReserveFund(mgmtCompId, propID, currentYear);
			LOGGER.info("Reserve Fund value is :::" + reserveFund);

			Double generalFund = reserveFundDao.getGeneralFund(mgmtCompId, propID, currentYear);
			LOGGER.info("General fund value is :::" + generalFund);

			if (reserveFund != null && generalFund != null && !reserveFund.isNaN() && !generalFund.isNaN()) {

				Double percentage = (reserveFund) / (generalFund + reserveFund) * 100; // raghuram
				String convertedValue = null;

				LOGGER.info("Caluclated percentage Value is ::" + percentage);
				DecimalFormat df = new DecimalFormat("#.#####");
				convertedValue = df.format(percentage).toString();
				LOGGER.info("percentage Value is  converted into String::" + convertedValue);

				LOGGER.info("Upadating Percentage.");
				int response = reserveFundDao.UpdateStatus(mgmtCompId, convertedValue, BuildingId);
				LOGGER.info("Percentage has updated successfully!");
				if (response > 0) {

					// calling API method
					apiResponse = apiCallForRetryMechanism(mgmtCompId, BuildingId, convertedValue, reserve_FundId);
					LOGGER.info("Percentage has updated successfully to Flex!");

				} else {
					LOGGER.error("Percentage is not updated in DB!");
				}

			} else {
				LOGGER.info("reserve Fund is NULL.");
			}

		} else {
			LOGGER.info("Prop_Group Id is not Available");
		}

		return apiResponse;
	}

	public String apiCallForRetryMechanism(Integer managementCompId, Integer buildingId, String percentage,
			Integer id) {

		ReservefundDetails details = null;
		String statusFromAPI = "";

		details = reserveFundDao.getAccountDetails(managementCompId, buildingId);
		details.setEscapeRowPercentage(percentage);
		LOGGER.info("Response from getAccountDetails::" + details);

		//String ISLAMIC_BRANCH_CODE = "091";
		String statusFromIslamicAPI = null;
		String statusFromConventionalAPI = null;
		String FinalResult = null;
		LOGGER.info("Branch Code From Account Details::" +details.getBranch());
		if (details.getBranch().equals(ISLAMIC_BRANCH_CODE)) {
			// update percentage & current date
			String statusAPI = "Success";
			// call Islamicconventional API here
			try {
				statusFromIslamicAPI = updateIslamicAccountPercentage(details);
				FinalResult = statusFromIslamicAPI;

				//int result = reserveFundDao.saveAPICallStatusInDB(id, FinalResult, statusAPI);// here need to write update call
				int result = reserveFundDao.UpdateRetryStatus(id, FinalResult, statusAPI);
				if (result > 0) {

					LOGGER.info("Success has been Updated in OA_RSERVE_FUND_ERROR TABLE For Islamic Account");

				}

			} catch (Exception ex) {
				String apiError = ex.getMessage();
				LOGGER.info("Exception Occured While Making a Call to updateIslamicAccountPercentage API in retry:"+apiError);
				//reserveFundDao.saveAPICallStatusInDB(id, apiError, "Failed");
				reserveFundDao.UpdateRetryStatus(id, FinalResult, "Failed");

			}
		} else {
			String statusAPI = "Success";
			try {
				statusFromConventionalAPI = updateConventionalAccountPercentage(details);
				FinalResult = statusFromConventionalAPI;
				
				LOGGER.info("Response::"+FinalResult);
				//int result = reserveFundDao.saveAPICallStatusInDB(id, FinalResult, statusAPI);// need to update the status insted of insertion
				int result = reserveFundDao.UpdateRetryStatus(id, FinalResult, statusAPI);
				if (result > 0) {
					LOGGER.info("Success has been Updated in OA_RSERVE_FUND_ERROR TABLE For Conventional Account");
				}

			} catch (Exception ex) {
				String apiError = ex.getMessage();
				LOGGER.info("Exception Occured While Making a Call to updateConventionalAccountPercentage API in retry:"+apiError);
				//reserveFundDao.saveAPICallStatusInDB(id, apiError, "Failed");
				reserveFundDao.UpdateRetryStatus(id, FinalResult, "Failed");
			}
		}

		return FinalResult;

	}

	// to generate random X_MSG_ID
	public String generateX_MSG_ID() {
		String digit = "";
		Random r = new Random();
		for (int i = 0; i < 10; i++)
			digit = digit + r.nextInt(10);
		LOGGER.info("Digit:" + digit);
		return digit;

	}
	public static void main(String[] args) {
		List<String> status = Arrays.asList("","Failed Due to Error","NA","","failed","Failed Due to Error");
		if(status.contains("Success")) {
			System.out.println("retry percentage");
			
		}else if(status.contains("Failed")) {
			System.out.println("update to flex");
			
		}else {
			System.out.println("No Record Found");
		}
	}

	
}
