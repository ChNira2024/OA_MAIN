package com.mashreq.oa.dao;

import java.util.List;

import com.mashreq.oa.entity.Buildings;
import com.mashreq.oa.entity.MappingData;
import com.mashreq.oa.entity.PaymentData;
import com.mashreq.oa.entity.PaymentSearchInput;
import com.mashreq.oa.entity.PropertyGroups;

public interface PaymentDataDao {
	
	public void uploadExcel(List<PaymentData> data);

	public List<PaymentData> getListData();
	
	public List<Long> getMatrixRefNos(List<Long> matrixList);

	public List<Integer> getMatrixFileRefNoList();
	
	

	public List<String> getSupplierList();

	public Integer insertSupplier(String supplierName);

	public Integer getSupplierId(String supplierName);
	
	

	public List<String> getBuildingList();

	public Integer insertBuilding(String buildingName);

	public Integer getBuildingId(String buildingName);
	
	

	public String getManagementCompany(Integer mgmtCompId);
	
	

	public Integer getPropId(String buildingName, Integer mgmtCompId);

	public void insertMappingData(Integer buildingId, Integer propId,Integer mgmtId);

	
	

	public List<PaymentData> getSearchList(PaymentSearchInput searchInput);

	public boolean checkForBuildingName(String buildingName);
	
	public boolean checkForMapping(Integer buildingId, Integer propId);
	
	
	
	public List<Buildings> getBuildings(Integer mgmtCompId);

	public List<PropertyGroups> getProperties(Integer mgmtCompId);

}
