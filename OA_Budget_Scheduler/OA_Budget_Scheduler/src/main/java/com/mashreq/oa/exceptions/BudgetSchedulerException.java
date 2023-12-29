package com.mashreq.oa.exceptions;

public class BudgetSchedulerException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4145338761562678821L;
	

	public BudgetSchedulerException(String message) {
		super(message);

	}

	public BudgetSchedulerException(String message,Throwable cause) {
		super(message,cause);
		
	}
	

}
