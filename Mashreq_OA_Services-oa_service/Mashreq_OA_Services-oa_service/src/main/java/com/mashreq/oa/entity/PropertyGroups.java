package com.mashreq.oa.entity;

public class PropertyGroups {

	private Integer propId;
	private Long propertyGroupId;
	private String propertyGroupNameEn;
	private String propertyGroupNameAr;
	private String masterCommunityNameEn;
	private String masterCommunityNameAr;
	private String projectNameEn;
	private String projectNameAr;
	private String status;
	private String merchantCode;
	private Integer dataId;
	private Integer serviceId;
	
	
	public Integer getPropId() {
		return propId;
	}
	public void setPropId(Integer propId) {
		this.propId = propId;
	}
	public Long getPropertyGroupId() {
		return propertyGroupId;
	}
	public void setPropertyGroupId(Long propertyGroupId) {
		this.propertyGroupId = propertyGroupId;
	}
	public String getPropertyGroupNameEn() {
		return propertyGroupNameEn;
	}
	public void setPropertyGroupNameEn(String propertyGroupNameEn) {
		this.propertyGroupNameEn = propertyGroupNameEn;
	}
	public String getPropertyGroupNameAr() {
		return propertyGroupNameAr;
	}
	public void setPropertyGroupNameAr(String propertyGroupNameAr) {
		this.propertyGroupNameAr = propertyGroupNameAr;
	}
	public String getMasterCommunityNameEn() {
		return masterCommunityNameEn;
	}
	public void setMasterCommunityNameEn(String masterCommunityNameEn) {
		this.masterCommunityNameEn = masterCommunityNameEn;
	}
	public String getMasterCommunityNameAr() {
		return masterCommunityNameAr;
	}
	public void setMasterCommunityNameAr(String masterCommunityNameAr) {
		this.masterCommunityNameAr = masterCommunityNameAr;
	}
	public String getProjectNameEn() {
		return projectNameEn;
	}
	public void setProjectNameEn(String projectNameEn) {
		this.projectNameEn = projectNameEn;
	}
	public String getProjectNameAr() {
		return projectNameAr;
	}
	public void setProjectNameAr(String projectNameAr) {
		this.projectNameAr = projectNameAr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	
	@Override
	public String toString() {
		return "PropertyGroups [propId=" + propId + ", propertyGroupId=" + propertyGroupId + ", propertyGroupNameEn="
				+ propertyGroupNameEn + ", propertyGroupNameAr=" + propertyGroupNameAr + ", masterCommunityNameEn="
				+ masterCommunityNameEn + ", masterCommunityNameAr=" + masterCommunityNameAr + ", projectNameEn="
				+ projectNameEn + ", projectNameAr=" + projectNameAr + ", status=" + status + ", merchantCode="
				+ merchantCode + ", dataId=" + dataId + ", serviceId=" + serviceId + "]";
	}
	
	
}
