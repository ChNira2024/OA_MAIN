package com.mashreq.oa.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.mashreq.oa.dao.BudgetDetailsDao;
import com.mashreq.oa.entity.ApprovedBudgetItems;
import com.mashreq.oa.entity.AuditTrail;
import com.mashreq.oa.entity.BeneficiaryListDetails;
import com.mashreq.oa.entity.BudgetDetails;
import com.mashreq.oa.entity.BudgetItemDetails;
import com.mashreq.oa.entity.BudgetItems;
import com.mashreq.oa.entity.BudgetResponse;
import com.mashreq.oa.entity.Language;
import com.mashreq.oa.entity.ManagementCompanies;
import com.mashreq.oa.entity.ManagementCompanyDetails;
import com.mashreq.oa.entity.ManagementCompanyResponse;
import com.mashreq.oa.entity.NotificationDetails;
import com.mashreq.oa.entity.PaymentRequestData;
import com.mashreq.oa.entity.PaymentRequestStatus;
import com.mashreq.oa.entity.PropertyGroupDetails;
import com.mashreq.oa.entity.PropertyGroups;
import com.mashreq.oa.entity.PropertyGroupsResponse;
import com.mashreq.oa.entity.ServiceChargeGroupDetails;
import com.mashreq.oa.entity.ServiceStatusDetails;
import com.mashreq.oa.entity.TokenResponse;
import com.mashreq.oa.exceptions.OAServiceException;
import com.mashreq.oa.utility.HttpUtil;

@Service
public class BudgetDetailsServiceImpl implements BudgetDetailsService {

