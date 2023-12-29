package com.mashreq.oa.service;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashreq.oa.dao.IndividualPayementsDAO;
import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.AuditTrail;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;

@Service
public class IndividualPayementsServiceImpl implements IndividualPayementsService {
	
	private static final Logger logger = LoggerFactory.getLogger(IndividualPayementsServiceImpl.class);
	@Autowired
	private IndividualPayementsDAO individualPayementsDAO;
	@Autowired
	public HttpSession session;
//	@Override
//	public List<PaymentData> getIndividualPayementsList() {
//
//		logger.info("calling getIndividualPayementsList service method");
//		List<PaymentData> list=individualPayementsDAO.getIndividualPayementsList();
//		for(PaymentData pymdata:list) {
//			pymdata.setNamevalue(new NameValue(pymdata.getMatrixRefNo(),pymdata.getPymtReqId()));
//		}
//		return list;
//	}
	
	@Override
	public List<PaymentData> getIndividualPayementsList() {

		logger.info("calling getIndividualPayementsList service method");
		List<PaymentData> list=individualPayementsDAO.getIndividualPayementsList();
		for(PaymentData pymtreq:list) {
			try {
				logger.info("pymtreq.getInitiatorDate()"+pymtreq.getInitiatorDate());
				if(pymtreq.getInitiatorDate()!=null) {
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			String convertedDate=sdf.format(pymtreq.getInitiatorDate());
			logger.info("Converted Format is:::"+convertedDate);
			pymtreq.setDisplayInitiatorDate(convertedDate);
			}
			}catch(Exception e)
			{
				e.printStackTrace();
				logger.info("Exception in getListData() ::: "+e.getCause());
				return null;
			}
		
	}
		return list;
	}
	
	@Override
	public List<PaymentData> getIndividualExceptions(PaymentSearchInput pymtSerachInput) {
		
		try{
			logger.info("Calling getIndividualExceptions() in IndividualPayementsServiceImpl");	
			List<PaymentData> individualList=individualPayementsDAO.getIndividualExceptions(pymtSerachInput);
			
			if(individualList != null && individualList.size()>0){
			for(PaymentData pymtData : individualList){
				
			logger.info("pymtData.getInitiatorDate()"+pymtData.getInitiatorDate());
			if(pymtData.getInitiatorDate()!=null) {
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			String convertedDate=sdf.format(pymtData.getInitiatorDate());
			logger.info("Converted Format is:::"+convertedDate);
			pymtData.setDisplayInitiatorDate(convertedDate);
				
			}
			
		}
			return individualList;
		}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Exception in getIndividualExceptions() in service ::: "+e.getCause());
			return null;
		}
		return null;
	
	}

	
	@Override
	public List<AttachmentData>  getDetails(Integer pymtReqId) {
		
		logger.info("getDetails() in CompletedRequest Service");
		return individualPayementsDAO.getDetails(pymtReqId);
	}
	
	@Override
	public void updateAttachment(AttachmentData updateData) {
		
		logger.info("Calling updateAttachment() in IndividualPaymentServiceImpl");
		
		String status = individualPayementsDAO.getStatusByPaymentreqID(updateData.getPymtReqId());
		logger.info("Status for sec in individualsdao.."+status);
		if(!"EXCEPTION".equals(status) && !"PENDING".equals(status)){
			return;
		}
		
		try{
		//to insert data into OA_AUDIT_TRAIL
		validate(updateData);
		
		individualPayementsDAO.updateAttachment(updateData);
		}
		catch(Exception e)
		{
			logger.info("Exception in updateAttachment() in IndividualPaymentServiceImpl: "+e.getCause());
		}
	
	}
	
