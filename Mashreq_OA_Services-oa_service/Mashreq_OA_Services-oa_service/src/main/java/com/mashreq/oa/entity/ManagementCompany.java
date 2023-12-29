package com.mashreq.oa.entity;

public class ManagementCompany {
	
	private Integer mgmtCompId;
	private String mcNameEn;
	
	public Integer getMgmtCompId() {
		return mgmtCompId;
	}
	public void setMgmtCompId(Integer mgmtCompId) {
		this.mgmtCompId = mgmtCompId;
	}
	public String getMcNameEn() {
		return mcNameEn;
	}
	public void setMcNameEn(String mcNameEn) {
		this.mcNameEn = mcNameEn;
	}
	
	@Override
	public String toString() {
		return "ManagementCompany [mgmtCompId=" + mgmtCompId + ", mcNameEn=" + mcNameEn + "]";
	}
	
	
	
	
	
	
	
	
	
}
