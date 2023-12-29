package com.mashreq.oa.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.Buildings;
import com.mashreq.oa.entity.MappingData;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;
import com.mashreq.oa.entity.PropertyGroups;

import ch.qos.logback.classic.Logger;

@Repository
public class PaymentRequestDaoImpl implements PaymentDataDao{
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(PaymentRequestDaoImpl.class);
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJDBCTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${OA-DBFLAG}")
	private String DBFLAG;
	
	@Value("${DBAPPEND}")
	private String DBAPPEND;

	@Override
	public void uploadExcel(List<PaymentData> data) {
		try {
			logger.info("uploadExcel() in PaymentDao");String seq =null;
			if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
			seq= "NEXT VALUE FOR " +DBAPPEND+"OA_PAYMENT_REQUESTS_SEQ";
			}else {
				seq="OA_PAYMENT_REQUESTS_SEQ.NEXTVAL";
			}
			for(int i=0;i<data.size();i++) {
				 
						 	
				String sql="INSERT INTO "+DBAPPEND+"oa_payment_requests (PYMT_REQ_ID, MATRIX_REF_NO, SUB_PRODUCT, "
						+ "DEBIT_ACCOUNT_NUMBER_DESC"
						+ ", BENEFICIARY_NAME, INITIATOR_DATE, PAYMENT_CURRENCY, INVOICE_AMOUNT, CUSTOMER_REFERENCE,"
						+ " INITIATOR_NAME_DATE_TIME, STATUS, DEBIT_ACCOUNT_NO, SUPPLIER_ID,UPLOADED_BY ,PAYMENT_TYPE, MATRIX_FILE_REF_NO,"
						+ " MGMT_COMP_ID, BUILDING_ID, SERVICE_CODE, BUDGET_YEAR, BIFURCATION)"
						+ " values(("+seq+"),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
			jdbcTemplate.update(sql,data.get(i).getMatrixRefNo(),data.get(i).getSubProduct(),
					data.get(i).getDebitAccountNumberDesc(),data.get(i).getBeneficiaryName(),
					data.get(i).getInitiatorDates(),data.get(i).getPaymentCurrency(),
					data.get(i).getInvoiceAmount(),data.get(i).getCustomerReference(),
					data.get(i).getInitiatorNameDateTime(),data.get(i).getStatus(),
					data.get(i).getDebitAccountNo(),data.get(i).getSupplierId(),
					data.get(i).getUploadedBy(),data.get(i).getPaymentType(),data.get(i).getMatrixFileRefNo(),
					data.get(i).getMgmtCompId(),data.get(i).getBuildingId(),
					data.get(i).getServiceCode(),data.get(i).getBudgetYear()
					,data.get(i).getBifurcation());			
			
		}
			logger.info("Data inserted into OA_PAYMENT_REQUESTS");

		}
		catch(Exception e) {
			logger.info("Failed to Upload Data into OA_PAYMENT_REQUESTS::"+e.getCause());
		}
		
		
			}

	@Override
	public List<PaymentData> getListData() {
		List<PaymentData> data=new ArrayList<PaymentData>();
		try {
		logger.info("getListData() in PaymentDao");
		
		String status="PENDING";
	
		String sql="SELECT PYMT_REQ_ID,MATRIX_REF_NO as matrixRefNo, MATRIX_REF_NO as strMatrixRefNo,SUB_PRODUCT,DEBIT_ACCOUNT_NUMBER_DESC,BENEFICIARY_NAME,"
				+ "INITIATOR_DATE,PAYMENT_CURRENCY,INVOICE_AMOUNT,CUSTOMER_REFERENCE,INITIATOR_NAME_DATE_TIME,"
				+ "STATUS,UPLOADED_BY,DEBIT_ACCOUNT_NO,SUPPLIER_ID,"
				+ "MATRIX_FILE_REF_NO FROM "+DBAPPEND+"oa_payment_requests WHERE STATUS=? ORDER BY UPLOADED_ON DESC";
		
		Object[] args= {status};
		
			//@SuppressWarnings("deprecation")
			  data=
					jdbcTemplate.query(sql, args,
							BeanPropertyRowMapper.newInstance(PaymentData.class));
			
		}catch(Exception e) {
			logger.info("cause of Exception is::"+e.getMessage()+"=="+e.getCause());
		}
		
		
		return data;
	}

	
	@Override
	public List<Long> getMatrixRefNos(List<Long> matrixList) {
		
		logger.info("Calling getMatrixRefNos() in PaymentRequestDaoImpl");

		try{
		String sql = null;
		
		sql = "SELECT MATRIX_REF_NO FROM "+DBAPPEND+"OA_PAYMENT_REQUESTS"
				+ " WHERE MATRIX_REF_NO IN ('";
		Object[] matrixData=((List<Long>) matrixList).toArray();
		String result=StringUtils.join(matrixData,"','");
		sql=sql+result+"')  AND  STATUS!='REJECTED'";  //changed by Naveen
		
		
		System.out.println(sql);
		
		List<Long> outputMatrixNo = jdbcTemplate.queryForList(sql,Long.class);
		
		return outputMatrixNo;
		}
		
		catch(Exception e)
		{
			logger.info("Exception in getMatrixRefNos() in PaymentRequestDaoImpl "+e.getCause());
			return null;
		}
	}

	@Override
	public List<Integer> getMatrixFileRefNoList() {
		
		logger.info("Calling getMatrixFileRefNoList() in PaymentRequestDaoImpl");

		try{
		List<Integer> matrixFileNo = jdbcTemplate.queryForList("SELECT DISTINCT(MATRIX_FILE_REF_NO) FROM "+DBAPPEND+"OA_PAYMENT_REQUESTS ", Integer.class);
		
		return matrixFileNo;
		}
		catch(Exception e)
		{
			logger.info("Exception in getMatrixFileRefNoList() in PaymentRequestDaoImpl "+e.getCause());
			return null;
		}
	}

	@Override
	public List<String> getSupplierList() {
		
		logger.info("Calling getSupplierList() in PaymentRequestDaoImpl");

		try{
			String sql = "SELECT SUPPLIER_NAME FROM "+DBAPPEND+"OA_SUPPLIERS";
			//List<String> suppliers = jdbcTemplate.queryForList(sql,String.class);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<String> suppliers = jdbcTemplate.query(sql,new RowMapper() {

				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					
					return rs.getString(1);
				}
			});
			if(suppliers.isEmpty()) {
				return null;
			}else {
				return suppliers;
			}
			
		}
		catch(Exception e)
		{
			logger.info("Exception in getSupplierList in PaymentRequestDaoImpl "+e.getCause());
			return null;
		}
	}

	@Override
	public Integer insertSupplier(String supplierName) {
		
		logger.info("Inside insertsupplier() in PaymentRequestDaoImpl");
		try{
			
			String seq =null;
			if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
			seq= "SELECT NEXT VALUE FOR " +DBAPPEND+"OA_SUPPLIERS_SEQ";
			}else {
				seq="SELECT OA_SUPPLIERS_SEQ.NEXTVAL FROM DUAL";
			}
		
		//String seq = "SELECT NEXT VALUE FOR " +DBAPPEND+"OA_SUPPLIERS_SEQ";
		Integer sequenceValue = jdbcTemplate.queryForObject(seq, Integer.class);
		logger.info("Sequence Value of supplier id is:: "+sequenceValue);
		
		String insertSql = "INSERT INTO "+DBAPPEND+"OA_SUPPLIERS(SUPPLIER_ID,SUPPLIER_NAME) VALUES(:SEQ,:SUPPLIER_NAME)";
		
		
		MapSqlParameterSource source=new MapSqlParameterSource();
		
		source.addValue("SEQ", sequenceValue);
		source.addValue("SUPPLIER_NAME", supplierName);
		
		namedParameterJDBCTemplate.update(insertSql, source );
		logger.info("Data inserted into OA_SUPPLIERS");
		
		return sequenceValue;
		
	}
		catch(Exception e)
		{
			logger.info("Exception in insertSupplier() in PaymentRequestDaoImpl");
			logger.info(e.getMessage() +" == "+ e.getCause());
			return 0;
		}

}

	@Override
	public Integer getSupplierId(String supplierName) {
		
		logger.info("Inside getSupplierId() in Dao");
		
		Integer supplierId = null;
		try{
			
			String sql = "SELECT SUPPLIER_ID FROM "+DBAPPEND+"OA_SUPPLIERS WHERE upper(SUPPLIER_NAME)=upper('"+supplierName+"' )" ;
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<Integer> suppliers = jdbcTemplate.query(sql,new RowMapper() {

				@Override
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					
					return rs.getInt(1);
				}
			});
			if(suppliers.isEmpty()) {
				return null;
			}else {
				supplierId = suppliers.get(0);
			}
			
			
			
			logger.info("Supplier id in dao is:: "+supplierId);
			return supplierId;
			
		}
		catch(Exception e)
		{
			logger.info("Inside getSupplierId catch block");
			logger.info(e.getMessage()+ " === " +e.getCause());
			return 0;
		}
		
		
	}

	@Override
	public List<String> getBuildingList() {
		logger.info("Calling getBuildingList() in PaymentRequestDaoImpl");

		try{
			String sql = "SELECT BUILDING_NAME FROM "+DBAPPEND+"OA_BUILDINGS";
			List<String> buildingNames = jdbcTemplate.queryForList(sql,String.class);
			
			return buildingNames;
		}
		catch(Exception e)
		{
			logger.info("Exception in getBuildingList() in PaymentRequestDaoImpl "+e.getCause());
			return null;
		}
	}
	
	public boolean checkForBuildingName(String buildingName) {
		logger.info("Calling checkForBuildingName() in PaymentRequestDaoImpl");
		boolean status=false;
		try{
			
			buildingName=buildingName.trim();
			String sql = "SELECT BUILDING_NAME FROM "+DBAPPEND+"OA_BUILDINGS WHERE upper(building_name)=upper('"+buildingName+"' )" ;
			logger.info("SQl for Getting Buildings is"+sql);
			List<String> buildingNames = jdbcTemplate.query(sql,
					BeanPropertyRowMapper.newInstance(String.class));
			
			if(buildingNames!=null && buildingNames.size()>0) {
				status=true;
				return status;
			}
		}
		catch(Exception e)
		{
			logger.info("Exception in getBuildingList() in PaymentRequestDaoImpl "+e.getCause());
			return status;
		}
		return status;
	}

	@Override
	public Integer insertBuilding(String buildingName) {

		logger.info("Inside insertBuilding() PaymentRequestDaoImpl");
		try{
			String seq =null;
			if(DBFLAG.equalsIgnoreCase("SQLSERVER")) {
			seq= "SELECT NEXT VALUE FOR " +DBAPPEND+"OA_BUILDINGS_SEQ";
			}else {
				seq="SELECT OA_BUILDINGS_SEQ.NEXTVAL FROM DUAL";
			}	
		
		//String seq = "SELECT NEXT VALUE FOR " +DBAPPEND+"OA_BUILDINGS_SEQ";
		Integer sequenceValue = jdbcTemplate.queryForObject(seq, Integer.class);
		logger.info("Sequence Value of building id is:: "+sequenceValue);
		
		String insertSql = "INSERT INTO "+DBAPPEND+"OA_BUILDINGS(BUILDING_ID,BUILDING_NAME,IS_ACTIVE) VALUES(:SEQ,:BUILDING_NAME,:IS_ACTIVE)";
		
		
		MapSqlParameterSource source=new MapSqlParameterSource();
		
		source.addValue("SEQ", sequenceValue);
		source.addValue("BUILDING_NAME", buildingName.trim());
		source.addValue("IS_ACTIVE", "Y");
		
		namedParameterJDBCTemplate.update(insertSql, source );
		logger.info("Data inserted into OA_BUILDINGS");
		
		return sequenceValue;
		
	}
		catch(Exception e)
		{
			logger.info("inside catch insertBuilding() in PaymentRequestDaoImpl");
			logger.info(e.getMessage() +" == "+ e.getCause());
			return 0;
		}
	}

	@Override
	public Integer getBuildingId(String buildingName) {
		logger.info("Inside getBuildingId() in PaymentRequestDaoImpl");
		
		Integer buildingId = null;
		try{
			buildingName=buildingName.trim();
			String sql = "SELECT MIN(BUILDING_ID) FROM "+DBAPPEND+"OA_BUILDINGS WHERE upper(BUILDING_NAME) = upper('"+buildingName+"' )";
			
			buildingId = jdbcTemplate.queryForObject(sql,Integer.class);
			
			logger.info("Building id in dao is:: "+buildingId);
			return buildingId;
			
		}
		catch(Exception e)
		{
			logger.info("Inside getBuildingId() catch block in PaymentRequestDaoImpl");
			logger.info(e.getMessage()+ " === " +e.getCause());
			return 0;
		}
	}

	@Override
	public String getManagementCompany(Integer mgmtCompId) {
		logger.info("Calling getManagementCompany() in PaymentRequestDaoImpl");
		try{
		
		String query = "SELECT MC_NAME_EN FROM "+DBAPPEND+"OA_MANAGEMENT_COMPANIES WHERE MGMT_COMP_ID ="+mgmtCompId+"";
		
		//Object[] args = {mgmtCompId};
		
		String mcName = jdbcTemplate.queryForObject(query, String.class);
		
		logger.info("McName in Dao is:: "+mcName);
		
		return mcName;
		}
		catch(Exception e)
		{
			logger.info("Exception in getManagementCompany() in PaymentRequestDaoImpl:: "+e.getCause());
			return null;
		}
	}

	@Override
	public Integer getPropId(String buildingName,Integer mgmtCompId) {
		
		try{
			logger.info("Calling getPropId() in PaymentRequestDaoImpl");
			buildingName=buildingName.trim();
			String query = "SELECT props.PROP_ID FROM "+DBAPPEND+"OA_PROPERTY_GROUPS props"
					+ " WHERE UPPER(props.PROPERTY_GROUP_NAME_EN)= UPPER('"+buildingName+"') AND props.MGMT_COMP_ID="+mgmtCompId+" ";
			
			List<Integer> propIds = jdbcTemplate.queryForList(query, Integer.class);
			
			logger.info("Prop Id fetched from Db:: "+propIds.get(0));
			
			if(propIds != null && propIds.size()>0){
				return propIds.get(0);
			}
			
			
		}
		catch(Exception e)
		{
			logger.info("Exception in  getPropId() in PaymentRequestDaoImpl");
			logger.info(e.getMessage()+" == "+e.getCause());
			return 0;
		}
		return 0;
		
	}
	
	@Override
	public boolean checkForMapping(Integer buildingId, Integer propId) {
		
		boolean flag = false;
		
		try{
			String  query = "SELECT BUILDING_ID, PROP_ID FROM "+DBAPPEND+"OA_BUILDING_PROPGROUP_MAPPING WHERE "
					+ " BUILDING_ID="+buildingId+" AND PROP_ID="+propId+" ";
			
			logger.info("QUERY FOR CHECKMAPPING:: "+query);
			
			List<MappingData> mapData = jdbcTemplate.query(query, 
					BeanPropertyRowMapper.newInstance(MappingData.class));
			
			logger.info("Mapping Data from MappingTable is:: "+mapData);
			
			if(mapData != null && mapData.size()>0)
			{
				flag = true;
				return flag;
			}
			
			
		}
		catch(Exception e)
		{
			logger.info("Exception in  checkForMapping() in PaymentRequestDaoImpl");
			logger.info(e.getMessage()+" == "+e.getCause());
			return flag;
		}
		return flag;
		
	}

	
	@Override
	public void insertMappingData(Integer buildingId, Integer propId, Integer mgmtId) {

		try{
			logger.info("Calling insertMappingData() in PaymentRequestDaoImpl");
			
			String query = "INSERT INTO "+DBAPPEND+"OA_BUILDING_PROPGROUP_MAPPING(BUILDING_ID,PROP_ID,MGMT_COMP_ID) VALUES(?,?,?)";
			
			Object[] args = {buildingId,propId,mgmtId};
			
			jdbcTemplate.update(query,args);
			
			logger.info("Data inserted into OA_BUILDING_PROPGROUP_MAPPING");
			
		}
		catch(Exception e)
		{
			logger.info("Exception in  insertMappingData() in PaymentRequestDaoImpl");
			logger.info(e.getMessage()+" == "+e.getCause());
		}
	}

	
	

	@Override
    public List<PaymentData> getSearchList(PaymentSearchInput searchInput) {
           try {
           logger.info("calling getSearchList() in PaymentDaoImpl");
           
           String status="PENDING";
           String sql="";
    
           if(searchInput.getBuildingId() != null && !"".equals(searchInput.getBuildingId())){
        	   sql="SELECT PYMT_REQ_ID,MATRIX_REF_NO as matrixRefNo, MATRIX_REF_NO as strMatrixRefNo,SUB_PRODUCT,DEBIT_ACCOUNT_NUMBER_DESC,BENEFICIARY_NAME,"
   					+ "INITIATOR_DATE,PAYMENT_CURRENCY,INVOICE_AMOUNT,CUSTOMER_REFERENCE,INITIATOR_NAME_DATE_TIME,"
   					+ "STATUS,UPLOADED_BY,DEBIT_ACCOUNT_NO,SUPPLIER_ID,"
   					+ "MATRIX_FILE_REF_NO FROM "+DBAPPEND+"oa_payment_requests WHERE STATUS=? AND MGMT_COMP_ID=? AND "
   							+ "BUILDING_ID=?";
                  
                  Object[] args= {status,searchInput.getMgmtCompId(),searchInput.getBuildingId()};
                  List<PaymentData> searchList= jdbcTemplate.query(sql, args,
                               BeanPropertyRowMapper.newInstance(PaymentData.class));
                  
                  return searchList;
                  
           }else{
        	   sql="SELECT PYMT_REQ_ID,MATRIX_REF_NO as matrixRefNo, MATRIX_REF_NO as strMatrixRefNo,SUB_PRODUCT,DEBIT_ACCOUNT_NUMBER_DESC,BENEFICIARY_NAME,"
   					+ "INITIATOR_DATE,PAYMENT_CURRENCY,INVOICE_AMOUNT,CUSTOMER_REFERENCE,INITIATOR_NAME_DATE_TIME,"
   					+ "STATUS,UPLOADED_BY,DEBIT_ACCOUNT_NO,SUPPLIER_ID,"
   					+ "MATRIX_FILE_REF_NO FROM "+DBAPPEND+"oa_payment_requests WHERE STATUS=? AND MGMT_COMP_ID=?";
                  
                  Object[] args= {status,searchInput.getMgmtCompId()};
                  List<PaymentData> searchList=jdbcTemplate.query(sql, args,
                               BeanPropertyRowMapper.newInstance(PaymentData.class));
                  
                  return searchList;
                  
           }
           

                  
           }catch(Exception e) {
                  logger.info("Exception in getSearchList() in PaymentDaoImpl ::"+e.getMessage()+"=="+e.getCause());
                  return null;
           }
           //return null;
           
    }

	
	
	@Override
	public List<Buildings> getBuildings(Integer mgmtCompId) {

		try {
		logger.info("calling getBuildings() in PaymentDaoImpl");
			
		String query = "SELECT PROP_ID FROM "+DBAPPEND+"OA_PROPERTY_GROUPS WHERE MGMT_COMP_ID="+mgmtCompId;
		
		List<Integer> propIds = jdbcTemplate.queryForList(query,Integer.class);
		
		logger.info("List of propIds from OA_PROPERTY_GROUPS is :: "+propIds);
		

		List<Integer> buildingIds =null;
		if(propIds.size() >0 && propIds!= null)
		{
			logger.info("Inside if to getBuildingIds");
			String queryBuildId = null;
			queryBuildId = "SELECT DISTINCT(BUILDING_ID) FROM "+DBAPPEND+"OA_BUILDING_PROPGROUP_MAPPING WHERE PROP_ID IN (";
			
			Object[] bd = propIds.toArray();
			String result = StringUtils.join(bd,",");
			queryBuildId = queryBuildId+result+")";
			
			logger.info("QUERY FOR GETTING BUILDING IDS::: "+queryBuildId);
			buildingIds = jdbcTemplate.queryForList(queryBuildId,Integer.class);
			
			logger.info("Building Ids From OA_BUILDING_PROPGROUP_MAPPING IS::: "+buildingIds);
		}
		
		List<Buildings> bdList = null;
		if(buildingIds != null && buildingIds.size()>0)
		{
			logger.info("Inside if to getBuildingNames");
			String queryBuildName = null;
			queryBuildName = "SELECT BUILDING_ID,BUILDING_NAME FROM "+DBAPPEND+"OA_BUILDINGS WHERE BUILDING_ID IN (";
			
			Object[] bn = buildingIds.toArray();
			String resultSet = StringUtils.join(bn,",");
			queryBuildName = queryBuildName+resultSet+")";
			
			logger.info("QUERY FOR GETTING BUILDING NAMES::: "+queryBuildName);
			bdList = jdbcTemplate.query(queryBuildName,BeanPropertyRowMapper.newInstance(Buildings.class));
			
			logger.info("List OF BUILDING NAMES FOR MgmtCompID "+mgmtCompId+" is "+bdList);
		
		}
		
		return bdList;
		
				
			}catch(Exception e) {
				logger.info("Exception in getBuildings() in PaymentDaoImpl ::"+e.getMessage()+"=="+e.getCause());
				e.printStackTrace();
				return null;
			}
	}
	public static void main(String[] args) {
		String d="019000052466 - CLAVERTON HOUSE 2";
		d= d.split("-|\\[")[1];
		System.out.println("value of dis:"+d.trim());
	}

	@Override
	public List<PropertyGroups> getProperties(Integer mgmtCompId) {
		// TODO Auto-generated method stub
		
		try {
			logger.info("calling getProperties() in PaymentDaoImpl");
			String query = "SELECT PROP_ID, PROPERTY_GROUP_NAME_EN FROM "+DBAPPEND+"OA_PROPERTY_GROUPS WHERE MGMT_COMP_ID="+mgmtCompId;
			logger.info("calling getProperties() in PaymentDaoImpl"+query);
			List<PropertyGroups> propIds  =  jdbcTemplate.query(query, 
					BeanPropertyRowMapper.newInstance(PropertyGroups.class));
			
			logger.info("List of propIds from OA_PROPERTY_GROUPS is :: "+propIds);
			return propIds;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
