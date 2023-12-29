package com.mashreq.oa.entity;

public class CompletedRequestInput {
	
	private Integer mgmtCompId;
	private Integer buildingId;
	private Integer supplierId;
	private String status;
	private Integer budgetYear;
	
	
	
	public Integer getBudgetYear() {
		return budgetYear;
	}
	public void setBudgetYear(Integer budgetYear) {
		this.budgetYear = budgetYear;
	}
	public Integer getMgmtCompId() {
		return mgmtCompId;
	}
	public void setMgmtCompId(Integer mgmtCompId) {
		this.mgmtCompId = mgmtCompId;
	}
	public Integer getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "CompletedRequestInput [mgmtCompId=" + mgmtCompId + ", buildingId=" + buildingId + ", supplierId="
				+ supplierId + ", status=" + status + ", budgetYear=" + budgetYear + "]";
	}
	
	
	

}