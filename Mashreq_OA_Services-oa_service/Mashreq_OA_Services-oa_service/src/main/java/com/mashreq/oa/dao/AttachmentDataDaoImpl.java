package com.mashreq.oa.dao;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.AgreementDetailsMapping;
import com.mashreq.oa.entity.AttachmentData;
import com.mashreq.oa.entity.ManagementCompany;

@Repository
public class AttachmentDataDaoImpl implements AttachmentDataDao {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(AttachmentDataDaoImpl.class);
	
	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	@Value("${OA-DBFLAG}")
	private String DBFLAG;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJDBCTemplate;
	
	@Override
    public int saveAttachmentData(AttachmentData data) {
           
           logger.info("saveAttachmentData() in Attachment Dao");
           
           try {
           
           data.setMgmtCompId(data.getMgmtCompId());
           
           logger.info("Payment Request Id: "+data.getPymtReqId());
           Integer pymtReqId=data.getPymtReqId();String seq = null;
           if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
           seq="SELECT NEXT VALUE FOR " +DBAPPEND+"OA_ATTACHMENTS_DATA_SEQ";
           }else {
                  seq="SELECT OA_ATTACHMENTS_DATA_SEQ.NEXTVAL FROM DUAL";
           }
           int sequenceValue=jdbcTemplate.queryForObject(seq, Integer.class);
                        logger.info("Sequence Value is::"+sequenceValue);
           String insertSql="INSERT INTO "+DBAPPEND+"OA_ATTACHMENTS_DATA(ATTACHMENT_DATA_ID,ISSUANCE_AUTHORITY,"
                        + "INVOICE_DATE_YEAR,INVOICE_AMOUNT,NO_PROPER_DOCUMENTS,TRADE_LICENSE_EXP_DATE,AGREEMENT_EXP_DATE,"
                        + "IS_GOVERNMENT_ENTITY,IS_INSURANCE_COMPANY,AUTO_RENEWAL, IS_EXCEPTIONAL_APPROVAL)"
                        + " values(:SEQ, :ISSUANCE_AUTHORITY, :INVOICE_DATE_YEAR, :INVOICE_AMOUNT, :NO_PROPER_DOCUMENTS,"
                                      + " :TRADE_LICENSE_EXP_DATE, :AGREEMENT_EXP_DATE, :IS_GOVERNMENT_ENTITY, :IS_INSURANCE_COMPANY,"
                                      + " :AUTO_RENEWAL, :IS_EXCEPTIONAL_APPROVAL)";
                  
    //KeyHolder keyHolder = new GeneratedKeyHolder();
           MapSqlParameterSource source=new MapSqlParameterSource();
           source.addValue("SEQ", sequenceValue);
           source.addValue("ISSUANCE_AUTHORITY", data.getIssuanceAuthority());
           source.addValue("INVOICE_DATE_YEAR", data.getInvoiceDateYear()!=null ? data.getInvoiceDateYear():null);
           source.addValue("INVOICE_AMOUNT", data.getInvoiceAmount());
           source.addValue("NO_PROPER_DOCUMENTS", data.getNoProperDocuments());
           
           if(data.getTradeLicenseExpDate() != null)
           {
                  source.addValue("TRADE_LICENSE_EXP_DATE",  data.getTradeLicenseExpDate());
           }
           else{
                  source.addValue("TRADE_LICENSE_EXP_DATE",  null);
           }
    
           if(data.getAgreementExpDate() != null)
           {
                  source.addValue("AGREEMENT_EXP_DATE", data.getAgreementExpDate());
           }
           else
           {
                  source.addValue("AGREEMENT_EXP_DATE", null);
           }
    
           
           source.addValue("IS_GOVERNMENT_ENTITY", data.getIsGovernmentEntity());
           source.addValue("IS_INSURANCE_COMPANY", data.getIsInsuranceCompany());
           source.addValue("AUTO_RENEWAL", data.getAutoRenewal());
           source.addValue("IS_EXCEPTIONAL_APPROVAL", data.getIsExceptionalApproval());
           
           
    //     namedParameterJDBCTemplate.update(insertSql, source, keyHolder, new String[] { "ATTACHMENT_DATA_ID" });
           namedParameterJDBCTemplate.update(insertSql, source );
           logger.info("Data inserted into OA_ATTACHMENTS_DATA"+data.getBifurcation());
           //Number attachmentId=keyHolder.getKey().intValue();
           
           if("true".equalsIgnoreCase(data.getAttachmentFlag())) {
                  
                  logger.info("Inside if :: Attachment flag is true");
                  boolean pymtFlag=false;
                  pymtFlag=checkforBulkpaymentData(pymtReqId);String updateSql=null;
                  if(pymtFlag) {
                	  updateSql="UPDATE "+DBAPPEND+"oa_payment_requests SET ATTACHMENT_DATA_ID=?, STATUS=?, ISMAILSENT=?"
                        		+ ""
                        		+ " WHERE MATRIX_FILE_REF_NO='"+data.getMatrixFileRefNo()+"' "
                                     + " AND STATUS='PENDING' ";
                        
                        int updateSttus=jdbcTemplate.update(updateSql , sequenceValue , "IN-PROGRESS", "N");

                        logger.info("AttachmentId Updated in OA_PAYMENT_REQUESTS for BULK payments, requests count:"+updateSttus);
                  }else {
                	  updateSql="UPDATE "+DBAPPEND+"oa_payment_requests SET ATTACHMENT_DATA_ID=?, STATUS=?, ISMAILSENT=?,"
                        		+ " SERVICE_CODE=?, BUDGET_YEAR=?"
                        		+ " WHERE MATRIX_FILE_REF_NO='"+data.getMatrixFileRefNo()+"' "
                                     + " AND STATUS='PENDING' ";
                        
                        int updateSttus=jdbcTemplate.update(updateSql , sequenceValue , "IN-PROGRESS", "N", data.getServiceCode() ,
                                data.getBudgetYear() );

                        logger.info("AttachmentId Updated in OA_PAYMENT_REQUESTS for BULK payments, requests count:"+updateSttus);
                  }
                   
           }else {
                  
                  logger.info("Inside else :: Attachment flag is false");
                
                
                  String updateSql="UPDATE "+DBAPPEND+"oa_payment_requests SET ATTACHMENT_DATA_ID=?, STATUS=?, SERVICE_CODE=?, BUDGET_YEAR=?,"
                             + "ISMAILSENT=?, BIFURCATION=?, REMARKS=?  WHERE PYMT_REQ_ID="+pymtReqId;
                	 int updateSttus=jdbcTemplate.update(updateSql , sequenceValue , "IN-PROGRESS" , data.getServiceCode() ,
                             data.getBudgetYear(), "N" ,  data.getBifurcation(), data.getRemarks() );
                	  logger.info("AttachmentId Updated in OA_PAYMENT_REQUESTS for Individual payments, requests count:"+updateSttus);
          
           }
           
           return sequenceValue;     
           
           }catch(Exception e) {
                  logger.info("Cause of Exception is::"+e.getMessage());
                  logger.info("Cause of Exception is::"+e.getCause());
                  return 0;
           }
           
           
    }

	
	private boolean checkforBulkpaymentData(Integer pymtReqId) {
		// TODO Auto-generated method stub
		boolean flag=false;
		AttachmentData approvedBudgetItems = null;
		try{
			logger.info("calling getApprovedBudgetItems in DAO");
			String query="select BUDGET_YEAR, SERVICE_CODE FROM "+DBAPPEND+"OA_PAYMENT_REQUESTS WHERE PYMT_REQ_ID='"+pymtReqId+"' ";
					
			logger.info("query of getApprovedBudgetItems:"+query);
			 approvedBudgetItems =  jdbcTemplate.queryForObject(query,
					BeanPropertyRowMapper.newInstance(AttachmentData.class));
			 
			 if(approvedBudgetItems!=null) {
				 if(approvedBudgetItems.getBudgetYear()!=null && approvedBudgetItems.getServiceCode()!=null) {
					 flag=true;
				 }
			 }
			}catch (Exception e) {
				e.printStackTrace();
				logger.info("Exception Cause:"+e.getMessage());
			}
		return flag;
	}


	/*
	 * update Trade License Expiry Date
	 */
	@Override
	public void updateTradeExpDate(Integer supplierId, Date tradeLicenseExpDate,String autoRenewal,
			String isGovernmentEntity, String isInsuranceCompany) {
		
		logger.info("Inside updateTradeExpDate() in AttachmentDataDaoImpl");
		
		try{
		String tradeUpadeSql="UPDATE "+DBAPPEND+"OA_SUPPLIERS SET TRADE_LICENSE_EXP_DATE=? , AUTO_RENEWAl=?, "
				+ "IS_GOVERNMENT_ENTITY=? , IS_INSURANCE_COMPANY=?  WHERE SUPPLIER_ID=?";
		
		Object[] args= {tradeLicenseExpDate, autoRenewal, isGovernmentEntity, isInsuranceCompany, supplierId};
		logger.info("updateTradeExpDate method dao query:"+tradeUpadeSql);
		jdbcTemplate.update(tradeUpadeSql,args);
		
		logger.info("TradeLicense Date successfully Updated in OA_SUPPLIERS");
		}
		catch(Exception e)
		{
			logger.error("Exception in updateTradeExpDate() in AttachmentDaoImpl:: "+e.getCause());
		}
	}
	
	

	/*
	 * Get Agreement ExpiryDate
	 */
	public Date getAgreementExpDate(Integer supplierId, Integer mgmtCompId) {
		
		logger.info("Calling getAgreementExpDate() in AttachmentDaoImpl");

		try{
		String agrDateSQl="select AGREEMENT_EXP_DATE from "+DBAPPEND+"OA_AGREEMENT_DETAILS where  SUPPLIER_ID=? and MGMT_COMP_ID=? ";
		
		Object[] args= {supplierId,mgmtCompId};
		
		@SuppressWarnings("deprecation")
		Date agrExpDate=jdbcTemplate.queryForObject(agrDateSQl,args,Date.class);
		
		return agrExpDate;
		}
		catch(Exception e)
		{
			logger.info("Exception in getAgreementExpDate() in AttachmentDaoImpl:: "+e.getCause());
			return null;
		}
		
	}
	
	
	
	/*
	 * Insert Agreement Expiry Date
	 */
	public void insertAgrExpDate(Integer supplierId, Integer mgmtCompId, Integer buildingId, Date agrExpDate) {
		
		logger.info("Inside insertAgrExpDate() in AttachmentDaoImpl");
		
		try{
		String agrInsertSql="INSERT INTO "+DBAPPEND+"OA_AGREEMENT_DETAILS (SUPPLIER_ID,MGMT_COMP_ID,AGREEMENT_EXP_DATE,BUILDING_ID) values (?, ?, ?, ?)";
		Object[] args= {supplierId, mgmtCompId, agrExpDate, buildingId};
		
		jdbcTemplate.update(agrInsertSql,args);
		
		logger.info("AgreementDate successfully inserted");
		}
		catch(Exception e)
		{
			logger.info("Exception in insertAgrExpDate() in AttachmentDaoImpl:: "+e.getCause());
			
		}
		
	}
	
	

	/*
	 * update Agreement Expiry Date
	 */
	public void updateAgrExpDate(Integer supplierId, Integer mgmtCompId, Integer buildingId, Date agreementExpDate) {
		
		logger.info("Inside updateAgrExpDate() in AttachmentDaoImpl");
		
		try{
		String agrUpdateSql="UPDATE "+DBAPPEND+"OA_AGREEMENT_DETAILS SET AGREEMENT_EXP_DATE=? WHERE SUPPLIER_ID=? AND MGMT_COMP_ID=?"
				+ " AND BUILDING_ID=? ";
		
		Object[] args= {agreementExpDate,supplierId,mgmtCompId,buildingId};
		
		jdbcTemplate.update(agrUpdateSql,args);
		
		logger.info("AgreementDate successfully Updated");
		}
		catch(Exception e)
		{
			logger.info("Exception in updateAgrExpDate() in AttachmentDaoImpl:: "+e.getCause());
		}
		
	}
	
	
	
	@Override
	public List<ManagementCompany> getManagementCompany() {
		
		logger.info("Calling getManagementDropdown() in AttachmentDaoImpl");
		
		try{
			
		String query = "SELECT MGMT_COMP_ID , "
				+ "MC_NAME_EN FROM "+DBAPPEND+"oa_management_companies ORDER BY MC_NAME_EN ASC";	
		List<ManagementCompany> companyList = jdbcTemplate.query(query,
				BeanPropertyRowMapper.newInstance(ManagementCompany.class));
		
		return companyList;
		}
		catch(Exception e){
			logger.info("Exception in getManagementCompany() in AttachmentDaoImpl:: "+e.getCause());
			return null;
		}
	}
	
	
	
	
	/*
	 * for displaying data in attachment screen
	 */
	
	@Override
	public AttachmentData getMatrixRefNo(Integer pymtReqId) {
		
		logger.info("Calling getMatrixRefNo() in AttachmentDaoImpl");

		try{
			String query="SELECT pmt.PYMT_REQ_ID, pmt.MATRIX_FILE_REF_NO, pmt.MATRIX_REF_NO as matrixRefNo, pmt.MATRIX_REF_NO as strMatrixRefNo,"
					+ "pmt.INVOICE_AMOUNT, pmt.SUPPLIER_ID, pmt.BUDGET_YEAR, pmt.SERVICE_CODE, "
					+ " pmt.MGMT_COMP_ID, pmt.BUILDING_ID, pmt.PAYMENT_TYPE, pmt.BIFURCATION, sp.TRADE_LICENSE_EXP_DATE,"
					+ " sp.AUTO_RENEWAL, sp.IS_GOVERNMENT_ENTITY, sp.IS_INSURANCE_COMPANY, agr.AGREEMENT_EXP_DATE " 
					+ "  FROM "+DBAPPEND+"oa_payment_requests pmt, "+DBAPPEND+"oa_suppliers sp,"
					+ " "+DBAPPEND+"oa_agreement_details agr WHERE pmt.SUPPLIER_ID=sp.SUPPLIER_ID AND"
					+ " pmt.MGMT_COMP_ID=agr.MGMT_COMP_ID AND pmt.SUPPLIER_ID=agr.SUPPLIER_ID AND"
					+ " PMT.BUILDING_ID=agr.BUILDING_ID AND pmt.PYMT_REQ_ID="+pymtReqId+"";			
		
		List<AttachmentData> attachmetDetails = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(AttachmentData.class));
		
		logger.info("Attchment Deatils from first query:: "+attachmetDetails);
		logger.info("Size:::" +attachmetDetails.size());
			
		if(attachmetDetails.size()==0)
		{
			logger.info("Inside if cause agreement date not present");
			
			String sql="SELECT pmt.PYMT_REQ_ID, pmt.MATRIX_FILE_REF_NO, pmt.MATRIX_REF_NO as matrixRefNo, pmt.MATRIX_REF_NO as strMatrixRefNo,"
					+ "pmt.INVOICE_AMOUNT, pmt.SUPPLIER_ID, pmt.BUDGET_YEAR, pmt.SERVICE_CODE, "
					+ " pmt.MGMT_COMP_ID, pmt.BUILDING_ID, pmt.PAYMENT_TYPE, pmt.BIFURCATION, sp.TRADE_LICENSE_EXP_DATE,"
					+ " sp.AUTO_RENEWAL, sp.IS_GOVERNMENT_ENTITY, sp.IS_INSURANCE_COMPANY " 
					+ "  FROM "+DBAPPEND+"oa_payment_requests pmt, "+DBAPPEND+"oa_suppliers sp "
					+ " WHERE pmt.SUPPLIER_ID=sp.SUPPLIER_ID AND"
					+ "  pmt.PYMT_REQ_ID="+pymtReqId+"";	
			
			List<AttachmentData> attachmetDetails2 = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(AttachmentData.class));
			
			logger.info("Attchment Deatils from second query:: "+attachmetDetails2);
			return attachmetDetails2.get(0);
		}
		
		else {
			return attachmetDetails.get(0);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Exception in getMatrixRefNo() in AttachmentDaoImpl:: "+e.getCause());
			return null;
		}
	}
	
	
	

	@Override
	public List<AttachmentData> getAttachmentData() {
		
		try{
		logger.info("Calling getAttchmentData() in AttachmentDaoImpl");
		
		return jdbcTemplate.query("SELECT * FROM "+DBAPPEND+"OA_ATTACHMENTS_DATA", 
				BeanPropertyRowMapper.newInstance(AttachmentData.class));
		}
		catch(Exception e)
		{
			logger.info("Exception in getAttachmentData() in AttachmentDaoImpl:: "+e.getCause());
			return null;
		}
	}
	
	

	/*
	 * for attachment dialog box
	 * count of sub requests
	 */
	@Override
	public Integer getSubPayments(long matrixFileRefNo) {
		
		try{
			
		logger.info("Calling getSubPayments() dao method");
		//String sql="select count(matrix_ref_no) from "+DBAPPEND+"oa_payment_requests where matrix_file_ref_no=?";
		
		String query = "SELECT COUNT(PYMT_REQ_ID) FROM "+DBAPPEND+"OA_PAYMENT_REQUESTS WHERE MATRIX_FILE_REF_NO='"+matrixFileRefNo+"' AND STATUS='PENDING' ";
		

		@SuppressWarnings("deprecation")
		Integer count = this.jdbcTemplate.queryForObject(query, Integer.class);

		logger.info("getSubPayments count:::" + count);
		return count;
		
	}
		catch(Exception e)
		{
			logger.info("Exception in getSubPayments() in AttachmentDaoImpl:: "+e.getCause());
			return 0;
		}
	}
	
	

	/*
	 * for displaying TRADE_LICENSE_EXP_DATE dynamically.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public AttachmentData  getExpiryDates(Integer splyer_id) {
		
		logger.info("Calling getExpiryDates() dao method");

		try{
		String sql="select TRADE_LICENSE_EXP_DATE, ENTITY_TYPE, AUTO_RENEWAL from "+DBAPPEND+"OA_SUPPLIERS where SUPPLIER_ID=?";
		Object[] args = {splyer_id};

		//@SuppressWarnings("deprecation")
		//Date date = this.jdbcTemplate.queryForObject(sql, args, Date.class);
				
		return this.jdbcTemplate.queryForObject(sql, args, BeanPropertyRowMapper.newInstance(AttachmentData.class));
		}
		catch(Exception e)
		{
			logger.info("Exception in getExpiryDates() in AttachmentDaoImpl:: "+e.getCause());
			return null;
		}
	}
	
	

	/*
	 * for displaying agreement_expiry_date dynamically 
	 */
	@Override
	public AttachmentData getExpiryDates(Integer splyer_id,Integer mgmt_cpny_id) {
		
		try{
		logger.info("Calling getExpiryDates(args) AttachmentDaoImpl method");
		
		String sql="SELECT AGREEMENT_EXP_DATE  from "+DBAPPEND+"OA_AGREEMENT_DETAILS where supplier_id=? AND mgmt_comp_id=? ";
		Object[] args = {splyer_id,mgmt_cpny_id};
		
		List<AttachmentData> attachmentData=jdbcTemplate.query(sql, args, BeanPropertyRowMapper.newInstance(AttachmentData.class));
		logger.info("getExpiryDates AttachmentData:::" + attachmentData);
		
		if(attachmentData!=null && attachmentData.size()>0) {
			return attachmentData.get(0);
		}else {
			return null;
		}
		}
		catch(Exception e)
		{
			logger.info("Exception in getExpiryDates(args) in AttachmentDaoImpl:: "+e.getCause());
			return null;
		}
		
	}
	
	@Override
