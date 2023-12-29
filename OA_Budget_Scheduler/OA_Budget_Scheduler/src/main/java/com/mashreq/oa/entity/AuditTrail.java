package com.mashreq.oa.entity;

import java.io.Serializable;

public class AuditTrail implements Serializable {
	
	private String fieldName;
	private String oldValue;
	private String newValue;
	private String updatedBy;
	private Integer id;
	
	
	
	
	public String getFieldName() {
		return fieldName;
	}




	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}




	public String getOldValue() {
		return oldValue;
	}




	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}




	public String getNewValue() {
		return newValue;
	}




	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}




	public String getUpdatedBy() {
		return updatedBy;
	}




	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}




	public Integer getId() {
		return id;
	}




	public void setId(Integer id) {
		this.id = id;
	}




	@Override
	public String toString() {
		return "AuditTrail [fieldName=" + fieldName + ", oldValue=" + oldValue + ", newValue=" + newValue
				+ ", updatedBy=" + updatedBy + ", id=" + id + "]";
	}
	
	
	

}
