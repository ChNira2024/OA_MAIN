package com.mashreq.oa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;

public interface IndividualPayementsService {
	
	public List<PaymentData> getIndividualPayementsList();

	public List<AttachmentData> getDetails(Integer pymtReqId);

	public void updateAttachment(AttachmentData updateData);
	
	public List<PaymentData> getIndividualExceptions(PaymentSearchInput pymtSerachInput);

}
