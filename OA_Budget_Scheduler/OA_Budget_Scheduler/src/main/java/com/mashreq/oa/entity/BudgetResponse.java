package com.mashreq.oa.entity;

import java.io.Serializable;
import java.util.Arrays;

public class BudgetResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 *  "timeStamp": "2019-07-15T13:19:46.3433716+04:00",
    "responseCode": 200,
    "validationErrorsList": [],
    "response"
	 */
	private String timeStamp;
	private String validationErrorsList[];
	private String responseCode;
	private BudgetDetails response;
	
	
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String[] getValidationErrorsList() {
		return validationErrorsList;
	}

	public void setValidationErrorsList(String[] validationErrorsList) {
		this.validationErrorsList = validationErrorsList;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	
	public BudgetDetails getResponse() {
		return response;
	}

	public void setResponse(BudgetDetails response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "BudgetResponse [timeStamp=" + timeStamp + ", validationErrorsList="
				+ Arrays.toString(validationErrorsList) + ", responseCode=" + responseCode + ", response=" + response
				+ "]";
	}
	
}
