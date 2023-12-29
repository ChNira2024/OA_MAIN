package com.mashreq.oa.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.entity.ReserveFundSaveData;
import com.mashreq.oa.entity.ReserveFundSearchInput;
import com.mashreq.oa.entity.ReserveFundSearchInputData;
import com.mashreq.oa.entity.ReserveFundUpdateData;
import com.mashreq.oa.service.ReserveFundSearchService;

@RestController
@CrossOrigin
public class ReserveFundSearchController {

	private static final Logger logger = LoggerFactory.getLogger(ReserveFundSearchController.class);
	@Autowired
	private ReserveFundSearchService service;

	@PostMapping("/data")
	public List<ReserveFundSearchInputData> EmployeeControllerSelectById(@RequestBody ReserveFundSearchInput inputs) {
		List<ReserveFundSearchInputData> response = null;
		try {
			logger.info(
					"com.mashreq.oa.controller::ReserveFundSearchController class EmployeeControllerSelectById() is executed successfully");
			response = service.fetchDataDetailsByService(inputs.getMgmtCompId(), inputs.getBuildingId());
			
		} catch (DataAccessException dae) {
			dae.printStackTrace();
			logger.error(
					"com.mashreq.oa.controller::ReserveFundSearchController class known DB exception is raised in EmployeeControllerSelectById() method:"
							+ dae.getMessage());
		}
		return response;
	}

	@PostMapping("/dataSave")
	public String ReserveFundDataDetails(@RequestBody ReserveFundSaveData inputs) {
		String data = null;
		try {
			logger.info(
					"com.mashreq.oa.controller::ReserveFundSearchController class ReserveFundDataDetails() method executed successfully");
			data = service.registerReserveFundData(inputs.getReserve_Fund_Id(), inputs.getAccount_Number(),
					inputs.getCif_Number(), inputs.getMgmnt_Comp_Id(), inputs.getBuilding_Id(),
					inputs.getReserve_Account_Number(), inputs.getIs_Active(), inputs.getBranch_Code());
		} catch (Exception e) {
			logger.error(
					"com.mashreq.oa.controller::ReserveFundSearchController class known DB exception is raised in ReserveFundDataDetails() method:"
							+ e.getMessage());
		}
		return data;
	}

	@PostMapping("/dataUpdate")
	public String ReserveFundDataDetailsTable(@RequestBody ReserveFundUpdateData inputs) {
		String data = null;
		try {
			logger.info(
					"com.mashreq.oa.controller::ReserveFundSearchController class ReserveFundDataDetailsTable() method executed successfully");
			data = service.updateReserveFundDataDetails(inputs.getReserveFundId(), inputs.getAccountNumber(),
					inputs.getReserveAccountNumber(), inputs.getIsActive(), inputs.getBranchCode());
		} catch (Exception e) {
			logger.error(
					"com.mashreq.oa.controller::ReserveFundSearchController class known DB exception is raised in updateReserveFundDataDetails() method:"
							+ e.getMessage());
		}
		return data;
	}

}
