package com.mashreq.oa.dao;

import java.util.List;

import com.mashreq.oa.entity.TransactionalReportInput;
import com.mashreq.oa.entity.TransactionalReportOutput;

public interface TransactionalReportDao {
	
	public List<TransactionalReportOutput> generateReport(TransactionalReportInput reportInput);

}
