package com.mashreq.oa.entity;

public class MappingData {
	
	private Integer buildingId;
	private Integer propId;
	
	
	public Integer getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	public Integer getPropId() {
		return propId;
	}
	public void setPropId(Integer propId) {
		this.propId = propId;
	}
	
	
	@Override
	public String toString() {
		return "MappingData [buildingId=" + buildingId + ", propId=" + propId + "]";
	}
	
	

}
