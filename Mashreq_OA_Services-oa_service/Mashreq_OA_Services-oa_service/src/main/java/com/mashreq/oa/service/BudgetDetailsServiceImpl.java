package com.mashreq.oa.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashreq.oa.dao.BudgetDetailsDao;
import com.mashreq.oa.entity.AuditTrail2;
import com.mashreq.oa.entity.AuditTrailLog;
import com.mashreq.oa.entity.BudgetDetailsInput;
import com.mashreq.oa.entity.BudgetDetailsOutput;
import com.mashreq.oa.entity.Reversal;

@Service
public class BudgetDetailsServiceImpl implements BudgetDetailsService {

	@Autowired
	private BudgetDetailsDao budgetDetailsDao;
	@Autowired
	public HttpSession session;
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(BudgetDetailsServiceImpl.class);
	
	@Override
	public List<BudgetDetailsOutput> getBudgetDetails(BudgetDetailsInput bdInput) {
		
		logger.info("Inside getBudgetDetails() in BudgetDetailsServiceImpl");
		
		List<BudgetDetailsOutput> bdDetails = budgetDetailsDao.getBudgetDetails(bdInput);
		/*Set<BudgetDetailsOutput> s=new HashSet<BudgetDetailsOutput>();
		s.addAll(bdDetails);
		bdDetails=new ArrayList<BudgetDetailsOutput>();
		bdDetails.addAll(s);*/
		return bdDetails;
		
	}

	@Override
	public void updateBudgetDetails(BudgetDetailsOutput bdOutput,String username) {
		
		logger.info("Inside updateBudgetDetails() in BudgetDetailsServiceImpl");
		
		
		budgetDetailsDao.updateBudgetDetails(bdOutput,username);
		
	}

	@Override
	public List<Integer> getBudgetYears() {
		try {
			logger.info("Calling getBudgetYears() in BudgetDetailsService");
			List<Integer> budgetYears=budgetDetailsDao.getBudgetYears();
			return budgetYears;
		}
		catch(Exception e)
		{
			logger.info("Exception in getBudgetYears() in BudgetDetailsService :: "+e.getCause());
			return null;
		}
	}
	
	@Override
//	@Transactional
	public BudgetDetailsOutput updateBudgetItems(Reversal reversal,String username) 
	{
		logger.info("inside com.mashreq.oa.service.BudgetItemsDetailsUpdateServiceImpl class having updateBudgetItems()");
		
		BudgetDetailsOutput budgetItemData = null;

		logger.info("BudgetItemsDetailsUpdateServiceImpl class updateBudgetItems()-Reversal Data: " + reversal);

		
		// Fetch all records from OA_AUDIT_TRAIL based on payment ID
		List<AuditTrail2> auditTrailRecords = budgetDetailsDao.getAuditTrailRecords(reversal);
		logger.info("fetched auditTrailRecords: " + auditTrailRecords);

		String fromScreen = "Updated From Screen By";
		String uploadedBy = fromScreen + ": " + username;
		
		// Iterate over audit trail records
		 for (AuditTrail2 auditTrailRecord : auditTrailRecords) 
		{
		
			 logger.info("fetched auditTrailRecord: " + auditTrailRecord); 
			 
			// Subtract new value from old value to get ConsumedAmount
			Double consumedAmount = Double.parseDouble(auditTrailRecord.getNewValue()) - Double.parseDouble(auditTrailRecord.getOldValue());
			logger.info("Consumed Amount: " +consumedAmount);
			logger.info("invoice amount: " + reversal.getInvoiceAmount());

			// Fetch BudgetItem based on budgetItemId
			budgetItemData = budgetDetailsDao.getBudgetItemById(auditTrailRecord.getId());
			
			// Add validation
			if (consumedAmount.equals(reversal.getInvoiceAmount())) 
			{

				logger.info("fetched data from oa_budget_items table by using budgetItemId: " + budgetItemData);

				// Update BudgetItem based on your logic
				if (budgetItemData != null) 
				{
					Double updatedConsumerAmount = budgetItemData.getConsumedAmount() - consumedAmount;
					Double updatedBalanceAmount = budgetItemData.getBalanceAmount() + consumedAmount;

					budgetItemData.setConsumedAmount(updatedConsumerAmount);
					budgetItemData.setBalanceAmount(updatedBalanceAmount);

					// Save the updated BudgetItem
					budgetDetailsDao.updateBudgetItemTable(budgetItemData);
				}
				// insert Log into OA_AUDIT_TRAIL_LOG
				budgetDetailsDao.logAuditTrail(budgetItemData, auditTrailRecord, reversal.getInvoiceAmount(),uploadedBy,"Reversal Action Done");
			}
			
		}//for-loop end
		return budgetItemData;
	}
	
	@Override
	public List<AuditTrailLog> getDataFromAuditTrailLog(String serviceCode, String userName, Date updatedFrom,Date updatedTo) 
	{
		List<AuditTrailLog> listOfData = null;
		logger.info("BudgetItemsDetailsUpdateServiceImpl class having getDataFromAuditTrailLog() method ");
		listOfData = budgetDetailsDao.fetchAuditTrailLog(serviceCode, userName, updatedFrom,updatedTo);
		logger.info("BudgetItemsDetailsUpdateServiceImpl class having getDataFromAuditTrailLog() method listOfData"+listOfData);
		return listOfData;
	}

}
