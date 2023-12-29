package com.mashreq.oa.model;

public class BudgetDetailsInfo {
	
	private Integer budgetInfoId;
	private Integer mgmtCompId;
	private Integer propId;
	private String budgetPeriodCode;
	
	public Integer getBudgetInfoId() {
		return budgetInfoId;
	}
	public void setBudgetInfoId(Integer budgetInfoId) {
		this.budgetInfoId = budgetInfoId;
	}
	public Integer getMgmtCompId() {
		return mgmtCompId;
	}
	public void setMgmtCompId(Integer mgmtCompId) {
		this.mgmtCompId = mgmtCompId;
	}
	public Integer getPropId() {
		return propId;
	}
	public void setPropId(Integer propId) {
		this.propId = propId;
	}
	public String getBudgetPeriodCode() {
		return budgetPeriodCode;
	}
	public void setBudgetPeriodCode(String budgetPeriodCode) {
		this.budgetPeriodCode = budgetPeriodCode;
	}
	@Override
	public String toString() {
		return "BudgetDetailsInfo [budgetInfoId=" + budgetInfoId + ", mgmtCompId=" + mgmtCompId + ", propId=" + propId
				+ ", budgetPeriodCode=" + budgetPeriodCode + "]";
	}
	
	

}
