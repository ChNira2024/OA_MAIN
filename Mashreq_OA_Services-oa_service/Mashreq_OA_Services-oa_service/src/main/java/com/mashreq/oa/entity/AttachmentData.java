package com.mashreq.oa.entity;

import java.io.Serializable;
import java.sql.Date;


public class AttachmentData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer pymtReqId;
	private int attachmentDataId;
	private long matrixRefNo;
	private String strMatrixRefNo;
	private String mcNameEn;
	private String issuanceAuthority;
	private Date tradeLicenseExpDate;
	private Date agreementExpDate;
	private Double budgetYear;
	private String invoiceDateYear;
	private Double invoiceAmount;
	private Integer supplierId;
	private String supplierName;
	private Integer mgmtCompId;
	private String serviceCode;
	private String status;
	private Date expDate;
	private String hiddenTrade;
	private String hiddenAgree;
	private String noProperDocuments;
	private String autoRenewal;
	private String insuranceCheckBox;
	private String governmentCheckBox;
	private String entityType;
	private Integer buildingId;
	private String buildingName;
	private String isGovernmentEntity;
	private String isInsuranceCompany;
	private String bifurcation;
	private String isExceptionalApproval;
	private String remarks;
	
	
	//for bulk functionality
	private Long matrixFileRefNo;
	private Integer subPaymentsCount;
	private String attachmentFlag;
	private String paymentType;
	
	public Integer getPymtReqId() {
		return pymtReqId;
	}
	public void setPymtReqId(Integer pymtReqId) {
		this.pymtReqId = pymtReqId;
	}
	public int getAttachmentDataId() {
		return attachmentDataId;
	}
	public void setAttachmentDataId(int attachmentDataId) {
		this.attachmentDataId = attachmentDataId;
	}
	public long getMatrixRefNo() {
		return matrixRefNo;
	}
	public void setMatrixRefNo(long matrixRefNo) {
		this.matrixRefNo = matrixRefNo;
	}
	
	public String getStrMatrixRefNo() {
		return strMatrixRefNo;
	}
	public void setStrMatrixRefNo(String strMatrixRefNo) {
		this.strMatrixRefNo = strMatrixRefNo;
	}
	public String getMcNameEn() {
		return mcNameEn;
	}
	public void setMcNameEn(String mcNameEn) {
		this.mcNameEn = mcNameEn;
	}
	public String getIssuanceAuthority() {
		return issuanceAuthority;
	}
	public void setIssuanceAuthority(String issuanceAuthority) {
		this.issuanceAuthority = issuanceAuthority;
	}
	public Date getTradeLicenseExpDate() {
		return tradeLicenseExpDate;
	}
	public void setTradeLicenseExpDate(Date tradeLicenseExpDate) {
		this.tradeLicenseExpDate = tradeLicenseExpDate;
	}
	public Date getAgreementExpDate() {
		return agreementExpDate;
	}
	public void setAgreementExpDate(Date agreementExpDate) {
		this.agreementExpDate = agreementExpDate;
	}
	
	public Double getBudgetYear() {
		return budgetYear;
	}
	public void setBudgetYear(Double budgetYear) {
		this.budgetYear = budgetYear;
	}
	public String getInvoiceDateYear() {
		return invoiceDateYear;
	}
	public void setInvoiceDateYear(String invoiceDateYear) {
		this.invoiceDateYear = invoiceDateYear;
	}
	public Double getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Integer getMgmtCompId() {
		return mgmtCompId;
	}
	public void setMgmtCompId(Integer mgmtCompId) {
		this.mgmtCompId = mgmtCompId;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public Integer getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getExpDate() {
		return expDate;
	}
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}
	public String getHiddenTrade() {
		return hiddenTrade;
	}
	public void setHiddenTrade(String hiddenTrade) {
		this.hiddenTrade = hiddenTrade;
	}
	public String getHiddenAgree() {
		return hiddenAgree;
	}
	public void setHiddenAgree(String hiddenAgree) {
		this.hiddenAgree = hiddenAgree;
	}
	public Long getMatrixFileRefNo() {
		return matrixFileRefNo;
	}
	public void setMatrixFileRefNo(Long matrixFileRefNo) {
		this.matrixFileRefNo = matrixFileRefNo;
	}
	
	public Integer getSubPaymentsCount() {
		return subPaymentsCount;
	}
	public void setSubPaymentsCount(Integer subPaymentsCount) {
		this.subPaymentsCount = subPaymentsCount;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getAttachmentFlag() {
		return attachmentFlag;
	}
	public void setAttachmentFlag(String attachmentFlag) {
		this.attachmentFlag = attachmentFlag;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getNoProperDocuments() {
		return noProperDocuments;
	}
	public void setNoProperDocuments(String noProperDocuments) {
		this.noProperDocuments = noProperDocuments;
	}
		
	public String getAutoRenewal() {
		return autoRenewal;
	}
	public void setAutoRenewal(String autoRenewal) {
		this.autoRenewal = autoRenewal;
	}
	public String getInsuranceCheckBox() {
		return insuranceCheckBox;
	}
	public void setInsuranceCheckBox(String insuranceCheckBox) {
		this.insuranceCheckBox = insuranceCheckBox;
	}
	public String getGovernmentCheckBox() {
		return governmentCheckBox;
	}
	public void setGovernmentCheckBox(String governmentCheckBox) {
		this.governmentCheckBox = governmentCheckBox;
	}
	
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	
	public String getIsGovernmentEntity() {
		return isGovernmentEntity;
	}
	public void setIsGovernmentEntity(String isGovernmentEntity) {
		this.isGovernmentEntity = isGovernmentEntity;
	}
	public String getIsInsuranceCompany() {
		return isInsuranceCompany;
	}
	public void setIsInsuranceCompany(String isInsuranceCompany) {
		this.isInsuranceCompany = isInsuranceCompany;
	}
	
	public String getBifurcation() {
		return bifurcation;
	}
	public void setBifurcation(String bifurcation) {
		this.bifurcation = bifurcation;
	}
	public String getIsExceptionalApproval() {
		return isExceptionalApproval;
	}
	public void setIsExceptionalApproval(String isExceptionalApproval) {
		this.isExceptionalApproval = isExceptionalApproval;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "AttachmentData [pymtReqId=" + pymtReqId + ", attachmentDataId=" + attachmentDataId + ", matrixRefNo="
				+ matrixRefNo + ", mcNameEn=" + mcNameEn + ", issuanceAuthority=" + issuanceAuthority
				+ ", tradeLicenseExpDate=" + tradeLicenseExpDate + ", agreementExpDate=" + agreementExpDate
				+ ", budgetYear=" + budgetYear + ", invoiceDateYear=" + invoiceDateYear + ", invoiceAmount="
				+ invoiceAmount + ", supplierId=" + supplierId + ", supplierName=" + supplierName + ", mgmtCompId="
				+ mgmtCompId + ", serviceCode=" + serviceCode + ", status=" + status + ", expDate=" + expDate
				+ ", hiddenTrade=" + hiddenTrade + ", hiddenAgree=" + hiddenAgree + ", noProperDocuments="
				+ noProperDocuments + ", autoRenewal=" + autoRenewal + ", insuranceCheckBox=" + insuranceCheckBox
				+ ", governmentCheckBox=" + governmentCheckBox + ", entityType=" + entityType + ", buildingId="
				+ buildingId + ", buildingName=" + buildingName + ", isGovernmentEntity=" + isGovernmentEntity
				+ ", isInsuranceCompany=" + isInsuranceCompany + ", bifurcation=" + bifurcation
				+ ", isExceptionalApproval=" + isExceptionalApproval + ", remarks=" + remarks + ", matrixFileRefNo="
				+ matrixFileRefNo + ", subPaymentsCount=" + subPaymentsCount + ", attachmentFlag=" + attachmentFlag
				+ ", paymentType=" + paymentType + "]";
	}
	
	

	
}
