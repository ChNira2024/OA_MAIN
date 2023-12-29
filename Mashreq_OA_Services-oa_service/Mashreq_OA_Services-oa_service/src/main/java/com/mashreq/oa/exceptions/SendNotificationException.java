package com.mashreq.oa.exceptions;

public class SendNotificationException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4145338761562678821L;
	

	public SendNotificationException(String message) {
		super(message);

	}

	public SendNotificationException(String message,Throwable cause) {
		super(message,cause);
		
	}
	

}
