package com.mashreq.oa.service;

import java.util.List;

import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;

public interface BulkPayementsService {
	public List<PaymentData> getBulkPayementsList();
	
	public List<PaymentData> getBulkExceptions(PaymentSearchInput pymtSerachInput);

	
}
