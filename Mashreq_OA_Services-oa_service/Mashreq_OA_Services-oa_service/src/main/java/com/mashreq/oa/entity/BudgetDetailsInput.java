package com.mashreq.oa.entity;

public class BudgetDetailsInput {
	
	private Integer buildingId;
	private Integer mgmtCompId;
	private Integer budgetYear;
	private Integer propId;
	
	
	
	public Integer getPropId() {
		return propId;
	}
	public void setPropId(Integer propId) {
		this.propId = propId;
	}
	public Integer getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	public Integer getMgmtCompId() {
		return mgmtCompId;
	}
	public void setMgmtCompId(Integer mgmtCompId) {
		this.mgmtCompId = mgmtCompId;
	}
	
	public Integer getBudgetYear() {
		return budgetYear;
	}
	public void setBudgetYear(Integer budgetYear) {
		this.budgetYear = budgetYear;
	}
	
	@Override
	public String toString() {
		return "BudgetDetailsInput [buildingId=" + buildingId + ", mgmtCompId=" + mgmtCompId + ", budgetYear="
				+ budgetYear + ", propId=" + propId + "]";
	}
	
	
	
	
	
	

}
