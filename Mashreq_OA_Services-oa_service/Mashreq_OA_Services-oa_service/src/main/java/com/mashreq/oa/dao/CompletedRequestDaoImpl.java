package com.mashreq.oa.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.CompletedRequestData;
import com.mashreq.oa.entity.PaymentData;

import ch.qos.logback.classic.Logger;

@Repository
public class CompletedRequestDaoImpl implements CompletedRequestDao{

	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CompletedRequestDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<PaymentData> getCompletedRequests() {
		
		LOGGER.info("Calling getCompletedRequests() in CompletedRequestDaoImpl");
		List<PaymentData> compReqs = null;
		try{
			
			String sql="SELECT PYMT_REQ_ID, MATRIX_REF_NO, SUB_PRODUCT, DEBIT_ACCOUNT_NUMBER_DESC,BENEFICIARY_NAME, INITIATOR_DATE, PAYMENT_CURRENCY, "
					+ "INVOICE_AMOUNT, CUSTOMER_REFERENCE, INITIATOR_NAME_DATE_TIME, STATUS, REMARKS FROM "+DBAPPEND+"oa_payment_requests WHERE STATUS IN('APPROVED','REJECTED')";
			
		 compReqs = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PaymentData.class));
		}
		catch(Exception e)
		{
			LOGGER.info("Inside getCompletedRequests() catch");
			LOGGER.info(e.getMessage() + " == " +e.getCause());
		}
		
		return compReqs;
		
	}
	
	@Override
	public List<AttachmentData>  getDetails(Integer pymtReqId) {
		
		LOGGER.info("Inside getDetails() in CompletedRequestDaoImpl");
		
		 
		try{
			
			LOGGER.info("Inside try block CompletedRequestDao");
			
			String sql="SELECT payment.MATRIX_REF_NO as matrixRefNo, payment.MATRIX_REF_NO as strMatrixRefNo,payment.STATUS, payment.BIFURCATION,payment.REMARKS, attachments.IS_INSURANCE_COMPANY,"
					+ " attachments.NO_PROPER_DOCUMENTS, attachments.IS_GOVERNMENT_ENTITY,"
					+ "attachments.ISSUANCE_AUTHORITY , attachments.TRADE_LICENSE_EXP_DATE,attachments.AGREEMENT_EXP_DATE, attachments.AUTO_RENEWAL, "
					+ "payment.BUDGET_YEAR , attachments.INVOICE_DATE_YEAR , payment.INVOICE_AMOUNT , payment.SERVICE_CODE, "
					+ " suppliers.SUPPLIER_NAME, buildings.BUILDING_NAME, attachments.IS_EXCEPTIONAL_APPROVAL, "
					+ "management.MC_NAME_EN from "+DBAPPEND+"oa_payment_requests payment , "+DBAPPEND+"oa_attachments_data attachments, "
					+ " "+DBAPPEND+"oa_management_companies management, "+DBAPPEND+"OA_SUPPLIERS suppliers, "+DBAPPEND+"OA_BUILDINGS buildings "
					+ " where payment.ATTACHMENT_DATA_ID=attachments.ATTACHMENT_DATA_ID and "
					+ " payment.MGMT_COMP_ID=management.MGMT_COMP_ID and payment.SUPPLIER_ID=suppliers.SUPPLIER_ID  and "
					+ " payment.BUILDING_ID=buildings.BUILDING_ID and payment.PYMT_REQ_ID="+pymtReqId; 
			
			List<AttachmentData> compReqs = jdbcTemplate.query(sql,BeanPropertyRowMapper.newInstance(AttachmentData.class));
					
			LOGGER.info("Details fetched: "+compReqs);
			return compReqs;
		}
		catch(Exception e)
		{
			LOGGER.info("Inside catch block CompletedRequestDao");
			LOGGER.info(e.getMessage() + " == " +e.getCause());
			return null;
		}
	
		
	}

	@Override
	public List<PaymentData> completedRequests(String reportQueryString, ArrayList param) {
		
		try {
			LOGGER.info("Calling completedRequests() in CompletedRequestDaoImpl");
			
			List<PaymentData> completedRequests = jdbcTemplate.query(reportQueryString, param.toArray(), BeanPropertyRowMapper.newInstance(PaymentData.class));
			
			LOGGER.info("completed requests in dao impl is:: "+completedRequests);
			
			return completedRequests;
			
		}
		catch(Exception e) {
			LOGGER.info("Exception raised in completedRequests in CompletedRequestDaoImpl");
			e.printStackTrace();
			return null;
		}
		
	}
		
}
