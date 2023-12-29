package com.mashreq.oa.dao;

import java.util.List;

import com.mashreq.oa.entity.ApiCallStatus;
import com.mashreq.oa.entity.ReservefundDetails;
import com.mashreq.oa.entity.ReserveseFundData;

public interface ReserveFundDetailsDao {

	public ReservefundDetails getAccountDetails(Integer mgmtCompId, Integer propId);

	public Integer getBuildingId(Integer propId);

	public Double getReserveFund(Integer mgmtCompId, Integer propId, String periodCode);

	public Double getGeneralFund(Integer mgmtCompId, Integer propId, String periodCode);

	public int UpdateStatus(Integer mgmtCompId, String percentage, Integer buildingId);

	public int saveAPICallStatusInDB(int id, String errorMsg, String statusAPI);

	public List<ReserveseFundData> getRecordsHavingNullForRetry(Integer id);

	public Integer getPropId(Integer buildingID);

	// public List<ApiCallStatus> getPercentageStatusRecords(Integer id);
	public Integer getReserveFundId(Integer mgmtId, Integer buildingId);

	public int UpdateRetryStatus(Integer mgmtCompId, String apiStatus, String flexStatus);

	public List<String> getStatus(Integer fundId);
}
