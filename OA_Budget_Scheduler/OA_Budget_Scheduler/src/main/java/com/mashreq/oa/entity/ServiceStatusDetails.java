package com.mashreq.oa.entity;

import java.sql.Date;



public class ServiceStatusDetails {
		
	private Integer service_Id;
	private String service_Name;
	private String response_Code;
	private String timestamp;
	private String validation_Errors;
	public Integer getService_Id() {
		return service_Id;
	}
	public void setService_Id(Integer service_Id) {
		this.service_Id = service_Id;
	}
	public String getService_Name() {
		return service_Name;
	}
	public void setService_Name(String service_Name) {
		this.service_Name = service_Name;
	}
	public String getResponse_Code() {
		return response_Code;
	}
	public void setResponse_Code(String response_Code) {
		this.response_Code = response_Code;
	}
	
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getValidation_Errors() {
		return validation_Errors;
	}
	public void setValidation_Errors(String validation_Errors) {
		this.validation_Errors = validation_Errors;
	}
	@Override
	public String toString() {
		return "ServiceStatusDetails [service_Id=" + service_Id + ", service_Name=" + service_Name + ", response_Code="
				+ response_Code + ", timestamp=" + timestamp + ", validation_Errors=" + validation_Errors + "]";
	}
	
	
	
}
