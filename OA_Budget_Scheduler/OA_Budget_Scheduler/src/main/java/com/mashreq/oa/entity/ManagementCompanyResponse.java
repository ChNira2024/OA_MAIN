package com.mashreq.oa.entity;

import java.io.Serializable;
import java.util.Arrays;

public class ManagementCompanyResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ManagementCompanies response;
	private String responseCode;
	private String timeStamp;
	private Object[] validationErrorsList;
	
	
	
	
	public Object[] getValidationErrorsList() {
		return validationErrorsList;
	}
	public void setValidationErrorsList(Object[] validationErrorsList) {
		this.validationErrorsList = validationErrorsList;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ManagementCompanies getResponse() {
		return response;
	}
	public void setResponse(ManagementCompanies response) {
		this.response = response;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	@Override
	public String toString() {
		return "ManagementCompanyResponse [response=" + response + ", responseCode=" + responseCode + ", timeStamp="
				+ timeStamp + ", validationErrorsList=" + Arrays.toString(validationErrorsList) + "]";
	}
	
	
	
}
