package com.mashreq.oa.entity;

import java.time.LocalDate;

public class IndividualPayements {
	private long matrixRefNo;
	private String subProduct;
	private long debitAccountNumber;
	private String benificiaryName;
	private LocalDate valueDate;
	private String paymentCurrency;
	private double paymentAmount;
	private String customerRef;
	private String initiatorName;
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

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getCustomerRef() {
		return customerRef;
	}

	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}

	public String getInitiatorName() {
		return initiatorName;
	}

	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "IndividualPayements [matrixRefNo=" + matrixRefNo + ", subProduct=" + subProduct
				+ ", debitAccountNumber=" + debitAccountNumber + ", benificiaryName=" + benificiaryName + ", valueDate="
				+ valueDate + ", paymentCurrency=" + paymentCurrency + ", paymentAmount=" + paymentAmount
				+ ", customerRef=" + customerRef + ", initiatorName=" + initiatorName + ", remarks=" + remarks + "]";
	}

}
