package com.mashreq.oa.entity;

public class NotificationDetails {
	
	private int budgetInfoId;
	private Integer mollakPropGrpId;
	private Integer mollakMcId;
	private String periodCode;
	private String currentDate;
	private String Status;
	private int count;
	private String source;
	private Integer pymtReqId;
	private Integer retryCount;
	// add count;
	
	
	
	public Integer getMollakPropGrpId() {
		return mollakPropGrpId;
	}
	public Integer getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	public void setMollakPropGrpId(Integer mollakPropGrpId) {
		this.mollakPropGrpId = mollakPropGrpId;
	}
	public Integer getMollakMcId() {
		return mollakMcId;
	}
	public void setMollakMcId(Integer mollakMcId) {
		this.mollakMcId = mollakMcId;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getBudgetInfoId() {
		return budgetInfoId;
	}
	public void setBudgetInfoId(int budgetInfoId) {
		this.budgetInfoId = budgetInfoId;
	}
	

	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	public Integer getPymtReqId() {
		return pymtReqId;
	}
	public void setPymtReqId(Integer pymtReqId) {
		this.pymtReqId = pymtReqId;
	}
	@Override
	public String toString() {
		return "NotificationDetails [budgetInfoId=" + budgetInfoId + ", mollakPropGrpId=" + mollakPropGrpId
				+ ", mollakMcId=" + mollakMcId + ", periodCode=" + periodCode + ", currentDate=" + currentDate
				+ ", Status=" + Status + ", count=" + count + ", source=" + source + ", pymtReqId=" + pymtReqId
				+ ", retryCount=" + retryCount + "]";
	}
	
	
}
