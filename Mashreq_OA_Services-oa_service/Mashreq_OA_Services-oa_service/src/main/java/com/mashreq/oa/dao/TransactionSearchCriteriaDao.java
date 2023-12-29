package com.mashreq.oa.dao;

import java.util.ArrayList;
import java.util.List;

import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.TransactionSearchCriteriaTable2;


public interface TransactionSearchCriteriaDao {

	public List<PaymentData> gettransactionList(String searchQueryString, ArrayList param);

	int countList(String searchQueryString, ArrayList param);
	
	
	
}