public boolean checkForAgrDetails(Integer mgmtCompId, Integer supplierId,Integer buildingId) {
		boolean flag = false;
		try{
			logger.info("Calling checkForAgrDetails() in AttachmentDaoImpl");
			
			
			
			String query = "SELECT MGMT_COMP_ID , SUPPLIER_ID, BUILDING_ID FROM "+DBAPPEND+"OA_AGREEMENT_DETAILS "
					+ "WHERE MGMT_COMP_ID="+mgmtCompId+ " AND SUPPLIER_ID="+supplierId+" AND BUILDING_ID="+buildingId ;
			
			List<AgreementDetailsMapping> agrMappingList = jdbcTemplate.query(query,
					BeanPropertyRowMapper.newInstance(AgreementDetailsMapping.class));
			
			if(agrMappingList != null && agrMappingList.size()>0)
			{
				flag=true;
				
			}
			
		}
		catch(Exception e)
		{
			logger.info("Exception in checkForAgrDetails() in AttachmentDaoImpl:: "+e.getCause());
			return false;
		}
		return flag;
	}
	
	@Override
	public String getStatusByPaymentreqID(Integer pymtReqId) {
		try{
			
			logger.info("Calling getStatusByPaymentreqID() in AttachmentDao");
			String query = "SELECT STATUS FROM "+DBAPPEND+"OA_PAYMENT_REQUESTS WHERE PYMT_REQ_ID="+pymtReqId;
			
			String status = jdbcTemplate.queryForObject(query,String.class);
			
			logger.info("Status is>>"+status);
			
			return status;
			
		}
		catch(Exception e)
		{
			logger.info("Exception raised in getStatusByPaymentreqID in AttachmentDao:: "+e.getCause());
			return null;
		}
	}

}




