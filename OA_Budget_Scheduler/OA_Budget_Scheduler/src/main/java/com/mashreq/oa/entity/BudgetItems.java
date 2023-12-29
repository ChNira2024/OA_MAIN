package com.mashreq.oa.entity;

import java.io.Serializable;

public class BudgetItems implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer budgetItemId;
	private Double totalBudget;
	private Double consumedAmount;
	private Double balanceAmount;
	private String serviceCode;
	
	public Integer getBudgetItemId() {
		return budgetItemId;
	}
	public void setBudgetItemId(Integer budgetItemId) {
		this.budgetItemId = budgetItemId;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	@Override
	public String toString() {
		return "BudgetItems [budgetItemId=" + budgetItemId + ", totalBudget=" + totalBudget + ", consumedAmount="
				+ consumedAmount + ", balanceAmount=" + balanceAmount + ", serviceCode=" + serviceCode + "]";
	}
	
	
}
