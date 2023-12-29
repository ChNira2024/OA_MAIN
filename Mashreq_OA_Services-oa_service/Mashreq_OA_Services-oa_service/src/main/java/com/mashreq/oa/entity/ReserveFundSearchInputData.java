package com.mashreq.oa.entity;

public class ReserveFundSearchInputData {

	private Integer reserveFundId;;
	private String accountNumber;
	private String cifNumber;
	private String reserveAccountNumber;
	private String isActive;
	private String mcNameEn;
	private String buildingName;
	private String branchCode;
	private String reserveFundPercentage;
	private String lastCalculatedOn;

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

	public String getCifNumber() {
		return cifNumber;
	}

	public void setCifNumber(String cifNumber) {
		this.cifNumber = cifNumber;
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

	public String getMcNameEn() {
		return mcNameEn;
	}

	public void setMcNameEn(String mcNameEn) {
		this.mcNameEn = mcNameEn;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getReserveFundPercentage() {
		return reserveFundPercentage;
	}

	public void setReserveFundPercentage(String reserveFundPercentage) {
		this.reserveFundPercentage = reserveFundPercentage;
	}

	public String getLastCalculatedOn() {
		return lastCalculatedOn;
	}

	public void setLastCalculatedOn(String lastCalculatedOn) {
		this.lastCalculatedOn = lastCalculatedOn;
	}

	@Override
	public String toString() {
		return "ReserveFundSearchInputData [reserveFundId=" + reserveFundId + ", accountNumber=" + accountNumber
				+ ", cifNumber=" + cifNumber + ", reserveAccountNumber=" + reserveAccountNumber + ", isActive="
				+ isActive + ", mcNameEn=" + mcNameEn + ", buildingName=" + buildingName + ", branchCode=" + branchCode
				+ ", reserveFundPercentage=" + reserveFundPercentage + ", lastCalculatedOn=" + lastCalculatedOn + "]";
	}

}
