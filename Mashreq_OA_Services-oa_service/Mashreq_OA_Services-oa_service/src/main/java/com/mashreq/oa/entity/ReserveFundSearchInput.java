package com.mashreq.oa.entity;

public class ReserveFundSearchInput {

	private Long mgmtCompId;
	private Long buildingId;
	public Long getMgmtCompId() {
		return mgmtCompId;
	}
	public void setMgmtCompId(Long mgmtCompId) {
		this.mgmtCompId = mgmtCompId;
	}
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	@Override
	public String toString() {
		return "ReserveFundSearchInput [mgmtCompId=" + mgmtCompId + ", buildingId=" + buildingId + "]";
	}
	public ReserveFundSearchInput(Long mgmtCompId, Long buildingId) {
		super();
		this.mgmtCompId = mgmtCompId;
		this.buildingId = buildingId;
	}
	public ReserveFundSearchInput() {

	}
	
	
	
}
