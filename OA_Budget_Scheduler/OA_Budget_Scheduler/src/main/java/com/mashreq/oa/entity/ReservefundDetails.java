package com.mashreq.oa.entity;

import java.io.Serializable;

public class ReservefundDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private String branch;
	private String account;
	private String cifId;
	private String escapeRowPercentage;
	private String escapeRowBranchCode;
	private String escapeRowAccount;

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCifId() {
		return cifId;
	}

	public void setCifId(String cifId) {
		this.cifId = cifId;
	}

	public String getEscapeRowPercentage() {
		return escapeRowPercentage;
	}

	public void setEscapeRowPercentage(String escapeRowPercentage) {
		this.escapeRowPercentage = escapeRowPercentage;
	}

	public String getEscapeRowBranchCode() {
		return escapeRowBranchCode;
	}

	public void setEscapeRowBranchCode(String escapeRowBranchCode) {
		this.escapeRowBranchCode = escapeRowBranchCode;
	}

	public String getEscapeRowAccount() {
		return escapeRowAccount;
	}

	public void setEscapeRowAccount(String escapeRowAccount) {
		this.escapeRowAccount = escapeRowAccount;
	}

	@Override
	public String toString() {
		return "ReservefundDetails [branch=" + branch + ", account=" + account + ", cifId=" + cifId
				+ ", escapeRowPercentage=" + escapeRowPercentage + ", escapeRowBranchCode=" + escapeRowBranchCode
				+ ", escapeRowAccount=" + escapeRowAccount + "]";
	}

}
