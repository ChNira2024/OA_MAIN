package com.mashreq.oa.entity;

public class AgreementDetailsMapping {

	private Integer mgmtCompId;
	private Integer supplierid;
	private Integer buildingId;
	
	public Integer getMgmtCompId() {
		return mgmtCompId;
	}
	public void setMgmtCompId(Integer mgmtCompId) {
		this.mgmtCompId = mgmtCompId;
	}
	public Integer getSupplierid() {
		return supplierid;
	}
	public void setSupplierid(Integer supplierid) {
		this.supplierid = supplierid;
	}

	public Integer getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	@Override
	public String toString() {
		return "AgreementDetailsMapping [mgmtCompId=" + mgmtCompId + ", supplierid=" + supplierid + ", buildingId="
				+ buildingId + "]";
	}
	
	
	
	
}
