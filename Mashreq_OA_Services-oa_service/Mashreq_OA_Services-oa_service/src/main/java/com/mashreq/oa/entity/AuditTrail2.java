package com.mashreq.oa.entity;

import java.sql.Timestamp;

public class AuditTrail2
{

    private String fieldName;
    private int id;
    private Timestamp updatedOn;
    private String oldValue;
    private String newValue;
    private String updatedBy;
    private String pymtReqId;

 

    public AuditTrail2(){}

    public AuditTrail2(String fieldName, int id, Timestamp updatedOn, String oldValue, String newValue,
                            String updatedBy, String pymtReqId) 
    {
        this.fieldName = fieldName;
        this.id = id;
        this.updatedOn = updatedOn;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.updatedBy = updatedBy;
        this.pymtReqId = pymtReqId;
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
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

    
	public String getPymtReqId() {
		return pymtReqId;
	}

	public void setPymtReqId(String pymtReqId) {
		this.pymtReqId = pymtReqId;
	}

	@Override
	public String toString() {
		return "AuditTrail [fieldName=" + fieldName + ", id=" + id + ", updatedOn=" + updatedOn + ", oldValue="
				+ oldValue + ", newValue=" + newValue + ", updatedBy=" + updatedBy + ", pymtReqId=" + pymtReqId + "]";
	}


    
}
