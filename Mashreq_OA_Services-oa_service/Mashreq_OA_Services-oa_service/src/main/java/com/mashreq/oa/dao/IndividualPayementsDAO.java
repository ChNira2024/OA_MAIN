package com.mashreq.oa.dao;

import java.util.List;

import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.AuditTrail;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;

public interface IndividualPayementsDAO {
	public List<PaymentData> getIndividualPayementsList();

	public List<AttachmentData> getDetails(Integer pymtReqId);

	public void updateAttachment(AttachmentData updateData);

	public Integer getAttachmentId(Integer pymtReqId);

	public String getUserName(Integer pymtReqId);

	public void saveAuditDetails(AuditTrail audit);
	
	public List<PaymentData> getIndividualExceptions(PaymentSearchInput pymtSerachInput);
	
	public String getStatusByPaymentreqID(Integer pymtReqId);
}
