package com.mashreq.oa.entity;


public class Reversal 
{
	private String paymentId;
	private Double invoiceAmount;
	private String userName;
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public Double getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "Reversal [paymentId=" + paymentId + ", invoiceAmount=" + invoiceAmount + ", userName=" + userName + "]";
	}
	
	
}
