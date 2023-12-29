package com.mashreq.oa.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mashreq.oa.dao.ReportsDao;
import com.mashreq.oa.entity.ReportsInput;
import com.mashreq.oa.entity.ReportsOutput;
import com.mashreq.oa.entity.Supplier;
import com.mashreq.oa.utils.ReportsExcel;

@Service
public class ReportsServiceImpl implements ReportsService {
	
	Logger logger = LoggerFactory.getLogger(ReportsServiceImpl.class);

	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	@Autowired
	public ReportsDao report2Dao;
	@Autowired
	public HttpSession session;
	
	ReportsExcel excel=new ReportsExcel();	
	
	@Override
	public List<ReportsOutput> generateReport(ReportsInput reports) {
		
		
		try{
		logger.info("calling generateReport() in  ReportsServiceImpl");
		
		ArrayList param = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		
		logger.info("MGMT ID::: "+reports.getMgmtCompId());
		logger.info("SUPlier ID::: "+reports.getSupplierId());
		logger.info("BudgetRangefrom::: "+reports.getBudgetRangeFrom());
		logger.info("BudgetRangeTo::: "+reports.getBudgetRangeTo());
		logger.info("BudgetYear::: "+reports.getBudgetYear());
		logger.info("InvoiceYear::: "+reports.getInvoiceYear());

		
		String select="SELECT MC.MC_NAME_EN , SP.SUPPLIER_NAME, BD.BUDGET_PERIOD_CODE AS BUDGET_YEAR, AD.INVOICE_DATE_YEAR AS INVOICE_YEAR,"
				+ "	SUM(BI.TOTAL_BUDGET) AS TOTAL_BUDGET, SUM(BI.CONSUMED_AMOUNT) AS CONSUMED_AMOUNT, SUM(BI.BALANCE_AMOUNT) AS BALANCE_AMOUNT ";
		
		String from="FROM "+DBAPPEND+"OA_PAYMENT_REQUESTS PMT,"+DBAPPEND+"OA_SUPPLIERS SP,"+DBAPPEND+"oa_attachments_data AD,"
				+ " "+DBAPPEND+"OA_MANAGEMENT_COMPANIES MC,"+DBAPPEND+"OA_BUDGET_DETAILS BD, "+DBAPPEND+"OA_BUDGET_ITEMS BI ";
		
		String where=" WHERE BD.MGMT_COMP_ID=PMT.MGMT_COMP_ID AND PMT.MGMT_COMP_ID = MC.MGMT_COMP_ID "
						+ "AND PMT.ATTACHMENT_DATA_ID = AD.ATTACHMENT_DATA_ID AND BD.BUDGET_ID=BI.BUDGET_ID "
						+ "AND PMT.SUPPLIER_ID = SP.SUPPLIER_ID AND ";
		
		
		String groupby= "GROUP BY MC.MC_NAME_EN , SP.SUPPLIER_NAME, BD.BUDGET_PERIOD_CODE, AD.INVOICE_DATE_YEAR "; 

		boolean whereAnd = false;
		
		if ((reports.getMgmtCompId() != null)) {
			where = where + " MC.MGMT_COMP_ID=? ";
			param.add(reports.getMgmtCompId());
			whereAnd = true;
		}
		
		if ((reports.getSupplierId() != null)) {
			if(whereAnd){
				where = where + " AND ";				
			}
			where = where + " SP.SUPPLIER_ID=? ";
			param.add(reports.getSupplierId());
			whereAnd = true;
		}
		
		if ((reports.getBudgetRangeFrom() != null)) {
			if(whereAnd){
				where = where + " AND ";				
			}
			where = where + " BI.TOTAL_BUDGET >= ?  ";
			param.add(reports.getBudgetRangeFrom());
			whereAnd = true;
		}
		
		if ((reports.getBudgetRangeTo() != null)) {
			if(whereAnd){
				where = where + " AND ";				
			}
			where = where + " BI.TOTAL_BUDGET <= ? ";
			param.add(reports.getBudgetRangeTo());			
		}
		
		boolean havingAnd = false; 
		
		if(reports.getBudgetYear() != null || reports.getInvoiceYear() != null){
			groupby = groupby + " HAVING ";
		//	havingFlag = false;
			
		}	
		
		if ((reports.getBudgetYear() != null)) {
			groupby = groupby+ "BD.BUDGET_PERIOD_CODE LIKE ? ";
			param.add("%"+reports.getBudgetYear()+"%");
			havingAnd = true;
		}
		
		if ((reports.getInvoiceYear() != null)) {
			if(havingAnd){
				groupby = groupby+ " AND ";
			}
			groupby = groupby+ " AD.INVOICE_DATE_YEAR=? ";
			param.add(reports.getInvoiceYear());
		}
		
		
		
		sb.append(select);
		sb.append(from);
		sb.append(where);
		sb.append(groupby);
		
		
		//int repchar = sb.lastIndexOf("AND");
		//logger.info("REPCHAR" + repchar);
		//if (repchar != -1) {
		//	sb.delete(repchar, repchar + "AND".length());
	//	}
		
	

		String reportQueryString = sb.toString();
		logger.info("reportQueryString::" + reportQueryString);
		for (int i = 0; i < param.toArray().length; i++) {
			logger.info(""+param.toArray()[i]);
		}
		

		
		
		List<ReportsOutput> reportData= report2Dao.generateReport(reportQueryString,param);
		return reportData;
		}
		catch(Exception e)
		{
			logger.info("Exception in  generateReport() in ReportsServiceImpl "+e.getCause());
			return null;
		}
	
}
		
		

		

	@Override
	public List<Supplier> supplierList() {
		logger.info("calling supplierList() in  ReportsServiceImpl");
		return report2Dao.supplierList();
	}

	@Override
	public List<ReportsOutput> generatedReportForExcel(ReportsInput reports) {
		logger.info("calling generatedReportForExcel() in  ReportsServiceImpl");
		return generateReport(reports);
		
		
	}

	@Override
	public List<ReportsOutput> generatedReportsForPdf(ReportsInput data) {
		logger.info("calling generatedReportsForPdf() in  ReportsServiceImpl");
		return generateReport(data);
	}





	@Override
	public String getDocumentPathByDocumentId(Integer documentId) {
		
		try {
			logger.info("Calling getDocumentPathByDocumentId() in Service");
			String base64path = report2Dao.getDocumentPathByDocumentId(documentId);
			return base64path;
		}
		catch(Exception e)
		{
			logger.info("Exception raised in getDocumentPathByDocumentId() in Service"+e.getCause());
			return null;
		}
		
	}
	
		

}



