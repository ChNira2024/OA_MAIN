package com.mashreq.oa.entity;

import java.io.Serializable;
import java.util.Arrays;

public class BudgetDetails implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 *  "propertyGroupId": 10004,
        "propertyGroupName": {
            "englishName": "Test Property",
            "??????? ??????": "arabicName "
        },
        "masterCommunityName": {
            "englishName": "Test Commnunities",
            "???????? ?????": "arabicName "
        },
        "managementCompanyId": 1000,
        "managementCompanyName": {
            "englishName": "Test Management Services",
            "???????? ????? ?????": "arabicName "
        },
        "propertyManagerEmail": "email@domain.com",
        "serviceChargeGroups":
	 */
	private Integer propertyGroupId;
	private Language propertyGroupName;
	private Language masterCommunityName;
	private Integer managementCompanyId;
	private Language managementCompanyName;
	private String propertyManagerEmail;
	private String additionalData;
	private ServiceChargeGroupDetails[] serviceChargeGroups;
	
	public Integer getPropertyGroupId() {
		return propertyGroupId;
	}
	public void setPropertyGroupId(Integer propertyGroupId) {
		this.propertyGroupId = propertyGroupId;
	}
	public Language getPropertyGroupName() {
		return propertyGroupName;
	}
	public void setPropertyGroupName(Language propertyGroupName) {
		this.propertyGroupName = propertyGroupName;
	}
	public Language getMasterCommunityName() {
		return masterCommunityName;
	}
	public void setMasterCommunityName(Language masterCommunityName) {
		this.masterCommunityName = masterCommunityName;
	}
	public Integer getManagementCompanyId() {
		return managementCompanyId;
	}
	public void setManagementCompanyId(Integer managementCompanyId) {
		this.managementCompanyId = managementCompanyId;
	}
	public Language getManagementCompanyName() {
		return managementCompanyName;
	}
	public void setManagementCompanyName(Language managementCompanyName) {
		this.managementCompanyName = managementCompanyName;
	}
	public String getPropertyManagerEmail() {
		return propertyManagerEmail;
	}
	public void setPropertyManagerEmail(String propertyManagerEmail) {
		this.propertyManagerEmail = propertyManagerEmail;
	}
	public String getAdditionalData() {
		return additionalData;
	}
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}
	public ServiceChargeGroupDetails[] getServiceChargeGroups() {
		return serviceChargeGroups;
	}
	public void setServiceChargeGroups(ServiceChargeGroupDetails[] serviceChargeGroups) {
		this.serviceChargeGroups = serviceChargeGroups;
	}
	
	
	@Override
	public String toString() {
		return "BudgetDetails [propertyGroupId=" + propertyGroupId + ", propertyGroupName=" + propertyGroupName
				+ ", masterCommunityName=" + masterCommunityName + ", managementCompanyId=" + managementCompanyId
				+ ", managementCompanyName=" + managementCompanyName + ", propertyManagerEmail=" + propertyManagerEmail
				+ ", additionalData=" + additionalData + ", serviceChargeGroups=" + Arrays.toString(serviceChargeGroups)
				+ "]";
	}
}
