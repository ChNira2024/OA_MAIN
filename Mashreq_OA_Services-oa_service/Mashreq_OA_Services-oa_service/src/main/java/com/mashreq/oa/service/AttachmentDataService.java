package com.mashreq.oa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.ManagementCompany;

public interface AttachmentDataService {

	public int saveAttachmentData(AttachmentData data);

	public List<ManagementCompany> getManagementCompany();

	public AttachmentData getMatrixRefNo(Integer pymtReqId);
	
	public List<AttachmentData> getAttachmentData();
	
	public AttachmentData getExpiryDates( Integer splyer_id,Integer mgmt_cpny_id) ;

}
