package com.mashreq.oa.entity;

import java.io.Serializable;
import java.util.Arrays;

public class PropertyGroups implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PropertyGroupDetails[] propertyGroups;
//	private BeneficiaryListDetails[] beneficiaryList;
	private String additionalData;
	
	public PropertyGroupDetails[] getPropertyGroups() {
		return propertyGroups;
	}
	public void setPropertyGroups(PropertyGroupDetails[] propertyGroups) {
		this.propertyGroups = propertyGroups;
	}
	/*public BeneficiaryListDetails[] getBeneficiaryList() {
		return beneficiaryList;
	}
	public void setBeneficiaryList(BeneficiaryListDetails[] beneficiaryList) {
		this.beneficiaryList = beneficiaryList;
	}*/
	public String getAdditionalData() {
		return additionalData;
	}
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}
	@Override
	public String toString() {
		return "PropertyGroups [propertyGroups=" + Arrays.toString(propertyGroups) + ", additionalData="
				+ additionalData + "]";
	}
	
	
	
}
