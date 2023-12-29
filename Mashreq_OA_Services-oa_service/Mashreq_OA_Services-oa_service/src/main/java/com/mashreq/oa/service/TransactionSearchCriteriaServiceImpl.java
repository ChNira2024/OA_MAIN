package com.mashreq.oa.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mashreq.oa.dao.TransactionSearchCriteriaDao;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.SearchResults;

@Service
public class TransactionSearchCriteriaServiceImpl implements TransactionSearchCriteriaService {

	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionSearchCriteriaServiceImpl.class);

	@Autowired
	private TransactionSearchCriteriaDao transactionDao;
	
	@Autowired
	public HttpSession session;

	@Value("${react.search.limit}")
	private int limit;

	/*
	 * this method is for search functionality query building.
	 */
	@Override
	public SearchResults gettransactionList(PaymentData inData) {
			
		try{
		StringBuilder countsb = new StringBuilder(); // builder for count of list
		logger.info("calling gettransactionList() service method");
		int count = 0;
		boolean fromFlag = false;
		boolean whereFlag = false;
		StringBuilder builder = new StringBuilder();
		ArrayList<Object> param = new ArrayList<>();
		
		StringBuilder sb = new StringBuilder();
		// query is building based on selected filters in reactjs application
		
		String countSelect = "Select count(pr.MATRIX_REF_NO)"; // select for count
		
		String select = "SELECT  pr.PYMT_REQ_ID, pr.MATRIX_REF_NO as matrixRefNo, pr.MATRIX_REF_NO as strMatrixRefNo, pr.SUB_PRODUCT, pr.DEBIT_ACCOUNT_NUMBER_DESC, pr.BENEFICIARY_NAME,"
				+ " pr.INITIATOR_DATE, pr.PAYMENT_CURRENCY, pr.INVOICE_AMOUNT, pr.CUSTOMER_REFERENCE, pr.INITIATOR_NAME_DATE_TIME,"
				+ " pr.STATUS, pr.UPLOADED_ON, pr.UPLOADED_BY, pr.PAYMENT_TYPE, pr.ATTACHMENT_DATA_ID, pr.REMARKS ";
		
		String from = " FROM "+DBAPPEND+"oa_payment_requests pr";
		
		/*if ((inData.getMcNameEn() != null && !inData.getMcNameEn().isEmpty()) && !fromFlag) {
			from = from + " ,"+DBAPPEND+"oa_attachments_data ad, "+DBAPPEND+"oa_management_companies mc ";
			fromFlag = true;
		}

		if ((inData.getBuildingName() != null && !inData.getBuildingName().isEmpty()) && !fromFlag) {
			from = from + " ,"+DBAPPEND+"oa_attachments_data ad, "+DBAPPEND+"oa_management_companies mc ";
			fromFlag = true;
		}
		if ((inData.getSortOrder() != null && !inData.getSortOrder().isEmpty())
				&& inData.getSortOrder().equalsIgnoreCase("MC_NAME_EN") && !fromFlag) {
			from = from + " ,"+DBAPPEND+"oa_attachments_data ad, "+DBAPPEND+"oa_management_companies mc ";
			fromFlag = true;
		}*/

		String where = " WHERE ";

		
		if ( inData.getMatrixRefNo() != null && (inData.getMatrixRefNo() != 0)) {
			where = where + " pr.matrix_ref_no LIKE ? AND";
			param.add("%"+inData.getMatrixRefNo() + "%");
		}

		if (inData.getSubProduct() != null && (!inData.getSubProduct().isEmpty()) ) {
			where = where + "  UPPER(pr.SUB_PRODUCT) LIKE UPPER(?) AND ";
			param.add("%" + inData.getSubProduct() + "%");
		}
		if (inData.getDebitAccountNumberDesc() != null && (!inData.getDebitAccountNumberDesc().isEmpty())) {
			where = where + "  UPPER(pr.DEBIT_ACCOUNT_NUMBER_DESC) LIKE UPPER(?) AND ";
			param.add("%" + inData.getDebitAccountNumberDesc() + "%");
		}
		if (inData.getBeneficiaryName() != null && (!inData.getBeneficiaryName().isEmpty()) ) {
			where = where + "  pr.SUPPLIER_ID=? AND ";
			param.add( inData.getBeneficiaryName() );
		}
		if ((inData.getCustomerReference() != "") && inData.getCustomerReference() != null) {
			where = where + " UPPER(pr.CUSTOMER_REFERENCE) LIKE UPPER(?) AND ";
			param.add("%" + inData.getCustomerReference() + "%");
		}
		if ((inData.getStatus() != "") && inData.getStatus() != null) {
			where = where + "  pr.STATUS LIKE ? AND ";
			param.add(inData.getStatus());
		}
		if ((inData.getPaymentCurrency() != "") && inData.getPaymentCurrency() != null) {
			where = where + "  pr.PAYMENT_CURRENCY LIKE ? AND ";
			param.add(inData.getPaymentCurrency());
		}
		if (inData.getAmountFrom() != 0) {
			where = where + "  pr.INVOICE_AMOUNT>=? AND ";
			param.add(inData.getAmountFrom());
		}
		if (inData.getAmountTo() != 0) {
			where = where + "  pr.INVOICE_AMOUNT<=? AND ";
			param.add(inData.getAmountTo());
		}
		if (inData.getInitiatorDateFrom() != null) {
			where = where + "  pr.initiator_date>=? AND ";
			param.add(inData.getInitiatorDateFrom());
		}
		if (inData.getInitiatorDateTo() != null) {
			where = where + "  pr.initiator_date<=? AND ";
			param.add(inData.getInitiatorDateTo());
		}
		/*if ((inData.getMcNameEn() != null && !inData.getMcNameEn().isEmpty()) && !whereFlag) {
			where = where + "  pr.attachment_data_id=ad.attachment_data_id AND ad.mgmt_comp_id=mc.mgmt_comp_id AND ";
			whereFlag = true;
		}

		if ((inData.getBuildingName() != null && !inData.getBuildingName().isEmpty()) && !whereFlag) {

			where = where + "  pr.attachment_data_id=ad.attachment_data_id AND ad.mgmt_comp_id=mc.mgmt_comp_id AND ";
			whereFlag = true;
		}
		if ((inData.getSortOrder() != null && !inData.getSortOrder().isEmpty())
				&& inData.getSortOrder().equalsIgnoreCase("MC_NAME_EN") && !whereFlag) {

			where = where + "  pr.attachment_data_id=ad.attachment_data_id AND ad.mgmt_comp_id=mc.mgmt_comp_id AND ";
			whereFlag = true;
		}*/

		if ((inData.getMcNameEn() != null && !inData.getMcNameEn().isEmpty())) {
			where = where + "   pr.mgmt_comp_id=? AND";
			param.add(inData.getMcNameEn());

		}
		if ((inData.getBuildingName() != null && !inData.getBuildingName().isEmpty())) {
			where = where + "  pr.BUILDING_ID=? AND";
			param.add(inData.getBuildingName());

		}

		String sortby = inData.getSortBy();
		if ((inData.getSortBy() != null) && inData.getSortBy() != "") {
			where = where + " ORDER BY pr.initiator_date " + sortby;

		}
		String sortor = inData.getSortOrder();
		if ((inData.getSortOrder() != null) && inData.getSortOrder() != ""
				&& inData.getSortOrder() != "INITIATOR_DATE") {
			int k = where.lastIndexOf("pr.initiator_date");

			builder.append(where.substring(0, k));
			
				
				builder.append("pr." + sortor);
			

			builder.append(where.substring(k + "pr.initiator_date".length()));
//			System.out.println("sortor:" + sortor);
//			System.out.println("builder:" + builder);

		}

		sb.append(select);

		sb.append(from);
		if ((inData.getSortOrder() != null) && inData.getSortOrder() != "") {
			sb.append(builder);
		} else {
			sb.append(where);
		}

		int repchar = sb.lastIndexOf("AND");
		if (repchar != -1) {
			sb.delete(repchar, repchar + "AND".length());
		}
		String searchQueryString = sb.toString();
		logger.info("main searchQueryString::" + searchQueryString);
		for (int i = 0; i < param.toArray().length; i++) {
			System.out.println(param.toArray()[i]);
		}

		// count functionality

		countsb.append(countSelect);
		countsb.append(from);
		countsb.append(where);

		int repchar1 = countsb.lastIndexOf("AND");
		if (repchar1 != -1) {
			countsb.delete(repchar1, repchar1 + "AND".length());
		}
		String countQueryString = countsb.toString();
		logger.info("countQueryString::" + countQueryString);
		for (int i = 0; i < param.toArray().length; i++) {
			logger.info(param.toArray()[i].toString());
		}
		if(countQueryString.contains("ORDER")) {
			count = transactionDao.countList(countQueryString.substring(0,countQueryString.indexOf("ORDER")), param);
		}
		else {
			count = transactionDao.countList(countQueryString, param);
		}

		if (count <= limit) {

			List<PaymentData> pmtDataList = transactionDao.gettransactionList(searchQueryString, param);
			SearchResults searchResults = new SearchResults();
			searchResults.setPaymentData(pmtDataList);

			searchResults.setCount(0);
			return searchResults;

		} else {
			SearchResults searchResults = new SearchResults();
			searchResults.setPaymentData(new ArrayList<>());

			searchResults.setCount(count);
			return searchResults;

		}
		}
		catch(Exception e)
		{
			logger.info("Exception in getTransactionList() in TransactionalServiceImpl "+e.getCause());
			return null;
		}

	

	}
}









































