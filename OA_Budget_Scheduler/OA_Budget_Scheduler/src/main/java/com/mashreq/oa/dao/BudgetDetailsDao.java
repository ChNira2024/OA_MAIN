package com.mashreq.oa.dao;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.mashreq.oa.entity.ApprovedBudgetItems;
import com.mashreq.oa.entity.AuditTrail;
import com.mashreq.oa.entity.BeneficiaryListDetails;
import com.mashreq.oa.entity.BudgetItemDetails;
import com.mashreq.oa.entity.BudgetItems;
import com.mashreq.oa.entity.ManagementCompanyDetails;
import com.mashreq.oa.entity.NotificationDetails;
import com.mashreq.oa.entity.PaymentRequestData;
import com.mashreq.oa.entity.PropertyGroupDetails;
import com.mashreq.oa.entity.ServiceChargeGroupDetails;
import com.mashreq.oa.entity.ServiceStatusDetails;
import com.mashreq.oa.entity.TokenResponse;

public interface BudgetDetailsDao {

	public NotificationDetails getNotificationDetails(String status, String compStatus, String source);
	public NotificationDetails getProvisionNotificationDetails(String pendingStatus, String completdStatus,String source);
	public List<Integer> ValidateManagementCompanyId(Integer mcId);

	public List<Integer> validatePropertyGroupId(Integer propertyGroupId);

	public Integer saveManagementCompany(ManagementCompanyDetails companyDetails);

	public Integer savePropertyGroups(PropertyGroupDetails propertyDetails);

	public void saveBeneficiaryList(BeneficiaryListDetails beneList);
	
	public Integer saveServiceStatus(ServiceStatusDetails serviceStatusDetails) throws ParseException;
	
	public Integer saveAdditionalData(String data);

	// public int getDataId();

	public Integer getMgmtCompId(Integer mollakmgmtCompId);

	public Integer getPropId(Integer mollakPropGrpId);

	public Integer saveBudgetDetails(ServiceChargeGroupDetails serviceDetails);

	public void saveBudgetItems(BudgetItemDetails budgetList);

	public Integer getSinglePropId(Integer propGrpId);

	public Integer getSingleMcId(Integer mcId);

	public List<ManagementCompanyDetails> getCompaniesId();

	public List<Integer> getListOfPropId();

	public void validateBudgetDetails(Map<Integer, Integer> mgmtCompIds, Map<Integer, Integer> propGrpIds,
			String periodCode);

	public int updateNotification(String status, Integer budgetInfoId);

	public List<Integer> validatePropertyGroupIdForExistingData(Integer propertyGroupId, Integer mgmtGrpId);

	public List<Integer> validateBudgetIdForExistingData(Integer propertyGroupId, Integer mgmtGrpId,String periodCode);
	
	public Map<Integer,String> validateBudgetIdForExistingDataMap(Integer propertyGroupId, Integer mgmtGrpId,String periodCode);

	public List<BudgetItemDetails> getOldBudgetItemDetails(List<Integer> budgetId );

	public int updateBudgetItemDetails(Integer budgetId, Double totalCost, Double vatAmount,
			Double modifiedTotalBudget, Double modifiedBalanceAmount,String serviceCode);

	public int updateActiveFlag(Integer budgetId, String serviceCode);
	
	public void insertAuditDetails(Integer id,String fieldName,String oldValue,String newValue,String updatedBy);

	public List<ServiceChargeGroupDetails> getOldBudgetDetails(List<Integer> budgetId);

	public void updateExistingBudgetList(ServiceChargeGroupDetails serviceChargeGroupDetails, Map<Integer,String> budgetId);

	public void updateActiveFlagForBudgetDetails(Map<Integer, String> budgetId, ServiceChargeGroupDetails oldData);
	
	/******************** for validation*********************/
	
	public List<PaymentRequestData> getPaymentRequests(String inproStatus, String failedStatus);
	
	public void updateAmounts(List<BudgetItems> budgetItems, Integer pymtReqId, String remarks, List<AuditTrail> auditLogs);
	
	public List<BudgetItems> getMollakData(Integer mgmtCompId, String propertyGroupId, Integer budgetYear,
			String serviceCode);
	
	public boolean getBudgetDetailsCount(Integer mgmtCompId, String propertyGroupId, Integer budgetYear);
	
	
	public List<String> getServiceCode(Integer mgmtCompId, Integer budgetYear, String propertyGroupId,
			String inputServiceCode);
	public void updatePaymentStatus(Integer pymtId,String status,String remarks);
	
	public List<PaymentRequestData> getPaymentRequestsIndividualMail(String approvedStatus, String rejectStatus,
			String excpStatus);

	public void isMailSentPymtReqId(Integer pymtReqId);
	

	public List<Integer> getPaymentRequestsBulkMailCount(String approvedStatus, String rejectStatus,
			String excpStatus);

	public PaymentRequestData getPaymentRequestsBulklMail(Integer matrixFileRefNo);

	public void isMailSentMatrixFileRefNo(Integer matrixFileRefNo);

	public void generateToken(TokenResponse tokenResponse);

	public Integer checkForBuildingId(PropertyGroupDetails details);

	public void insertBuildingMapping(Integer buildingId, Integer propIdInteger,Integer mgmtId);
	
	public boolean checkBuildingMapping(Integer buildingId, Integer propId);
	
	public void insertMollakBudgetInfo(Integer mollakMcId, Integer mollakPropGrpId, String mollaknodatastatus2,
			String mollaksource2, Integer pymtReqId);

	public Integer getMollakMcId(Integer mgmtCompId);

	public Integer getMollakPropGrpId(Integer propId);

	public int updateNotificationForFailedrecords(String status, Integer budgetInfoId, Integer retryCount);
	
	public List<ApprovedBudgetItems> getApprovedBudgetItems(String status);
	
	public void insertApprovedBudgetItems(Integer mgmtCompId, String propId, Integer budgetYear, String serviceCode,
				Double invoiceAmount, Integer supplierId, String bifurcation);


		public void updateApprovedAmounts(List<BudgetItems> newBudgetItemdetails, Integer dataId,
				List<AuditTrail> auditLogs);
		public List<BudgetItems> fetchNewBudgetItemList(Integer mgmtCompId, Integer propId, String serviceCodes,
				String budgetYear);
		public boolean checkforPropertyGroupId(Integer propertyGroupId);

	
}