	public void validate(AttachmentData updateData) {
		//need to pass paymt_req_ID
		try{
		
		logger.info("Calling validate in Individual PaymentService service");
		
		Integer attachmentId = individualPayementsDAO.getAttachmentId(updateData.getPymtReqId());
		
		String userName = individualPayementsDAO.getUserName(updateData.getPymtReqId());
		
		List<AttachmentData> attachmentDetails =  getDetails(updateData.getPymtReqId());
		
		AttachmentData attch = attachmentDetails.get(0);
		
		logger.info("BACKEND ATTACHMENT DATA IS: "+attch);
		
		logger.info("FRONTEND ATTACHMENT DATA IS: "+updateData);
		
		logger.info("OLD TradeDate is: "+attch.getTradeLicenseExpDate());
		logger.info("New TradeDate is: "+updateData.getTradeLicenseExpDate());
		
		logger.info("OLD AgrData is: "+attch.getAgreementExpDate());
		logger.info("New AgrDate is: "+updateData.getAgreementExpDate());
		
		logger.info("OLD ServiceCode is: "+attch.getServiceCode());
		logger.info("New ServiceCode is: "+updateData.getServiceCode());
		
		logger.info("OLD BudgetYear is: "+attch.getBudgetYear());
		logger.info("New BudgetYear is: "+updateData.getBudgetYear());


		if(Integer.compare(attch.getMgmtCompId(), updateData.getMgmtCompId())!=0)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("MGMTCOMPID");
			audit.setOldValue(attch.getMgmtCompId().toString());
			audit.setNewValue(updateData.getMgmtCompId().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			
			individualPayementsDAO.saveAuditDetails(audit);		
		}
		
		if(Integer.compare(attch.getSupplierId(), updateData.getSupplierId())!=0)
		{
			logger.info("Inside if supplier");
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("SUPPLIERID");
			audit.setOldValue(attch.getSupplierId().toString());
			audit.setNewValue(updateData.getSupplierId().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);	
		}
		
		if(attch.getTradeLicenseExpDate() == null &&  updateData.getTradeLicenseExpDate() != null)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("TradeLicenseExpDate");
			audit.setOldValue(null);
			audit.setNewValue(updateData.getTradeLicenseExpDate().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);	
		}
		
		if(attch.getTradeLicenseExpDate() != null && updateData.getTradeLicenseExpDate() == null)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("TradeLicenseExpDate");
			audit.setOldValue(attch.getTradeLicenseExpDate().toString());
			audit.setNewValue(null);
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);	
		}
		
		if(attch.getTradeLicenseExpDate() !=null && updateData.getTradeLicenseExpDate() != null)
		{
			if(!attch.getTradeLicenseExpDate().toString().equals(updateData.getTradeLicenseExpDate().toString())){
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("TradeLicenseExpDate");
			audit.setOldValue(attch.getTradeLicenseExpDate().toString());
			audit.setNewValue(updateData.getTradeLicenseExpDate().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);	
			}
		}
		
		if(attch.getAgreementExpDate() == null &&  updateData.getAgreementExpDate() != null)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("AgreementExpDate");
			audit.setOldValue(null);
			audit.setNewValue(updateData.getAgreementExpDate().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(attch.getAgreementExpDate() != null &&  updateData.getAgreementExpDate() == null)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("AgreementExpDate");
			audit.setOldValue(attch.getAgreementExpDate().toString());
			audit.setNewValue(null);
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(attch.getAgreementExpDate() != null &&  updateData.getAgreementExpDate() != null)
		{
			if(!attch.getAgreementExpDate().toString().equals (updateData.getAgreementExpDate().toString())){
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("AgreementExpDate");
			audit.setOldValue(attch.getAgreementExpDate().toString());
			audit.setNewValue(updateData.getAgreementExpDate().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
			}
		}
		
		if(!attch.getBuildingId().equals(updateData.getBuildingId()))
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("PropertyGroupId");
			audit.setOldValue(attch.getBuildingId().toString());
			audit.setNewValue(updateData.getBuildingId().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);	
		}
		
		if(attch.getBudgetYear()== null && updateData.getBudgetYear()!=null)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("BudgetYear");
			audit.setOldValue(null);
			audit.setNewValue(updateData.getBudgetYear().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(attch.getBudgetYear()!= null && updateData.getBudgetYear()==null)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("BudgetYear");
			audit.setOldValue(attch.getBudgetYear().toString());
			audit.setNewValue(null);
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(attch.getBudgetYear()!= null && updateData.getBudgetYear()!= null){
		if(Double.compare(attch.getBudgetYear(), updateData.getBudgetYear())!=0)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("BudgetYear");
			audit.setOldValue(attch.getBudgetYear().toString());
			audit.setNewValue(updateData.getBudgetYear().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		}
		
		if(attch.getInvoiceDateYear() == null && updateData.getInvoiceDateYear() != null)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("InvoiceDateYear");
			audit.setOldValue(null);
			audit.setNewValue(updateData.getInvoiceDateYear().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(attch.getInvoiceDateYear() != null && updateData.getInvoiceDateYear() == null)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("InvoiceDateYear");
			audit.setOldValue(attch.getInvoiceDateYear().toString());
			audit.setNewValue(null);
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(attch.getInvoiceDateYear() != null && updateData.getInvoiceDateYear() != null)
		{
			if(!attch.getInvoiceDateYear().equals (updateData.getInvoiceDateYear())){
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("InvoiceDateYear");
			audit.setOldValue(attch.getInvoiceDateYear().toString());
			audit.setNewValue(updateData.getInvoiceDateYear().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
			}
		}
		
		if(Double.compare(attch.getInvoiceAmount(), updateData.getInvoiceAmount())!=0)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("InvoiceAmount");
			audit.setOldValue(attch.getInvoiceAmount().toString());
			audit.setNewValue(updateData.getInvoiceAmount().toString());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(!attch.getIssuanceAuthority().equals (updateData.getIssuanceAuthority()))
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("IssuanceAuthority");
			audit.setOldValue(attch.getIssuanceAuthority());
			audit.setNewValue(updateData.getIssuanceAuthority());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(!attch.getStatus().equals (updateData.getStatus()))
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("Status");
			audit.setOldValue(attch.getStatus());
			audit.setNewValue(updateData.getStatus());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(attch.getServiceCode() == null && updateData.getServiceCode() !=null)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("ServiceCode");
			audit.setOldValue(null);
			audit.setNewValue(updateData.getServiceCode());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(attch.getServiceCode() != null && updateData.getServiceCode() == null)
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("ServiceCode");
			audit.setOldValue(attch.getServiceCode());
			audit.setNewValue(null);
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		
		if(attch.getServiceCode() != null && updateData.getServiceCode() != null){
		if(!attch.getServiceCode().equals(updateData.getServiceCode()))
		{
			AuditTrail audit = new AuditTrail();
			audit.setFieldName("ServiceCode");
			audit.setOldValue(attch.getServiceCode());
			audit.setNewValue(updateData.getServiceCode());
			audit.setUpdatedBy(userName);
			audit.setId(attachmentId);
			individualPayementsDAO.saveAuditDetails(audit);
		}
		}
		
	}
		catch(Exception e)
		{
			logger.info("Exception while inserting data to Audit table in IndividualServiceImpl "+e.getCause());
		}
	}
	

}
