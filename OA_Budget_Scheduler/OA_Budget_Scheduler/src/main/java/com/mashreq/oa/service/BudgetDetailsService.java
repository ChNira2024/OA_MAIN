package com.mashreq.oa.service;

import java.text.ParseException;

public interface BudgetDetailsService {

	public void fetchBudgetData();

	public void getPreviousBudgetDetails();

	public void getMissingBudgetData();
	
	public void processPaymentRequest() throws ParseException;
	
    public void sendingStatusMailIndividual();
    
    public void sendingStatusMailBulk();
}
