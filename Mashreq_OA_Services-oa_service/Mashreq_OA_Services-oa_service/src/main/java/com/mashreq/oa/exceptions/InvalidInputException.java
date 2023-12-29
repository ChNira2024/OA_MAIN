package com.mashreq.oa.exceptions;

public class InvalidInputException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2521797873928239322L;

	public InvalidInputException(String message, Throwable cause) {
		super(message, cause);

	}

	public InvalidInputException(String message) {
		super(message);
		
	}	

}
