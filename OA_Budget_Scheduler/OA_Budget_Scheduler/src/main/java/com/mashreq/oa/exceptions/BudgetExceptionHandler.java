package com.mashreq.oa.exceptions;

import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class BudgetExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BudgetExceptionHandler.class);
	
	@ExceptionHandler( OAServiceException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorMessage handleInvalidInputException(OAServiceException ex, WebRequest request) {
		 ErrorMessage message = new ErrorMessage(
			        HttpStatus.BAD_REQUEST.value(),
			        new Date(),
			        ex.getMessage(),
			        request.getDescription(false));
		LOGGER.error("OAServiceException: ",  ex.getMessage());
		return message;

	}
	
	@ExceptionHandler(BudgetSchedulerException.class )
	 @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage handleCaseCreationException(BudgetSchedulerException ex, WebRequest request) {
		 ErrorMessage message = new ErrorMessage(
			        HttpStatus.INTERNAL_SERVER_ERROR.value(),
			        new Date(),
			        ex.getMessage(),
			        request.getDescription(false));
		LOGGER.error("SendNotificationException: ", message);
		return message;

	}
	
	@ExceptionHandler( SQLException.class )
	 @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage handleSQLException(SQLException ex, WebRequest request) {
		 ErrorMessage message = new ErrorMessage(
			        HttpStatus.INTERNAL_SERVER_ERROR.value(),
			        new Date(),
			        ex.getMessage(),
			        request.getDescription(false));
		LOGGER.error("SQLException: ", message);
		return message;

	}

	/*@ExceptionHandler(Exception.class )
	 @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage handleException(Exception ex, WebRequest request) {

		ErrorMessage message = new ErrorMessage(
		        HttpStatus.INTERNAL_SERVER_ERROR.value(),
		        new Date(),
		        ex.getMessage(),
		        request.getDescription(false));
	LOGGER.error("Exception: ", message);
	return message;

	}*/

}
