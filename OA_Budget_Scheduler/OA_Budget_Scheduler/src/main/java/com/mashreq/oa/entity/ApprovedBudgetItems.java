package com.mashreq.oa.entity;

import java.io.Serializable;

public class ApprovedBudgetItems implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer dataId;
	private Integer mgmtCompId;
	private Integer propId;
	private String budgetYear;
	private String serviceCode;
	private double approvedAmount;
	private Integer supplierId;
	private String status;
	private String bifurcation;
	
	public Integer getDataId() {
		return dataId;
	}
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	public Integer getMgmtCompId() {
		return mgmtCompId;
	}
	public void setMgmtCompId(Integer mgmtCompId) {
		this.mgmtCompId = mgmtCompId;
	}
	public Integer getPropId() {
		return propId;
	}
	public void setPropId(Integer propId) {
		this.propId = propId;
	}
	public String getBudgetYear() {
		return budgetYear;
	}
	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(double approvedAmount) {
		this.approvedAmount = approvedAmount;
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
	public String getBifurcation() {
		return bifurcation;
	}
	public void setBifurcation(String bifurcation) {
		this.bifurcation = bifurcation;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "ApprovedBudgetItems [dataId=" + dataId + ", mgmtCompId=" + mgmtCompId + ", propId=" + propId
				+ ", budgetYear=" + budgetYear + ", serviceCode=" + serviceCode + ", approvedAmount=" + approvedAmount
				+ ", supplierId=" + supplierId + ", status=" + status + ", bifurcation=" + bifurcation + "]";
	} 
	
	
	

	
	
	
	
	

}
