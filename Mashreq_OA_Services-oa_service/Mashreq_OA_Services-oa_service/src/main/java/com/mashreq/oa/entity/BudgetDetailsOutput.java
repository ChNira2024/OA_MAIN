package com.mashreq.oa.entity;

public class BudgetDetailsOutput {

	private Integer budgetItemId;
	private String serviceCode;
	private String serviceNameEn;
	private Double totalCost;
	private Double vatAmount;
	private Double totalBudget;
	private Double consumedAmount;
	private Double balanceAmount;
	private Boolean isEditMode;
	private String usageEn;
	private String budgetPeriodCode;

	public Integer getBudgetItemId() {
		return budgetItemId;
	}

	public void setBudgetItemId(Integer budgetItemId) {
		this.budgetItemId = budgetItemId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceNameEn() {
		return serviceNameEn;
	}

	public void setServiceNameEn(String serviceNameEn) {
		this.serviceNameEn = serviceNameEn;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Double getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
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

	public Boolean getIsEditMode() {
		return isEditMode;
	}

	public void setIsEditMode(Boolean isEditMode) {
		this.isEditMode = isEditMode;
	}

	public String getUsageEn() {
		return usageEn;
	}

	public void setUsageEn(String usageEn) {
		this.usageEn = usageEn;
	}

	public String getBudgetPeriodCode() {
		return budgetPeriodCode;
	}

	public void setBudgetPeriodCode(String budgetPeriodCode) {
		this.budgetPeriodCode = budgetPeriodCode;
	}

	@Override
	public String toString() {
		return "BudgetDetailsOutput [budgetItemId=" + budgetItemId + ", serviceCode=" + serviceCode + ", serviceNameEn="
				+ serviceNameEn + ", totalCost=" + totalCost + ", vatAmount=" + vatAmount + ", totalBudget="
				+ totalBudget + ", consumedAmount=" + consumedAmount + ", balanceAmount=" + balanceAmount
				+ ", isEditMode=" + isEditMode + ", usageEn=" + usageEn + ", budgetPeriodCode=" + budgetPeriodCode
				+ "]";
	}

}
