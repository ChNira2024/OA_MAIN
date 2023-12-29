package com.mashreq.oa.entity;



public class BeneficiaryListDetails{
	
	//Rest Response Fields
	private String accountType;
	private String beneficiaryCode;
	
	//DB Fields
	private Integer propId;
	
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getBeneficiaryCode() {
		return beneficiaryCode;
	}
	public void setBeneficiaryCode(String beneficiaryCode) {
		this.beneficiaryCode = beneficiaryCode;
	}
	
	public Integer getPropId() {
		return propId;
	}
	public void setPropId(Integer propId) {
		this.propId = propId;
	}
	@Override
	public String toString() {
		return "BeneficiaryListDetails [accountType=" + accountType + ", beneficiaryCode=" + beneficiaryCode
				+ ", propId=" + propId + "]";
	}

}
