package com.mashreq.oa.entity;

public class PaymentRequestStatus {
	private String status;
	private String remarks;
	
	public PaymentRequestStatus(){
		super();
	}
	
	public PaymentRequestStatus(String status, String remarks){
		this.status=status;
		this.remarks=remarks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	

}
