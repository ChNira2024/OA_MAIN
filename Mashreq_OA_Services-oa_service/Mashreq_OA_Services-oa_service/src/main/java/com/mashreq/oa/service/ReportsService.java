package com.mashreq.oa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mashreq.oa.entity.ReportsInput;
import com.mashreq.oa.entity.ReportsOutput;
import com.mashreq.oa.entity.Supplier;

public interface ReportsService {
	
	public List<ReportsOutput> generateReport(ReportsInput reports);

	public List<Supplier> supplierList();

	public List<ReportsOutput> generatedReportForExcel(ReportsInput reports);

	public List<ReportsOutput> generatedReportsForPdf(ReportsInput data);

	public String getDocumentPathByDocumentId(Integer documentId);

}
