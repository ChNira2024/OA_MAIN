package com.mashreq.oa.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mashreq.oa.entity.ReportsInput;
import com.mashreq.oa.entity.ReportsOutput;
import com.mashreq.oa.entity.Supplier;

public interface ReportsDao {
	
	public List<Supplier> supplierList();

	public List<ReportsOutput> generateReport(String reportQueryString, ArrayList param);

	public String getDocumentPathByDocumentId(Integer documentId);


}
