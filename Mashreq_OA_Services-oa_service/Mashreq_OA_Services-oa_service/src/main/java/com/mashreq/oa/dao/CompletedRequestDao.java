package com.mashreq.oa.dao;

import java.util.ArrayList;
import java.util.List;

import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.PaymentData;

public interface CompletedRequestDao {

	public List<PaymentData> getCompletedRequests();

	public List<AttachmentData>   getDetails(Integer pymtReqId);
	
	public List<PaymentData> completedRequests(String reportQueryString, ArrayList param);

}
