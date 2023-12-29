package com.mashreq.oa.entity;


public class ReserveFundUpdateData {
	
	private Integer reserveFundId;
	
	private String accountNumber;
	private String branchCode;
	
	private String reserveAccountNumber;
	private String isActive;
	@Override
	public String toString() {
		return "ReserveFundUpdateData [reserveFundId=" + reserveFundId + ", accountNumber=" + accountNumber
				+ ", branchCode=" + branchCode + ", reserveAccountNumber=" + reserveAccountNumber + ", isActive="
				+ isActive + "]";
	}
	public Integer getReserveFundId() {
		return reserveFundId;
	}
	public void setReserveFundId(Integer reserveFundId) {
		this.reserveFundId = reserveFundId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getReserveAccountNumber() {
		return reserveAccountNumber;
	}
	public void setReserveAccountNumber(String reserveAccountNumber) {
		this.reserveAccountNumber = reserveAccountNumber;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	
		
}
