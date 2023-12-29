package com.mashreq.oa.utility;

public enum SchProcessStatus {

	QUEUED("QUEUED"),
	IN_PROGRESS("IN_PROGRESS"), 
	COMPLETED("COMPLETED"),
	FAILED("FAILED"),
	NOT_RUNNING("NOT_RUNNING"),
	RUNNING("RUNNING"),
	SOURCE_FILE_NOT_EXIST("SOURCE_FILE_NOT_EXIST"),
	FILE_COPY_SUCCESS_BUT_FAILED_UPDATE_transaction_MST_TM("FILE_COPY_SUCCESS_BUT_FAILED_UPDATE_transaction_MST_TM");
	
	private final String value;
	
	private SchProcessStatus(String value) {
		this.value=value;
	}
	public String getValue() {
		return this.value;
	}
}
