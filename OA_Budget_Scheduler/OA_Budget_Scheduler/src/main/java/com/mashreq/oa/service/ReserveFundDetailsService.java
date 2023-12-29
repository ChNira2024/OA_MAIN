package com.mashreq.oa.service;

import java.util.List;

import com.mashreq.oa.entity.ReservefundDetails;

public interface ReserveFundDetailsService {

	public void updateReserveFundData(Integer compId, Integer propId, String periodCode);
	
	public void retryToUpdatePercentage(Integer Reserve_FundId);
	
}
