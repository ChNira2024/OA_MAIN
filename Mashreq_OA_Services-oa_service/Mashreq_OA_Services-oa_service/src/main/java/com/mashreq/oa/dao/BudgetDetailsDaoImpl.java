package com.mashreq.oa.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.AmountDetails;
import com.mashreq.oa.entity.AuditTrail2;
import com.mashreq.oa.entity.AuditTrailLog;
import com.mashreq.oa.entity.BudgetDetailsInput;
import com.mashreq.oa.entity.BudgetDetailsOutput;
import com.mashreq.oa.entity.Reversal;

@Repository
public class BudgetDetailsDaoImpl implements BudgetDetailsDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${MOLLAK.DBAPPEND}")
	private String DBAPPEND;

	@Value("${OA-DBFLAG}")
	private String DBFLAG;
	private static final Logger logger = (Logger) LoggerFactory.getLogger(BudgetDetailsDaoImpl.class);

	@Override
	public List<BudgetDetailsOutput> getBudgetDetails(BudgetDetailsInput bdInput) {

		try {
			logger.info("Inside BudgetDetailsDaoImpl");

			String query = "SELECT bi.BUDGET_ITEM_ID, bi.SERVICE_CODE, bi.SERVICE_NAME_EN, bi.TOTAL_COST, bi.VAT_AMOUNT, "
					+ "bi.TOTAL_BUDGET, bi.CONSUMED_AMOUNT, bi.BALANCE_AMOUNT , bd.USAGE_EN , bd.BUDGET_PERIOD_CODE FROM "
					+ DBAPPEND + "OA_BUDGET_ITEMS bi , " + "" + DBAPPEND + "OA_BUDGET_DETAILS bd, " + DBAPPEND
					+ "OA_BUILDINGS bl, " + DBAPPEND + "OA_BUILDING_PROPGROUP_MAPPING bpm "
					+ "WHERE bl.BUILDING_ID=bpm.BUILDING_ID AND bd.PROP_ID=bpm.PROP_ID AND "
					+ "bi.BUDGET_ID=bd.BUDGET_ID AND bl.BUILDING_ID=" + bdInput.getBuildingId() + " AND "
					+ "bd.MGMT_COMP_ID=" + bdInput.getMgmtCompId() + " AND bd.BUDGET_PERIOD_CODE LIKE('%"
					+ bdInput.getBudgetYear() + "%')" + " ORDER BY bi.SERVICE_CODE ASC ";

			/*
			 * String query =
			 * "SELECT bi.BUDGET_ITEM_ID, bi.SERVICE_CODE, bi.SERVICE_NAME_EN, bi.TOTAL_COST, bi.VAT_AMOUNT, "
			 * +
			 * "bi.TOTAL_BUDGET, bi.CONSUMED_AMOUNT, bi.BALANCE_AMOUNT , bd.USAGE_EN , bd.BUDGET_PERIOD_CODE FROM "
			 * + DBAPPEND + "OA_BUDGET_ITEMS bi , " + "" + DBAPPEND +
			 * "OA_BUDGET_DETAILS bd, " + DBAPPEND + "OA_PROPERTY_GROUPS bl " +
			 * "WHERE bi.BUDGET_ID=bd.BUDGET_ID AND bl.PROP_ID=bd.PROP_ID AND bd.PROP_ID=" +
			 * bdInput.getPropId() + " AND " + "bd.MGMT_COMP_ID=" + bdInput.getMgmtCompId()
			 * + " AND bd.BUDGET_PERIOD_CODE LIKE('%" + bdInput.getBudgetYear() + "%')" +
			 * " ORDER BY bi.SERVICE_CODE ASC ";
			 */

			logger.info("Query For BudgetDetails:::" + query);

			List<BudgetDetailsOutput> bdDetails = jdbcTemplate.query(query,
					BeanPropertyRowMapper.newInstance(BudgetDetailsOutput.class));

			logger.info("Fetched Budget Details are:: " + bdDetails);

			return bdDetails;

		} catch (Exception e) {
			logger.info("Exception in  getBudgetDetails in BudgetDetailsDaoImpl " + e.getCause());
			return null;
		}

	}

	@Override
	public void updateBudgetDetails(BudgetDetailsOutput bdOutput, String username) {

		try {
			logger.info("Calling  updateBudgetDetails() in BudgetDetailsDaoImpl");

			logger.info("Budget Item Id is:: " + bdOutput.getBudgetItemId());
			// get old value using budget_Item_id
			AmountDetails oldBalanceinfo = getAmountDetails(bdOutput.getBudgetItemId());
			
//			Double previousBalanceAmount = getBalanceAmount(bdOutput.getBudgetItemId());
//			Double previousConsumedAmount = getConsumedAmount(bdOutput.getBudgetItemId());
//			logger.info("Balance Amount:" + previousBalanceAmount);
//			logger.info("Balance Amount:" + previousConsumedAmount);
			String fromScreen = "Updated From Screen By";
			String uploadedBy = fromScreen + ": " + username;
			
			
			logger.info("Inserted Successfully in OA_AUDIT_TRAIL Table");
			
			String updateQuery = "UPDATE " + DBAPPEND
					+ "OA_BUDGET_ITEMS SET CONSUMED_AMOUNT=? , BALANCE_AMOUNT=? WHERE BUDGET_ITEM_ID="
			+ bdOutput.getBudgetItemId() + " ";
			
			Object[] args = { bdOutput.getConsumedAmount(), bdOutput.getBalanceAmount() };
			jdbcTemplate.update(updateQuery, args);
			// call audit_trail Insert method and insert details for Consumed_amount,
			// Balance_amount
			
			logger.info("Inserting Balanace Details in OA_AUDIT_TRAIL Table");
			
			insertAuditDetails(bdOutput.getBudgetItemId(), "BALANCE_AMOUNT", oldBalanceinfo.getBalanceAmount().toString(),
					bdOutput.getBalanceAmount().toString(), uploadedBy);
			
			logger.info("Inserting Balanace Details After updating");
			insertAuditDetails(bdOutput.getBudgetItemId(), "CONSUMED_AMOUNT", oldBalanceinfo.getConsumedAmount().toString(),
					bdOutput.getConsumedAmount().toString(), uploadedBy);
			logger.info("Inserted Successfully!");

			logger.info("Data sucessfully updated in OA_BUDGET_ITEMS in BudgetDetailsDaoImpl");

		} catch (Exception e) {
			logger.info("Exception in updateBudgetDetails() in BudgetDetailsDaoImpl :: " + e.getCause());
			e.printStackTrace();

		}
	}

	@Override
	public List<Integer> getBudgetYears() {
		try {
			logger.info("Inside getBudgetYears of BudgetDetailsDaoImpl");

			String query = "SELECT BUDGET_YEAR FROM " + DBAPPEND + "OA_BUDGET_YEAR";

			logger.info("Query For getBudgetYears:::" + query);

			List<Integer> bdYears = jdbcTemplate.queryForList(query, Integer.class);

			logger.info("Fetched Budget Details are:: " + bdYears);

			return bdYears;

		} catch (Exception e) {
			logger.info("Exception in  getBudgetDetails in BudgetDetailsDaoImpl " + e.getCause());
			return null;
		}
	}

	public void insertAuditDetails(Integer id, String fieldName, String oldValue, String newValue, String updatedBy) {
		try {
			// TODO Auto-generated method stub
			logger.info("Entered into insertAuditDetails()");
			String query = "INSERT INTO " + DBAPPEND
					+ "OA_AUDIT_TRAIL (ID,FIELDNAME,OLDVALUE,NEWVALUE,UPDATEDBY) VALUES (?,?,?,?,?)";
			jdbcTemplate.update(query, id, fieldName, oldValue, newValue, updatedBy);
			logger.info("Inserted Audit Data Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Cause of Exception in insertAuditDetails::" + e.getMessage());
			logger.error("Cause of Exception in insertAuditDetails::" + e.getCause());

		}
	}

	
	public AmountDetails getAmountDetails(Integer id) {
		String query = "SELECT BALANCE_AMOUNT,CONSUMED_AMOUNT FROM " + DBAPPEND
				+ "OA_BUDGET_ITEMS where BUDGET_ITEM_ID=" + id + "";
		logger.info("Query For getting balance amount/Consumed Amount ::" + query);
		try {
			AmountDetails listOfData = jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(AmountDetails.class));
			logger.info("Response after Excuting query::" + listOfData.toString());
			if (listOfData != null) {
				return listOfData;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception Raised while getting records from DB::" + e.getMessage());
			return null;
		}

	}

	/*public Double getBalanceAmount(Integer id) {
		try {
			logger.info("Calling getBalanceAmount()");
			List<Double> getAmount = jdbcTemplate.queryForList(
					"SELECT BALANCE_AMOUNT FROM " + DBAPPEND + "OA_BUDGET_ITEMS WHERE BUDGET_ITEM_ID=" + id + " ",
					Double.class);
			logger.info("Balance_Amount is: " + getAmount);

			if (getAmount != null && getAmount.size() > 0) {
				return getAmount.get(0);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Cause of Exception in getBalanceAmount()::" + e.getMessage());
			logger.error("Cause of Exception in getBalanceAmount()::" + e.getCause());
			return null;
		}

	}*/

	/*public Double getConsumedAmount(Integer id) {
		try {
			logger.info("Calling getConsumedAmount()");
			List<Double> getAmount = jdbcTemplate.queryForList(
					"SELECT CONSUMED_AMOUNT FROM " + DBAPPEND + "OA_BUDGET_ITEMS WHERE BUDGET_ITEM_ID=" + id + " ",
					Double.class);
			logger.info("Consumed_Amount is: " + getAmount);

			if (getAmount != null && getAmount.size() > 0) {
				return getAmount.get(0);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Cause of Exception in getConsumedAmount()::" + e.getMessage());
			logger.error("Cause of Exception in getConsumedAmount()::" + e.getCause());
			return null;
		}
	}*/
	
	@Override
	public List<AuditTrail2> getAuditTrailRecords(Reversal reversal) 
	{
		 // Implement logic to fetch audit trail records based on payment ID using jdbcTemplate
	    List<AuditTrail2> auditTrailFetchedData = null;
		String FETCH_QUERY = "select FIELDNAME,ID,OLDVALUE,NEWVALUE,UPDATEDON,UPDATEDBY,PYMT_REQ_ID from OA_AUDIT_TRAIL where PYMT_REQ_ID=?";
		Object[] args = {reversal.getPaymentId()};
		try 
		{
			logger.info("inside com.mashreq.oa.dao.BudgetDetailsDaoImpl class having getAuditTrailRecords()");
			if(reversal.getPaymentId()!=null)
			{
				logger.info("reversal.getPaymentId is: "+reversal.getPaymentId());
				if(reversal.getPaymentId()!=null)
				{
					Object[] args1 = {reversal.getPaymentId()};	
					args = args1;
				}
				auditTrailFetchedData = jdbcTemplate.query(FETCH_QUERY,BeanPropertyRowMapper.newInstance(AuditTrail2.class),args); //this is for List<AuditTrail>
				//auditTrailFetchedData = jdbcTemplate.queryForObject(FETCH_QUERY, new BeanPropertyRowMapper<AuditTrail>(AuditTrail.class),args);  //this is for  "AuditTrail"
				logger.info("auditTrailFetchData:"+auditTrailFetchedData);
				logger.info("BudgetDetailsDaoImpl.getAuditTrailRecords() Audit trail table data fetched successfully");
			}
			else
			{
				logger.info("BudgetDetailsDaoImpl.getAuditTrailRecords() Audit trail table data not fetched becoz paytMentId is Null");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("com.mashreq.oa.dao:BudgetDetailsDaoImpl class known DB exception is raised in getAuditTrailRecords() method:"+e.getMessage());
		}
		return auditTrailFetchedData;
	}

	
	
	@Override
	public BudgetDetailsOutput getBudgetItemById(int budgetItemId) 
	{
		logger.info("inside com.mashreq.oa.dao:BudgetDetailsDaoImpl class having getBudgetItemById() method");
		// Implement logic to fetch BudgetItem based on budgetItemId using jdbcTemplate
		BudgetDetailsOutput budgetItemData = null;
		Object[] args = {budgetItemId};
	    String sql = "SELECT * FROM OA_BUDGET_ITEMS WHERE budget_item_id = ?";
	   budgetItemData = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<BudgetDetailsOutput>(BudgetDetailsOutput.class),args);
	   logger.info("fetched data from oa_budget_items table by using bugetItemId: "+budgetItemData);
	    return budgetItemData;
	}

	@Override
	public void updateBudgetItemTable(BudgetDetailsOutput budgetItem)
	{
		logger.info("inside com.mashreq.oa.dao:BudgetDetailsDaoImpl class having updateBudgetItemTable() method");
		// Implement logic to update BudgetItem using jdbcTemplate
	    String sql = "UPDATE OA_BUDGET_ITEMS SET consumed_amount = ?, balance_amount = ? WHERE budget_item_id = ?";
	    jdbcTemplate.update(sql, budgetItem.getConsumedAmount(), budgetItem.getBalanceAmount(), budgetItem.getBudgetItemId());
	}

	public void logAuditTrail(BudgetDetailsOutput budgetItemsData, AuditTrail2 auditTrail,Double amount,String username, String comment) 
	{
		logger.info("inside com.mashreq.oa.dao:BudgetDetailsDaoImpl class having logAuditTrail() method");
        //insert into OA_AUDIT_TRAIL_LOG using jdbcTemplate  (here COMMENT is a reserved keyword in database so we are taking as "COMMENT")
        String sql = "INSERT INTO OA_AUDIT_TRAIL_LOG (ID, PYMT_REQ_ID, SERVICE_CODE, REVERSAL_AMOUNT, UPDATEDBY, UPDATEDON, \"COMMENT\")VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,auditTrail.getId(), auditTrail.getPymtReqId(),budgetItemsData.getServiceCode(),amount,username,auditTrail.getUpdatedOn(),comment);
    }

	@Override
	public List<AuditTrailLog> fetchAuditTrailLog(String serviceCode, String userName, Date updatedFrom,Date updatedTo) 
	{
		List<AuditTrailLog> fetchedListData = null;
		logger.info("BudgetDetailsDaoImpl class having fetchAuditTrailLog() method ");

		/*
		String sql = "SELECT ID as budgetItemId,PYMT_REQ_ID,SERVICE_CODE,REVERSAL_AMOUNT,UPDATEDBY,UPDATEDON,\"COMMENT\" FROM OA_AUDIT_TRAIL_LOG "
				+ "WHERE SERVICE_CODE = ? AND UPDATEDBY = ? AND UPDATEDON BETWEEN TO_DATE(?,'dd-MM-yy') AND TO_DATE(?,'dd-MM-yy')";   //ALSO WORKING
	
		*/
		
		String sql = "SELECT ID as budgetItemId,PYMT_REQ_ID,SERVICE_CODE,REVERSAL_AMOUNT,UPDATEDBY,UPDATEDON,\"COMMENT\" FROM OA_AUDIT_TRAIL_LOG "
				+ "WHERE SERVICE_CODE = ? AND UPDATEDBY = ? AND UPDATEDON >= TO_DATE(?,'dd-MM-yy') AND UPDATEDON < TO_DATE(?,'dd-MM-yy')";

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
		String newUpdatedFrom = formatter.format(updatedFrom);
		System.out.println(newUpdatedFrom);

		String newUpdatedTo = formatter.format(updatedTo);
		System.out.println(newUpdatedTo);

		try {
			fetchedListData = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AuditTrailLog.class),new Object[] { serviceCode, userName, newUpdatedFrom, newUpdatedTo });
			logger.info("BudgetDetailsDaoImpl class having fetchAuditTrailLog() method fetchedListData: "+ fetchedListData);
		} catch (DataAccessException dae) {
			dae.printStackTrace();
			logger.error(">>>>>>" + dae.getMessage());
		}
		return fetchedListData;
	}
}
