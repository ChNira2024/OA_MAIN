package com.mashreq.oa;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mashreq.oa.configuration.SchedulerConfig;
import com.mashreq.oa.dao.ManageSchedulerLockDAO;
import com.mashreq.oa.dto.SchLockStatusDetails;
import com.mashreq.oa.exceptions.OAServiceException;
import com.mashreq.oa.service.BudgetDetailsService;
import com.mashreq.oa.service.ReserveFundDetailsService;

@RestController
public class BudgetCreationController implements ApplicationRunner {

	@Autowired
	private ManageSchedulerLockDAO schedulerLockDAO;
	@Autowired
	private BudgetDetailsService budgetDetailsService;

	@Autowired
	private ReserveFundDetailsService detailsService;

	private SchedulerConfig schedulerConfig;
	private TaskScheduler taskScheduler;

	@Autowired
	public BudgetCreationController(@Qualifier("BudgetScheduler") TaskScheduler budgetScheduler,
			SchedulerConfig schedulerConfig, TaskScheduler taskScheduler, BudgetDetailsService budgetDetailsService) {
		this.schedulerConfig = schedulerConfig;
		this.taskScheduler = taskScheduler;
		this.budgetDetailsService = budgetDetailsService;
	}

	private static Logger LOGGER = LoggerFactory.getLogger(BudgetCreationController.class);

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		taskScheduler.scheduleWithFixedDelay(() -> {
			LOGGER.info("Started Update Budget Scheduler::::");
			try {
				updateBudgetDetails();
			} catch (Exception e) {
				LOGGER.error("Exception :" + ExceptionUtils.getStackTrace(e));

			}
		}, schedulerConfig.getUpdateBudgetDetailsInterval());

		taskScheduler.scheduleWithFixedDelay(() -> {
			LOGGER.info("Started getProcessPaymentRequest Budget Scheduler::::");
			try {
				processPaymentRequest();
			} catch (Exception e) {
				LOGGER.error("Exception :" + ExceptionUtils.getStackTrace(e));

			}
		}, schedulerConfig.getProcessPaymentRequestInterval());

