package com.mashreq.oa.entity;

import java.time.LocalDate;

public class BulkPayements {
	
	private String matrixFileRefNo;
	private String subProduct;
	private long debitAccountNumber;
	private String benificiaryName;
	private LocalDate valueDate;
	private String paymentCurrency;
	private double paymentAmount;
	private String customerRef;
	private String initiatorName;
	private String remarks;
	private String managementCompany;
	private String budgetYear;
	private String invoiceYear;
	private double availableBalance;
	
	@Override
	public String toString() {
		return "BulkPayements [matrixFileRefNo=" + matrixFileRefNo + ", subProduct=" + subProduct
				+ ", debitAccountNumber=" + debitAccountNumber + ", benificiaryName=" + benificiaryName + ", valueDate="
				+ valueDate + ", paymentCurrency=" + paymentCurrency + ", paymentAmount=" + paymentAmount
				+ ", customerRef=" + customerRef + ", initiatorName=" + initiatorName + ", remarks=" + remarks
				+ ", managementCompany=" + managementCompany + ", budgetYear=" + budgetYear + ", invoiceYear="
				+ invoiceYear + ", availableBalance=" + availableBalance + "]";
	}
	/**
	 * @return the matrixFileRefNo
	 */
	public String getMatrixFileRefNo() {
		return matrixFileRefNo;
	}
	/**
	 * @param matrixFileRefNo the matrixFileRefNo to set
	 */
	public void setMatrixFileRefNo(String matrixFileRefNo) {
		this.matrixFileRefNo = matrixFileRefNo;
	}
	/**
	 * @return the subProduct
	 */
	public String getSubProduct() {
		return subProduct;
	}
	/**
	 * @param subProduct the subProduct to set
	 */
	public void setSubProduct(String subProduct) {
		this.subProduct = subProduct;
	}
	/**
	 * @return the debitAccountNumber
	 */
	public long getDebitAccountNumber() {
		return debitAccountNumber;
	}
	/**
	 * @param debitAccountNumber the debitAccountNumber to set
	 */
	public void setDebitAccountNumber(long debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}
	/**
	 * @return the benificiaryName
	 */
	public String getBenificiaryName() {
		return benificiaryName;
	}
	/**
	 * @param benificiaryName the benificiaryName to set
	 */
	public void setBenificiaryName(String benificiaryName) {
		this.benificiaryName = benificiaryName;
	}
	/**
	 * @return the valueDate
	 */
	public LocalDate getValueDate() {
		return valueDate;
	}
	/**
	 * @param valueDate the valueDate to set
	 */
	public void setValueDate(LocalDate valueDate) {
		this.valueDate = valueDate;
	}
	/**
	 * @return the paymentCurrency
	 */
	public String getPaymentCurrency() {
		return paymentCurrency;
	}
	/**
	 * @param paymentCurrency the paymentCurrency to set
	 */
	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}
	/**
	 * @return the paymentAmount
	 */
	public double getPaymentAmount() {
		return paymentAmount;
	}
	/**
	 * @param paymentAmount the paymentAmount to set
	 */
	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	/**
	 * @return the customerRef
	 */
	public String getCustomerRef() {
		return customerRef;
	}
	/**
	 * @param customerRef the customerRef to set
	 */
	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}
	/**
	 * @return the iniatorName
	 */
	public String getInitiatorName() {
		return initiatorName;
	}
	/**
	 * @param iniatorName the iniatorName to set
	 */
	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the managementCompany
	 */
	public String getManagementCompany() {
		return managementCompany;
	}
	/**
	 * @param managementCompany the managementCompany to set
	 */
	public void setManagementCompany(String managementCompany) {
		this.managementCompany = managementCompany;
	}
	/**
	 * @return the budgetYear
	 */
	public String getBudgetYear() {
		return budgetYear;
	}
	/**
	 * @param budgetYear the budgetYear to set
	 */
	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}
	/**
	 * @return the invoiceYear
	 */
	public String getInvoiceYear() {
		return invoiceYear;
	}
	/**
	 * @param invoiceYear the invoiceYear to set
	 */
	public void setInvoiceYear(String invoiceYear) {
		this.invoiceYear = invoiceYear;
	}
	/**
	 * @return the availableBalance
	 */
	public double getAvailableBalance() {
		return availableBalance;
	}
	/**
	 * @param availableBalance the availableBalance to set
	 */
	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	
}
