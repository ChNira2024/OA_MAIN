package com.mashreq.oa.entity;

public class ResponseEntityBO {
	
	private String attachments;
	private String bccAddress;
	private String ccAddress;
	private String emailBody;
	private String fromAddress;
	private String subject;
	private String toAddress;
	public String getAttachments() {
		return attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
	public String getBccAddress() {
		return bccAddress;
	}
	public void setBccAddress(String bccAddress) {
		this.bccAddress = bccAddress;
	}
	public String getCcAddress() {
		return ccAddress;
	}
	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}
	public String getEmailBody() {
		return emailBody;
	}
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	
	@Override
	public String toString() {
		return "ResponseEntityBO [attachments=" + attachments + ", bccAddress=" + bccAddress + ", ccAddress="
				+ ccAddress + ", emailBody=" + emailBody + ", fromAddress=" + fromAddress + ", subject=" + subject
				+ ", toAddress=" + toAddress + "]";
	}
	
	

}
