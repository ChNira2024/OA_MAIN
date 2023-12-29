package com.mashreq.oa.entity;

public class AmountDetails {
	private Double consumedAmount;
	private Double balanceAmount;
	public Double getConsumedAmount() {
		return consumedAmount;
	}
	public void setConsumedAmount(Double consumedAmount) {
		this.consumedAmount = consumedAmount;
	}
	public Double getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	@Override
	public String toString() {
		return "AmountDetails [consumedAmount=" + consumedAmount + ", balanceAmount=" + balanceAmount + "]";
	}

}
