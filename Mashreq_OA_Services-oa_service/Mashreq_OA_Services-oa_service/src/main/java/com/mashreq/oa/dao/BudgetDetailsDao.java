package com.mashreq.oa.dao;

import java.util.Date;
import java.util.List;

import com.mashreq.oa.entity.AuditTrail2;
import com.mashreq.oa.entity.AuditTrailLog;
import com.mashreq.oa.entity.BudgetDetailsInput;
import com.mashreq.oa.entity.BudgetDetailsOutput;
import com.mashreq.oa.entity.Reversal;

public interface BudgetDetailsDao {
	
	public List<BudgetDetailsOutput> getBudgetDetails(BudgetDetailsInput bdInput);

	public void updateBudgetDetails(BudgetDetailsOutput bdOutput,String username);

	public List<Integer> getBudgetYears();

	public List<AuditTrail2> getAuditTrailRecords(Reversal reversal); //for reversal
	
	public BudgetDetailsOutput getBudgetItemById(int budgetItemId); //for reversal
	
	public void updateBudgetItemTable(BudgetDetailsOutput budgetItem);//for reversal

	public void logAuditTrail(BudgetDetailsOutput budgetItem, AuditTrail2 auditTrail,Double amount,String username, String comment);//for reversal

	public List<AuditTrailLog> fetchAuditTrailLog(String serviceCode,String userName,Date updatedFrom,Date updatedTo); //for fetchAudit Trail Log
}
