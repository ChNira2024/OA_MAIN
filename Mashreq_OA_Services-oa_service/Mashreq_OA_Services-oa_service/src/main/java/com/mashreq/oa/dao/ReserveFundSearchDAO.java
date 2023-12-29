package com.mashreq.oa.dao;

import java.util.List;

import com.mashreq.oa.entity.ReserveFundSearchInputData;

public interface ReserveFundSearchDAO {

	public List<ReserveFundSearchInputData> getDataDetailsDAO(Long mgmtId, Long bldId);

	public String insertReserveFundData(Integer reserve_Fund_Id, String account_Number, String cif_Number,
			Integer mgmnt_Comp_Id, Integer building_Id, String reserve_Account_Number, String is_Active,
			String branch_Code);

	public String updateReserveFundData(Integer reserveFundId, String accountNumber, String reserveAccountNumber,
			String isActive, String branchCode);

	public List<ReserveFundSearchInputData> getAllreserveFundRecords();
}
