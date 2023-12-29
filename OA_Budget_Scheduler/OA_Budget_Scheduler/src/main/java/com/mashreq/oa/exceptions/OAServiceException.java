package com.mashreq.oa.exceptions;

public class OAServiceException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2521797873928239322L;

	public OAServiceException(String message, Throwable cause) {
		super(message, cause);

	}
	public OAServiceException() {

		
	}	
	public OAServiceException(String message) {
		super(message);
	}	

}