		taskScheduler.scheduleWithFixedDelay(() -> {
			LOGGER.info("Started getSendingStatusMail Budget Scheduler::::");
			try {
				sendingStatusMail();
			} catch (Exception e) {
				LOGGER.error("Exception :" + ExceptionUtils.getStackTrace(e));

			}
		}, schedulerConfig.getSendingStatusMailInterval());
	}

	// @Scheduled(cron = "${spring.oa.updateBudgetDetail}")
	@GetMapping(value = "/updateBudgetDetails")
	public void updateBudgetDetails() {

		// LOGGER.info("Started Update Budget Scheduler::::");
		String status = "Success";
		HttpHeaders headerResponse = new HttpHeaders();
		SchLockStatusDetails schLD = schedulerLockDAO.getSchLockStatusforUpdateBudgetDetailsService();
		if (schLD == null || schLD.getLastUpdatedTime() == null) {
			LOGGER.info("Getting Empty Record From GetSchLock table");
			return;
		}
		// LOGGER.info("Found SchLockStatus Records From DB");
		int schLockUpdateStatus = schedulerLockDAO.updateSchLock(schLD.getLastUpdatedTime(), schLD.getId());
		// LOGGER.info("Locking Sch table >> updated Count :" + schLockUpdateStatus);
		if (schLockUpdateStatus != 1) {
			LOGGER.info("Not able to update Sch lock table >> may be its done by another Machine");
			return;
		}
		try {
			// LOGGER.info("Executing fetchBudgetData()");
			budgetDetailsService.fetchBudgetData();
			// return new ResponseEntity<String>(status, headerResponse, HttpStatus.OK);
		} catch (Exception e) {
			status = "Failed";
			e.printStackTrace();
			LOGGER.error("Exception Main:" + e.getCause());
			e.printStackTrace();
			throw new OAServiceException("Failed To Update Budget Details");

		} finally {
			int releaseLockUpdateCount = schedulerLockDAO.updateSchLockAFCJ(schLD.getId());
			// LOGGER.info("releaseLockUpdateCount :" + releaseLockUpdateCount);
			LOGGER.info("OABudget Update Budget Details Scheduler Ended..");
		}

		// return new ResponseEntity<String>(status,headerResponse,HttpStatus.OK);

	}

	/*
	 * @GetMapping(value = "/getPreviousBudgetDetails") public
	 * ResponseEntity<String> getPreviousBudgetDetails() { String status =
	 * "Success"; HttpHeaders headerResponse = new HttpHeaders();
	 * 
	 * try { LOGGER.info("Executing updateBudgetData()");
	 * budgetDetailsService.getPreviousBudgetDetails(); return new
	 * ResponseEntity<String>(status, headerResponse, HttpStatus.OK); } catch
	 * (Exception e) { status = "Failed"; LOGGER.error("Exception Main:");
	 * e.printStackTrace(); throw new OAServiceException("");
	 * 
	 * }
	 * 
	 * // return new ResponseEntity<String>(status,headerResponse,HttpStatus.OK);
	 * 
	 * }
	 */

	// @Scheduled(cron = "${spring.oa.processPaymentRequest}")
	@GetMapping("/processPaymentRequest")
	public void processPaymentRequest() {
		// LOGGER.info("Started processPaymentRequest Scheduler");
		String status = "Success";
		HttpHeaders headerResponse = new HttpHeaders();
		SchLockStatusDetails schLD = schedulerLockDAO.getSchLockStatusforProcessPaymentRequestService();
		if (schLD == null || schLD.getLastUpdatedTime() == null) {
			LOGGER.info("Getting Empty Record From GetSchLock table");
			return;
		}
		// LOGGER.info("Found SchLockStatus Records From DB");
		int schLockUpdateStatus = schedulerLockDAO.updateSchLock(schLD.getLastUpdatedTime(), schLD.getId());
		LOGGER.info("Locking Sch table >> updated Count :" + schLockUpdateStatus);
		if (schLockUpdateStatus != 1) {
			LOGGER.info("Not able to update Sch lock table >> may be its done by another Machine");
			return;
		}
		try {
			// LOGGER.info("Before executing processPaymentRequest()");
			budgetDetailsService.processPaymentRequest();
			// return new ResponseEntity<String>(status,headerResponse,HttpStatus.OK);
		} catch (Exception e) {
			status = "Failed";

			LOGGER.error("Exception Main:" + e.getMessage());
			e.printStackTrace();
			throw new OAServiceException(e.getMessage());
		} finally {
			int releaseLockUpdateCount = schedulerLockDAO.updateSchLockAFCJ(schLD.getId());
			// LOGGER.info("releaseLockUpdateCount :" + releaseLockUpdateCount);
			LOGGER.info("OABudget processPaymentRequest Scheduler Ended..");
		}

	}

	// @Scheduled(cron = "${spring.oa.sendingStatusMail}")
	@GetMapping("/sendMails")
	public void sendingStatusMail() {
		// LOGGER.info("Started sendingStatusMail Scheduler::::");
		String status = "Success";
		HttpHeaders headerResponse = new HttpHeaders();
		SchLockStatusDetails schLD = schedulerLockDAO.getSchLockStatusforSendingStatusMailService();
		if (schLD == null || schLD.getLastUpdatedTime() == null) {
			LOGGER.info("Getting Empty Record From GetSchLock table");
			return;
		}
		// LOGGER.info("Found SchLockStatus Records From DB");
		int schLockUpdateStatus = schedulerLockDAO.updateSchLock(schLD.getLastUpdatedTime(), schLD.getId());
		// LOGGER.info("Locking Sch table >> updated Count :" + schLockUpdateStatus);
		if (schLockUpdateStatus != 1) {
			// LOGGER.info("Not able to update Sch lock table >> may be its done by another
			// Machine");
			return;
		}
		try {
			// LOGGER.info("Executing processPaymentRequest()");
			budgetDetailsService.sendingStatusMailIndividual();
			// return new ResponseEntity<String>(status,headerResponse,HttpStatus.OK);
		} catch (Exception e) {
			status = "Failed";

			LOGGER.error("Exception Main:" + e.getCause());
			e.printStackTrace();
			throw new OAServiceException("");
		} finally {
			int releaseLockUpdateCount = schedulerLockDAO.updateSchLockAFCJ(schLD.getId());
			// LOGGER.info("releaseLockUpdateCount :" + releaseLockUpdateCount);
			LOGGER.info("OABudget sendingStatusMail Scheduler Ended..");
		}

	}

	/*
	 * @GetMapping("/updatePercentage") public void
	 * updatePercentage(@RequestParam("compId") Integer
	 * compId, @RequestParam("propId") Integer propId,
	 * 
	 * @RequestParam("year") String year) {
	 * 
	 * detailsService.updateReserveFundData(compId, propId, year); }
	 */
	@GetMapping("/updatePercentage")
	public void updatePercentage(@RequestParam("compId") Integer compId,@RequestParam("propId") Integer propId,@RequestParam("year") String year) 
	{
		detailsService.updateReserveFundData(compId, propId, year);
		
	}

	@GetMapping(value = "/retry-percentage-update")
	public void retryPercentage(@RequestParam("reserveFund_Id") Integer reserverFund_Id) 
	{
		LOGGER.info("Entered into retry percentage mechanism Controller");
		detailsService.retryToUpdatePercentage(reserverFund_Id);
	}
	
	
}
