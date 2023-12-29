package com.mashreq.oa.entity;

public class ManagementCompanies {
	private ManagementCompanyDetails[] managementCompanies;
	private String additionalData;
	
	
	public ManagementCompanyDetails[] getManagementCompanies() {
		return managementCompanies;
	}
	public void setManagementCompanies(ManagementCompanyDetails[] managementCompanies) {
		this.managementCompanies = managementCompanies;
	}
	public String getAdditionalData() {
		return additionalData;
	}
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	@Override
	public String toString() {
		return "ManagementCompanyDetails [managementCompanies=" + managementCompanies + ", additionalData=" + additionalData + " + ]";
	}
}
