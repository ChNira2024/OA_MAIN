package com.mashreq.oa.entity;

import java.util.Date;

public class CompletedRequestData {
	private long pymtReqId;
	private int attachmentId;
	private long matrixRefNo;
	private String subProduct;
	private String debitAccountNumberDesc;
	private String beneficiaryName;
	private Date initiatorDate;
	private String paymentCurrency;
	private double invoiceAmount;
	private String customerReference;
	private String initiatorNameDateTime;
	private String status;
	private NameValue namevalue;
	private String remarks;
	
	public CompletedRequestData()
	{
		
	}

	public long getPymtReqId() {
		return pymtReqId;
	}

	public void setPymtReqId(long pymtReqId) {
		this.pymtReqId = pymtReqId;
	}

	public int getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(int attachmentId) {
		this.attachmentId = attachmentId;
	}

	public long getMatrixRefNo() {
		return matrixRefNo;
	}

	public void setMatrixRefNo(long matrixRefNo) {
		this.matrixRefNo = matrixRefNo;
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

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	public double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(double invoiceAmount) {
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

	public NameValue getNamevalue() {
		return namevalue;
	}

	public void setNamevalue(NameValue namevalue) {
		this.namevalue = namevalue;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "CompletedRequestData [pymtReqId=" + pymtReqId + ", attachmentId=" + attachmentId + ", matrixRefNo="
				+ matrixRefNo + ", subProduct=" + subProduct + ", debitAccountNumberDesc=" + debitAccountNumberDesc
				+ ", beneficiaryName=" + beneficiaryName + ", initiatorDate=" + initiatorDate + ", paymentCurrency="
				+ paymentCurrency + ", invoiceAmount=" + invoiceAmount + ", customerReference=" + customerReference
				+ ", initiatorNameDateTime=" + initiatorNameDateTime + ", status=" + status + ", namevalue=" + namevalue
				+ ", remarks=" + remarks + "]";
	}
}
