package com.mashreq.oa.entity;

public class ReserveFundUpdateBoForIslamicAccount {
	private String escrowtrn;
	private String branch;
	private String account;
	private String custno;
	private String escapeRowPercentage;
	private String escapeRowBranchCode;
	private String escapeRowAccount;

	public String getEscrowtrn() {
		return escrowtrn;
	}

	public void setEscrowtrn(String escrowtrn) {
		this.escrowtrn = escrowtrn;
	}

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

	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
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
		return "ReserveFundUpdateBoForIslamicAccount [escrowtrn=" + escrowtrn + ", branch=" + branch + ", account="
				+ account + ", custno=" + custno + ", escapeRowPercentage=" + escapeRowPercentage
				+ ", escapeRowBranchCode=" + escapeRowBranchCode + ", escapeRowAccount=" + escapeRowAccount + "]";
	}

}
