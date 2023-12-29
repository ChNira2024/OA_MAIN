package com.mashreq.oa.entity;

import java.util.List;

public class SearchResults {
	private int count;
	public List<PaymentData> paymentData;
	
	public SearchResults() {
		// TODO Auto-generated constructor stub
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<PaymentData> getPaymentData() {
		return paymentData;
	}

	public void setPaymentData(List<PaymentData> paymentData) {
		this.paymentData = paymentData;
	}

	@Override
	public String toString() {
		return "SearchResults [count=" + count + ", paymentData=" + paymentData + "]";
	}
	
	

	

}
