package com.mashreq.oa.entity;

public class TransactionalReportOutput {

	private String totalTransaction;
	private String inProgress;
	private String approved;
	private String exception;
	private String rejected;
	private String userName;
	
	
	public String getTotalTransaction() {
		return totalTransaction;
	}

	public void setTotalTransaction(String totalTransaction) {
		this.totalTransaction = totalTransaction;
	}

	public String getInProgress() {
		return inProgress;
	}

	public void setInProgress(String inProgress) {
		this.inProgress = inProgress;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getRejected() {
		return rejected;
	}

	public void setRejected(String rejected) {
		this.rejected = rejected;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	@Override
	public String toString() {
		return "TransactionalReportOutput [totalTransaction=" + totalTransaction + ", inProgress=" + inProgress
				+ ", approved=" + approved + ", exception=" + exception + ", rejected=" + rejected + ", userName="
				+ userName + "]";
	}
	
}
