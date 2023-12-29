package com.mashreq.oa.entity;

import java.io.Serializable;
import java.util.Arrays;

public class PropertyGroupDetails implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer propId;
	private Integer propertyGroupId;
	private Language masterCommunityName;
	private Language projectName;
	private Language propertyGroupName;
	private String Status;
	private String merchantCode;
	private BeneficiaryListDetails[] beneficiaryList;
	private Integer dataId;
	private Integer serviceId;
	private Integer buildingId;
	private ManagementCompany managementComapny;
	private Integer mgmtCompId;
	
	
	
	
	public ManagementCompany getManagementComapny() {
		return managementComapny;
	}
	public void setManagementComapny(ManagementCompany managementComapny) {
		this.managementComapny = managementComapny;
	}
	public Integer getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	
	
	
	public Integer getPropId() {
		return propId;
	}
	public void setPropId(Integer propId) {
		this.propId = propId;
	}
	public Integer getPropertyGroupId() {
		return propertyGroupId;
	}
	public void setPropertyGroupId(Integer propertyGroupId) {
		this.propertyGroupId = propertyGroupId;
	}
	public Language getMasterCommunityName() {
		return masterCommunityName;
	}
	public void setMasterCommunityName(Language masterCommunityName) {
		this.masterCommunityName = masterCommunityName;
	}
	public Language getProjectName() {
		return projectName;
	}
	public void setProjectName(Language projectName) {
		this.projectName = projectName;
	}
	public Language getPropertyGroupName() {
		return propertyGroupName;
	}
	public void setPropertyGroupName(Language propertyGroupName) {
		this.propertyGroupName = propertyGroupName;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
	public BeneficiaryListDetails[] getBeneficiaryList() {
		return beneficiaryList;
	}
	public void setBeneficiaryList(BeneficiaryListDetails[] beneficiaryList) {
		this.beneficiaryList = beneficiaryList;
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
	@Override
	public String toString() {
		return "PropertyGroupDetails [propId=" + propId + ", propertyGroupId=" + propertyGroupId
				+ ", masterCommunityName=" + masterCommunityName + ", projectName=" + projectName
				+ ", propertyGroupName=" + propertyGroupName + ", Status=" + Status + ", merchantCode=" + merchantCode
				+ ", beneficiaryList=" + Arrays.toString(beneficiaryList) + ", dataId=" + dataId + ", serviceId="
				+ serviceId + ", buildingId=" + buildingId + ", managementComapny=" + managementComapny
				+ ", mgmtCompId=" + mgmtCompId + "]";
	}
	
	
	
	
}
