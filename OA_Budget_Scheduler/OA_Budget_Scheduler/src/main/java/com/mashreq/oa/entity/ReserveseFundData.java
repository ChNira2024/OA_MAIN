package com.mashreq.oa.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReserveseFundData {

	public int reserveFundId;
	public String accountNumber;
	public String cifNumber;
	public int mgmntCompId;
	public int buildingId;
	public String reserveAccountNumber;
	public String branchCode;
	public String reserveFundPercentage;
	public Date lastCalculatedOn;
	public String isActive;

	public int getReserveFundId() {
		return reserveFundId;
	}

	public void setReserveFundId(int reserveFundId) {
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

	public int getMgmntCompId() {
		return mgmntCompId;
	}

	public void setMgmntCompId(int mgmntCompId) {
		this.mgmntCompId = mgmntCompId;
	}

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public String getReserveAccountNumber() {
		return reserveAccountNumber;
	}

	public void setReserveAccountNumber(String reserveAccountNumber) {
		this.reserveAccountNumber = reserveAccountNumber;
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

	public Date getLastCalculatedOn() {
		return lastCalculatedOn;
	}

	public void setLastCalculatedOn(Date lastCalculatedOn) {
		this.lastCalculatedOn = lastCalculatedOn;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public ReserveseFundData() {
		super();
	}

	public ReserveseFundData(int reserveFundId, String accountNumber, String cifNumber, int mgmntCompId, int buildingId,
			String reserveAccountNumber, String branchCode, String reserveFundPercentage, Date lastCalculatedOn) {
		super();
		this.reserveFundId = reserveFundId;
		this.accountNumber = accountNumber;
		this.cifNumber = cifNumber;
		this.mgmntCompId = mgmntCompId;
		this.buildingId = buildingId;
		this.reserveAccountNumber = reserveAccountNumber;
		this.branchCode = branchCode;
		this.reserveFundPercentage = reserveFundPercentage;
		this.lastCalculatedOn = lastCalculatedOn;
	}

}
