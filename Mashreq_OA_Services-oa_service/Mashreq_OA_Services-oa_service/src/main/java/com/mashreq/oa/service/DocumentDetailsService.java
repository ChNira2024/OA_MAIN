package com.mashreq.oa.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mashreq.oa.entity.DocumentDetails;

public interface DocumentDetailsService {
	
	public List<DocumentDetails> getDocumentDetails();
	public List<DocumentDetails> getDocumentPymt(int paymentReqID);
	public void saveDocument(MultipartFile file, int paymentReqID);
	public DocumentDetails getDocumentPath(long documentId);

}
