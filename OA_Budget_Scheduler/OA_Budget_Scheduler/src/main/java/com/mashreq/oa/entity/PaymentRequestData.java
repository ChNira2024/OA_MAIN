package com.mashreq.oa.entity;

import java.io.Serializable;
import java.sql.Date;


public class PaymentRequestData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer pymtReqId;
	private int attachmentDataId;
	private long matrixRefNo;
	private String managementCompanyName;
	private String issuanceAuthority;
	private Date tradeLicenseExpDate;
	private Date agreementExpDate;
	private Integer budgetYear;
	private String invoiceDateYear;
	private Double invoiceAmount;
	private Integer supplierId;
	private Integer mgmtCompId;
	private String serviceCode;
	private String buildingId;
	private String propId;
	private String paymentType;
	//private String entityType;
	private String autoRenewal;
	private String noProperDocuments;
	private String isGovernmentEntity;
	private String isInsuranceCompany;
	
	//FOR MAIL
	private String subProduct;
	private String debitAccountNumberDesc;
	private String beneficiaryName;
	private Date initiatorDate;
	private String status;
	private String uploadedBy;
	private String supplierName;
	private Long matrixFileRefNo;
	private String remarks;
	
	//Mollakdata
	private Integer mollakMcId;
	private Integer mollakPropGrpId;
	private String isExceptionalApproval;
	private String bifurcation;
	
	
	
	public String getIsExceptionalApproval() {
		return isExceptionalApproval;
	}
	public void setIsExceptionalApproval(String isExceptionalApproval) {
		this.isExceptionalApproval = isExceptionalApproval;
	}
	public String getBifurcation() {
		return bifurcation;
	}
	public void setBifurcation(String bifurcation) {
		this.bifurcation = bifurcation;
	}
	public String getPropId() {
		return propId;
	}
	public void setPropId(String propId) {
		this.propId = propId;
	}
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
	public String getManagementCompanyName() {
		return managementCompanyName;
	}
	public void setManagementCompanyName(String managementCompanyName) {
		this.managementCompanyName = managementCompanyName;
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
	public Integer getBudgetYear() {
		return budgetYear;
	}
	public void setBudgetYear(Integer budgetYear) {
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	

	
	public String getAutoRenewal() {
		return autoRenewal;
	}
	public void setAutoRenewal(String autoRenewal) {
		this.autoRenewal = autoRenewal;
	}
	public String getNoProperDocuments() {
		return noProperDocuments;
	}
	public void setNoProperDocuments(String noProperDocuments) {
		this.noProperDocuments = noProperDocuments;
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
	public String getBeneficiaryName() {
		return beneficiaryName;
	}
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
	public Date getInitiatorDate() {
		return initiatorDate;
	}
	public void setInitiatorDate(Date initiatorDate) {
		this.initiatorDate = initiatorDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Long getMatrixFileRefNo() {
		return matrixFileRefNo;
	}
	public void setMatrixFileRefNo(Long matrixFileRefNo) {
		this.matrixFileRefNo = matrixFileRefNo;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public Integer getMollakMcId() {
		return mollakMcId;
	}
	public void setMollakMcId(Integer mollakMcId) {
		this.mollakMcId = mollakMcId;
	}
	public Integer getMollakPropGrpId() {
		return mollakPropGrpId;
	}
	public void setMollakPropGrpId(Integer mollakPropGrpId) {
		this.mollakPropGrpId = mollakPropGrpId;
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
	@Override
	public String toString() {
		return "PaymentRequestData [pymtReqId=" + pymtReqId + ", attachmentDataId=" + attachmentDataId
				+ ", matrixRefNo=" + matrixRefNo + ", managementCompanyName=" + managementCompanyName
				+ ", issuanceAuthority=" + issuanceAuthority + ", tradeLicenseExpDate=" + tradeLicenseExpDate
				+ ", agreementExpDate=" + agreementExpDate + ", budgetYear=" + budgetYear + ", invoiceDateYear="
				+ invoiceDateYear + ", invoiceAmount=" + invoiceAmount + ", supplierId=" + supplierId + ", mgmtCompId="
				+ mgmtCompId + ", serviceCode=" + serviceCode + ", buildingId=" + buildingId + ", propId=" + propId
				+ ", paymentType=" + paymentType + ", autoRenewal=" + autoRenewal + ", noProperDocuments="
				+ noProperDocuments + ", isGovernmentEntity=" + isGovernmentEntity + ", isInsuranceCompany="
				+ isInsuranceCompany + ", subProduct=" + subProduct + ", debitAccountNumberDesc="
				+ debitAccountNumberDesc + ", beneficiaryName=" + beneficiaryName + ", initiatorDate=" + initiatorDate
				+ ", status=" + status + ", uploadedBy=" + uploadedBy + ", supplierName=" + supplierName
				+ ", matrixFileRefNo=" + matrixFileRefNo + ", remarks=" + remarks + ", mollakMcId=" + mollakMcId
				+ ", mollakPropGrpId=" + mollakPropGrpId + ", isExceptionalApproval=" + isExceptionalApproval
				+ ", bifurcation=" + bifurcation + "]";
	}
	
	

}
