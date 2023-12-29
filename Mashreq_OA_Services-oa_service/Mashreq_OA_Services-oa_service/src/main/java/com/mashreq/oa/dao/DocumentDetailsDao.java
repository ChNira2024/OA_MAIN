package com.mashreq.oa.dao;

import java.util.List;

import com.mashreq.oa.entity.DocumentDetails;


public interface DocumentDetailsDao {
	
	public List<DocumentDetails> getDocumentDetails();
	public List<DocumentDetails> getDocumentPymt(int paymentReqID);
	public void saveDocument(String fileName, String extension, String path, int paymentReqID);
	public DocumentDetails getDocumentPath(long documentId);
	public String getStatusByPaymentreqID(int paymentReqID);

}
