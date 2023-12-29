package com.mashreq.oa.entity;

import java.util.Arrays;

public class ServiceChargeGroupDetails {
	
	//Response
	private String serviceChargeGroupId;
	private Language serviceChargeGroupName;
	private Language usage;
	private String budgetPeriodCode;
	private String budgetPeriodTitle;
	private String budgetPeriodFrom;
	private String budgetPeriodTo;
	private BudgetItemDetails[] budgetItems;
	private Integer serviceId;
	//DB
	private Integer dataId;
	private String email;
	private Integer propId;
	private Integer mgmtId;
	private Integer budgetId;
	
	
	public Integer getBudgetId() {
		return budgetId;
	}
	public void setBudgetId(Integer budgetId) {
		this.budgetId = budgetId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceChargeGroupId() {
		return serviceChargeGroupId;
	}
	public void setServiceChargeGroupId(String serviceChargeGroupId) {
		this.serviceChargeGroupId = serviceChargeGroupId;
	}
	public Language getServiceChargeGroupName() {
		return serviceChargeGroupName;
	}
	public void setServiceChargeGroupName(Language serviceChargeGroupName) {
		this.serviceChargeGroupName = serviceChargeGroupName;
	}
	public Language getUsage() {
		return usage;
	}
	public void setUsage(Language usage) {
		this.usage = usage;
	}
	public String getBudgetPeriodCode() {
		return budgetPeriodCode;
	}
	public void setBudgetPeriodCode(String budgetPeriodCode) {
		this.budgetPeriodCode = budgetPeriodCode;
	}
	public String getBudgetPeriodTitle() {
		return budgetPeriodTitle;
	}
	public void setBudgetPeriodTitle(String budgetPeriodTitle) {
		this.budgetPeriodTitle = budgetPeriodTitle;
	}
	public String getBudgetPeriodFrom() {
		return budgetPeriodFrom;
	}
	public void setBudgetPeriodFrom(String budgetPeriodFrom) {
		this.budgetPeriodFrom = budgetPeriodFrom;
	}
	public String getBudgetPeriodTo() {
		return budgetPeriodTo;
	}
	public void setBudgetPeriodTo(String budgetPeriodTo) {
		this.budgetPeriodTo = budgetPeriodTo;
	}
	
	public BudgetItemDetails[] getBudgetItems() {
		return budgetItems;
	}
	public void setBudgetItems(BudgetItemDetails[] budgetItems) {
		this.budgetItems = budgetItems;
	}
	
	public Integer getDataId() {
		return dataId;
	}
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public Integer getPropId() {
		return propId;
	}
	public void setPropId(Integer propId) {
		this.propId = propId;
	}
		
	public Integer getMgmtId() {
		return mgmtId;
	}
	public void setMgmtId(Integer mgmtId) {
		this.mgmtId = mgmtId;
	}
	
	@Override
	public String toString() {
		return "ServiceChargeGroupDetails [serviceChargeGroupId=" + serviceChargeGroupId + ", serviceChargeGroupName="
				+ serviceChargeGroupName + ", usage=" + usage + ", budgetPeriodCode=" + budgetPeriodCode
				+ ", budgetPeriodTitle=" + budgetPeriodTitle + ", budgetPeriodFrom=" + budgetPeriodFrom
				+ ", budgetPeriodTo=" + budgetPeriodTo + ", budgetItems=" + Arrays.toString(budgetItems) + ", dataId="
				+ dataId + ", email=" + email + ", propId=" + propId + ", mgmtId=" + mgmtId + "]";
	}
}
