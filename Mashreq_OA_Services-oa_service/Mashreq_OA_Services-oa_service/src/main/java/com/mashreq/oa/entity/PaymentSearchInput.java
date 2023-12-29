package com.mashreq.oa.entity;

public class PaymentSearchInput {
	
	private Integer mgmtCompId;
	private Integer buildingId;
	
	
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
	
	
	@Override
	public String toString() {
		return "PaymentSearchInput [mgmtCompId=" + mgmtCompId + ", buildingId=" + buildingId + "]";
	}
	
	

}