	private static Logger LOGGER = LoggerFactory.getLogger(BudgetDetailsServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	private static final String INPROGRESS = "IN-PROGRESS";
	private static final String COMPLETED = "COMPLETED";
	private static final String FAILED = "FAILED";
	private static final String SOURCE_MOLLAK = "MOLLAK";
	private static final String SOURCE_OA = "OA";

	public static final String SCHEDULER_NAME = "OA_BUDGET_SCHEDULER";
	public static final String MANAGEMENT_COMPANIES = "MANAGEMENT_COMPANIES";
	public static final String PROPERTY_GROUPS = "PROPERTY_GROUPS";
	public static final String BUDGET_DETAILS = "BUDGET_DETAILS";
	/********** for validation ***********/
	private static final String INPROGSTATUS = "IN-PROGRESS";

	private static final String VALIDATINGSTATUS = "VALIDATING_PAYMENT_REQUEST";

	private static final String EXCPSTATUS = "EXCEPTION";

	private static final String REJECTSTATUS = "REJECTED";

	private static final String FAILEDSTATUS = "FAILED";

	private static final String APPROVEDSTATUS = "APPROVED";

	private static final String MOLLAKNODATASTATUS = "PENDING";

	private static final String MOLLAKSOURCE = "OA";

	private static final String SERVICEREMARKS = " Service Code is missing : ";

	private static final String TRADEDATEREMARKS = "Trade License Date is expired  ";

	private static final String AGREEMENTDATEREMARKS = "Agreement Date is Expired ";

	private static final String BUDGETREMARKS = "Invoice Amount is exceeded Budget Amount ";

	private static final String BUDGETDETAILSREQUIRED = "Budget details are required from Mollak ";

	private static final String BUDGETYEARREMARKS = "Budget Year is not matched  ";

	private static final String NOMAPPINGREMARKS = " No mapping between Building Name & Property Group Name ";

	private static final String NOMOLLAKDATAREMARKS = " No Mollak data for this Management Company or Property Group ";

	private static final String SERVICECODESFORMATREMARKS = "Invalid Service Codes format :";

	private static final String NOTMATCHEDREMARKS = "Service Code is invalid: ";

	private static final String NOPROPERDOCUMENTS = "No Proper Data/Documents ";

	private static final String NODATAINMOLLAKSTATUS = "BUDGETDETAILS_PENDING";

	// private static String STATUS = "";
	/********** for validation ***********/

	@Value("${client_id}")
	private String client_id;

	@Autowired
	private BudgetDetailsDao budgetDetailsDao;

	@Autowired
	private ReserveFundDetailsService fundDetailsService;

	@Value("${managementCompany.url}")
	private String managementCompanyurl;

	@Value("${propertyGroups.url}")
	private String propertyGroupsurl;

	@Value("${budgetDetails.url}")
	private String budgetDetailsurl;

	@Value("${spring.mcIdStatus}")
	private String completdStatus;

	@Value("${spring.mcIdStatuPending}")
	private String pendingStatus;

	@Value("${spring.budgetDetailsPending}")
	private String budgetDetailsPending;

	@Value("${budgetDetails.retryCount}")
	private int budgetRetryCount;

	@Autowired
	private HttpUtil httpUtil;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void fetchBudgetData() {
		LOGGER.info("calling updateBudgetData");
		try {

			Boolean statusFlag = false;
			NotificationDetails provisionResult = getProvisionNotificationDetails(pendingStatus, completdStatus,
					SOURCE_MOLLAK);
			if (provisionResult != null && provisionResult.getCount() == 1) {
				Integer mgmtCompId = budgetDetailsDao.getMgmtCompId(provisionResult.getMollakMcId());
				LOGGER.info(" MCID:>" + mgmtCompId);
				Integer propId = budgetDetailsDao.getPropId(provisionResult.getMollakPropGrpId());
				LOGGER.info(" PROPID:>" + propId);

				List<Integer> budgetIdList = null;
				Map<Integer, String> mapbudgetId = null;
				String periodCode = provisionResult.getPeriodCode();
				periodCode = "P-" + periodCode;
				if (mgmtCompId != null && propId != null) {
					budgetIdList = budgetDetailsDao.validateBudgetIdForExistingData(propId, mgmtCompId, periodCode);

				}
				LOGGER.info("Budget  Id ::" + budgetIdList);

				if (mgmtCompId != null && propId != null) {
					mapbudgetId = budgetDetailsDao.validateBudgetIdForExistingDataMap(propId, mgmtCompId, periodCode);

				}
				LOGGER.info("Map Budget  Primary Key is::" + mapbudgetId);
				List<BudgetItemDetails> oldBudgetItemsList = budgetDetailsDao.getOldBudgetItemDetails(budgetIdList);

				for (BudgetItemDetails bean : oldBudgetItemsList) {

					if (bean != null) {

						for (Map.Entry<Integer, String> entry : mapbudgetId.entrySet()) {
							Integer key = entry.getKey();

							String value = entry.getValue();
							if (Integer.compare(bean.getBudgetId(), key) == 0) {
								bean.setServiceChrgGrpId(value);
							}

						}

					}

				}
				LOGGER.info("OLD Budget Item Details Size::" + oldBudgetItemsList.size());

				try {
					updateProvisionalBudgetItems(oldBudgetItemsList, budgetDetailsurl, provisionResult, mgmtCompId,
							propId);

					statusFlag = true;
				} catch (Exception e) {
					LOGGER.error("Exception while Updating provisional Budget Data" + e.getMessage());
				} finally {

					if (statusFlag) {
						updateNotification(COMPLETED, provisionResult.getBudgetInfoId(),
								provisionResult.getRetryCount());

					} else {
						updateNotification(FAILED, provisionResult.getBudgetInfoId(), provisionResult.getRetryCount());
					}
				}

			} else {
				// call get - return object
				NotificationDetails result = getNotificationDetails(pendingStatus, completdStatus, SOURCE_MOLLAK); // SOURCE_MOLLAK

				// statusFlag = false;
				if (result != null && result.getCount() == 0) {

					// add try block
					try {
						// add new method updateNotification("in progress") call
						// here
						int resultValue = updateNotification(INPROGRESS, result.getBudgetInfoId(),
								result.getRetryCount());
						if (resultValue == 1) {

							Map<Integer, Integer> mgmtCompIds = validateManagementCompany(result.getMollakMcId(),
									managementCompanyurl);
							LOGGER.info(">>>>>>>>>>>" + mgmtCompIds.get(result.getMollakMcId()));

							LOGGER.info("Validated MGMT Successfully");

							Map<Integer, Integer> propGrpIds = validatePropertyGroupId(result, propertyGroupsurl,
									mgmtCompIds.get(result.getMollakMcId()), result.getMollakMcId());

							/*
							 * for (Map.Entry<Integer, Integer> entry : mgmtCompIds.entrySet()) { Integer
							 * key = entry.getKey(); Integer value = entry.getValue();
							 * 
							 * LOGGER.info(">>>>>>>>>>>" + key); LOGGER.info(">>>>>>>>>>>" + value); }
							 */

							LOGGER.info("After Validating Property Groups >>>>>>>>>>>" + propGrpIds);
							/*
							 * for (Map.Entry<Integer, Integer> entry : propGrpIds.entrySet()) { Integer key
							 * = entry.getKey(); Integer value = entry.getValue();
							 * 
							 * LOGGER.info(">>>>>>>>>>>" + key); LOGGER.info(">>>>>>>>>>>" + value); }
							 */

							getBudgetDetails(mgmtCompIds, propGrpIds, result.getMollakPropGrpId(), budgetDetailsurl,
									result);

							// statusFlag = true; it means successfully completed
							statusFlag = true;
							// close try block
						} else {
							LOGGER.info("update status to IN-PROGRESS failed");
						}
					}

					// start catch block
					catch (Exception e) {
						/// write some log
						LOGGER.info("Exception raised due to:" + e.getMessage());
					}
					// start finally block
					finally {
						// if(statusFlag){
						// call updateNotification("Completed") call here
						// }else{
						// call updateNotification method("Failed")
						// }
						if (statusFlag) {
							updateNotification(COMPLETED, result.getBudgetInfoId(), result.getRetryCount());
							if (result.getPymtReqId() != null && SOURCE_OA.equals(result.getSource())) {
								// update payment re table to IN-PROGRESS
								budgetDetailsDao.updatePaymentStatus(result.getPymtReqId(), INPROGSTATUS, "");
							}
						} else {
							updateNotification(FAILED, result.getBudgetInfoId(), result.getRetryCount());
						}
					}

				} else if (result != null && result.getCount() > 0) {
					// update case
					// write new method for update
					try {
						int resultValue = updateNotification(INPROGRESS, result.getBudgetInfoId(),
								result.getRetryCount());
						if (resultValue == 1) {
							LOGGER.info("No. of Already Completed cases..." + result.getCount()
									+ result.getBudgetInfoId() + result.getMollakMcId() + result.getPeriodCode()
									+ result.getMollakPropGrpId());
							Integer mcId = result.getMollakMcId();
							Integer pId = result.getMollakPropGrpId();
							Integer mgmtCmpId = null;
							Integer propertyId = null;
							Integer budgetId = null;
							List<Integer> budgetIdList = null;
							String periodCode = result.getPeriodCode();
							Map<Integer, String> mapbudgetId = null;
							if (mcId != null) {
								List<Integer> mgmtCompIdList = budgetDetailsDao.ValidateManagementCompanyId(mcId);
								if (mgmtCompIdList != null && mgmtCompIdList.size() > 0) {
									mgmtCmpId = mgmtCompIdList.get(0);

								}
								LOGGER.info("MGMT Primary Key is::" + mgmtCmpId);
								if (pId != null && mgmtCmpId != null) {
									List<Integer> propertyGroupIdList = budgetDetailsDao
											.validatePropertyGroupIdForExistingData(pId, mgmtCmpId);
									if (propertyGroupIdList != null && propertyGroupIdList.size() > 0) {
										propertyId = propertyGroupIdList.get(0);

									}
								}
								LOGGER.info("Property  Primary Key is::" + propertyId);

								if (mgmtCmpId != null && propertyId != null) {
									budgetIdList = budgetDetailsDao.validateBudgetIdForExistingData(propertyId,
											mgmtCmpId, periodCode);
									if (budgetIdList != null && budgetIdList.size() > 0) {
										budgetId = budgetIdList.get(0);

									}
								}
								LOGGER.info("Budget  Id ::" + budgetIdList);

								if (mgmtCmpId != null && propertyId != null) {
									mapbudgetId = budgetDetailsDao.validateBudgetIdForExistingDataMap(propertyId,
											mgmtCmpId, periodCode);

								}

								LOGGER.info("Map Budget  Primary Key is::" + mapbudgetId);

								for (Map.Entry<Integer, String> entry : mapbudgetId.entrySet()) {
									Integer key = entry.getKey();

									String value = entry.getValue();
									// LOGGER.info("Key is::" + key + "Value::" + value);
								}
								List<ServiceChargeGroupDetails> oldBudgetDetailsList = budgetDetailsDao
										.getOldBudgetDetails(budgetIdList);
								LOGGER.info("Old Budget  List   is::" + oldBudgetDetailsList.get(0).getBudgetId());
								LOGGER.info("Old Budget  List  size  is::" + oldBudgetDetailsList.size());
								List<ServiceChargeGroupDetails> newBudgetDetailsList = getNewBudgetDetails(mgmtCmpId,
										propertyId, budgetDetailsurl, result);
								LOGGER.info("New Budget  List size  is::" + newBudgetDetailsList.size());

								// LOGGER.info("New Budget List is::" + newBudgetDetailsList);

								checkforBudgetDetails(oldBudgetDetailsList, newBudgetDetailsList, result, mapbudgetId); // validate
																														// budget
																														// Details

								List<BudgetItemDetails> oldBudgetItemsList = budgetDetailsDao
										.getOldBudgetItemDetails(budgetIdList);

								for (BudgetItemDetails bean : oldBudgetItemsList) {

									if (bean != null) {

										for (Map.Entry<Integer, String> entry : mapbudgetId.entrySet()) {
											Integer key = entry.getKey();

											String value = entry.getValue();
											if (Integer.compare(bean.getBudgetId(), key) == 0) {
												bean.setServiceChrgGrpId(value);
											}

										}

									}

								}
								LOGGER.info("OLD Budget Item Details Size::" + oldBudgetItemsList.size());
								// LOGGER.info("OLD Budget Item Details::" + oldBudgetItemsList);
								List<BudgetItemDetails> newBudgetItemsList = getNewBudgetItemDetails(mgmtCmpId,
										propertyId, budgetDetailsurl, result);

								for (BudgetItemDetails bean : newBudgetItemsList) {
									// LOGGER.info("Inside loop::");
									if (bean != null) {
										for (Map.Entry<Integer, String> entry : mapbudgetId.entrySet()) {
											Integer key = entry.getKey();

											String value = entry.getValue();
											if (value.equalsIgnoreCase(bean.getServiceChrgGrpId())) {
												bean.setBudgetId(key);
											}
											// LOGGER.info("Key is::" + key + "Value::" + value);
										}

									}

								}
								LOGGER.info("New Budget Item Details Size::" + newBudgetItemsList.size());
								LOGGER.info("New Budget Item Details::" + newBudgetItemsList.get(0).getBudgetId());
								checkForBudgetItemDetails(oldBudgetItemsList, newBudgetItemsList, result, mapbudgetId);
								statusFlag = true;
							}

						} else {
							LOGGER.info("update status to IN-PROGRESS failed");
						}
					} // start catch block
					catch (Exception e) {
						/// write some log
						LOGGER.info("Exception raised due to:" + e.getMessage());
					} finally {
						// if(statusFlag){
						// call updateNotification("Completed") call here
						// }else{
						// call updateNotification method("Failed")
						// }
						if (statusFlag == true) {
							updateNotification(COMPLETED, result.getBudgetInfoId(), result.getRetryCount());
						} else {
							updateNotification(FAILED, result.getBudgetInfoId(), result.getRetryCount());
						}
					}

				} else {
					// TODO: print logger - No notifications for processing
					LOGGER.info("No Notification For Processing...");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateProvisionalBudgetItems(List<BudgetItemDetails> oldBudgetItemsList, String url,
			NotificationDetails result, Integer mgmtCompId, Integer propId) {
		try {
			LOGGER.info("Calling updateProvisionalBudgetItems method calling");
			ResponseEntity<BudgetResponse> budgetDetails = null;
			TokenResponse tokenResponse = httpUtil.generateToken();
			LOGGER.info("Access TOken::" + tokenResponse.getAccess_token());
			HttpHeaders headers = new HttpHeaders();
			headers.set("clientid", client_id);
			headers.set("Authorization", "Bearer " + tokenResponse.getAccess_token());
			HttpEntity request = new HttpEntity(headers);
			if (result != null && result.getSource().equals("MOLLAK")) {
				budgetDetails = restTemplate.exchange(
						url + "/" + result.getMollakPropGrpId() + "/all" + "/" + result.getPeriodCode(), HttpMethod.GET,
						request, BudgetResponse.class);
			}
			LOGGER.info("Additional Data:>>" + budgetDetails.getBody().getResponse().getAdditionalData());
			if (budgetDetails != null && budgetDetails.getBody() != null) {
				Integer serviceId = null;
				LOGGER.info("budgetDetails" + budgetDetails.getBody().getResponseCode() + "=="
						+ budgetDetails.getBody().getTimeStamp() + "=="
						+ budgetDetails.getBody().getValidationErrorsList().toString());
				ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
				serviceStatusDetails.setService_Name(BUDGET_DETAILS);
				serviceStatusDetails.setResponse_Code(budgetDetails.getBody().getResponseCode());
				serviceStatusDetails.setTimestamp(budgetDetails.getBody().getTimeStamp());

				try {
					serviceId = budgetDetailsDao.saveServiceStatus(serviceStatusDetails);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BudgetDetails details = budgetDetails.getBody().getResponse();

				String data = details.getAdditionalData();
				Integer dataId = null;

				if (data != null) {
					Integer data_id = budgetDetailsDao.saveAdditionalData(data);
					dataId = data_id;
				}
				ServiceChargeGroupDetails[] detailList = details.getServiceChargeGroups();
				for (int i = 0; i < detailList.length; i++) {
					ServiceChargeGroupDetails serviceDetails = detailList[i];
					LOGGER.info("prop_Id" + propId);
					serviceDetails.setDataId(dataId);
					serviceDetails.setEmail(details.getPropertyManagerEmail());
					serviceDetails.setPropId(propId);
					serviceDetails.setMgmtId(mgmtCompId);
					serviceDetails.setServiceId(serviceId);
					int budgetId = budgetDetailsDao.saveBudgetDetails(serviceDetails);
					LOGGER.info("Service charge Group ID" + serviceDetails.getServiceChargeGroupId());
					BudgetItemDetails[] budgetdata = serviceDetails.getBudgetItems();
					// for{
					for (int j = 0; j < budgetdata.length; j++) {
						BudgetItemDetails budgetList = budgetdata[j];
						budgetList.setBudgetId(budgetId);
						budgetList.setServiceId(serviceId);
						budgetList.setTotalBudget(budgetList.getTotalCost());
						for (BudgetItemDetails oldData : oldBudgetItemsList) {
							if (serviceDetails.getServiceChargeGroupId().equalsIgnoreCase(oldData.getServiceChrgGrpId())
									&& oldData.getServiceCode().replace("P", "")
											.contains(budgetList.getServiceCode())) {
								budgetList.setConsumedAmount(oldData.getConsumedAmount());
								budgetList.setBalanceAmount(oldData.getBalanceAmount());
							}
						}
						budgetList.setTotalCost(budgetList.getTotalCost() - budgetList.getVatAmount());
						budgetList.setIsActive("Y");
						budgetDetailsDao.saveBudgetItems(budgetList);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause of Exception in updateProvisionalBudgetItems::" + e.getCause());

		}

	}

	private NotificationDetails getProvisionNotificationDetails(String pendingStatus, String completdStatus,
			String source) {
		try {
			LOGGER.info("Calling getProvisionNotificationDetails method calling");
			NotificationDetails result = budgetDetailsDao.getProvisionNotificationDetails(pendingStatus, completdStatus,
					source);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause fo Exception in getProvisionNotificationDetails::" + e.getCause());
			return null;
		}
	}

	private void checkforBudgetDetails(List<ServiceChargeGroupDetails> oldBudgetDetailsList,
			List<ServiceChargeGroupDetails> newBudgetDetailsList, NotificationDetails result,
			Map<Integer, String> budgetId) {
		List<ServiceChargeGroupDetails> modifiedNewBudgetList = new ArrayList<ServiceChargeGroupDetails>();
		// TODO Auto-generated method stub

		for (ServiceChargeGroupDetails newData : newBudgetDetailsList) {
			int matchCount = 0;
			boolean checkFLag = false;
			for (ServiceChargeGroupDetails oldData : oldBudgetDetailsList) {

				if ((newData.getServiceChargeGroupId().equals(oldData.getServiceChargeGroupId()))
						&& (newData.getUsage().getEnglishName().equalsIgnoreCase(oldData.getUsage().getEnglishName())
								&& result.getSource().equals("MOLLAK"))) {
					if (newData.getBudgetPeriodCode().equalsIgnoreCase(oldData.getBudgetPeriodCode())) {
						LOGGER.info("Data Matched ::" + newData.getBudgetPeriodFrom() + "==="
								+ oldData.getBudgetPeriodTo());
					} else {
						LOGGER.info("Data Not Matched ::" + newData.getBudgetPeriodFrom() + "==="
								+ oldData.getBudgetPeriodTo());
						ServiceChargeGroupDetails serviceChargeGroupDetails = new ServiceChargeGroupDetails();
						serviceChargeGroupDetails.setBudgetPeriodCode(newData.getBudgetPeriodCode());
						serviceChargeGroupDetails.setBudgetPeriodTitle(newData.getBudgetPeriodTitle());
						serviceChargeGroupDetails.setBudgetPeriodFrom(newData.getBudgetPeriodFrom());
						serviceChargeGroupDetails.setBudgetPeriodTo(newData.getBudgetPeriodTo());
						serviceChargeGroupDetails.setServiceChargeGroupId(newData.getServiceChargeGroupId());
						Language language = new Language();
						language.setEnglishName(newData.getUsage().getEnglishName());
						serviceChargeGroupDetails.setUsage(language);
						budgetDetailsDao.updateExistingBudgetList(serviceChargeGroupDetails, budgetId);
					}
					matchCount++;
					checkFLag = true;
					break;
				} /*
					 * else { LOGGER.info("Data Not Matched ::" + newData.getServiceChargeGroupId()
					 * + "==OLD" + (oldData.getServiceChargeGroupId()));
					 * //modifiedNewBudgetList.add(newData); } if (matchCount ==1) { break; }
					 */
			}
			if (checkFLag) {
				LOGGER.info("Data already Exists ");

			} else {
				modifiedNewBudgetList.add(newData);
				for (ServiceChargeGroupDetails budgetList : modifiedNewBudgetList) {
					LOGGER.info("New Data :: " + budgetList.getServiceChargeGroupId() + "=="
							+ budgetList.getBudgetPeriodFrom() + "==" + budgetList.getBudgetPeriodTo()
							+ budgetList.getBudgetPeriodCode());
					Integer newBudgetId = budgetDetailsDao.saveBudgetDetails(budgetList);
					if (newBudgetId != null && newBudgetId != 0) {
						BudgetItemDetails[] budgetdata = budgetList.getBudgetItems();
						for (int i = 0; i < budgetdata.length; i++) {
							BudgetItemDetails budgetItemList = budgetdata[i];
							budgetItemList.setBudgetId(newBudgetId);
							budgetDetailsDao.saveBudgetItems(budgetItemList);
						}
					}

				}
			}

		} // end of Loop
		LOGGER.info("Checking for Not Existing Service Charge Group Id's");

		for (ServiceChargeGroupDetails oldData : oldBudgetDetailsList) {
			boolean duplicateFalg = false;
			for (ServiceChargeGroupDetails newData : newBudgetDetailsList) {
				if (newData.getServiceChargeGroupId().equalsIgnoreCase(oldData.getServiceChargeGroupId())) {

					duplicateFalg = true;
					break;
				}

			}
			if (!duplicateFalg) {
				LOGGER.info("Service Charge Group Id does not Exists:: " + oldData.getServiceChargeGroupId());
				budgetDetailsDao.updateActiveFlagForBudgetDetails(budgetId, oldData);

			} else {
				LOGGER.info("Service Charge Group Id  Exists:: ");
			}
		}

	}

	private void checkForBudgetItemDetails(List<BudgetItemDetails> oldbudgetItemsList,
			List<BudgetItemDetails> newBudgetItemsList, NotificationDetails result, Map<Integer, String> budgetId) {
		// TODO Auto-generated method stub
		List<BudgetItemDetails> addNewListData = new ArrayList<BudgetItemDetails>();

		for (BudgetItemDetails newData : newBudgetItemsList) {
			boolean checkFlag = false;
			for (BudgetItemDetails oldData : oldbudgetItemsList) {

				if ((newData.getServiceCode().equals(oldData.getServiceCode()))
						&& newData.getServiceChrgGrpId().equalsIgnoreCase(oldData.getServiceChrgGrpId())
						&& Integer.compare(newData.getBudgetId(), oldData.getBudgetId()) == 0) {

					if ((Math.floor(newData.getTotalCost()) == Math.floor(oldData.getTotalBudget()))
							&& (Math.floor(newData.getVatAmount()) == Math.floor(oldData.getVatAmount()))) {
						LOGGER.info("Data Matched ::" + newData.getTotalCost() + "===" + oldData.getTotalCost());
						// LOGGER.info("Data Matched ::" + newData.getVatAmount() + "===" +
						// oldData.getVatAmount());
					} else {

						if (Math.floor(newData.getTotalCost()) != Math.floor(oldData.getTotalBudget())) {
							budgetDetailsDao.insertAuditDetails(newData.getBudgetId(), "TOTAL_COST",
									String.valueOf(oldData.getTotalCost()), String.valueOf(newData.getTotalCost()),
									SCHEDULER_NAME);
						}
						if (Math.floor(newData.getVatAmount()) != Math.floor(oldData.getVatAmount())) {
							budgetDetailsDao.insertAuditDetails(newData.getBudgetId(), "VAT_AMOUNT",
									String.valueOf(oldData.getVatAmount()), String.valueOf(newData.getVatAmount()),
									SCHEDULER_NAME);
						}
						Double modifedTotalCost = null;
						Double modifiedTotalVatAmount = null;
						Double modifiedTotalBudget = null;
						Double modifiedBalanceAmount = null;
						modifedTotalCost = newData.getTotalCost() - oldData.getTotalBudget();
						modifiedTotalVatAmount = newData.getVatAmount() - oldData.getVatAmount();
						LOGGER.info("Modified values::" + modifedTotalCost + "==" + modifiedTotalVatAmount);
						modifiedTotalBudget = newData.getTotalBudget();
						budgetDetailsDao.insertAuditDetails(newData.getBudgetId(), "TOTAL_BUDGET",
								String.valueOf(oldData.getTotalBudget()), String.valueOf(modifiedTotalBudget),
								SCHEDULER_NAME);
						LOGGER.info("Modified Total Budget values::" + modifiedTotalBudget);
						modifiedBalanceAmount = oldData.getBalanceAmount() + modifedTotalCost;
						budgetDetailsDao.insertAuditDetails(newData.getBudgetId(), "BALANCE_AMOUNT",
								String.valueOf(oldData.getBalanceAmount()), String.valueOf(modifiedBalanceAmount),
								SCHEDULER_NAME);
						LOGGER.info("Modified Balance Amountt values::" + modifiedBalanceAmount);
						budgetDetailsDao.updateBudgetItemDetails(newData.getBudgetId(), newData.getTotalCost(),
								newData.getVatAmount(), modifiedTotalBudget, modifiedBalanceAmount,
								newData.getServiceCode());

					}

					checkFlag = true;
					break;

				} /*
					 * else { LOGGER.info("Data Not Matched"); } if (matchCount == 1) { break; }
					 */
			}
			if (checkFlag) {
				// LOGGER.info("Data already Exists ");

			} else {
				addNewListData.add(newData);
				for (BudgetItemDetails budgetList : addNewListData) {
					LOGGER.info("New Data :: " + budgetList.getCategoryCode() + "==" + budgetList.getSubCategoryCode()
							+ "==" + budgetList.getServiceCode());
					budgetList.setBudgetId(budgetList.getBudgetId());
					budgetList.setTotalBudget(budgetList.getTotalCost());
					budgetList.setConsumedAmount(0.0);
					budgetList.setBalanceAmount(budgetList.getTotalCost());
					budgetList.setIsActive("Y");
					budgetList.setServiceId(budgetList.getServiceId());
					budgetDetailsDao.saveBudgetItems(budgetList);
				}
			}
		}
		LOGGER.info("Add New Data is" + addNewListData.size());

		for (BudgetItemDetails oldData : oldbudgetItemsList) {
			boolean duplicateFalg = false;
			for (BudgetItemDetails newData : newBudgetItemsList) {
				if (newData.getServiceChrgGrpId().equalsIgnoreCase(oldData.getServiceChrgGrpId())
						&& Integer.compare(newData.getBudgetId(), oldData.getBudgetId()) == 0) {
					if (oldData.getServiceCode().equals(newData.getServiceCode())) {
						// LOGGER.info("Data Matched::");
						duplicateFalg = true;
					}
				}
				if (duplicateFalg) {
					break;
				}
			}
			if (!duplicateFalg) {
				// LOGGER.info("Service Code Data does not Exists:: " +
				// oldData.getServiceCode());
				budgetDetailsDao.updateActiveFlag(oldData.getBudgetId(), oldData.getServiceCode());

			} else {
				// LOGGER.info("Service Code Data Exists:: ");
			}
		}

	}

	private List<BudgetItemDetails> getNewBudgetItemDetails(Integer mgmtCmpId, Integer propertyId,
			String budgetDetailsurl, NotificationDetails result) {
		// TODO Auto-generated method stub
		TokenResponse tokenResponse = httpUtil.generateToken();
		LOGGER.info("Access TOken::" + tokenResponse.getAccess_token());
		List<BudgetItemDetails> budgetItemDetails = new ArrayList<BudgetItemDetails>();
		LOGGER.info("Calling getNewBudgetItemDetails::> : " + mgmtCmpId + "==" + propertyId + "=="
				+ result.getPeriodCode());
		ResponseEntity<BudgetResponse> budgetDetails = null;

		HttpHeaders headers = new HttpHeaders();

		headers.set("clientid", client_id);
		headers.set("Authorization", "Bearer " + tokenResponse.getAccess_token());
		HttpEntity request = new HttpEntity(headers);
		if (result != null && result.getSource().equals("OA")) {

			budgetDetails = restTemplate.exchange(budgetDetailsurl + "/" + result.getMollakPropGrpId(), HttpMethod.GET,
					request, BudgetResponse.class);
		}
		if (result != null && result.getPeriodCode() != "" && (result.getSource().equals("MOLLAK"))) {
			budgetDetails = restTemplate.exchange(
					budgetDetailsurl + "/" + result.getMollakPropGrpId() + "/all" + "/" + result.getPeriodCode(),
					HttpMethod.GET, request, BudgetResponse.class);

		}
		if (budgetDetails != null && budgetDetails.getBody() != null) {
			Integer serviceId = null;
			/*
			 * LOGGER.info("budgetDetails" + budgetDetails.getBody().getResponseCode() +
			 * "==" + budgetDetails.getBody().getTimeStamp() + "==" +
			 * budgetDetails.getBody().getValidationErrorsList().toString());
			 */
			ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
			serviceStatusDetails.setService_Name(BUDGET_DETAILS);
			serviceStatusDetails.setResponse_Code(budgetDetails.getBody().getResponseCode());
			serviceStatusDetails.setTimestamp(budgetDetails.getBody().getTimeStamp());

			try {
				serviceId = budgetDetailsDao.saveServiceStatus(serviceStatusDetails);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BudgetDetails details = budgetDetails.getBody().getResponse();

			ServiceChargeGroupDetails[] detailList = details.getServiceChargeGroups();
			for (int i = 0; i < detailList.length; i++) {
				ServiceChargeGroupDetails serviceDetails = detailList[i];
				BudgetItemDetails[] budgetdata = serviceDetails.getBudgetItems();
				for (int j = 0; j < budgetdata.length; j++) {
					BudgetItemDetails budgetList = budgetdata[j];
					budgetList.setTotalBudget(budgetList.getTotalCost());
					budgetList.setConsumedAmount(0.0);
					budgetList.setBalanceAmount(budgetList.getTotalCost());
					budgetList.setIsActive("Y");
					budgetList.setServiceId(serviceId);
					budgetList.setServiceChrgGrpId(serviceDetails.getServiceChargeGroupId());
					budgetItemDetails.add(budgetList);
				}
			}
		}
		return budgetItemDetails;
	}

	private List<ServiceChargeGroupDetails> getNewBudgetDetails(Integer mgmtCmpId, Integer propertyId,
			String budgetDetailsurl, NotificationDetails result) {
		// TODO Auto-generated method stub
		TokenResponse tokenResponse = httpUtil.generateToken();
		LOGGER.info("Access TOken::" + tokenResponse.getAccess_token());
		List<ServiceChargeGroupDetails> budgetDetailsList = new ArrayList<ServiceChargeGroupDetails>();
		LOGGER.info("Calling getNewBudgetDetails::> : " + mgmtCmpId + "==" + propertyId + "==" + result.getPeriodCode()
				+ "Source" + result.getSource());
		ResponseEntity<BudgetResponse> budgetDetails = null;

		HttpHeaders headers = new HttpHeaders();

		headers.set("clientid", client_id);
		headers.set("Authorization", "Bearer " + tokenResponse.getAccess_token());
		HttpEntity request = new HttpEntity(headers);

		if (result != null && result.getSource().equals("OA")) {
			LOGGER.info("OA URL:::" + budgetDetailsurl + "/" + result.getMollakPropGrpId());
			budgetDetails = restTemplate.exchange(budgetDetailsurl + "/" + result.getMollakPropGrpId(), HttpMethod.GET,
					request, BudgetResponse.class);
			LOGGER.info("Budget Details Status Code::" + budgetDetails.getBody());
		}

		if (result != null && result.getSource().equals("MOLLAK")) {
			budgetDetails = restTemplate.exchange(
					budgetDetailsurl + "/" + result.getMollakPropGrpId() + "/all" + "/" + result.getPeriodCode(),
					HttpMethod.GET, request, BudgetResponse.class);
		}
		if (budgetDetails != null && budgetDetails.getBody() != null) {
			Integer serviceId = null;
			/*
			 * LOGGER.info("budgetDetails" + budgetDetails.getBody().getResponseCode() +
			 * "==" + budgetDetails.getBody().getTimeStamp() + "==" +
			 * budgetDetails.getBody().getValidationErrorsList().toString());
			 */
			ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
			serviceStatusDetails.setService_Name(BUDGET_DETAILS);
			serviceStatusDetails.setResponse_Code(budgetDetails.getBody().getResponseCode());
			serviceStatusDetails.setTimestamp(budgetDetails.getBody().getTimeStamp());

			try {
				serviceId = budgetDetailsDao.saveServiceStatus(serviceStatusDetails);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BudgetDetails details = budgetDetails.getBody().getResponse();

			String data = details.getAdditionalData();
			// Integer propGrpId = details.getPropertyGroupId();
			// Integer mcId = details.getManagementCompanyId();
			Integer dataId = null;

			if (data != null) {
				Integer data_id = budgetDetailsDao.saveAdditionalData(data);
				dataId = data_id;
			}

			ServiceChargeGroupDetails[] detailList = details.getServiceChargeGroups();
			for (int i = 0; i < detailList.length; i++) {
				ServiceChargeGroupDetails serviceDetails = detailList[i];
				LOGGER.info("prop_Id" + propertyId);
				serviceDetails.setDataId(dataId);
				serviceDetails.setEmail(details.getPropertyManagerEmail());
				serviceDetails.setPropId(propertyId);
				serviceDetails.setMgmtId(mgmtCmpId);
				serviceDetails.setServiceId(serviceId);
				BudgetItemDetails[] budgetdata = serviceDetails.getBudgetItems();
				for (int j = 0; j < budgetdata.length; j++) {
					BudgetItemDetails budgetList = budgetdata[j];
					// budgetList.setBudgetId(budgetId);
					budgetList.setServiceId(serviceId);
					budgetList.setTotalBudget(budgetList.getTotalCost());
					budgetList.setConsumedAmount(0.0);
					budgetList.setBalanceAmount(budgetList.getTotalCost());
					budgetList.setIsActive("Y");
					// budgetDetailsDao.saveBudgetItems(budgetList);
				}
				serviceDetails.setBudgetItems(budgetdata);
				budgetDetailsList.add(serviceDetails);
			}

		}

		return budgetDetailsList;
	}

	public void getMissingBudgetData() {
		LOGGER.info("calling updateBudgetData");
		// call get - return object
		NotificationDetails result = getNotificationDetails(budgetDetailsPending, null, SOURCE_OA);// SORCE_OA
		Boolean statusFlag = false;
		// statusFlag = false;
		if (result != null && result.getCount() == 0) {

			// add try block
			try {
				// add new method updateNotification("in progress") call here
				int resultValue = updateNotification(INPROGRESS, result.getBudgetInfoId(), result.getRetryCount());
				if (resultValue == 1) {

					Map<Integer, Integer> mgmtCompIds = validateManagementCompany(result.getMollakMcId(),
							managementCompanyurl);
					LOGGER.info(">>>>>>>>>>>" + mgmtCompIds.get(result.getMollakMcId()));
					LOGGER.info("Validated MGMT Successfully");
					Map<Integer, Integer> propGrpIds = validatePropertyGroupId(result, propertyGroupsurl,
							mgmtCompIds.get(result.getMollakMcId()), result.getMollakMcId());
					//

					/*
					 * for (Map.Entry<Integer, Integer> entry : mgmtCompIds.entrySet()) { Integer
					 * key = entry.getKey(); Integer value = entry.getValue();
					 * 
					 * LOGGER.info(">>>>>>>>>>>" + key); LOGGER.info(">>>>>>>>>>>" + value); }
					 */
					//
					// LOGGER.info(">>>>>>>>>>>" + propGrpIds);
					/*
					 * for (Map.Entry<Integer, Integer> entry : propGrpIds.entrySet()) { Integer key
					 * = entry.getKey(); Integer value = entry.getValue();
					 * 
					 * LOGGER.info(">>>>>>>>>>>" + key); LOGGER.info(">>>>>>>>>>>" + value); }
					 */
					getAllBudgetDetails(result.getMollakPropGrpId(), mgmtCompIds.get(result.getMollakMcId()),
							propGrpIds.get(result.getMollakPropGrpId()));

					// getBudgetDetails(mgmtCompIds, propGrpIds,
					// result.getPropertyGroupId(),
					// budgetDetailsurl, result);
					// statusFlag = true; it means successfully completed
					
					statusFlag = true;
					LOGGER.info("statusFlag::"+statusFlag);
					// close try block
				} else {
					LOGGER.info("update status to IN-PROGRESS failed");
				}
			}

			// start catch block
			catch (Exception e) {
				/// write some log
				e.printStackTrace();
				LOGGER.info("Exception raised due to:" + e.getMessage());
			}
			// start finally block
			finally {
				// if(statusFlag){
				// call updateNotification("Completed") call here
				// }else{
				// call updateNotification method("Failed")
				// }
				LOGGER.info("statusFlag in FinallyBlock::"+statusFlag);
				if (statusFlag) {
					
					updateNotification(COMPLETED, result.getBudgetInfoId(), result.getRetryCount());
				} else {
					updateNotification(FAILED, result.getBudgetInfoId(), result.getRetryCount());
				}
			}

		} else {
			// TODO: print logger - No notifications for processing
			LOGGER.info("No Notification For Processing...");
		}
	}

	public NotificationDetails getNotificationDetails(String pendingStatus, String completdStatus, String source) {
		try {
			LOGGER.info("Calling getNotification method calling");
			NotificationDetails result = budgetDetailsDao.getNotificationDetails(pendingStatus, completdStatus, source);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Cause fo Exception in getNotificationDetails::" + e.getCause());
			return null;
		}

	}

	// TODO: execute below method after go live - only one time
	public Map<Integer, Integer> getAllManagementCompanies() {
		// String urlForAll = "managementCompanyurl"; //get from properties file

		return validateManagementCompany(null, managementCompanyurl);

	}

	public Map<Integer, Integer> validateManagementCompany(Integer mcId, String url) {
		LOGGER.info("validateManagementCompany method calling");

		Map<Integer, Integer> mgmtCompIds = new HashMap<Integer, Integer>();

		if (mcId != null) {
			List<Integer> mgmtCompIdList = budgetDetailsDao.ValidateManagementCompanyId(mcId);
			LOGGER.info("mgmtCompIdList size:::"+mgmtCompIdList.size());
			if (mgmtCompIdList != null && mgmtCompIdList.size() > 0) {
				mgmtCompIds.put(mcId, mgmtCompIdList.get(0));
			}
		}
		// if count = 0, call service
		// insert managment company details in table
		// else ignore
		ResponseEntity<ManagementCompanyResponse> companyDetails = null;

		TokenResponse tokenResponse = httpUtil.generateToken();
		LOGGER.info("Access TOken::" + tokenResponse.getAccess_token());
		try {
			if (mgmtCompIds.size() == 0) {
				HttpHeaders headers = new HttpHeaders();
				// headers.setContentType(MediaType.APPLICATION_JSON);
				// TODO: need to set headerValue once we get the token
				headers.set("clientid", client_id);
				headers.set("Authorization", "Bearer " + tokenResponse.getAccess_token());
			
				HttpEntity request = new HttpEntity(headers);
				if (mcId == null) {
					LOGGER.info("inside If");
					companyDetails = restTemplate.exchange(url, HttpMethod.GET, request,
							ManagementCompanyResponse.class);
					LOGGER.info("Management companyDetails" + companyDetails.toString());
				} else {
					LOGGER.info("inside else");// exchange(url + "/"+ mcId,
												// HttpMethod.GET, request,
												// ManagementCompanyResponse.class);
					companyDetails = restTemplate.exchange(url + "/" + mcId, HttpMethod.GET, request,
							ManagementCompanyResponse.class);
					// companyDetails = restTemplate.exchange(url,
					// HttpMethod.GET, request,
					// ManagementCompanyResponse.class);
					LOGGER.info(
							"Management companyDetails" + companyDetails + "=======================================");
					LOGGER.info("Management companyDetails" + companyDetails.getBody());

					LOGGER.info("companyDetails" + companyDetails.getBody().getResponseCode() + "=="
							+ companyDetails.getBody().getTimeStamp() + "=="
							+ companyDetails.getBody().getValidationErrorsList().toString()
							+ companyDetails.getBody().getResponse().getManagementCompanies()[0].getName()
									.getArabicName());

				}

				LOGGER.info("Additional Data:>>" + companyDetails.getBody().getResponse().getAdditionalData());
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception is:" + e.getCause());
		}
		// need to remove

		if (companyDetails != null && companyDetails.getBody() != null) {
			Integer serviceId = null;
			LOGGER.info("companyDetails" + companyDetails.getBody().getResponseCode() + "=="
					+ companyDetails.getBody().getTimeStamp() + "=="
					+ companyDetails.getBody().getValidationErrorsList().toString());
			ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
			serviceStatusDetails.setService_Name(MANAGEMENT_COMPANIES);
			serviceStatusDetails.setResponse_Code(companyDetails.getBody().getResponseCode());
			serviceStatusDetails.setTimestamp(companyDetails.getBody().getTimeStamp());

			try {
				serviceId = budgetDetailsDao.saveServiceStatus(serviceStatusDetails);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LOGGER.info("Service Id::" + serviceId);
			ManagementCompanies companies = companyDetails.getBody().getResponse();

			String data = companies.getAdditionalData();
			Integer dataId = null;
			if (data != null) {
				// TODO call dao method and save additional data in
				// OA_ADDITIONAL_DATA table and return data_id
				dataId = budgetDetailsDao.saveAdditionalData(data);
				// int data_id = budgetDetailsDao.getDataId();
				LOGGER.info("DataId:>>" + dataId);
			}
			ManagementCompanyDetails detailsList[] = companies.getManagementCompanies();
			for (int i = 0; i < detailsList.length; i++) {
				ManagementCompanyDetails details = detailsList[i];
				// details.set - TODO
				if (dataId != null) {
					details.setDataId(dataId);
				}
				LOGGER.info("Calling saveMc Method");
				details.setServiceId(serviceId);
				// after save return MGMT_COMP_ID from dao
				Integer id = budgetDetailsDao.saveManagementCompany(details);
				// add MGMT_COMP_ID to list - list name as abc
				// mgmtCompIds.add(mgmtCompId)
				mgmtCompIds.put(details.getId(), id);
				LOGGER.info("Calling saveMc Method End" + id);
			}

			// }
			// return abc
			// return mgmtCompIds;
		}
		// else {
		// return list of MGMT_COMP_ID which comes from DB

		// return budgetDetailsDao.getMgmtCompId();

		return mgmtCompIds;

	}

	public Map<Integer, Integer> getAllPropertyGroups(Integer mgmtCompId, Integer mcId) {

		// String
		// url="https://qagate.dubailand.gov.ae/secure/mollak/sync/managementcompany/{companyId}/propertygroups";
		// url.replace("{companyId}", mcId+"");

//		return validatePropertyGroupId(null, managementCompanyurl + 12345 + "/propertygroups", mcId, mgmtCompId);
		return validatePropertyGroupId(null, managementCompanyurl + mcId + "/propertygroups", mcId, mgmtCompId);
	}

	public Map<Integer, Integer> validatePropertyGroupId(NotificationDetails result, String url, Integer mgmtCompId,
			Integer mollakMcId) {
		LOGGER.info("validatePropertyGroupId method calling");
		TokenResponse tokenResponse = httpUtil.generateToken();
		LOGGER.info("Access TOken::" + tokenResponse.getAccess_token());
		Map<Integer, Integer> propIds = new HashMap<Integer, Integer>();

		if (result != null) {
			List<Integer> propIdList = budgetDetailsDao.validatePropertyGroupId(result.getMollakPropGrpId());
			if (propIdList != null && propIdList.size() > 0) {
				propIds.put(result.getMollakPropGrpId(), propIdList.get(0));
			}
		}

		ResponseEntity<PropertyGroupsResponse> propertyDetails = null;

		if (propIds.size() == 0) {
			HttpHeaders headers = new HttpHeaders();
			// headers.setContentType(MediaType.APPLICATION_JSON);
			// TODO: need to set headerValue once we get the token
			headers.set("clientid", client_id);
			headers.set("Authorization", "Bearer " + tokenResponse.getAccess_token());
			HttpEntity request = new HttpEntity(headers);

			if (result == null) {
				propertyDetails = restTemplate.exchange(url, HttpMethod.GET, request, PropertyGroupsResponse.class);
			} else {
				// propertyDetails = restTemplate.exchange(url +
				// "/"+result.getMollakPropGrpId(), HttpMethod.GET, request,
				// PropertyGroupsResponse.class);
				url = url.replace("{companyID}", String.valueOf(mollakMcId));
				LOGGER.info("URL ==========" + url);
				propertyDetails = restTemplate.exchange(url, HttpMethod.GET, request, PropertyGroupsResponse.class);
			}
			LOGGER.info("dataId:.." + propertyDetails.getBody().getResponse().getAdditionalData());

			if (propertyDetails != null && propertyDetails.getBody() != null)
				;
			Integer serviceId = null;
			LOGGER.info("propertyDetails" + propertyDetails.getBody().getResponseCode() + "=="
					+ propertyDetails.getBody().getTimeStamp() + "=="
					+ propertyDetails.getBody().getValidationErrorsList().toString());
			ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
			serviceStatusDetails.setService_Name(PROPERTY_GROUPS);
			serviceStatusDetails.setResponse_Code(propertyDetails.getBody().getResponseCode());
			serviceStatusDetails.setTimestamp(propertyDetails.getBody().getTimeStamp());

			try {
				serviceId = budgetDetailsDao.saveServiceStatus(serviceStatusDetails);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PropertyGroups groups = propertyDetails.getBody().getResponse();

			String data = groups.getAdditionalData();
			Integer dataId = null;

			if (data != null) {
				dataId = budgetDetailsDao.saveAdditionalData(data);

			}
			PropertyGroupDetails detailList[] = groups.getPropertyGroups();
			for (int i = 0; i < detailList.length; i++) {
				LOGGER.info("Loop is" + i + "==" + detailList.length);

				PropertyGroupDetails details = detailList[i];
				// new code for bug fix
				boolean checkforExistingPropId = budgetDetailsDao.checkforPropertyGroupId(details.getPropertyGroupId());
				if (checkforExistingPropId) {
					LOGGER.info("Property Group Id already exists::" + details.getPropertyGroupId());
				} else {
					if (dataId != null) {
						details.setDataId(dataId);
					}
					details.setMgmtCompId(mgmtCompId);
					details.setServiceId(serviceId);
					// set mgmtCompId in details object
					Integer propId = budgetDetailsDao.savePropertyGroups(details);
					BeneficiaryListDetails[] beneData = details.getBeneficiaryList();
					propIds.put(details.getPropertyGroupId(), propId);

					for (int j = 0; j < beneData.length; j++) {
						BeneficiaryListDetails beneList = beneData[j];
						beneList.setPropId(propId);
						budgetDetailsDao.saveBeneficiaryList(beneList);
					}

					if (propId != 0) {
						LOGGER.info("Property Group Name" + details.getPropertyGroupName().getEnglishName());
						if (!details.getPropertyGroupName().getEnglishName()
								.contains("Nakheel Strata Management L.L.C")) {
							Integer buildingId = budgetDetailsDao.checkForBuildingId(details);

							if (buildingId != null && buildingId != 0) {
								LOGGER.info("Building Id exists" + buildingId);
								boolean isMappingAvailable = budgetDetailsDao.checkBuildingMapping(buildingId, propId);
								if (!isMappingAvailable) {
									budgetDetailsDao.insertBuildingMapping(buildingId, propId,details.getMgmtCompId());  //raghuram
								} else {
									LOGGER.info("Mapping already present in Mapping table");
								}
							} else {
								LOGGER.info("No Building Id present in Mollak");
							}
						}

					}

				}
			}

			// return count;
		} // else {
			// return budgetDetailsDao.getPropId();
			// }

		return propIds;

	}

	public void getAllBudgetDetails(Integer propGroupId, Integer mcId, Integer propGroupKey) {

		getBudgetDetails(null, null, propGroupId, budgetDetailsurl + "," + mcId + "," + propGroupKey, null);
	}

	public void getBudgetDetails(Map<Integer, Integer> mgmtCompIds, Map<Integer, Integer> propGrpIds,
			Integer propGroupId, String url, NotificationDetails result) {
		LOGGER.info("getBudgetDetails Service is calling");
		TokenResponse tokenResponse = httpUtil.generateToken();
		LOGGER.info("Access TOken::" + tokenResponse.getAccess_token());
		Integer modifiedmcId = null;
		Integer modifiedPropGropKey = null;
		String modifiedURL = null;

		LOGGER.info("propGroupId : " + propGroupId);

		ResponseEntity<BudgetResponse> budgetDetails = null;

		// if (count.size() == 0) {
		HttpHeaders headers = new HttpHeaders();

		headers.set("clientid", client_id);
		headers.set("Authorization", "Bearer " + tokenResponse.getAccess_token());
		HttpEntity request = new HttpEntity(headers);
		if (result != null && result.getSource().equals("OA")) {
			LOGGER.info("Inside OA" + url);
			budgetDetails = restTemplate.exchange(url + "/" + propGroupId, HttpMethod.GET, request,
					BudgetResponse.class);
		}
		if (result != null && result.getSource().equals("MOLLAK")) {
			budgetDetails = restTemplate.exchange(
					url + "/" + result.getMollakPropGrpId() + "/all" + "/" + result.getPeriodCode(), HttpMethod.GET,
					request, BudgetResponse.class);
		} /*
			 * else { String[] splitData = url.split(","); modifiedURL = splitData[0];
			 * modifiedmcId = Integer.parseInt(splitData[1]); modifiedPropGropKey =
			 * Integer.parseInt(splitData[2]);
			 * 
			 * budgetDetails = restTemplate.exchange(url + "/"+propGroupId, HttpMethod.GET,
			 * request,BudgetResponse.class); }
			 */

		LOGGER.info("Additional Data:>>" + budgetDetails.getBody().getResponse().getAdditionalData());
		// LOGGER.info("PRopID:>>"+budgetDetails.getBody().getResponse().getPropertyGroupId());

		if (budgetDetails != null && budgetDetails.getBody() != null) {

			Integer serviceId = null;
			LOGGER.info("budgetDetails" + budgetDetails.getBody().getResponseCode() + "=="
					+ budgetDetails.getBody().getTimeStamp() + "=="
					+ budgetDetails.getBody().getValidationErrorsList().toString());
			ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
			serviceStatusDetails.setService_Name(BUDGET_DETAILS);
			serviceStatusDetails.setResponse_Code(budgetDetails.getBody().getResponseCode());
			serviceStatusDetails.setTimestamp(budgetDetails.getBody().getTimeStamp());

			try {
				serviceId = budgetDetailsDao.saveServiceStatus(serviceStatusDetails);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BudgetDetails details = budgetDetails.getBody().getResponse();

			String data = details.getAdditionalData();
			Integer propGrpId = details.getPropertyGroupId();
			Integer mcId = details.getManagementCompanyId();

			Integer prop_Id = null;
			Integer mgmtCompId = null;

			Integer dataId = null;

			if (data != null) {
				Integer data_id = budgetDetailsDao.saveAdditionalData(data);
				dataId = data_id;
			}
			if (mgmtCompIds != null && propGrpIds != null) {
				if (propGrpId != null && propGrpIds.containsKey(propGrpId)) {
					// Integer
					// propId=budgetDetailsDao.getSinglePropId(propGrpId);
					prop_Id = propGrpIds.get(propGrpId);

				}

				if (mcId != null && mgmtCompIds.containsKey(result.getMollakMcId())) {
					// Integer mc_Id=budgetDetailsDao.getSingleMcId(mcId);
					mgmtCompId = mgmtCompIds.get(result.getMollakMcId());

				}
			} else {
				prop_Id = modifiedPropGropKey;
				mgmtCompId = modifiedmcId;
			}
			List<Integer> budgetIdList = new ArrayList<Integer>();
			ServiceChargeGroupDetails[] detailList = details.getServiceChargeGroups();
			String periodCode = "";
			for (int i = 0; i < detailList.length; i++) {
				ServiceChargeGroupDetails serviceDetails = detailList[i];
				if (i == 0) {
					periodCode = serviceDetails.getBudgetPeriodCode();
				}
				LOGGER.info("prop_Id" + prop_Id);
				serviceDetails.setDataId(dataId);
				serviceDetails.setEmail(details.getPropertyManagerEmail());
				serviceDetails.setPropId(prop_Id);
				serviceDetails.setMgmtId(mgmtCompId);
				serviceDetails.setServiceId(serviceId);
				int budgetId = budgetDetailsDao.saveBudgetDetails(serviceDetails);
				budgetIdList.add(budgetId);
				// TODO this should return budget_id
				// start for loop for budget items
				BudgetItemDetails[] budgetdata = serviceDetails.getBudgetItems();
				// for{
				for (int j = 0; j < budgetdata.length; j++) {
					BudgetItemDetails budgetList = budgetdata[j];
					budgetList.setBudgetId(budgetId);
					budgetList.setServiceId(serviceId);
					budgetList.setTotalBudget(budgetList.getTotalCost());
					budgetList.setConsumedAmount(0.0);
					budgetList.setBalanceAmount(budgetList.getTotalCost());
					budgetList.setTotalCost(budgetList.getTotalCost() - budgetList.getVatAmount());
					budgetList.setIsActive("Y");
					budgetDetailsDao.saveBudgetItems(budgetList);
				}

				// set budget id to budgetitem object
				// call save method of budget item
				// end for loop
			}
			// String budgetPeriodCode=detailList[0].getBudgetPeriodCode();
			getPendingAprrovedAmounts();
			
			

			// Call flex service
			if(!periodCode.contains("P-")) {
				try {
					fundDetailsService.updateReserveFundData(mgmtCompId, prop_Id, periodCode);
				}
				catch(Exception e) {
					LOGGER.error("Error While updating Persectage:: "+e.getMessage());
				}
			}
			
		}
	}

	// private void AprrovedAmounts(Integer mgmtCompId,Integer prop_Id,String
	// budgetPeriodCode,List<Integer> budgetIdList) {
	private void getPendingAprrovedAmounts() {
		List<ApprovedBudgetItems> approvedBudgetItems = budgetDetailsDao.getApprovedBudgetItems("PENDING");//
		for (int i = 0; i < approvedBudgetItems.size(); i++) {
			String serviceCodes = approvedBudgetItems.get(i).getServiceCode().replaceAll(",", "','");
			List<BudgetItems> newBudgetItemdetails = budgetDetailsDao.fetchNewBudgetItemList(
					approvedBudgetItems.get(i).getMgmtCompId(), approvedBudgetItems.get(i).getPropId(), serviceCodes,
					approvedBudgetItems.get(i).getBudgetYear());
			updateAprrovedAmounts(newBudgetItemdetails, approvedBudgetItems.get(i));
		}

	}

	private void updateAprrovedAmounts(List<BudgetItems> newBudgetItemdetails,
			ApprovedBudgetItems approvedBudgetItems) {

		if (approvedBudgetItems == null || newBudgetItemdetails == null) {
			LOGGER.info("NO approved amounts or budget items to update.");
			return;
		}

		double totalAmount = 0;
		for (int i = 0; i < newBudgetItemdetails.size(); i++) {
			totalAmount = totalAmount + newBudgetItemdetails.get(i).getBalanceAmount();

		}

		List<AuditTrail> auditLogs = new ArrayList<AuditTrail>();
		int biSize = newBudgetItemdetails.size();

		double approvedAmount = approvedBudgetItems.getApprovedAmount();
		String[] serviceCodeArray = approvedBudgetItems.getServiceCode().split(",");
		Map<String, String> bifurCodeMap = new HashMap<String, String>();
		double totalBifurAmount = 0;
		if (approvedBudgetItems.getBifurcation() != null && !"".equals(approvedBudgetItems.getBifurcation())) {
			boolean isInvalidFormat = false;

			String bifurCodesList[] = approvedBudgetItems.getBifurcation().split(",");
			LOGGER.info("bifurCodesList:" + bifurCodesList);
			if (bifurCodesList != null && bifurCodesList.length > 0) {
				LOGGER.info("bifurCodesList SIZE:" + bifurCodesList.length);
				for (int i = 0; i < bifurCodesList.length; i++) {
					String bifurCodePair = bifurCodesList[i];
					LOGGER.info("bifurCodePair:" + bifurCodePair);
					String bifurCodes[] = bifurCodePair.split("=");
					LOGGER.info("bifurCodes:" + bifurCodes);
					if (bifurCodes != null && bifurCodes.length > 1) {
						LOGGER.info("bifurCodes CODE :" + bifurCodes[0].toUpperCase());
						LOGGER.info("bifurCodes VALUE:" + bifurCodes[1]);
						bifurCodeMap.put(bifurCodes[0].trim().toUpperCase(), bifurCodes[1]);
						try {
							totalBifurAmount = totalBifurAmount + Double.parseDouble(bifurCodes[1]);
						} catch (Exception e) {
							e.printStackTrace();
							isInvalidFormat = true;
						}

					} else {
						isInvalidFormat = true;
					}
				}
			} else {
				isInvalidFormat = true;
			}

			if (isInvalidFormat) {
				LOGGER.info("Invalid Bifurcation format;");
				return;
			}
		}
		for (int k = 0; k < serviceCodeArray.length; k++) {

			for (int i = 0; i < newBudgetItemdetails.size(); i++) {
				if (newBudgetItemdetails.get(i).getServiceCode().equalsIgnoreCase(serviceCodeArray[k])) {
					AuditTrail auditLogBal = new AuditTrail();
					AuditTrail auditLogCon = new AuditTrail();
					auditLogBal.setId(newBudgetItemdetails.get(i).getBudgetItemId());
					auditLogBal.setFieldName("BALANCE_AMOUNT");
					auditLogCon.setId(newBudgetItemdetails.get(i).getBudgetItemId());
					auditLogCon.setFieldName("CONSUMED_AMOUNT");
					LOGGER.info("old balance amount:" + newBudgetItemdetails.get(i).getBalanceAmount());
					LOGGER.info("old consumed amount:" + newBudgetItemdetails.get(i).getConsumedAmount());

					if (approvedAmount > 0) {
						double balAmount = newBudgetItemdetails.get(i).getBalanceAmount();
						double conAmount = newBudgetItemdetails.get(i).getConsumedAmount();
						auditLogBal.setOldValue(balAmount + "");
						auditLogCon.setOldValue(conAmount + "");

						LOGGER.info("Service Code:" + newBudgetItemdetails.get(i).getServiceCode());

						if (bifurCodeMap.size() > 0
								&& bifurCodeMap.containsKey(newBudgetItemdetails.get(i).getServiceCode())) {
							String value = bifurCodeMap.get(newBudgetItemdetails.get(i).getServiceCode()) != null
									? bifurCodeMap.get(newBudgetItemdetails.get(i).getServiceCode())
									: "0";
							LOGGER.info("value:" + value);
							double amount = Double.parseDouble(value); //
							LOGGER.info("bifer code Value:" + amount);

							double newBalAmount = 0;
							double newConAmount = 0;

							if (amount > balAmount) {
								newBalAmount = balAmount - balAmount;
								newConAmount = conAmount + balAmount;
								approvedAmount = approvedAmount - balAmount;
								double newAmount = amount - balAmount;
								bifurCodeMap.put(newBudgetItemdetails.get(i).getServiceCode(), newAmount + "");
							} else {
								newBalAmount = balAmount - amount;
								newConAmount = conAmount + amount;
								approvedAmount = approvedAmount - amount;
								double newAmount = 0;
								bifurCodeMap.put(newBudgetItemdetails.get(i).getServiceCode(), newAmount + "");
							}
							newBudgetItemdetails.get(i).setBalanceAmount(newBalAmount);
							newBudgetItemdetails.get(i).setConsumedAmount(newConAmount);

							auditLogBal.setNewValue(newBalAmount + "");
							auditLogCon.setNewValue(newConAmount + "");
							auditLogs.add(auditLogBal);
							auditLogs.add(auditLogCon);

						} else {
							if (i == (biSize - 1) && approvedAmount <= totalAmount) {
								double newBalAmount = balAmount - approvedAmount;
								double newConAmount = conAmount + approvedAmount;
								newBudgetItemdetails.get(i).setBalanceAmount(newBalAmount);
								newBudgetItemdetails.get(i).setConsumedAmount(newConAmount);
								approvedAmount = 0;
								auditLogBal.setNewValue(newBalAmount + "");
								auditLogCon.setNewValue(newConAmount + "");
								auditLogs.add(auditLogBal);
								auditLogs.add(auditLogCon);
							} else if (balAmount > 0) {
								if (balAmount <= approvedAmount) {
									double newBalAmount = balAmount - balAmount;
									double newConAmount = conAmount + balAmount;
									newBudgetItemdetails.get(i).setBalanceAmount(newBalAmount);
									newBudgetItemdetails.get(i).setConsumedAmount(newConAmount);
									approvedAmount = approvedAmount - balAmount;
									auditLogBal.setNewValue(newBalAmount + "");
									auditLogCon.setNewValue(newConAmount + "");
									auditLogs.add(auditLogBal);
									auditLogs.add(auditLogCon);
								} else {
									double newBalAmount = balAmount - approvedAmount;
									double newConAmount = conAmount + approvedAmount;
									newBudgetItemdetails.get(i).setBalanceAmount(newBalAmount);
									newBudgetItemdetails.get(i).setConsumedAmount(newConAmount);
									approvedAmount = 0;
									auditLogBal.setNewValue(newBalAmount + "");
									auditLogCon.setNewValue(newConAmount + "");
									auditLogs.add(auditLogBal);
									auditLogs.add(auditLogCon);
									break;
								}
							} else {
								LOGGER.info("Not deducting amount from service code:"
										+ newBudgetItemdetails.get(i).getServiceCode() + ",budgetId:"
										+ newBudgetItemdetails.get(i).getBudgetItemId());
							}
						}
					}
				}
				updateApprovedAmounts(newBudgetItemdetails, approvedBudgetItems.getDataId(), auditLogs);
			}

		}
	}

	private void updateApprovedAmounts(List<BudgetItems> newBudgetItemdetails, Integer dataId,
			List<AuditTrail> auditLogs) {
		budgetDetailsDao.updateApprovedAmounts(newBudgetItemdetails, dataId, auditLogs);

	}

	public int updateNotification(String status, Integer budgetInfoId, Integer retryCount) {
		// raghu call dao method

		// inside dao method update status in OA_MOLLAK_BUDGET_INFO
		LOGGER.info("Calling the updateNotification() method" + retryCount);
		int recordCount = 0;
		if (status.equalsIgnoreCase("FAILED")) {
			if (retryCount == null) {
				retryCount = 1;
			} else {
				retryCount = retryCount + 1;
			}

			recordCount = budgetDetailsDao.updateNotificationForFailedrecords(status, budgetInfoId, retryCount);
		} else {
			recordCount = budgetDetailsDao.updateNotification(status, budgetInfoId);
		}

		// return count of updated records
		LOGGER.info("status is updated for budgetInfoId");
		return recordCount;
		// return 0;

	}

	@Override
	public void getPreviousBudgetDetails() {

		LOGGER.info("calling getPreviousBudgetDetails");

		Map<Integer, Integer> mgmtCompanies = getAllManagementCompanies();
		// TODO get MGMT_COMP_ID as mgmtCompId & MC_ID as mcId from All
		// management companies
		// key mc_id comes from mollak, value is mgmtCompanies which is Pkey
		LOGGER.info("Outside Iterating the Loop====>" + mgmtCompanies.size());
		while (mgmtCompanies.keySet().iterator().hasNext()) {
			LOGGER.info("Iterating the Loop====>");
			Integer key = mgmtCompanies.keySet().iterator().next();
			Integer mgmtCompId = mgmtCompanies.get(key);
			LOGGER.info("Iterating the Loop====>" + key + mgmtCompId);
			Map<Integer, Integer> propertyGroups = getAllPropertyGroups(mgmtCompId, key);
			// get map of prop group ids
			// while{
			// get budget details
			// }
			while (propertyGroups.keySet().iterator().hasNext()) {

				Integer property_Grp_Id = propertyGroups.keySet().iterator().next();
				LOGGER.info("Iterating the Loop====>" + property_Grp_Id + "====" + mgmtCompId + "======"
						+ propertyGroups.get(property_Grp_Id));
				getAllBudgetDetails(property_Grp_Id, mgmtCompId, propertyGroups.get(property_Grp_Id));
			}
		}
		LOGGER.info("End of Iterating the Loop====>");
		// List<ManagementCompanyDetails> mcIdList =
		// budgetDetailsDao.getCompaniesId();

		/*
		 * LOGGER.info("LIST OF MGMT_COMP_ID, MC_ID :" + mcIdList); for (int i = 0; i <
		 * mcIdList.size(); i++) { Integer mgmtCompId = mcIdList.get(i).getMgmtCompId();
		 * Integer mcId = mcIdList.get(i).getMcId();
		 * 
		 * LOGGER.info("Mgmt Id : " + mgmtCompId); LOGGER.info("mcID : " + mcId);
		 * 
		 * getAllPropertyGroups(mgmtCompId, mcId);
		 * 
		 * } List<Integer> propGrpsId = budgetDetailsDao.getListOfPropId();
		 * 
		 * LOGGER.info("List of Property Id : " + propGrpsId);
		 * 
		 * for (int j = 0; j < propGrpsId.size(); j++) { Integer PropertyId =
		 * propGrpsId.get(j);
		 * 
		 * LOGGER.info("Prop Group Id : " + PropertyId);
		 * 
		 * getAllBudgetDetails(PropertyId); }
		 */

	}

	/*
	 * public static void main(String[] args) throws ParseException { String tm =
	 * "2020-04-01T11:10:04.3052311+04:00";
	 * 
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); Date d
	 * = sdf.parse(tm); System.out.println(d); SimpleDateFormat sdf2 = new
	 * SimpleDateFormat("dd-MMMM-yy"); String x = sdf2.format(d); Date d2 =
	 * sdf2.parse(x); System.out.println(d2); }
	 */
	/****************** for validation *******************/

	/*
	 * 
	 * Second method for validations
	 */

	@Override
	public void processPaymentRequest() {

		// here we will call below methods
		List<PaymentRequestData> pmtRequestList = getPaymentRequests(INPROGSTATUS, FAILEDSTATUS);
		if (pmtRequestList != null) {
			LOGGER.info("Payment Requests which are in IN-PROGRESS & FAILED count is:" + pmtRequestList.size());
			LOGGER.info("pmtRequestList:::" + pmtRequestList.toString());

			validatePaymentRequestData(pmtRequestList);
		} else {
			LOGGER.info("No Records for Processing");
		}

	}

	/*
	 * getting all in progress state records
	 */
	public List<PaymentRequestData> getPaymentRequests(String inproStatus, String failedStatus) {

		// here we will get list of in-progress records
		List<PaymentRequestData> pmtRequestList = budgetDetailsDao.getPaymentRequests(inproStatus, failedStatus);

		return pmtRequestList;
	}

	public void validatePaymentRequestData(List<PaymentRequestData> pmtRequestList) {
		PaymentRequestData payment = null;
		PaymentRequestStatus status = null;
		LOGGER.info("records for validating:" + pmtRequestList.size());
		for (int k = 0; k < pmtRequestList.size(); k++) {
			try {
				payment = pmtRequestList.get(k);
				status = validatePaymentRequest(payment);

			} catch (OAServiceException oaex) {
				String errorMessag = "Failed due to some exception, system will retry after few minutes";
				LOGGER.error("Failed Payment Request (Payment ID:" + payment.getPymtReqId() + ") process:"
						+ oaex.getMessage());
				if (status != null) {
					errorMessag = status.getRemarks() + ":" + errorMessag;
				} else {
					status = new PaymentRequestStatus("FAILED", errorMessag);
				}
			} catch (Exception ex) {
				String errorMessag = "Failed due to some exception, system will retry after few minutes";
				LOGGER.error("Failed Payment Request (Payment ID:" + payment.getPymtReqId()
						+ ") process due to some exception:" + ex.getMessage());
				if (status != null) {
					errorMessag = status.getRemarks() + ":" + errorMessag;
				} else {
					status = new PaymentRequestStatus("FAILED", errorMessag);
				}
			} finally {
				if (payment != null && status != null) {
					LOGGER.info("Updating Payment Request (Payment ID:" + payment.getPymtReqId() + ") with status:"
							+ status.getStatus() + " and remarks:" + status.getRemarks());
					String remarks = status.getRemarks();
					if (remarks != null && remarks.length() > 198) {
						remarks = remarks.substring(0, 198);
					}
					updatePaymentStatus(payment.getPymtReqId(), status.getStatus(), remarks);
				}
			}
		}

	}

	/*
	 * validation starts
	 * 
	 * 
	 */
	public PaymentRequestStatus validatePaymentRequest(PaymentRequestData payment) throws OAServiceException {
		StringBuffer remarks = new StringBuffer();

		if (payment.getRemarks() != null && !"null".equals(payment.getRemarks())) {
			remarks.append(payment.getRemarks());
			remarks.append("; ");
		}
		// String status = "";
		try {

			LOGGER.info("calling validatePaymentRequestData method in service");

			List<String> inputServiceCodesList = new ArrayList<String>();

			// Initial state updating status as VALIDATING
			updatePaymentStatus(payment.getPymtReqId(), VALIDATINGSTATUS, remarks.toString());

			// Payment Request Details from DB
			LOGGER.info("Payment Request Details from DB - In-Progress Record");
			LOGGER.info("ATTACHMENTID: " + payment.getAttachmentDataId());
			LOGGER.info("PYMTREQID: " + payment.getPymtReqId());
			LOGGER.info("TradeDate: " + payment.getTradeLicenseExpDate());
			LOGGER.info("AgreementDate: " + payment.getAgreementExpDate());
			LOGGER.info("MGMT COMP ID: " + payment.getMgmtCompId());
			LOGGER.info("BUDGET YEAR: " + payment.getBudgetYear());
			LOGGER.info("PROPERTYGROUPID: " + payment.getPropId());
			LOGGER.info("SERVICE CODE: " + payment.getServiceCode());
			LOGGER.info("Invoice Amount: " + payment.getInvoiceAmount());
			LOGGER.info("SUPPLIER NAME: " + payment.getSupplierId());
			LOGGER.info("INVOICE YEAR: " + payment.getInvoiceDateYear());
			LOGGER.info("PAYMENT TYPE: " + payment.getPaymentType());
			LOGGER.info("IS GOVERMENT ENTITY:" + payment.getIsGovernmentEntity());
			LOGGER.info("IS INSURANCE COMPANY: " + payment.getIsInsuranceCompany());
			LOGGER.info("AUTO_RENEWAL: " + payment.getAutoRenewal());
			LOGGER.info("NO_PROPER_DOCUMENTS: " + payment.getNoProperDocuments());
			LOGGER.info("Is Exceptional Approval:" + payment.getIsExceptionalApproval());
			LOGGER.info("Bifurcation:" + payment.getBifurcation());
			LOGGER.info("Remarks:" + payment.getRemarks());

			// Business Rules validation start
			// No proper documents
			if ("Y".equals(payment.getNoProperDocuments())) {
				remarks.append(NOPROPERDOCUMENTS);
				LOGGER.info("Rejected the payment due to no Proper Documents");
				return new PaymentRequestStatus(REJECTSTATUS, remarks.toString());
			}

			// is Exceptional Approval
			if ("Y".equals(payment.getIsExceptionalApproval())
					&& (payment.getServiceCode() == null || "".equals(payment.getServiceCode().trim()))) {
				LOGGER.info("is Exceptioanal Approval Checked, service codes are null.::"
						+ payment.getIsExceptionalApproval());
				remarks.append(" Approved the Payment Request as it is exceptional approval");
				return new PaymentRequestStatus(APPROVEDSTATUS, remarks.toString());
			}

			// Validate Trade License Expiry Date & Agreement Expiry Date
			if (!"Y".equals(payment.getIsExceptionalApproval())) { // Srini
				PaymentRequestStatus prStatus = validateTLAndAEDates(payment, remarks);
				if (prStatus != null) {
					return prStatus;
				}
			}

			String inputServiceCode = payment.getServiceCode();
			if (inputServiceCode != null && !"".equals(inputServiceCode.trim())) {
				inputServiceCode = inputServiceCode.replaceAll("\\s+", "").toUpperCase();
				PaymentRequestStatus servStatus = validateServiceCodeFormat(inputServiceCode, remarks);
				if (servStatus != null) {
					return servStatus;
				}
			}

			LOGGER.info("service code after trim: " + inputServiceCode);

			// Checking respective data is available in Mollak to validate payment requests
			boolean checkBudgetFlag = checkBudgetInfo(payment.getMgmtCompId(), payment.getPropId(),
					payment.getBudgetYear());

			LOGGER.info("checkBudgetFlag " + checkBudgetFlag);

			if (!checkBudgetFlag) {
				// return validateMollakData(payment, remarks);
				return budgetNotAvailable(payment, remarks);
			}
			// code moved from here
			List<String> mollakServiceCodes = getServiceCode(payment.getMgmtCompId(), payment.getBudgetYear(),
					payment.getPropId(), inputServiceCode);
			mollakServiceCodes.replaceAll(String::toUpperCase);
			LOGGER.info(" mollak Service Codes : " + mollakServiceCodes);

			if ("Y".equals(payment.getIsExceptionalApproval()) && payment.getServiceCode() != null
					&& !"".equals(payment.getServiceCode().trim())) {
				LOGGER.info("is Exceptioanal Approval Checked, service codes are present.::"
						+ payment.getIsExceptionalApproval());
				return approvePaymentRequest(payment, inputServiceCodesList, mollakServiceCodes, remarks,
						payment.getIsExceptionalApproval());
			}

			// Validate Service codes & Invoice Amounts
			return validateServiceCodesAndAmounts(payment, inputServiceCode, inputServiceCodesList, mollakServiceCodes,
					remarks);

		} catch (OAServiceException e) {
			e.printStackTrace();
			LOGGER.error("OAServiceException:" + e.getMessage());
			throw new OAServiceException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception Cause:" + e.getMessage());
			throw new OAServiceException(e.getMessage());
		}
	}

	// MC, BU Name, Budget Year Budget is not available in Mollak
	private PaymentRequestStatus budgetNotAvailable(PaymentRequestData payment, StringBuffer remarks)
			throws OAServiceException {
		// is Exceptional Approval
		if ("Y".equals(payment.getIsExceptionalApproval())) {// Srini
			remarks.append(
					" Approved the Payment Request as it is Exceptional Approval even though Budget is not available");

			return new PaymentRequestStatus(APPROVEDSTATUS, remarks.toString());
		} else if ("Y".equals(payment.getIsGovernmentEntity())) {
			remarks.append(
					" Approved the Payment Request as it is Government Entity even though Budget is not available");
			budgetDetailsDao.insertApprovedBudgetItems(payment.getMgmtCompId(), payment.getPropId(),
					payment.getBudgetYear(), payment.getServiceCode(), payment.getInvoiceAmount(),
					payment.getSupplierId(), payment.getBifurcation());
			return new PaymentRequestStatus(APPROVEDSTATUS, remarks.toString());
		} else if ("Y".equals(payment.getIsInsuranceCompany())) {
			remarks.append(
					" Approved the Payment Request as it is Insurance Company even though Budget is not available");
			budgetDetailsDao.insertApprovedBudgetItems(payment.getMgmtCompId(), payment.getPropId(),
					payment.getBudgetYear(), payment.getServiceCode(), payment.getInvoiceAmount(),
					payment.getSupplierId(), payment.getBifurcation());
			return new PaymentRequestStatus(APPROVEDSTATUS, remarks.toString());
		} else {
			remarks.append(" Budget is not available, so Rejected the Payment Request");
			return new PaymentRequestStatus(REJECTSTATUS, remarks.toString());
		}
	}

	private PaymentRequestStatus validateMollakData(PaymentRequestData payment, StringBuffer remarks)
			throws OAServiceException {
		LOGGER.info("Budget Details required to process payment request");

		// getting mollak data for insertMollakBudgetInfo
		Integer mollakmcid = budgetDetailsDao.getMollakMcId(payment.getMgmtCompId());
		// print mollak mc id
		if (payment.getPropId() == null) {
			remarks.append(NOMAPPINGREMARKS);
			return new PaymentRequestStatus(FAILEDSTATUS, remarks.toString());
		}
		Integer mollakPropGrpId = budgetDetailsDao.getMollakPropGrpId(Integer.parseInt(payment.getPropId()));
		payment.setMollakMcId(mollakmcid);
		payment.setMollakPropGrpId(mollakPropGrpId);
		if (payment.getMollakMcId() != null && payment.getMollakPropGrpId() != null) {
			remarks.append(BUDGETDETAILSREQUIRED);
			insertMollakBudgetInfo(payment.getMollakMcId(), payment.getMollakPropGrpId(), MOLLAKNODATASTATUS,
					MOLLAKSOURCE, payment.getPymtReqId());
			return new PaymentRequestStatus(NODATAINMOLLAKSTATUS, remarks.toString());
		} else {
			LOGGER.info(" FAILED the status, no mollak data for MGMT_COMP_ID:" + payment.getMgmtCompId() + ","
					+ "PROP_ID:" + payment.getPropId());
			remarks.append(NOMOLLAKDATAREMARKS);
			return new PaymentRequestStatus(FAILEDSTATUS, remarks.toString());
		}
	}

	private PaymentRequestStatus validateServiceCodeFormat(String inputServiceCode, StringBuffer remarks)
			throws OAServiceException {
		LOGGER.info("calling validateServiceCodeFormat in service.");
		String status = "";
		// Validating service codes format

		if (inputServiceCode != null) {
			// Pattern regex = Pattern.compile("[$&+:;=?@#|'<>-^*()%!]");
			// matcher to find if there is any special character in string
			// Matcher matcher = regex.matcher(inputServiceCode);
			// matcher.find()
			boolean match = false;
			String specialCharactersString = "[$&+:;=?@#|'<>-^*()%!/\\]";

			for (int i = 0; i < inputServiceCode.length(); i++) {
				char ch = inputServiceCode.charAt(i);
				if (specialCharactersString.contains(Character.toString(ch))) {
					match = true;
					break;
				}
			}
			if (match) {
				status = EXCPSTATUS;
				remarks.append(SERVICECODESFORMATREMARKS + inputServiceCode);
				remarks.append(";");
				LOGGER.info("service codes format is wrong!");
				return new PaymentRequestStatus(status, remarks.toString());
			}
		}
		return null;
	}

	private PaymentRequestStatus validateServiceCodesAndAmounts(PaymentRequestData payment, String inputServiceCode,
			List<String> inputServiceCodesList, List<String> mollakServiceCodes, StringBuffer remarks)
			throws OAServiceException {
		LOGGER.info("calling validateServiceCodesAndAmounts in service.");
		String status = "";

		// miss match condition.
		if (mollakServiceCodes.isEmpty()) {
			LOGGER.info("PYMTREQID " + payment.getPymtReqId());
			status = EXCPSTATUS;
			remarks.append(SERVICEREMARKS + inputServiceCode);
			remarks.append(";");
			return new PaymentRequestStatus(status, remarks.toString());

		} else {
			LOGGER.info("mollakServiceCodes : " + mollakServiceCodes);
			String[] split = inputServiceCode.split(",");
			for (String sc : split) {
				inputServiceCodesList.add(sc);
			}
			Set<String> set = new LinkedHashSet<String>(mollakServiceCodes);
			mollakServiceCodes.clear();
			mollakServiceCodes.addAll(set);

			LOGGER.info("Service Codes : " + mollakServiceCodes);

			Collections.sort(inputServiceCodesList);
			Collections.sort(mollakServiceCodes);
		}

		// validating service codes not matched then exception
		if (Collections.disjoint(inputServiceCodesList, mollakServiceCodes)) {

			LOGGER.info("miss match input service codes are: " + inputServiceCode);
			LOGGER.info("miss match mollak service codes are: " + mollakServiceCodes);
			status = EXCPSTATUS;
			remarks.append(NOTMATCHEDREMARKS + mollakServiceCodes);
			remarks.append(";");
			return new PaymentRequestStatus(status, remarks.toString());
		}

		LOGGER.info("remarks:" + remarks + " \\n " + "status:" + status);

		// Exception cases end Reject cases start
		if (status.equals(EXCPSTATUS)) {
			// updatePaymentStatus(payment.getPymtReqId(), EXCPSTATUS,
			// remarks);
			return new PaymentRequestStatus(EXCPSTATUS, remarks.toString());

		} else {
			if (status.equals(REJECTSTATUS)) {
				return new PaymentRequestStatus(REJECTSTATUS, remarks.toString());
			}
			if (status.equals(EXCPSTATUS)) {
				return new PaymentRequestStatus(EXCPSTATUS, remarks.toString());
			}
			// Approve/Reject payment requests
			return approvePaymentRequest(payment, inputServiceCodesList, mollakServiceCodes, remarks, "N");
		}

	}

	private PaymentRequestStatus approvePaymentRequest(PaymentRequestData payment, List<String> inputServiceCodesList,
			List<String> mollakServiceCodes, StringBuffer remarks, String isExceptionalApproval)
			throws OAServiceException {
		LOGGER.info("calling approvePaymentRequest method.");
		String status = "";
		// if (inputServiceCodesList.equals(mollakServiceCodes)) {
//if(Collections.disjoint(inputServiceCodesList,mollakServiceCodes)){

		List<BudgetItems> budgetItems = getMollakData(payment.getMgmtCompId(), payment.getPropId(),
				payment.getBudgetYear(), payment.getServiceCode().replaceAll("\\s", "").toUpperCase());
		LOGGER.info("input service codes:" + payment.getServiceCode().replaceAll("\\s", "").toUpperCase());
		LOGGER.info("data from mollak::" + budgetItems);

		if (budgetItems != null && budgetItems.isEmpty()) {
			if ("Y".equals(isExceptionalApproval)) { // Srini
				remarks.append(
						"Approved the Payment Request as it is Exceptional Approval, but Amount is not deducted from Service Code as it is invalid service code");
				return new PaymentRequestStatus(APPROVEDSTATUS, remarks.toString());
			} else { // Srini
				remarks.append("Service Code is not avialable in Mollak, so added into Exception queue");
				return new PaymentRequestStatus(EXCPSTATUS, remarks.toString());
			}
		}

		Map<String, String> bifurCodeMap = new HashMap<String, String>();
		Map<String, Double> budgetCodeMap = new HashMap<String, Double>();
		double totalBifurAmount = 0;

		if (payment.getBifurcation() != null && !"".equals(payment.getBifurcation())) {
			boolean isInvalidFormat = false;

			String bifurCodesList[] = payment.getBifurcation().split(",");
			LOGGER.info("bifurCodesList:" + bifurCodesList);
			if (bifurCodesList != null && bifurCodesList.length > 0) {
				LOGGER.info("bifurCodesList SIZE:" + bifurCodesList.length);
				for (int i = 0; i < bifurCodesList.length; i++) {
					String bifurCodePair = bifurCodesList[i];
					LOGGER.info("bifurCodePair:" + bifurCodePair);
					String bifurCodes[] = bifurCodePair.split("=");
					LOGGER.info("bifurCodes:" + bifurCodes);
					if (bifurCodes != null && bifurCodes.length > 1) {
						LOGGER.info("bifurCodes CODE :" + bifurCodes[0].toUpperCase());
						LOGGER.info("bifurCodes VALUE:" + bifurCodes[1]);
						bifurCodeMap.put(bifurCodes[0].trim().toUpperCase(), bifurCodes[1]);
						totalBifurAmount = totalBifurAmount + Double.parseDouble(bifurCodes[1]);
					} else {
						isInvalidFormat = true;
					}
				}
			} else {
				isInvalidFormat = true;
			}

			if (isInvalidFormat) {
				remarks.append("Invalid Bifurcation format;");
				return new PaymentRequestStatus(EXCPSTATUS, remarks.toString());
			}
		}

		double totalAmount = 0;
		for (int i = 0; i < budgetItems.size(); i++) {
			if (budgetCodeMap.containsKey(budgetItems.get(i).getServiceCode().trim().toUpperCase())) {
				Double totBalAmount = budgetCodeMap.get(budgetItems.get(i).getServiceCode().trim().toUpperCase())
						+ budgetItems.get(i).getBalanceAmount();
				budgetCodeMap.put(budgetItems.get(i).getServiceCode().trim().toUpperCase(), totBalAmount);
			} else {
				budgetCodeMap.put(budgetItems.get(i).getServiceCode().trim().toUpperCase(),
						budgetItems.get(i).getBalanceAmount());
			}
			totalAmount = totalAmount + budgetItems.get(i).getBalanceAmount();
		}
		LOGGER.info("total amount ofter caliculation:" + totalAmount);

		if (bifurCodeMap.size() > 0 && budgetCodeMap.size() > 0) {
			Iterator<String> bifurCodeSet = bifurCodeMap.keySet().iterator();
			while (bifurCodeSet.hasNext()) {
				String code = bifurCodeSet.next();
				LOGGER.info("Bifurcation - code:" + code);
				if (!budgetCodeMap.containsKey(code)) {
					status = EXCPSTATUS;
					remarks.append("Bifurcation code:" + code + " is not available in Budget codes");
					remarks.append(";");
					return new PaymentRequestStatus(status, remarks.toString());
				}
			}
		}
		LOGGER.info("out side condition payment.getInvoiceAmount() != totalBifurAmount:"
				+ (payment.getInvoiceAmount() != totalBifurAmount));
		/// Validation for Bifurcation amounts
		if (payment.getBifurcation() != null && !payment.getBifurcation().isEmpty()) {
			LOGGER.info("payment.getInvoiceAmount():" + payment.getInvoiceAmount() + ": totalBifurAmount"
					+ totalBifurAmount);
			if (Math.round(payment.getInvoiceAmount()) != Math.round(totalBifurAmount)) { // change to be done
																							// if(Math.round(sum*100.0)/100.0!=Math.round(sum2*100.0)/100.0)
																							// {
				LOGGER.info("in side condition payment.getInvoiceAmount() != totalBifurAmount:"
						+ (payment.getInvoiceAmount() != totalBifurAmount));
				return new PaymentRequestStatus(EXCPSTATUS, "Invoice amount is not equal to bifurcation amount");

			}
		}

		if (bifurCodeMap.size() > 0) {
			if ("Y".equals(payment.getIsGovernmentEntity()) || "Y".equals(isExceptionalApproval)
					|| "Y".equals(payment.getIsInsuranceCompany())) {
				LOGGER.info(
						"Bifurcation - validation, Not validating bifurcation amounts as it is Gov/Insurance/Exceptional case");
			} else {

				Iterator<String> budCodeSet = budgetCodeMap.keySet().iterator();
				while (budCodeSet.hasNext()) {
					String code = budCodeSet.next();
					double balAmount = budgetCodeMap.get(code);
					LOGGER.info("Bifurcation - balAmount:" + balAmount);
					if (bifurCodeMap.containsKey(code)) {
						String value = bifurCodeMap.get(code) != null ? bifurCodeMap.get(code) : "0";
						double bifurcationAmount = Double.parseDouble(value);
						LOGGER.info("Bifurcation - Code:" + code + ", Amount:" + bifurcationAmount);
						LOGGER.info("BudgetItem - Code:" + code + ", Amount:" + balAmount);
						if (Math.round(bifurcationAmount) > Math.round(balAmount)) {
							status = REJECTSTATUS;
							remarks.append("Payment is Rejected as Bifurcation Amount is exceeded Balance Amount");
							remarks.append(";");
							return new PaymentRequestStatus(status, remarks.toString());
						}
					}
				}
			}

		}
		if (Math.round(payment.getInvoiceAmount()) <= Math.round(totalAmount)) { 
			calculateAmounts(payment, budgetItems, bifurCodeMap, false, remarks);
		} else if ("Y".equals(payment.getIsGovernmentEntity()) || "Y".equals(isExceptionalApproval)
				|| "Y".equals(payment.getIsInsuranceCompany())) {
			calculateAmounts(payment, budgetItems, bifurCodeMap, true, remarks);
		} else {
			status = REJECTSTATUS;
			remarks.append(BUDGETREMARKS);
			remarks.append(";");
			return new PaymentRequestStatus(status, remarks.toString());
		}

		return null;

	}

	private void calculateAmounts(PaymentRequestData payment, List<BudgetItems> budgetItems,
			Map<String, String> bifurCodeMap, boolean flag, StringBuffer remarks) {
		double approvedAmount = payment.getInvoiceAmount(); // 5000 TODO
		List<AuditTrail> auditLogs = new ArrayList<AuditTrail>();
		int biSize = budgetItems.size();
		for (int i = 0; i < budgetItems.size(); i++) {
			AuditTrail auditLogBal = new AuditTrail();
			AuditTrail auditLogCon = new AuditTrail();
			auditLogBal.setId(budgetItems.get(i).getBudgetItemId());
			auditLogBal.setFieldName("BALANCE_AMOUNT");
			auditLogCon.setId(budgetItems.get(i).getBudgetItemId());
			auditLogCon.setFieldName("CONSUMED_AMOUNT");
			LOGGER.info("old balance amount:" + budgetItems.get(i).getBalanceAmount());
			LOGGER.info("old consumed amount:" + budgetItems.get(i).getConsumedAmount());
			// String spiltData[]=payment.getServiceCode().split(",");
			// LOGGER.info("spiltData:"+spiltData.length);

			if (approvedAmount > 0) {
				double balAmount = budgetItems.get(i).getBalanceAmount(); // 1000
				double conAmount = budgetItems.get(i).getConsumedAmount();
				auditLogBal.setOldValue(balAmount + "");
				auditLogCon.setOldValue(conAmount + "");
				LOGGER.info("Service Code:" + budgetItems.get(i).getServiceCode());
				if (bifurCodeMap.size() > 0 && bifurCodeMap.containsKey(budgetItems.get(i).getServiceCode())) {
					String value = bifurCodeMap.get(budgetItems.get(i).getServiceCode()) != null
							? bifurCodeMap.get(budgetItems.get(i).getServiceCode())
							: "0";
					LOGGER.info("value:" + value);
					double amount = Double.parseDouble(value); // Srini
					LOGGER.info("bifer code Value:" + amount);
					double newBalAmount = 0;
					double newConAmount = 0;
					if (Math.round(amount) > Math.round(balAmount)) { // change to be done
																		// if(Math.round(sum*100.0)/100.0!=Math.round(sum2*100.0)/100.0)
																		// {
						newBalAmount = balAmount - balAmount;
						newConAmount = conAmount + balAmount;
						approvedAmount = approvedAmount - balAmount;
						double newAmount = amount - balAmount;
						bifurCodeMap.put(budgetItems.get(i).getServiceCode(), newAmount + "");
					} else {
						newBalAmount = balAmount - amount;
						newConAmount = conAmount + amount;
						approvedAmount = approvedAmount - amount;
						double newAmount = 0;
						bifurCodeMap.put(budgetItems.get(i).getServiceCode(), newAmount + "");
					}
					budgetItems.get(i).setBalanceAmount(newBalAmount);
					budgetItems.get(i).setConsumedAmount(newConAmount);

					auditLogBal.setNewValue(newBalAmount + "");
					auditLogCon.setNewValue(newConAmount + "");
					auditLogs.add(auditLogBal);
					auditLogs.add(auditLogCon);
				} else {
					if (i == (biSize - 1) && flag) {
						double newBalAmount = balAmount - approvedAmount;
						double newConAmount = conAmount + approvedAmount;
						budgetItems.get(i).setBalanceAmount(newBalAmount);
						budgetItems.get(i).setConsumedAmount(newConAmount);
						approvedAmount = 0;
						auditLogBal.setNewValue(newBalAmount + "");
						auditLogCon.setNewValue(newConAmount + "");
						auditLogs.add(auditLogBal);
						auditLogs.add(auditLogCon);
					} else if (balAmount > 0) {
						if (balAmount <= approvedAmount) {
							double newBalAmount = balAmount - balAmount;
							double newConAmount = conAmount + balAmount;
							budgetItems.get(i).setBalanceAmount(newBalAmount);
							budgetItems.get(i).setConsumedAmount(newConAmount);
							approvedAmount = approvedAmount - balAmount;
							auditLogBal.setNewValue(newBalAmount + "");
							auditLogCon.setNewValue(newConAmount + "");
							auditLogs.add(auditLogBal);
							auditLogs.add(auditLogCon);
						} else {
							double newBalAmount = balAmount - approvedAmount;
							double newConAmount = conAmount + approvedAmount;
							budgetItems.get(i).setBalanceAmount(newBalAmount);
							budgetItems.get(i).setConsumedAmount(newConAmount);
							approvedAmount = 0;
							auditLogBal.setNewValue(newBalAmount + "");
							auditLogCon.setNewValue(newConAmount + "");
							auditLogs.add(auditLogBal);
							auditLogs.add(auditLogCon);
							break;
						}
					} else {
						LOGGER.info("Not deducting amount from service code:" + budgetItems.get(i).getServiceCode()
								+ ",budgetId:" + budgetItems.get(i).getBudgetItemId());
					}
				}
			}
		}
		String prRemarks = remarks.toString() + " Payment Request is Approved";
		if (prRemarks != null && prRemarks.length() > 198) {
			prRemarks = prRemarks.substring(0, 198);
		}
		// updating amounts
		updateAmounts(budgetItems, payment.getPymtReqId(), prRemarks, auditLogs); // auditLogs

	}

	private PaymentRequestStatus validateTLAndAEDates(PaymentRequestData payment, StringBuffer remarks)
			throws OAServiceException {
		LOGGER.info("calling validateTLAndAEDates method in service");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

		/*
		 * validating trade license expire date
		 */
		String tradeLinceseDate = payment.getTradeLicenseExpDate() != null ? payment.getTradeLicenseExpDate().toString()
				: null;
		// String tradeLinceseDate =
		// payment.getTradeLicenseExpDate().toString();
		try {
			if (!"Y".equals(payment.getIsGovernmentEntity())) {
				if (tradeLinceseDate != null && tradeLinceseDate != "") {
					Date tradelicence = sdFormat.parse(tradeLinceseDate);
					Date todaydate = sdFormat.parse(dtf.format(now));

					if (tradelicence.compareTo(todaydate) < 0) {
						LOGGER.info("Trade Date Expired");
						remarks.append(TRADEDATEREMARKS);
						remarks.append(";");
						return new PaymentRequestStatus(REJECTSTATUS, remarks.toString());

					}
				} else {
					LOGGER.info("Invalid Trade Licence Exp Date");
					remarks.append("Invalid Trade Licence Exp Date");
					remarks.append(";");
					return new PaymentRequestStatus(REJECTSTATUS, remarks.toString());
				}
			} else {
				LOGGER.info("Trade Licence is Not Validated.");
			}

		} catch (Exception e) {
			LOGGER.error("trade Date Exception Occured" + e.getMessage());
			e.printStackTrace();
			throw new OAServiceException(e.getMessage());
		}

		/*
		 * validating Agreement Expire date
		 */
		String agreementLincese = payment.getAgreementExpDate() != null ? payment.getAgreementExpDate().toString()
				: null;

		try {
			if (!("Y".equals(payment.getIsGovernmentEntity()) || "Y".equals(payment.getAutoRenewal()))) {
				if (agreementLincese != null && !"".equals(agreementLincese)) {
					Date agreementdate = sdFormat.parse(agreementLincese);
					Date today = sdFormat.parse(dtf.format(now));

					if (agreementdate.compareTo(today) < 0) {

						LOGGER.info("Agreement Date Expired");

						// status = EXCPSTATUS;
						remarks.append(AGREEMENTDATEREMARKS);
						remarks.append(";");
						return new PaymentRequestStatus(REJECTSTATUS, remarks.toString());

					}
				} else {
					LOGGER.info("Invalid Agreement Exp Date");
					remarks.append("Invalid Agreement Exp Date");
					return new PaymentRequestStatus(REJECTSTATUS, remarks.toString());
				}
			} else {
				LOGGER.info("Agreement Expiry date is not validated.");
			}

		} catch (Exception e) {
			LOGGER.error("Agreement Date Exception Occured:" + e.getMessage());
			throw new OAServiceException(e.getMessage());
		}
		return null;

	}

	private void updateAmounts(List<BudgetItems> budgetItems, Integer pymtReqId, String remarks,
			List<AuditTrail> auditLogs) {
		budgetDetailsDao.updateAmounts(budgetItems, pymtReqId, remarks, auditLogs);

	}

	private List<BudgetItems> getMollakData(Integer mgmtCompId, String propertyGroupId, Integer budgetYear,
			String serviceCode) {
		List<BudgetItems> mollakData = budgetDetailsDao.getMollakData(mgmtCompId, propertyGroupId, budgetYear,
				serviceCode);

		return mollakData;
	}

	private void insertMollakBudgetInfo(Integer mollakMcId, Integer mollakPropGrpId, String mollaknodatastatus2,
			String mollaksource2, Integer pymtReqId) {
		// TODO Auto-generated method stub
		budgetDetailsDao.insertMollakBudgetInfo(mollakMcId, mollakPropGrpId, mollaknodatastatus2, mollaksource2,
				pymtReqId);

	}

	private boolean checkBudgetInfo(Integer mgmtCompId, String propertyGroupId, Integer budgetYear) {
		boolean checkBudgetFlag = budgetDetailsDao.getBudgetDetailsCount(mgmtCompId, propertyGroupId, budgetYear);
		return checkBudgetFlag;
	}

	public List<String> getServiceCode(Integer mgmtCompId, Integer budgetYear, String propertyGroupId,
			String inputServiceCode) {

		String serviceCode = inputServiceCode.replaceAll(",", "','");
		List<String> serviceCodes = budgetDetailsDao.getServiceCode(mgmtCompId, budgetYear, propertyGroupId,
				serviceCode);

		return serviceCodes;
	}

	public void updatePaymentStatus(Integer pymtId, String status, String remarks) {

		budgetDetailsDao.updatePaymentStatus(pymtId, status, remarks);
	}

	/*
	 * public List<BudgetTrackDetails> getBudgetTrackDetails(Integer mgmtCompId,
	 * Integer supplierId, Integer budgetYear, String invoiceYear) {
	 * 
	 * List<BudgetTrackDetails> trackDetails =
	 * budgetDetailsDao.getBudgetTrackDetails(mgmtCompId, supplierId, budgetYear,
	 * invoiceYear); return trackDetails; }
	 */

	/*
	 * public void updateBudgetTrackDetails(Integer mgmtCompId, Integer supplierId,
	 * Integer budgetYear, String invoiceYear, Double consAmount, Double balAmount,
	 * Integer pymtReqId) {
	 * 
	 * budgetDetailsDao.updateBudgetTrackDetails(mgmtCompId, supplierId, budgetYear,
	 * invoiceYear, consAmount, balAmount, pymtReqId); }
	 */

	@Override
	public void sendingStatusMailIndividual() {
		try {

			LOGGER.info("calling sendingStatusMailIndividual in service");
			List<PaymentRequestData> pmtRequestList = getPaymentRequestsIndividualMail(APPROVEDSTATUS, REJECTSTATUS,
					EXCPSTATUS);
			LOGGER.info("pmtRequestList For Mail:: " + pmtRequestList);
			LOGGER.info("pmtRequestList size:: " + pmtRequestList.size());
			String mail = "";
			for (int i = 0; i < pmtRequestList.size(); i++) {
				mail = pmtRequestList.get(i).getUploadedBy() + "@mashreq.com";
				// mail = "BasheerB@mashreq.com";
				LOGGER.info("individual mail::" + mail);
				// calling utility method
				String Subject = "Payment Request " + pmtRequestList.get(i).getMatrixRefNo() + " has been "
						+ pmtRequestList.get(i).getStatus() + "";
				String Values = "Matrix Ref No:" + pmtRequestList.get(i).getMatrixRefNo() + "<br> STATUS:"
						+ pmtRequestList.get(i).getStatus() + "<br> " + "Benificiary Name:"
						+ pmtRequestList.get(i).getBeneficiaryName() + "<br> Debit Account Number:"
						+ pmtRequestList.get(i).getDebitAccountNumberDesc() + "<br>" + "Supplier Name:"
						+ pmtRequestList.get(i).getSupplierName() + "<br> Uploaded By:"
						+ pmtRequestList.get(i).getUploadedBy() + "<br>" + "Remarks:"
						+ pmtRequestList.get(i).getRemarks() + "";
				try {

					httpUtil.sendMail(Subject, Values, mail);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LOGGER.info("Exception while sending Individual mail::" + e.getMessage());
				}
				budgetDetailsDao.isMailSentPymtReqId(pmtRequestList.get(i).getPymtReqId());
			}
			try {

				sendingStatusMailBulk();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.info("Exception while sending Bulk mail::" + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Exception Cause:" + e.getCause());
		}

	}

	public List<PaymentRequestData> getPaymentRequestsIndividualMail(String approvedStatus, String rejectStatus,
			String excpStatus) {

		// here we will get list Approved, Rejected, Exception records.
		List<PaymentRequestData> pmtRequestList = budgetDetailsDao.getPaymentRequestsIndividualMail(approvedStatus,
				rejectStatus, excpStatus);

		return pmtRequestList;
	}

	@Override
	public void sendingStatusMailBulk() {
		try {

			LOGGER.info("calling sendingStatusMailBulk method in service");
			String mail = "";
			List<Integer> matrixFileRefNo = getPaymentRequestsBulkMailCount(APPROVEDSTATUS, REJECTSTATUS, EXCPSTATUS);
			// check with
			for (int b = 0; b < matrixFileRefNo.size(); b++) {
				PaymentRequestData paymentRequestData = budgetDetailsDao
						.getPaymentRequestsBulklMail(matrixFileRefNo.get(b));
				mail = paymentRequestData.getUploadedBy() + "@mashreq.com";
				// mail = "BasheerB@mashreq.com";
				LOGGER.info("bulk mail::" + mail);
				// calling utility method
				String Subject = "Bulk Payment " + paymentRequestData.getMatrixFileRefNo() + " has been processed";
				String Values = "Matrix File Ref No:" + paymentRequestData.getMatrixFileRefNo() + "<br> STATUS:"
						+ paymentRequestData.getStatus() + "<br> " + "Benificiary Name:"
						+ paymentRequestData.getBeneficiaryName() + "<br> Debit Account Number:"
						+ paymentRequestData.getDebitAccountNumberDesc() + "<br>" + "Supplier Name:"
						+ paymentRequestData.getSupplierName() + "<br> Uploaded By:"
						+ paymentRequestData.getUploadedBy() + "<br>" + "Remarks:" + paymentRequestData.getRemarks()
						+ "";
				try {

					httpUtil.sendMail(Subject, Values, mail);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				budgetDetailsDao.isMailSentMatrixFileRefNo(paymentRequestData.getMatrixFileRefNo().intValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Exception Cause:" + e.getCause());
		}

	}

	public List<Integer> getPaymentRequestsBulkMailCount(String approvedStatus, String rejectStatus,
			String excpStatus) {

		// here we will get list Approved, Rejected, Exception records.
		List<Integer> matrixFileRefNo = budgetDetailsDao.getPaymentRequestsBulkMailCount(approvedStatus, rejectStatus,
				excpStatus);

		return matrixFileRefNo;
	}

	// && Math.floor(sum)!=Math.floor(sum2)
	public static void main(String[] args) {
		// Double x=27.30;Double y=187.11;
		// Double x=235.21;Double y=595.46;
		// double x=242.55;double y=165.15;
		double x = 260;
		double y = 2.996;
		double sum = x + y;
		// double sumround = Math.floor(sum);
		// double sumfinal = sumround;
		System.out.println("Value is" + sum);
		/// System.out.println("Value is"+sumround);
		// System.out.println("Value is"+sumfinal);
		double invoice = 262.29;
		// double sumround1 = Math.floor(sum2);
		double sumround2 = sum - invoice;
		// double sumfinal2 = sum2-sum;
		// System.out.println("Value is"+sumround1);
		System.out.println("Value is" + sumround2);
//	System.out.println("Value is"+sumfinal2);

		if (sumround2 < 1 && sumround2 > -1) {
			System.out.println("Match" + sumround2);
		} else {
			System.out.println(" No Match" + sumround2);
		}
		if (Math.round(sum) != Math.round(invoice) && Math.floor(sum) != Math.floor(invoice)) {
			System.out.println(" No Match" + sumround2);
		} else {
			System.out.println("  Match" + sumround2);
		}
		if (Math.round(sum) <= Math.round(invoice)) {
			System.out.println("  Match" + sumround2);
		} else {
			System.out.println("No  Match" + Math.round(sum) + "==" + Math.round(invoice));
		}

		/*
		 * if(!String.format("%f", sum).equalsIgnoreCase(String.format("%f", invoice)))
		 * { System.out.println("No Match"+String.format("%f",
		 * sum)+"==="+String.format("%f", invoice)); }else {
		 * System.out.println("Match"); } if(sum!=sum) {
		 * System.out.println("No Match"+String.format("%f",
		 * sum)+"===="+String.format("%f", sum)); }else { System.out.println("Match"); }
		 */
	}
	
	
}
