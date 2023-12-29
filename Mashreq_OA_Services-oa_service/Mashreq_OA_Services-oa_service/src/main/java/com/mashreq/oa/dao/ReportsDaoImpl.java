package com.mashreq.oa.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.ReportsInput;
import com.mashreq.oa.entity.ReportsOutput;
import com.mashreq.oa.entity.Supplier;


@Repository
public class ReportsDaoImpl implements ReportsDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	Logger logger=org.slf4j.LoggerFactory.getLogger(ReportsDaoImpl.class);
	
	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	
	@Override
	public List<Supplier> supplierList() {
		
		try{
		logger.info("Calling supplierList() in ReportsDaoImpl ");

		String query = "SELECT SUPPLIER_ID , SUPPLIER_NAME, TRADE_LICENSE_EXP_DATE"
				+ " FROM "+DBAPPEND+"oa_suppliers ORDER BY SUPPLIER_NAME ASC";
		List<Supplier> supplier=jdbcTemplate.query(query,
				BeanPropertyRowMapper.newInstance( Supplier.class));
		
		return supplier;
		}
		catch(Exception e)
		{
			logger.info("Exception in supplierList() in ReportsDaoImpl "+e.getCause());
			return null;
		}
	}


	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public List<ReportsOutput> generateReport(String reportQueryString, ArrayList param) {
		
		try{
		logger.info("calling generateReport() in ReportsDaoImpl");
		
		@SuppressWarnings("static-access")
		List<ReportsOutput> data = jdbcTemplate.query(reportQueryString, param.toArray(),
				new BeanPropertyRowMapper().newInstance(ReportsOutput.class));
		
		
		logger.info("Data:::>"+data);
		
		return data;
		}
		catch(Exception e)
		{
			logger.info("Exception in generateReport() in ReportsDaoImpl "+e.getCause());
			e.printStackTrace();
			return null;
		}

	}


	@Override
	public String getDocumentPathByDocumentId(Integer documentId) {
		try{
			String documentPath=null;
			logger.info("calling getDocumentPathByDocumentId() in ReportsDaoImpl");
			
			String query = "SELECT DOCUMENT_PATH FROM "+DBAPPEND+"OA_DOCUMENTS WHERE DOCUMENT_ID="+documentId+"";
			documentPath=jdbcTemplate.queryForObject(query, String.class);
		
			return documentPath;
			}
			catch(Exception e)
			{
				logger.info("Exception in getDocumentPathByDocumentId() in ReportsDaoImpl "+e.getCause());
				e.printStackTrace();
				return null;
			}
	}


}
