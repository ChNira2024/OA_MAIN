package com.mashreq.oa.entity;

public class NotificationDetails {
	
	private int budgetInfoId;
	private String propertyGroupId;
	private String mcId;
	private String periodCode;
	private String currentDate;
	private String status;
	
	public int getBudgetInfoId() {
		return budgetInfoId;
	}
	public void setBudgetInfoId(int budgetInfoId) {
		this.budgetInfoId = budgetInfoId;
	}
	public String getPropertyGroupId() {
		return propertyGroupId;
	}
	public void setPropertyGroupId(String propertyGroupId) {
		this.propertyGroupId = propertyGroupId;
	}
	public String getMcId() {
		return mcId;
	}
	public void setMcId(String mcId) {
		this.mcId = mcId;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "NotificationDetails [budgetInfoId=" + budgetInfoId + ", propertyGroupId=" + propertyGroupId + ", mcId="
				+ mcId + ", periodCode=" + periodCode + ", currentDate=" + currentDate + ", status=" + status + "]";
	}	
}
