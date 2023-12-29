package com.mashreq.oa.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;

@Repository
public class BulkPayementsDAOImpl implements BulkPayementsDAO {

	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(BulkPayementsDAOImpl.class);

	@Override
	public List<PaymentData> getBulkPayementsList() {
		logger.info("calling getBulkPayementsList() in BulkPayementsDAOImpl");
		
		try{
			
			String query = "SELECT PYMT_REQ_ID, MATRIX_REF_NO, SUB_PRODUCT, DEBIT_ACCOUNT_NUMBER_DESC, BENEFICIARY_NAME, "
					+ " INITIATOR_DATE, PAYMENT_CURRENCY, INVOICE_AMOUNT, CUSTOMER_REFERENCE, INITIATOR_NAME_DATE_TIME, "
					+ "STATUS, REMARKS FROM "+DBAPPEND+"oa_payment_requests WHERE payment_type='BULK' AND status='EXCEPTION'";
//			"select * from "+DBAPPEND+"oa_payment_requests WHERE payment_type='BULK' AND status='EXCEPTION'"
		List<PaymentData> list = jdbcTemplate.query(query,
				BeanPropertyRowMapper.newInstance(PaymentData.class));

		logger.info("BulkPayments is:" + list);
		return list;
		}
		catch(Exception e)
		{
			logger.info("Exception in  getBulkPayementsList() in BulkPayementsDAOImpl "+e.getCause());
			return null;
		}
	}

	@Override
	public List<PaymentData> getBulkExceptions(PaymentSearchInput pymtSerachInput) {
		try{
			logger.info("Calling getBulkExceptions() in BulkPayementsDAOImpl");
			
			String query = "SELECT PYMT_REQ_ID, MATRIX_REF_NO as matrixRefNo, MATRIX_REF_NO as strMatrixRefNo, SUB_PRODUCT, DEBIT_ACCOUNT_NUMBER_DESC, BENEFICIARY_NAME, "
					+ " INITIATOR_DATE, PAYMENT_CURRENCY, INVOICE_AMOUNT, CUSTOMER_REFERENCE, INITIATOR_NAME_DATE_TIME, "
					+ "STATUS, REMARKS FROM "+DBAPPEND+"oa_payment_requests WHERE PAYMENT_TYPE='BULK' AND STATUS='EXCEPTION' "
							+ "AND MGMT_COMP_ID="+pymtSerachInput.getMgmtCompId()+" AND BUILDING_ID="+pymtSerachInput.getBuildingId();
			
			List<PaymentData> bulkList = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(PaymentData.class));
			
			logger.info("Bulk Payment List fetched from DB:: "+bulkList);
			
			if(bulkList != null && bulkList.size()>0)
			{
				return bulkList;
			}
			
			
		}catch(Exception e)
		{
			logger.info("Exception raised in getBulkExceptions() in IndividualPayementsDAOImpl :: "+e.getCause());
			return null;
		}
		return null;
		
	}
			
}
