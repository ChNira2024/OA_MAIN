package com.mashreq.oa.entity;

import java.sql.Date;

public class Supplier {
	
	private Integer supplierId;
	private String SupplierName;
	private Date tradeLicenseExpDate;
	private String autoRenewal;
	
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return SupplierName;
	}
	public void setSupplierName(String supplierName) {
		SupplierName = supplierName;
	}
	
	public Date getTradeLicenseExpDate() {
		return tradeLicenseExpDate;
	}
	public void setTradeLicenseExpDate(Date tradeLicenseExpDate) {
		this.tradeLicenseExpDate = tradeLicenseExpDate;
	}
	
	public String getAutoRenewal() {
		return autoRenewal;
	}
	public void setAutoRenewal(String autoRenewal) {
		this.autoRenewal = autoRenewal;
	}
	
	@Override
	public String toString() {
		return "Supplier [supplierId=" + supplierId + ", SupplierName=" + SupplierName + ", tradeLicenseExpDate="
				+ tradeLicenseExpDate + ", autoRenewal=" + autoRenewal + "]";
	}

	
}
