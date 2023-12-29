package com.mashreq.oa.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.controller.TransactionSearchCriteriaController;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.TransactionSearchCriteriaTable2;


@Repository
public class TransactionSearchCriteriaDaoImpl implements TransactionSearchCriteriaDao {
	private static final Logger logger = LoggerFactory.getLogger(TransactionSearchCriteriaController.class);

	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "static-access", "rawtypes" })
	@Override
	public List<PaymentData> gettransactionList(String searchQueryString, ArrayList param) {
		
		try{
		logger.info("calling getTransactionList() in TransactionSearchCriteriaDaoImpl");
		
		List<PaymentData> data = jdbcTemplate.query(searchQueryString, param.toArray(),
				new BeanPropertyRowMapper().newInstance(PaymentData.class));
		
		
		logger.info("Data:::>"+data);
		
		return data;
		}
		catch(Exception e)
		{
			logger.info("Exception in gettransactionList() in TransactionSearchCriteriaDaoImpl "+e.getCause());
			return null;
		}
	}
	
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public int countList(String countQueryString, ArrayList param) {
		
		try{
		logger.info("countList dao method");
		
 		int count = this.jdbcTemplate.queryForObject(countQueryString, param.toArray(), Integer.class);
 		
 		logger.info("search countList count:::"+count);
		
		return count;
		}
		catch(Exception e)
		{
			logger.info("Exception in countList() in TransactionSearchCriteriaDaoImpl "+e.getCause());
			return 0;
		}
		
	}

	
}