//package com.mashreq.oa.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.mashreq.oa.dao.TransactionSearchCriteriaDao;
//import com.mashreq.oa.entity.PaymentData;
//import com.mashreq.oa.entity.SearchResults;
//
//@Service
//public class TransactionSearchCriteriaServiceImpl implements TransactionSearchCriteriaService {
//
//	@Value("${MOLLAK.DBAPPEND}")
//	private String DBAPPEND;
//	
//	private static final Logger logger = LoggerFactory.getLogger(TransactionSearchCriteriaServiceImpl.class);
//
//	@Autowired
//	private TransactionSearchCriteriaDao transactionDao;
//
//	@Value("${react.search.limit}")
//	private int limit;
//
//	/*
//	 * this method is for search functionality query building.
//	 */
//	@Override
//	public SearchResults gettransactionList(PaymentData inData) {
//		try{
//		StringBuilder countsb = new StringBuilder(); // builder for count of list
//		logger.info("calling gettransactionList() service method");
//		int count = 0;
//		boolean fromFlag = false;
//		boolean whereFlag = false;
//		StringBuilder builder = new StringBuilder();
//		ArrayList<Object> param = new ArrayList<>();
//		
//		StringBuilder sb = new StringBuilder();
//		// query is building based on selected filters in reactjs application
//		
//		String countSelect = "Select count(pr.MATRIX_REF_NO)"; // select for count
//		
//		String select = "SELECT  pr.PYMT_REQ_ID, pr.MATRIX_REF_NO, pr.SUB_PRODUCT, pr.DEBIT_ACCOUNT_NUMBER_DESC, pr.BENEFICIARY_NAME,"
//				+ " pr.INITIATOR_DATE, pr.PAYMENT_CURRENCY, pr.INVOICE_AMOUNT, pr.CUSTOMER_REFERENCE, pr.INITIATOR_NAME_DATE_TIME,"
//				+ " pr.STATUS, pr.UPLOADED_ON, pr.UPLOADED_BY, pr.PAYMENT_TYPE, pr.ATTACHMENT_DATA_ID, pr.REMARKS ";
//		
//		String from = " FROM "+DBAPPEND+"oa_payment_requests pr";
//		
//		if ((inData.getMcNameEn() != null && !inData.getMcNameEn().isEmpty()) && !fromFlag) {
//			from = from + " ,"+DBAPPEND+"oa_attachments_data ad, "+DBAPPEND+"oa_management_companies mc ";
//			fromFlag = true;
//		}
//
//		if ((inData.getBuildingName() != null && !inData.getBuildingName().isEmpty()) && !fromFlag) {
//			from = from + " ,"+DBAPPEND+"oa_attachments_data ad, "+DBAPPEND+"oa_management_companies mc ";
//			fromFlag = true;
//		}
//		if ((inData.getSortOrder() != null && !inData.getSortOrder().isEmpty())
//				&& inData.getSortOrder().equalsIgnoreCase("MC_NAME_EN") && !fromFlag) {
//			from = from + " ,"+DBAPPEND+"oa_attachments_data ad, "+DBAPPEND+"oa_management_companies mc ";
//			fromFlag = true;
//		}
//
//		String where = " WHERE ";
//
//		
//		if ( inData.getMatrixRefNo() != null && (inData.getMatrixRefNo() != 0)) {
//			where = where + " pr.matrix_ref_no LIKE ? AND";
//			param.add(inData.getMatrixRefNo() + "%");
//		}
//
//		if (inData.getSubProduct() != null && (!inData.getSubProduct().isEmpty()) ) {
//			where = where + "  UPPER(pr.SUB_PRODUCT) LIKE UPPER(?) AND ";
//			param.add("%" + inData.getSubProduct() + "%");
//		}
//		if (inData.getDebitAccountNumberDesc() != null && (!inData.getDebitAccountNumberDesc().isEmpty())) {
//			where = where + "  UPPER(pr.DEBIT_ACCOUNT_NUMBER_DESC) LIKE UPPER(?) AND ";
//			param.add("%" + inData.getDebitAccountNumberDesc() + "%");
//		}
//		if (inData.getBeneficiaryName() != null && (!inData.getBeneficiaryName().isEmpty()) ) {
//			where = where + "  UPPER(pr.BENEFICIARY_NAME) LIKE UPPER(?) AND ";
//			param.add("%" + inData.getBeneficiaryName() + "%");
//		}
//		if ((inData.getCustomerReference() != "") && inData.getCustomerReference() != null) {
//			where = where + " UPPER(pr.CUSTOMER_REFERENCE) LIKE UPPER(?) AND ";
//			param.add("%" + inData.getCustomerReference() + "%");
//		}
//		if ((inData.getStatus() != "") && inData.getStatus() != null) {
//			where = where + "  pr.STATUS LIKE ? AND ";
//			param.add(inData.getStatus());
//		}
//		if ((inData.getPaymentCurrency() != "") && inData.getPaymentCurrency() != null) {
//			where = where + "  pr.PAYMENT_CURRENCY LIKE ? AND ";
//			param.add(inData.getPaymentCurrency());
//		}
//		if (inData.getAmountFrom() != 0) {
//			where = where + "  pr.INVOICE_AMOUNT>=? AND ";
//			param.add(inData.getAmountFrom());
//		}
//		if (inData.getAmountTo() != 0) {
//			where = where + "  pr.INVOICE_AMOUNT<=? AND ";
//			param.add(inData.getAmountTo());
//		}
//		if (inData.getInitiatorDateFrom() != null) {
//			where = where + "  pr.initiator_date>=? AND ";
//			param.add(inData.getInitiatorDateFrom());
//		}
//		if (inData.getInitiatorDateTo() != null) {
//			where = where + "  pr.initiator_date<=? AND ";
//			param.add(inData.getInitiatorDateTo());
//		}
//		if ((inData.getMcNameEn() != null && !inData.getMcNameEn().isEmpty()) && !whereFlag) {
//			where = where + "  pr.attachment_data_id=ad.attachment_data_id AND ad.mgmt_comp_id=mc.mgmt_comp_id AND ";
//			whereFlag = true;
//		}
//
//		if ((inData.getBuildingName() != null && !inData.getBuildingName().isEmpty()) && !whereFlag) {
//
//			where = where + "  pr.attachment_data_id=ad.attachment_data_id AND ad.mgmt_comp_id=mc.mgmt_comp_id AND ";
//			whereFlag = true;
//		}
//		if ((inData.getSortOrder() != null && !inData.getSortOrder().isEmpty())
//				&& inData.getSortOrder().equalsIgnoreCase("MC_NAME_EN") && !whereFlag) {
//
//			where = where + "  pr.attachment_data_id=ad.attachment_data_id AND ad.mgmt_comp_id=mc.mgmt_comp_id AND ";
//			whereFlag = true;
//		}
//
//		if ((inData.getMcNameEn() != null && !inData.getMcNameEn().isEmpty())) {
//			where = where + "   ad.mgmt_comp_id=? AND";
//			param.add(inData.getMcNameEn());
//
//		}
//		if ((inData.getBuildingName() != null && !inData.getBuildingName().isEmpty())) {
//			where = where + "  ad.BUILDING_ID=? AND";
//			param.add(inData.getBuildingName());
//
//		}
//
//		String sortby = inData.getSortBy();
//		if ((inData.getSortBy() != null) && inData.getSortBy() != "") {
//			where = where + " ORDER BY pr.initiator_date " + sortby;
//
//		}
//		String sortor = inData.getSortOrder();
//		if ((inData.getSortOrder() != null) && inData.getSortOrder() != ""
//				&& inData.getSortOrder() != "INITIATOR_DATE") {
//			int k = where.lastIndexOf("pr.initiator_date");
//
//			builder.append(where.substring(0, k));
//			if (inData.getSortOrder().equalsIgnoreCase("MC_NAME_EN")) {
//				builder.append("mc." + sortor);
//			} else {
//				builder.append("pr." + sortor);
//			}
//
//			builder.append(where.substring(k + "pr.initiator_date".length()));
////			System.out.println("sortor:" + sortor);
////			System.out.println("builder:" + builder);
//
//		}
//
//		sb.append(select);
//
//		sb.append(from);
//		if ((inData.getSortOrder() != null) && inData.getSortOrder() != "") {
//			sb.append(builder);
//		} else {
//			sb.append(where);
//		}
//
//		int repchar = sb.lastIndexOf("AND");
//		if (repchar != -1) {
//			sb.delete(repchar, repchar + "AND".length());
//		}
//		String searchQueryString = sb.toString();
//		logger.info("main searchQueryString::" + searchQueryString);
//		for (int i = 0; i < param.toArray().length; i++) {
//			System.out.println(param.toArray()[i]);
//		}
//
//		// count functionality
//
//		countsb.append(countSelect);
//		countsb.append(from);
//		countsb.append(where);
//
//		int repchar1 = countsb.lastIndexOf("AND");
//		if (repchar1 != -1) {
//			countsb.delete(repchar1, repchar1 + "AND".length());
//		}
//		String countQueryString = countsb.toString();
//		logger.info("countQueryString::" + countQueryString);
//		for (int i = 0; i < param.toArray().length; i++) {
//			logger.info(param.toArray()[i].toString());
//		}
//		if(countQueryString.contains("ORDER")) {
//			count = transactionDao.countList(countQueryString.substring(0,countQueryString.indexOf("ORDER")), param);
//		}
//		else {
//			count = transactionDao.countList(countQueryString, param);
//		}
//
//		if (count <= limit) {
//
//			List<PaymentData> pmtDataList = transactionDao.gettransactionList(searchQueryString, param);
//			SearchResults searchResults = new SearchResults();
//			searchResults.setPaymentData(pmtDataList);
//
//			searchResults.setCount(0);
//			return searchResults;
//
//		} else {
//			SearchResults searchResults = new SearchResults();
//			searchResults.setPaymentData(new ArrayList<>());
//
//			searchResults.setCount(count);
//			return searchResults;
//
//		}
//		}
//		catch(Exception e)
//		{
//			logger.info("Exception in getTransactionList() in TransactionalServiceImpl "+e.getCause());
//			return null;
//		}
//
//	}
//
//	
//	public static void main(String[] args) {
//		String query="Select count(pr.MATRIX_REF_NO) FROM MollakDB.dbo.oa_payment_requests pr WHERE  pr.matrix_ref_no LIKE ? AND  UPPER(pr.BENEFICIARY_NAME) LIKE UPPER(?) AND  UPPER(pr.CUSTOMER_REFERENCE) LIKE UPPER(?)   ORDER BY pr.initiator_date DESC\n" + 
//				"";
//		System.out.println("Query is::"+query.substring(0,query.indexOf("ORDER")));
//	}
//}
//
//
//


