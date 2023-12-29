package com.mashreq.oa.entity;

import java.util.Date;

public class PaymentData {
	
	private Long pymtReqId;
	private Long matrixRefNo;
	private String strMatrixRefNo;
	private String subProduct;
	private String debitAccountNumberDesc;
	private String debitAccountNo;
	private String supplierName;
	private String beneficiaryName;
	private java.sql.Date initiatorDate;
	private Date initiatorDates;
	private String paymentCurrency;
	private Double invoiceAmount;
	private String customerReference;
	private String initiatorNameDateTime;
	private String status;
	private String remarks;
	private String uploadedBy;
	private Integer supplierId;
	private NameValue namevalue;
	private Long matrixFileRefNo;
	private String paymentType;
	private Double budgetYear;
	private String serviceCode;
	private Integer mgmtCompId;
	private Integer buildingId;
	private String initiatiorName;
	private String displayInitiatorDate;
	//below fields for search functionality validation
	private double amountFrom;
	private double amountTo;
	private Date initiatorDateFrom;
	private Date initiatorDateTo;
	private String sortBy;
	private String sortOrder;
	private String mcNameEn;
	private String buildingName;
	private int count;
	private String bifurcation;
	public PaymentData()
	{
		
	}
	
	public String getBifurcation() {
		return bifurcation;
	}

	public void setBifurcation(String bifurcation) {
		this.bifurcation = bifurcation;
	}

	public String getDisplayInitiatorDate() {
		return displayInitiatorDate;
	}

	public void setDisplayInitiatorDate(String displayInitiatorDate) {
		this.displayInitiatorDate = displayInitiatorDate;
	}

	public Date getInitiatorDates() {
		return initiatorDates;
	}

	public void setInitiatorDates(Date initiatorDates) {
		this.initiatorDates = initiatorDates;
	}

	public Long getPymtReqId() {
		return pymtReqId;
	}

	public void setPymtReqId(Long pymtReqId) {
		this.pymtReqId = pymtReqId;
	}

	public Long getMatrixRefNo() {
		return matrixRefNo;
	}

	public void setMatrixRefNo(Long matrixRefNo) {
		this.matrixRefNo = matrixRefNo;
	}

	public String getStrMatrixRefNo() {
		return strMatrixRefNo;
	}

	public void setStrMatrixRefNo(String strMatrixRefNo) {
		this.strMatrixRefNo = strMatrixRefNo;
	}

	public String getSubProduct() {
		return subProduct;
	}

	public void setSubProduct(String subProduct) {
		this.subProduct = subProduct;
	}

	public String getDebitAccountNumberDesc() {
		return debitAccountNumberDesc;
	}

	public void setDebitAccountNumberDesc(String debitAccountNumberDesc) {
		this.debitAccountNumberDesc = debitAccountNumberDesc;
	}

	public String getDebitAccountNo() {
		return debitAccountNo;
	}

	public void setDebitAccountNo(String debitAccountNo) {
		this.debitAccountNo = debitAccountNo;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public java.sql.Date getInitiatorDate() {
		return initiatorDate;
	}

	public void setInitiatorDate(java.sql.Date initiatorDate) {
		this.initiatorDate = initiatorDate;
	}

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	public String getInitiatorNameDateTime() {
		return initiatorNameDateTime;
	}

	public void setInitiatorNameDateTime(String initiatorNameDateTime) {
		this.initiatorNameDateTime = initiatorNameDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public NameValue getNamevalue() {
		return namevalue;
	}

	public void setNamevalue(NameValue namevalue) {
		this.namevalue = namevalue;
	}

	public Long getMatrixFileRefNo() {
		return matrixFileRefNo;
	}

	public void setMatrixFileRefNo(Long matrixFileRefNo) {
		this.matrixFileRefNo = matrixFileRefNo;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Double getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(Double budgetYear) {
		this.budgetYear = budgetYear;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Integer getMgmtCompId() {
		return mgmtCompId;
	}

	public void setMgmtCompId(Integer mgmtCompId) {
		this.mgmtCompId = mgmtCompId;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}

	public String getInitiatiorName() {
		return initiatiorName;
	}

	public void setInitiatiorName(String initiatiorName) {
		this.initiatiorName = initiatiorName;
	}

	public double getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(double amountFrom) {
		this.amountFrom = amountFrom;
	}

	public double getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(double amountTo) {
		this.amountTo = amountTo;
	}

	public Date getInitiatorDateFrom() {
		return initiatorDateFrom;
	}

	public void setInitiatorDateFrom(Date initiatorDateFrom) {
		this.initiatorDateFrom = initiatorDateFrom;
	}

	public Date getInitiatorDateTo() {
		return initiatorDateTo;
	}

	public void setInitiatorDateTo(Date initiatorDateTo) {
		this.initiatorDateTo = initiatorDateTo;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getMcNameEn() {
		return mcNameEn;
	}

	public void setMcNameEn(String mcNameEn) {
		this.mcNameEn = mcNameEn;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "PaymentData [pymtReqId=" + pymtReqId + ", matrixRefNo=" + matrixRefNo + ", subProduct=" + subProduct
				+ ", debitAccountNumberDesc=" + debitAccountNumberDesc + ", debitAccountNo=" + debitAccountNo
				+ ", supplierName=" + supplierName + ", beneficiaryName=" + beneficiaryName + ", initiatorDate="
				+ initiatorDate + ", initiatorDates=" + initiatorDates + ", paymentCurrency=" + paymentCurrency
				+ ", invoiceAmount=" + invoiceAmount + ", customerReference=" + customerReference
				+ ", initiatorNameDateTime=" + initiatorNameDateTime + ", status=" + status + ", remarks=" + remarks
				+ ", uploadedBy=" + uploadedBy + ", supplierId=" + supplierId + ", namevalue=" + namevalue
				+ ", matrixFileRefNo=" + matrixFileRefNo + ", paymentType=" + paymentType + ", budgetYear=" + budgetYear
				+ ", serviceCode=" + serviceCode + ", mgmtCompId=" + mgmtCompId + ", buildingId=" + buildingId
				+ ", initiatiorName=" + initiatiorName + ", displayInitiatorDate=" + displayInitiatorDate
				+ ", amountFrom=" + amountFrom + ", amountTo=" + amountTo + ", initiatorDateFrom=" + initiatorDateFrom
				+ ", initiatorDateTo=" + initiatorDateTo + ", sortBy=" + sortBy + ", sortOrder=" + sortOrder
				+ ", mcNameEn=" + mcNameEn + ", buildingName=" + buildingName + ", count=" + count + ", bifurcation="
				+ bifurcation + "]";
	}

	
	

}
