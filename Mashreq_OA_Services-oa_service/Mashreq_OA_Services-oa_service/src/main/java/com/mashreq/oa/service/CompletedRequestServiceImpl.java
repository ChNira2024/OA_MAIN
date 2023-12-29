package com.mashreq.oa.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mashreq.oa.dao.CompletedRequestDao;
import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.CompletedRequestData;
import com.mashreq.oa.entity.CompletedRequestInput;
import com.mashreq.oa.entity.NameValue;
import com.mashreq.oa.entity.PaymentData;

import ch.qos.logback.classic.Logger;

@Service
public class CompletedRequestServiceImpl implements CompletedRequestService {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(CompletedRequestServiceImpl.class);
	
	@Autowired
	private CompletedRequestDao completedRequestDao;
	
	@Value("${DBAPPEND}")
	private String DBAPPEND;
	@Autowired
	public HttpSession session;
	
//	@Override
//	public List<CompletedRequestData> getCompletedRequests() {
//		
//		try{
//		 
//		logger.info("getCompletedRequests() in CompletedRequest Service");
//		List<CompletedRequestData> completedReqList=completedRequestDao.getCompletedRequests();
//		
//		for(CompletedRequestData compreq: completedReqList) {
//			compreq.setNamevalue(new NameValue(compreq.getMatrixRefNo(),compreq.getPymtReqId()));	
//		}
//		return completedReqList;
//		}
//		catch(Exception e)
//		{
//			logger.info("Exception in getCompletedRequests() in CompletedRequest Service");
//			return null;
//		}
//	}

	
	@Override
	public List<PaymentData> getCompletedRequests() {
		
	
		logger.info("getCompletedRequests() in CompletedRequest Service");
			
		List<PaymentData> completedReqList=completedRequestDao.getCompletedRequests();
		
		for(PaymentData compreq: completedReqList) {
			try {
				logger.info("compreq.getInitiatorDate()"+compreq.getInitiatorDate());
				if(compreq.getInitiatorDate()!=null) {
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			String convertedDate=sdf.format(compreq.getInitiatorDate());
			logger.info("Converted Format is:::"+convertedDate);
			compreq.setDisplayInitiatorDate(convertedDate);	
			
			}
			}catch(Exception e)
			{
				e.printStackTrace();
				logger.info("Exception in getListData() ::: "+e.getCause());
				return null;
			}
			
		}
		return completedReqList;
		
	}
	
	@Override
	public List<AttachmentData>  getDetails(Integer pymtReqId) {
		
		logger.info("getDetails() in CompletedRequest Service");
		
		return completedRequestDao.getDetails(pymtReqId);
		
	}
	
	@Override
	public List<PaymentData> completedRequests(CompletedRequestInput compInput) {
		
		try {
			
			logger.info("Calling completedRequests in Service::");
			
			logger.info("MGMT_ID:: "+compInput.getMgmtCompId());
			logger.info("BUILD_ID:: "+compInput.getBuildingId());
			logger.info("SUPPLIER_ID:: "+compInput.getSupplierId());
			logger.info("STATUS:: "+compInput.getStatus());

			
			ArrayList param = new ArrayList<>();
			StringBuilder sb = new StringBuilder();
			
			String select = "SELECT PYMT_REQ_ID, MATRIX_REF_NO as matrixRefNo, MATRIX_REF_NO as strMatrixRefNo, SUB_PRODUCT, DEBIT_ACCOUNT_NUMBER_DESC,"
					+ "BENEFICIARY_NAME, INITIATOR_DATE, PAYMENT_CURRENCY, INVOICE_AMOUNT, CUSTOMER_REFERENCE,"
					+ "INITIATOR_NAME_DATE_TIME, STATUS ,REMARKS, UPLOADED_BY, SERVICE_CODE, BUDGET_YEAR ";
			
			String from = "FROM "+DBAPPEND+"OA_PAYMENT_REQUESTS ";
			
			//String where = "WHERE STATUS IN('APPROVED','REECTED') AND ";
			
			String where = "WHERE";
			
			boolean whereAnd = false;
			
			if(compInput.getMgmtCompId() != null )
			{
				where = where+ " MGMT_COMP_ID= ? ";
				param.add(compInput.getMgmtCompId());
				whereAnd = true;
			}
			if(compInput.getBuildingId() != null )
			{
				if(whereAnd) {
					where = where+" AND ";
				}
				where = where+ " BUILDING_ID= ? ";
				param.add(compInput.getBuildingId());
				whereAnd = true;
			}
			if(compInput.getSupplierId() != null )
			{
				if(whereAnd) {
					where = where+" AND ";
				}
				where = where+ " SUPPLIER_ID= ? ";
				param.add(compInput.getSupplierId());
				whereAnd = true;
			}
			if(compInput.getBudgetYear() != null )
			{
				if(whereAnd) {
					where = where+" AND ";
				}
				where = where+ " BUDGET_YEAR= ? ";
				param.add(compInput.getBudgetYear());
				whereAnd = true;
			}
			if(compInput.getStatus() == null)
			{
				if(whereAnd) {
					where = where+" AND ";
				}
				where = where + " STATUS IN('APPROVED','REJECTED') ";
			}
			
			if(compInput.getStatus() != null )
			{
				if(whereAnd) {
					where = where+" AND ";
				}
				
				if("ALL".equalsIgnoreCase(compInput.getStatus()))
				{
					where = where + " STATUS IN('APPROVED','REJECTED') "; 
				}
				if("REJECTED".equalsIgnoreCase(compInput.getStatus()))
				{
					where = where + " STATUS = 'REJECTED' ";
				}
				if("APPROVED".equalsIgnoreCase(compInput.getStatus()))
				{
					where = where + " STATUS = 'APPROVED' ";
				}
				
			}
			
			sb.append(select);
			sb.append(from);
			sb.append(where);
			
			String reportQueryString = sb.toString();
			logger.info("reportQueryString::" + reportQueryString);
			for (int i = 0; i < param.toArray().length; i++) {
				logger.info(""+param.toArray()[i]);
			}
			
			List<PaymentData> completedRequests = completedRequestDao.completedRequests(reportQueryString,param);
			
			for(PaymentData compreq: completedRequests) {
				logger.info("compreq.getInitiatorDate()"+compreq.getInitiatorDate());
				if(compreq.getInitiatorDate()!=null) {
				SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
				String convertedDate=sdf.format(compreq.getInitiatorDate());
				logger.info("Converted Format is:::"+convertedDate);
				compreq.setDisplayInitiatorDate(convertedDate);	
				}
			}
			return completedRequests;
			
			
		}catch(Exception e)
		{
			//e.printStackTrace();
			logger.info("Exception raised in completedRequests in service::" +e.getCause());
			return null;
		}
	
}


	@Override
	public List<PaymentData> generateExcel(CompletedRequestInput compInput) {
		
		try {
			logger.info("Calling generateExcel() in completedrequestservice");
		return completedRequests(compInput);
		}catch(Exception e)
		{
			logger.info("Exception raised in generateExcel() in completedrequestservice::"+e.getCause());
			return null;
		}
	}


	@Override
	public List<PaymentData> generatedPdf(CompletedRequestInput compInput) {
		
		try {
			logger.info("Calling generatedPdf() in completedrequestservice");
			return completedRequests(compInput);
		}
		catch(Exception e)
		{
			logger.info("Exception raised in generatedPdf() in completedrequestservice::"+e.getCause());
			return null;
		}
	}


		

}
