package com.mashreq.oa.entity;

public class PropertyGroupsResponse {
	
	private PropertyGroups response;
	private String responseCode;

	private String timeStamp;
	private Object[] validationErrorsList;
	
	
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Object[] getValidationErrorsList() {
		return validationErrorsList;
	}
	public void setValidationErrorsList(Object[] validationErrorsList) {
		this.validationErrorsList = validationErrorsList;
	}
	public PropertyGroups getResponse() {
		return response;
	}
	public void setResponse(PropertyGroups response) {
		this.response = response;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
}
