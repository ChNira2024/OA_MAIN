package com.mashreq.oa.entity;

public class ReportsOutput {
	
	private String mcNameEn;
	private String supplierName;
	private String budgetYear;
	private Integer invoiceYear;
	private Double totalBudget;
	private Double consumedAmount;
	private Double balanceAmount;
	
	
	
	public String getMcNameEn() {
		return mcNameEn;
	}
	public void setMcNameEn(String mcNameEn) {
		this.mcNameEn = mcNameEn;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	public String getBudgetYear() {
		return budgetYear;
	}
	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}
	public Integer getInvoiceYear() {
		return invoiceYear;
	}
	public void setInvoiceYear(Integer invoiceYear) {
		this.invoiceYear = invoiceYear;
	}
	public Double getTotalBudget() {
		return totalBudget;
	}
	public void setTotalBudget(Double totalBudget) {
		this.totalBudget = totalBudget;
	}
	public Double getConsumedAmount() {
		return consumedAmount;
	}
	public void setConsumedAmount(Double consumedAmount) {
		this.consumedAmount = consumedAmount;
	}
	public Double getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	@Override
	public String toString() {
		return "ReportsOutput [mcNameEn=" + mcNameEn + ", supplierName=" + supplierName + ", budgetYear=" + budgetYear
				+ ", invoiceYear=" + invoiceYear + ", totalBudget=" + totalBudget + ", consumedAmount=" + consumedAmount
				+ ", balanceAmount=" + balanceAmount + "]";
	}
	
	
	

}
