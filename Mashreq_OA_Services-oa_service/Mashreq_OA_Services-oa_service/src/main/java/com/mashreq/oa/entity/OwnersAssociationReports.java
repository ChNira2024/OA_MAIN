package com.mashreq.oa.entity;

public class OwnersAssociationReports {
	
	private String managementCompany;
	private String supplier;
	private int budgetYear;
	private int invoiceYear;
	private long totalBudget;
	private long consumedAmount;
	private long balanceAmount;
	
	
	
	
	public String getManagementCompany() {
		return managementCompany;
	}




	public void setManagementCompany(String managementCompany) {
		this.managementCompany = managementCompany;
	}




	public String getSupplier() {
		return supplier;
	}




	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}




	public int getBudgetYear() {
		return budgetYear;
	}




	public void setBudgetYear(int budgetYear) {
		this.budgetYear = budgetYear;
	}




	public int getInvoiceYear() {
		return invoiceYear;
	}




	public void setInvoiceYear(int invoiceYear) {
		this.invoiceYear = invoiceYear;
	}




	public long getTotalBudget() {
		return totalBudget;
	}




	public void setTotalBudget(long totalBudget) {
		this.totalBudget = totalBudget;
	}




	public long getConsumedAmount() {
		return consumedAmount;
	}




	public void setConsumedAmount(long consumedAmount) {
		this.consumedAmount = consumedAmount;
	}




	public long getBalanceAmount() {
		return balanceAmount;
	}




	public void setBalanceAmount(long balanceAmount) {
		this.balanceAmount = balanceAmount;
	}




	@Override
	public String toString() {
		return "OwnersAssociationReports [managementCompany=" + managementCompany + ", supplier=" + supplier
				+ ", budgetYear=" + budgetYear + ", invoiceYear=" + invoiceYear + ", totalBudget=" + totalBudget
				+ ", consumedAmount=" + consumedAmount + ", balanceAmount=" + balanceAmount + "]";
	}
	

	

}
