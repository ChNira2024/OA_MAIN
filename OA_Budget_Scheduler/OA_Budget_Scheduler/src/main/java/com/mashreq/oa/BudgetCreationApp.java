package com.mashreq.oa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;


@EnableScheduling
@SpringBootApplication
public class BudgetCreationApp extends SpringBootServletInitializer implements WebApplicationInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(BudgetCreationApp.class);
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BudgetCreationApp.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BudgetCreationApp.class, args);
		
		LOGGER.info("Spring Boot BudgetCreation App has been started successfully");
	
	}
	

}