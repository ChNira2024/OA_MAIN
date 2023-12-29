package com.mashreq.oa.dao;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mashreq.oa.entity.ReserveFundSearchInputData;
@Repository
public class ReserveFundSearchDAOImpl implements ReserveFundSearchDAO {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(ReserveFundSearchDAOImpl.class);
	@Autowired
	private JdbcTemplate jt;
	@Value("${DBAPPEND}")
	private String DBAPPEND;
	
	@Override
	public List<ReserveFundSearchInputData> getDataDetailsDAO(Long mgmtId, Long bldId) {
		String resQuery = "select res.reserve_fund_id , res.account_number, res.cif_number, mngt.MC_NAME_EN, bdg.building_name,"
				+ " res.reserve_account_number, res.is_active,res.branch_code,res.reserve_fund_percentage,res.last_calculated_on from "+DBAPPEND+"OA_RESERVE_FUND_DETAILS res,"
				+ " "+DBAPPEND+"OA_MANAGEMENT_COMPANIES mngt,"+DBAPPEND+"OA_BUILDINGS bdg where res.building_id=bdg.building_id "
						+ "AND res.mgmnt_comp_id=mngt.mgmt_comp_id AND res.mgmnt_comp_id=?";
		Object[] args = {mgmtId};
		List<ReserveFundSearchInputData> list =null;
		if(bldId!=null){
			Object[] args1 = {mgmtId,bldId};
			args = args1;
			resQuery = resQuery + "  AND res.building_id=?";			
		}
		
		try {
			logger.info("com.oa.dao.Impl::ReserveFundSearchDAOImpl class getDataDetailsDAO() is executed successfully");
			list=jt.query(resQuery, args, BeanPropertyRowMapper.newInstance(ReserveFundSearchInputData.class));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("com.oa.dao.Impl::ReserveFundSearchDAOImpl class known DB exception is raised in getDataDetailsDAO() method:"+e.getMessage());
		}
		return list;
	}
	//changes for getall records in 'all' condition--->raghuram
	public List<ReserveFundSearchInputData> getAllreserveFundRecords() {
		String resQuery = "select res.reserve_fund_id , res.account_number, res.cif_number, mngt.MC_NAME_EN, bdg.building_name,"
				+ " res.reserve_account_number, res.is_active,res.branch_code,res.reserve_fund_percentage,res.last_calculated_on from "+DBAPPEND+"OA_RESERVE_FUND_DETAILS res,"
				+ " "+DBAPPEND+"OA_MANAGEMENT_COMPANIES mngt,"+DBAPPEND+"OA_BUILDINGS bdg where res.building_id=bdg.building_id AND res.mgmnt_comp_id=mngt.mgmt_comp_id";
		//Object[] args = {mgmtId};
		List<ReserveFundSearchInputData> list =null;
		try {
			logger.info("getAllDataDetails() is executing");
			list=jt.query(resQuery, BeanPropertyRowMapper.newInstance(ReserveFundSearchInputData.class));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("com.oa.dao.Impl::ReserveFundSearchDAOImpl class known DB exception is raised in getDataDetailsDAO() method:"+e.getMessage());
		}
		return list;
	}


	

	@Override
	public String insertReserveFundData(Integer reserve_Fund_Id, String account_Number, String cif_Number,
			Integer mgmnt_Comp_Id, Integer building_Id, String reserve_Account_Number, String is_Active, String branch_Code) {
		String seq="SELECT NEXT VALUE FOR " +DBAPPEND+"RESERVE_FUND_ID";
		int sequenceValue=jt.queryForObject(seq, Integer.class);
		String INSERT_DATA= "INSERT INTO "+DBAPPEND+"OA_RESERVE_FUND_DETAILS(reserve_Fund_Id,account_Number,cif_Number,mgmnt_Comp_Id,building_Id,reserve_Account_Number,is_Active,branch_Code) "
				+ " VALUES("+sequenceValue+",?,?,?,?,?,?,?)";
		int count=0;
		try {
			logger.info("com.oa.dao.Impl::ReserveFundSearchDAOImpl class insertReserveFundData() is executed successfully");
			count = jt.update(INSERT_DATA, account_Number, cif_Number,mgmnt_Comp_Id,building_Id,reserve_Account_Number,is_Active,branch_Code);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("com.oa.dao.Impl::ReserveFundSearchDAOImpl class known DB exception is raised in insertReserveFundData() method:"+e.getMessage());
		}
		System.out.println(count);
		return "success";
	}




	@Override
	public String updateReserveFundData(Integer reserveFundId, String accountNumber,String reserveAccountNumber, String isActive,String branchCode) {
		String UPDATE_DATA= "UPDATE "+DBAPPEND+"OA_RESERVE_FUND_DETAILS SET ACCOUNT_NUMBER=?,RESERVE_ACCOUNT_NUMBER=?,IS_ACTIVE=?,BRANCH_CODE=? WHERE RESERVE_FUND_ID=?";
		int count=0;
		try {
			logger.info("com.oa.dao.Impl::ReserveFundSearchDAOImpl class updateReserveFundData() is executed successfully");
			count=jt.update(UPDATE_DATA,accountNumber,reserveAccountNumber,isActive,branchCode,reserveFundId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("com.oa.dao.Impl::ReserveFundSearchDAOImpl class known DB exception is raised in updateReserveFundData() method:"+ e.getMessage());
		}
		
		System.out.println(count);
		return "Data update successfully";
	}


	
	
}
