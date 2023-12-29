package com.mashreq.oa.service;

import java.util.List;

import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.CompletedRequestInput;
import com.mashreq.oa.entity.PaymentData;

public interface CompletedRequestService {

	public List<PaymentData> getCompletedRequests();

	public List<AttachmentData>  getDetails(Integer pymtReqId);

	public List<PaymentData> completedRequests(CompletedRequestInput compInput);

	public List<PaymentData> generateExcel(CompletedRequestInput compInput);

	public List<PaymentData> generatedPdf(CompletedRequestInput compInput);
	
}
