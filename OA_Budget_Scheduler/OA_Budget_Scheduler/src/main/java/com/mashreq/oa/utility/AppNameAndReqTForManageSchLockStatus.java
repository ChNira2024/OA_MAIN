package com.mashreq.oa.utility;

public enum AppNameAndReqTForManageSchLockStatus {
	OABUDGETSCHEDULER("OABUDGETSCHEDULER"),
	OA_UPDATE_BUDGET_DETAILS_SERVICE("OA_Update_Budget_Details_Service"),
	OA_PROCESS_PAYMENT_REQUEST_SERVICE("OA_Process_Payment_Request_Service"),
	OA_SENDING_STATUS_MAIL_SERVICE("OA_Sending_Status_Mail_Service");
	
	
	
	private final String value;
	
	private AppNameAndReqTForManageSchLockStatus(String value) {
		this.value=value;
	}
	public String getValue() {
		return this.value;
	}		
}
