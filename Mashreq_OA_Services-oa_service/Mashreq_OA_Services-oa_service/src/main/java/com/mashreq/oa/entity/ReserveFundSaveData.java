package com.mashreq.oa.entity;


public class ReserveFundSaveData {
	
	private Integer reserve_Fund_Id;
	
	private String account_Number;
	private String cif_Number;
	private Integer mgmnt_Comp_Id;
	private Integer building_Id;
	private String reserve_Account_Number;
	private String is_Active;
	private String branch_Code;
	public Integer getReserve_Fund_Id() {
		return reserve_Fund_Id;
	}
	public void setReserve_Fund_Id(Integer reserve_Fund_Id) {
		this.reserve_Fund_Id = reserve_Fund_Id;
	}
	public String getAccount_Number() {
		return account_Number;
	}
	public void setAccount_Number(String account_Number) {
		this.account_Number = account_Number;
	}
	public String getCif_Number() {
		return cif_Number;
	}
	public void setCif_Number(String cif_Number) {
		this.cif_Number = cif_Number;
	}
	public Integer getMgmnt_Comp_Id() {
		return mgmnt_Comp_Id;
	}
	public void setMgmnt_Comp_Id(Integer mgmnt_Comp_Id) {
		this.mgmnt_Comp_Id = mgmnt_Comp_Id;
	}
	public Integer getBuilding_Id() {
		return building_Id;
	}
	public void setBuilding_Id(Integer building_Id) {
		this.building_Id = building_Id;
	}
	public String getReserve_Account_Number() {
		return reserve_Account_Number;
	}
	public void setReserve_Account_Number(String reserve_Account_Number) {
		this.reserve_Account_Number = reserve_Account_Number;
	}
	public String getIs_Active() {
		return is_Active;
	}
	public void setIs_Active(String is_Active) {
		this.is_Active = is_Active;
	}
	public String getBranch_Code() {
		return branch_Code;
	}
	public void setBranch_Code(String branch_Code) {
		this.branch_Code = branch_Code;
	}
	@Override
	public String toString() {
		return "ReserveFundSaveData [reserve_Fund_Id=" + reserve_Fund_Id + ", account_Number=" + account_Number
				+ ", cif_Number=" + cif_Number + ", mgmnt_Comp_Id=" + mgmnt_Comp_Id + ", building_Id=" + building_Id
				+ ", reserve_Account_Number=" + reserve_Account_Number + ", is_Active=" + is_Active + ", branch_Code="
				+ branch_Code + "]";
	}
		
		
}
