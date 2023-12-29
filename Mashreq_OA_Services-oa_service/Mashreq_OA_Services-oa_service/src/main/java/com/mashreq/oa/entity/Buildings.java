package com.mashreq.oa.entity;

public class Buildings {
	
	private Integer buildingId;
	private String buildingName;
	private String isActive;
	
	public Integer getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "Buildings [buildingId=" + buildingId + ", buildingName=" + buildingName + ", isActive=" + isActive
				+ "]";
	}

	
	

}
