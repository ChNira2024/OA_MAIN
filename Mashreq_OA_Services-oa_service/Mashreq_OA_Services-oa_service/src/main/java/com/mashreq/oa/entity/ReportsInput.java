package com.mashreq.oa.entity;

public class ReportsInput {
	

	private Integer budgetYear;
	private Integer invoiceYear;
	private Integer budgetRangeFrom;
	private Integer budgetRangeTo;
	private Integer mgmtCompId;
	private Integer supplierId;
	
	public ReportsInput()
	{
		
	}

	public Integer getBudgetYear() {
		return budgetYear;
	}

	public void setBudgetYear(Integer budgetYear) {
		this.budgetYear = budgetYear;
	}

	public Integer getInvoiceYear() {
		return invoiceYear;
	}

	public void setInvoiceYear(Integer invoiceYear) {
		this.invoiceYear = invoiceYear;
	}

	public Integer getBudgetRangeFrom() {
		return budgetRangeFrom;
	}

	public void setBudgetRangeFrom(Integer budgetRangeFrom) {
		this.budgetRangeFrom = budgetRangeFrom;
	}

	public Integer getBudgetRangeTo() {
		return budgetRangeTo;
	}

	public void setBudgetRangeTo(Integer budgetRangeTo) {
		this.budgetRangeTo = budgetRangeTo;
	}

	public Integer getMgmtCompId() {
		return mgmtCompId;
	}

	public void setMgmtCompId(Integer mgmtCompId) {
		this.mgmtCompId = mgmtCompId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	@Override
	public String toString() {
		return "ReportsInput [budgetYear=" + budgetYear + ", invoiceYear=" + invoiceYear + ", budgetRangeFrom="
				+ budgetRangeFrom + ", budgetRangeTo=" + budgetRangeTo + ", mgmtCompId=" + mgmtCompId + ", supplierId="
				+ supplierId + "]";
	}

}
