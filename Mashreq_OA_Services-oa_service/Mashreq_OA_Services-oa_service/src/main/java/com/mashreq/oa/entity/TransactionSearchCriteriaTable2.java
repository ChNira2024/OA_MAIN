package com.mashreq.oa.entity;

import java.time.LocalDate;

public class TransactionSearchCriteriaTable2 {
	private long matrixRefNo;
	private String subProduct;
	private long debitAccountNumber;
	private String benificiaryName;
	private LocalDate valueDate;
	private String paymentCurrency;
	private long paymentAmount;
	private String customerRef;
	private String iniatorName;
	private String remarks;


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


	public long getDebitAccountNumber() {
		return debitAccountNumber;
	}




	public void setDebitAccountNumber(long debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}




	public String getBenificiaryName() {
		return benificiaryName;
	}




	public void setBenificiaryName(String benificiaryName) {
		this.benificiaryName = benificiaryName;
	}




	public LocalDate getValueDate() {
		return valueDate;
	}




	public void setValueDate(LocalDate valueDate) {
		this.valueDate = valueDate;
	}




	public String getPaymentCurrency() {
		return paymentCurrency;
	}




	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}




	public long getPaymentAmount() {
		return paymentAmount;
	}




	public void setPaymentAmount(long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}




	public String getCustomerRef() {
		return customerRef;
	}




	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}




	public String getIniatorName() {
		return iniatorName;
	}




	public void setIniatorName(String iniatorName) {
		this.iniatorName = iniatorName;
	}




	public String getRemarks() {
		return remarks;
	}




	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}




	@Override
	public String toString() {
		return "TransactionSearchCriteriaTable2 [matrixRefNo=" + matrixRefNo + ", subProduct=" + subProduct
				+ ", debitAccountNumber=" + debitAccountNumber + ", benificiaryName=" + benificiaryName + ", valueDate="
				+ valueDate + ", paymentCurrency=" + paymentCurrency + ", paymentAmount=" + paymentAmount
				+ ", customerRef=" + customerRef + ", iniatorName=" + iniatorName + ", remarks=" + remarks + "]";
	}




}
