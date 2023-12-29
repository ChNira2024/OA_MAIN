package com.mashreq.oa.entity;

import java.io.Serializable;

public class ManagementCompanyDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5210519140522847772L;

	private Integer id;
	private Language name;
	private String contactNumber;
	private String email;
	private String trn;
	private String address;
	private String merchantCode;
	private Integer dataId;
	private Integer serviceId;
	
	//DB Related
	
	private Integer mgmtCompId;
	private Integer mcId;
	
	
	
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	

	public Language getName() {
		return name;
	}

	public void setName(Language name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTrn() {
		return trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
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

	public Integer getMcId() {
		return mcId;
	}

	public void setMcId(Integer mcId) {
		this.mcId = mcId;
	}

	@Override
	public String toString() {
		return "ManagementCompanyDetails [id=" + id + ", name=" + name + ", contactNumber=" + contactNumber + ", email="
				+ email + ", trn=" + trn + ", address=" + address + ", merchantCode=" + merchantCode + ", dataId="
				+ dataId + ", serviceId=" + serviceId + ", mgmtCompId=" + mgmtCompId + ", mcId=" + mcId + "]";
	}



		
	
}
