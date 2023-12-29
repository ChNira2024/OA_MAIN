package com.mashreq.oa.entity;

import java.sql.Timestamp;

public class AuditTrailLog 
{
	private Integer budgetItemId;
	private String pymtReqId;
	private String serviceCode;
	private String reversalAmount;
	private String updatedBy;
	private Timestamp updatedOn;
	private String comment;
	public Integer getBudgetItemId() {
		return budgetItemId;
	}
	public void setBudgetItemId(Integer budgetItemId) {
		this.budgetItemId = budgetItemId;
	}
	public String getPymtReqId() {
		return pymtReqId;
	}
	public void setPymtReqId(String pymtReqId) {
		this.pymtReqId = pymtReqId;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getReversalAmount() {
		return reversalAmount;
	}
	public void setReversalAmount(String reversalAmount) {
		this.reversalAmount = reversalAmount;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Timestamp getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "AuditTrailLog [budgetItemId=" + budgetItemId + ", pymtReqId=" + pymtReqId + ", serviceCode="
				+ serviceCode + ", reversalAmount=" + reversalAmount + ", updatedBy=" + updatedBy + ", updatedOn="
				+ updatedOn + ", comment=" + comment + "]";
	}
	
	
}
