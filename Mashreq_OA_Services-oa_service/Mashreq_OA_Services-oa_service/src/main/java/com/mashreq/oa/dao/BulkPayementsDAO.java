package com.mashreq.oa.dao;

import java.util.List;

import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;

public interface BulkPayementsDAO {
	public List<PaymentData> getBulkPayementsList();

	public List<PaymentData> getBulkExceptions(PaymentSearchInput pymtSerachInput);
}
