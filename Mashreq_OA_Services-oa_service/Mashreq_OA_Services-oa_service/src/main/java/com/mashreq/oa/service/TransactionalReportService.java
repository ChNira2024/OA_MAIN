package com.mashreq.oa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mashreq.oa.entity.TransactionalReportInput;
import com.mashreq.oa.entity.TransactionalReportOutput;

public interface TransactionalReportService {
	
	public List<TransactionalReportOutput> generateReport(TransactionalReportInput reportInput);
	
	public List<TransactionalReportOutput> generatedReportForExcel(TransactionalReportInput reports);

	public List<TransactionalReportOutput> generatedReportsForPdf(TransactionalReportInput reports);

}
