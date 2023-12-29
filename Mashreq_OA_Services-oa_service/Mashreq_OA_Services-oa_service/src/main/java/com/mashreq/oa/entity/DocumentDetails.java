package com.mashreq.oa.entity;

import java.sql.Date;

public class DocumentDetails {
	private long documentId;
	private String documentName;
	private String documentType;
	private String expiresOn;
	private String documentPath;
	private NameValueDoc nameValue;
	
	public long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	
	public String getExpiresOn() {
		return expiresOn;
	}
	public void setExpiresOn(String expiresOn) {
		this.expiresOn = expiresOn;
	}
	public String getDocumentPath() {
		return documentPath;
	}
	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}
	
	
	public NameValueDoc getNameValue() {
		return nameValue;
	}
	public void setNameValue(NameValueDoc nameValue) {
		this.nameValue = nameValue;
	}
	@Override
	public String toString() {
		return "DocumentDetails [documentId=" + documentId + ", documentName=" + documentName + ", documentType="
				+ documentType + ", expiresOn=" + expiresOn + ", documentPath=" + documentPath + ", nameValue="
				+ nameValue + "]";
	}
	
	
	
	
}
