package com.mashreq.oa.entity;

public class ApiCallStatus {
	
	private String percentageValue;
	private String apiStatus;
	public String getPercentageValue() {
		return percentageValue;
	}
	public void setPercentageValue(String percentageValue) {
		this.percentageValue = percentageValue;
	}
	public String getApiStatus() {
		return apiStatus;
	}
	public void setApiStatus(String apiStatus) {
		this.apiStatus = apiStatus;
	}
	@Override
	public String toString() {
		return "ApiCallStatus [percentageValue=" + percentageValue + ", apiStatus=" + apiStatus + "]";
	}
}
