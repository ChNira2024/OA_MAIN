package com.mashreq.oa.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.TransactionalReportInput;
import com.mashreq.oa.entity.TransactionalReportOutput;

@Repository
public class TransactionalReportDaoImpl implements TransactionalReportDao{
	
	Logger logger = LoggerFactory.getLogger(TransactionalReportDaoImpl.class);
	
	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<TransactionalReportOutput> generateReport(TransactionalReportInput reportInput) {
		try {
			logger.info("Inside TransactionalReportDaoImpl");
			
			logger.info("Transaction From date:"+reportInput.getTransactionFrom());
			logger.info("Transaction To date:"+reportInput.getTransactionTo());
			
			SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
			String fromDate=formatter.format(reportInput.getTransactionFrom());
			String toDate=formatter.format(reportInput.getTransactionTo());
			logger.info("Transaction Date DD-MM-YYYY:"+fromDate);
			logger.info("Transaction TO Date DD-MM-YYYY:"+toDate);
			
			
			
			String sql= "select  distinct uploaded_by as username, count(status) totalTransaction, "
					+ "sum(case when status='EXCEPTION' then 1 else 0 end) exception,"
					+ "sum(case when status='APPROVED' then 1 else 0 end) approved,"
					+ "sum(case when status='REJECTED' then 1 else 0 end) rejected,"
					+ "sum(case when status='IN-PROGRESS' then 1 else 0 end) inProgress "
					+ "FROM "+DBAPPEND+"oa_payment_requests WHERE "
					+ "uploaded_on between '"+reportInput.getTransactionFrom()+"' and '"+reportInput.getTransactionTo()+"' "
					+ " group by uploaded_by"; 
			
			
			List<TransactionalReportOutput> outputList = jdbcTemplate.query(sql,
					BeanPropertyRowMapper.newInstance(TransactionalReportOutput.class));
			
			logger.info("Transactional Reports Output Are: " +outputList);
			
			return outputList;

		}catch(Exception e) {
			logger.info("Inside TransactionalReportDaoImpl Eception cause is::"+e.getCause());
			return null;
		}
			}
	
	public static void main(String[] args) throws ParseException {
		String dat="2020-06-30";
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
		Date cov=sdf2.parse(dat);
		System.out.println("Date is"+sdf2.format(cov));
	}

}
