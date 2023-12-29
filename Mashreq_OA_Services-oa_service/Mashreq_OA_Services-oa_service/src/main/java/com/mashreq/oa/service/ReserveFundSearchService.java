package com.mashreq.oa.service;
import java.util.List;

import com.mashreq.oa.entity.ReserveFundSearchInputData;

public interface ReserveFundSearchService {

	public   List<ReserveFundSearchInputData> fetchDataDetailsByService(Long mgmtId,Long bldId);
	
	
	
	public String registerReserveFundData(Integer reserve_Fund_Id,String account_Number, String cif_Number, Integer mgmnt_Comp_Id, Integer building_Id,String reserve_Account_Number, String is_Active,String branch_Code);
	
	public String updateReserveFundDataDetails(Integer reserveFundId,String accountNumber,String reserveAccountNumber, String isActive,String branchCode);
}
