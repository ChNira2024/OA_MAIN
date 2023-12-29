package com.mashreq.oa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.mashreq.oa.service.BudgetDetailsService;

public class MollakIntegrationTest {
	
	private static Logger LOGGER = LoggerFactory.getLogger(MollakIntegrationTest.class);

	
	@Autowired
	private static BudgetDetailsService budgetDetailsService;
	
	public MollakIntegrationTest() {
		System.out.println("running test");
	}
	
	public static void main (String args[]) {
		
		execute(); 
		System.out.println("running execute method");
	}

	public static void execute() {

			try {
				LOGGER.info("Executing Exuecute()");
			//	budgetDetailsService.updateBudgetData();
			} catch (Exception e) {
				LOGGER.error("Exception Main:" + e.getCause());
			}
	}
	
		
	}
	
	

