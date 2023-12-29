package com.mashreq.oa.dao;

import java.sql.Date;
import java.util.List;
import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.ManagementCompany;

public interface AttachmentDataDao {

	public int saveAttachmentData(AttachmentData data);

	public List<AttachmentData> getAttachmentData();
	
	public List<ManagementCompany> getManagementCompany();

	public AttachmentData getMatrixRefNo(Integer pymtReqId);

	public Date getAgreementExpDate(Integer supplierId, Integer mgmtCompId);

	public void insertAgrExpDate(Integer supplierId, Integer mgmtCompId, Integer buildingId, Date agrExpDate);

	public void updateAgrExpDate(Integer supplierId, Integer mgmtCompId, Integer buildingId, Date agreementExpDate);

	public void updateTradeExpDate(Integer SupplierId, Date tradeLicenseExpDate, String autoRenewal,  String isGovernmentEntity, String isInsuranceCompany);

	public Integer getSubPayments(long matrixFileRefNo);

	public AttachmentData getExpiryDates(Integer splyer_id);

	public AttachmentData getExpiryDates( Integer splyer_id,Integer mgmt_cpny_id);
	
	public boolean checkForAgrDetails(Integer mgmtCompId, Integer supplierId, Integer buildingId);
		
	public String getStatusByPaymentreqID(Integer pymtReqId);
}
