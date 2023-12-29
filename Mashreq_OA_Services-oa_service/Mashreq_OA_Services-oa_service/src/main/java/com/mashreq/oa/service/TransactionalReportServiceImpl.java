package com.mashreq.oa.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashreq.oa.dao.TransactionalReportDao;
import com.mashreq.oa.entity.TransactionalReportInput;
import com.mashreq.oa.entity.TransactionalReportOutput;

@Service
public class TransactionalReportServiceImpl implements TransactionalReportService {
	
	Logger logger = LoggerFactory.getLogger(TransactionalReportServiceImpl.class);
	@Autowired
	private TransactionalReportDao trReportDao;
	@Autowired
	public HttpSession session;
	
	@Override
	public List<TransactionalReportOutput> generateReport(TransactionalReportInput reportInput) {
		
		try {
		return trReportDao.generateReport(reportInput);
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Exception in TransactionalReport Generation "+e.getCause());
			return null;
		}
		
	}
	

	@Override
	public List<TransactionalReportOutput> generatedReportForExcel(TransactionalReportInput reports) {
		return generateReport(reports);
	}

	@Override
	public List<TransactionalReportOutput> generatedReportsForPdf(TransactionalReportInput reports) {
		return generateReport(reports);
	}

}
