package com.mashreq.oa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;


@SpringBootApplication
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
/*@ComponentScan(
	    basePackages = "com.mashreq.oa",
	    excludeFilters = @ComponentScan.Filter(
	        type = FilterType.ASSIGNABLE_TYPE,
	        classes = DataSourceConfig.class
	    ) )*/
public class OwnersAssociationsService extends SpringBootServletInitializer implements WebApplicationInitializer {
private static final Logger LOGGER=LoggerFactory.getLogger(OwnersAssociationsService.class);

@Override
protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	return builder.sources(OwnersAssociationsService.class);
}
	public static void main(String[] args) {
		SpringApplication.run(OwnersAssociationsService.class, args);
		LOGGER.info("Owners Associations Service-app is running...");
	}
}
