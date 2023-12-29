package com.mashreq.oa.entity;

import java.io.Serializable;

public class BudgetItemDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//REST Response fields
	private String categoryCode;
	private Language categoryName;
	private String subCategoryCode;
	private Language subCategoryName;
	private String serviceCode;
	private Language serviceName;
	private Double totalCost;
	private Double vatAmount;
	private Double totalBudget;
	private Double consumedAmount;
	private Double balanceAmount;
	private String isActive;
	private Integer serviceId;
	private String serviceChrgGrpId;
	private Integer budget_Item_Id;
	
	
	
	
	public Integer getBudget_Item_Id() {
		return budget_Item_Id;
	}
	public void setBudget_Item_Id(Integer budget_Item_Id) {
		this.budget_Item_Id = budget_Item_Id;
	}
	public String getServiceChrgGrpId() {
		return serviceChrgGrpId;
	}
	public void setServiceChrgGrpId(String serviceChrgGrpId) {
		this.serviceChrgGrpId = serviceChrgGrpId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	//Database fields
	private Integer budgetId;
	
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public Language getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(Language categoryName) {
		this.categoryName = categoryName;
	}
	public String getSubCategoryCode() {
		return subCategoryCode;
	}
	public void setSubCategoryCode(String subCategoryCode) {
		this.subCategoryCode = subCategoryCode;
	}
	public Language getSubCategoryName() {
		return subCategoryName;
	}
	public void setSubCategoryName(Language subCategoryName) {
		this.subCategoryName = subCategoryName;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public Language getServiceName() {
		return serviceName;
	}
	public void setServiceName(Language serviceName) {
		this.serviceName = serviceName;
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
	
	public Integer getBudgetId() {
		return budgetId;
	}
	public void setBudgetId(Integer budgetId) {
		this.budgetId = budgetId;
	}
	@Override
	public String toString() {
		return "BudgetItemDetails [categoryCode=" + categoryCode + ", categoryName=" + categoryName
				+ ", subCategoryCode=" + subCategoryCode + ", subCategoryName=" + subCategoryName + ", serviceCode="
				+ serviceCode + ", serviceName=" + serviceName + ", totalCost=" + totalCost + ", vatAmount=" + vatAmount
				+ ", totalBudget=" + totalBudget + ", consumedAmount=" + consumedAmount + ", balanceAmount="
				+ balanceAmount + ", isActive=" + isActive + ", serviceId=" + serviceId + ", serviceChrgGrpId="
				+ serviceChrgGrpId + ", budget_Item_Id=" + budget_Item_Id + ", budgetId=" + budgetId + "]";
	}
	
	
	
}
