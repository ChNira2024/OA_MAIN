package com.mashreq.oa.entity;

import java.sql.Date;

public class TransactionalReportInput {
	
	private Date transactionFrom;
	private Date transactionTo;
	
	
	public Date getTransactionFrom() {
		return transactionFrom;
	}

	public void setTransactionFrom(Date transactionFrom) {
		this.transactionFrom = transactionFrom;
	}


	public Date getTransactionTo() {
		return transactionTo;
	}

	public void setTransactionTo(Date transactionTo) {
		this.transactionTo = transactionTo;
	}


	@Override
	public String toString() {
		return "TransactionalReport [transactionFrom=" + transactionFrom + ", transactionTo=" + transactionTo + "]";
	}
	
	

}
