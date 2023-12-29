package com.mashreq.oa.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mashreq.oa.dao.AttachmentDataDao;
import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.ManagementCompany;

import ch.qos.logback.classic.Logger;
 
@Service
public class AttachmentDataServiceImpl implements AttachmentDataService {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(AttachmentDataServiceImpl.class);

	@Autowired
	private AttachmentDataDao attachmentDao;
	
	@Autowired
	public HttpSession session;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveAttachmentData(AttachmentData data) {
		
		String status = attachmentDao.getStatusByPaymentreqID(data.getPymtReqId());
		logger.info("Status for sec in AttachmentService.."+status);
		
		if(!"EXCEPTION".equals(status) && !"PENDING".equals(status)){
			return 0;
		}
		
		try {
			logger.info("entered PostService");
			
			logger.info("Management Company ID: "+data.getMgmtCompId());
			logger.info("Supplier ID: "+data.getSupplierId());
			logger.info("Trade Date: "+data.getTradeLicenseExpDate());
			logger.info("Agreement Date: "+data.getAgreementExpDate());
			logger.info("Invoice Amount: "+data.getInvoiceAmount());
			logger.info("Hidden Trade"+data.getHiddenTrade());
			logger.info("Hidden Agree"+data.getHiddenAgree());
			logger.info("No Proper Document "+data.getNoProperDocuments());
			logger.info("Auto Renewal "+data.getAutoRenewal());
			logger.info("Entity type "+data.getEntityType());
			logger.info("Service Code "+data.getServiceCode());
			logger.info("Budget Year "+data.getBudgetYear());
			logger.info("BIFURCATION "+data.getBifurcation());
			logger.info("EXCEPTION APPROVAL "+data.getIsExceptionalApproval());
			logger.info("Remarks "+data.getRemarks());
			logger.info("IS Gov  "+data.getIsGovernmentEntity());
			logger.info("IS Insurance "+data.getIsInsuranceCompany());
			
			//For TradeLicense Date
			attachmentDao.updateTradeExpDate(data.getSupplierId(),data.getTradeLicenseExpDate(), data.getAutoRenewal(),
					 data.getIsGovernmentEntity(), data.getIsInsuranceCompany());
			
				
			//For AgreementExpDate
			boolean agrFlag = attachmentDao.checkForAgrDetails(data.getMgmtCompId(),
					data.getSupplierId(),data.getBuildingId());
			logger.info("agrFlag in AttachmentService ::"+agrFlag);
			
			if(agrFlag)
			{
				logger.info("Update Agreement Details");
				attachmentDao.updateAgrExpDate(data.getSupplierId(),data.getMgmtCompId(),data.getBuildingId(),data.getAgreementExpDate());
				
			}
			else{
				logger.info("Insert Agreement Details");
				attachmentDao.insertAgrExpDate(data.getSupplierId(),data.getMgmtCompId(),data.getBuildingId(),data.getAgreementExpDate());
				
			}
				
			
			Integer attachmentId=attachmentDao.saveAttachmentData(data);	
			
			return attachmentId;
			
		}catch(Exception e) {
			e.printStackTrace();
			logger.info("Exception in saveAttachmentData"+e.getCause());
			return 0;
		}
		
		
		
	}
	
	

	@Override
	public List<AttachmentData> getAttachmentData() {	
		
		try{
		logger.info("Calling  getAttachmentData() in Attachment Service");
		return attachmentDao.getAttachmentData();
		}
		catch(Exception e)
		{
			logger.info("Exception in getAttachmentData() in Attachment Service "+e.getCause());
			return null;
		}
	}


	@Override
	public List<ManagementCompany> getManagementCompany() {
		
		try{
		logger.info("Calling getManagementCompany() in AttachmentServiceImpl");
		
		List<ManagementCompany> list=attachmentDao.getManagementCompany();
		return list;
		}
		catch(Exception e)
		{
			logger.info("Exception in getManagementCompany() in AttachmentServiceImpl "+e.getCause());
			return null;
		}
		
	}

	
	@Override
	public AttachmentData getMatrixRefNo(Integer pymtReqId) {
		
		logger.info("Calling getMatrixRefNo() in AttachmentServiceImpl");

		try{
		logger.info("Fetching getMatrixRefNo Data"+pymtReqId);
		AttachmentData attachmentData=attachmentDao.getMatrixRefNo(pymtReqId);
		if(attachmentData.getMatrixFileRefNo()!=null && attachmentData.getMatrixFileRefNo()!=0 && "BULK".equals(attachmentData.getPaymentType())) {
			Integer subPaymentsCount=attachmentDao.getSubPayments(attachmentData.getMatrixFileRefNo());
			attachmentData.setSubPaymentsCount(subPaymentsCount);
			return attachmentData;
		}
		else {
			attachmentData.setSubPaymentsCount(0);
			return attachmentData;
		}
		
		
		}
		catch(Exception e)
		{
			logger.info("Exception in getMatrixRefNo() in AttachmentServiceImpl "+e.getCause());
			return null;
		}
	}

	
	@Override
	public AttachmentData getExpiryDates(Integer splyer_id,Integer mgmt_cpny_id)  {
		
		try{
			
		logger.info("Calling getExpiryDates() in AttachmentServiceImpl ");
			
		AttachmentData attachmentData = new AttachmentData();
		AttachmentData attachment= new AttachmentData();
		AttachmentData data=attachmentDao.getExpiryDates(splyer_id);
		attachmentData.setTradeLicenseExpDate(data.getTradeLicenseExpDate());
		attachmentData.setAutoRenewal(data.getAutoRenewal());
		attachmentData.setEntityType(data.getEntityType());
		
		if(mgmt_cpny_id!=0) {
			attachment=attachmentDao.getExpiryDates(splyer_id,mgmt_cpny_id);
		
		}
		logger.info("attachmentData::"+attachmentData);
		if(attachment!=null) {
			attachment.setTradeLicenseExpDate(data.getTradeLicenseExpDate());
			attachment.setAutoRenewal(data.getAutoRenewal());
			attachment.setEntityType(data.getEntityType());
			return attachment;
		}
		else {
			return attachmentData;
		}
		}
		catch(Exception e)
		{
			logger.info("Exception in getExpiryDates() in AttachmentServiceImpl "+e.getCause());
			return null;
		}
	}

}

