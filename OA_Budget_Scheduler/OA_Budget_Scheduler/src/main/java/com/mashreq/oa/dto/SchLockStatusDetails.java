package com.mashreq.oa.dto;

public class SchLockStatusDetails {

	private long id;
	private String lastUpdatedTime;
	
	
	
	public SchLockStatusDetails(long id, String lastUpdatedTime) {
		super();
		this.id = id;
		this.lastUpdatedTime = lastUpdatedTime;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	@Override
	public String toString() {
		return "SchLockStatusDetails [id=" + id + ", lastUpdatedTime=" + lastUpdatedTime + "]";
	}
	
	
}
