package com.mashreq.oa.service;

import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.SearchResults;


public interface TransactionSearchCriteriaService {

	public SearchResults gettransactionList(PaymentData pd);
	
	
}
