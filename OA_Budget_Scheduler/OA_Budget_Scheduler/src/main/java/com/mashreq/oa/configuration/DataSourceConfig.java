package com.mashreq.oa.configuration;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.jmx.config.EnableIntegrationMBeanExport;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jmx.support.RegistrationPolicy;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableIntegrationMBeanExport(defaultDomain = "Send-Notification-Create")
//@ConfigurationProperties(prefix = "oracle")
@ConfigurationProperties(prefix = "spring.datasource")
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class DataSourceConfig {
    public static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    private String username;
    private String password;
    private String url;
//    private String driver;
    
   private String driverClassName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

   

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	@Bean(name = "sendNotifiactionDataSource")
    public DataSource dataSource() {
    	
    	HikariConfig hikariConfig = new HikariConfig();
    	hikariConfig.setMaximumPoolSize(16);
//    	hikariConfig.setDriverClassName(this.getDriver());
    	hikariConfig.setDriverClassName(this.getDriverClassName());
    	hikariConfig.setJdbcUrl(this.getUrl());
    	hikariConfig.setUsername(this.getUsername());
    	hikariConfig.setPassword(this.getPassword());
    	hikariConfig.setAutoCommit(true);
    	hikariConfig.setPoolName("hikariP00l-1" + Math.random());
    	hikariConfig.setInitializationFailFast(false);
        
        HikariDataSource ds = new HikariDataSource(hikariConfig);
        
        logger.info("hikaridatasource=" + ds);
        logger.info("hikaridatasource=" + ds.getPoolName());
//        logger.info("this.getDriver()=" + this.getDriver());
        hikariConfig.setDriverClassName(this.getDriverClassName());
        logger.info("this.getUrl()=" + this.getUrl());
        logger.info("this.getUsername()=" + this.getUsername());
       // logger.info("this.getPassword()=" + this.getPassword());
        return ds;
    }

    @Bean
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

  
    
    @Bean(name = "sendNotificationDataSourceCommit")
    @Primary
    public DataSource createDataSource() {
    	
    	HikariConfig hikariConfig = new HikariConfig();
    	hikariConfig.setMaximumPoolSize(16);
//    	hikariConfig.setDriverClassName(this.getDriver());
    	hikariConfig.setDriverClassName(this.getDriverClassName());
    	hikariConfig.setJdbcUrl(this.getUrl());
    	hikariConfig.setUsername(this.getUsername());
    	hikariConfig.setPassword(this.getPassword());
    	hikariConfig.setAutoCommit(true);        //keep false
    	hikariConfig.setPoolName("hikariP00l-1" + Math.random());
    	hikariConfig.setInitializationFailFast(false);
        
        HikariDataSource ds = new HikariDataSource(hikariConfig);
        
        logger.info("hikaridatasource=" + ds);
        logger.info("hikaridatasource=" + ds.getPoolName());
//        logger.info("this.getDriver()=" + this.getDriver());
        hikariConfig.setDriverClassName(this.getDriverClassName());
        logger.info("this.getUrl()=" + this.getUrl());
        logger.info("this.getUsername()=" + this.getUsername());
       // logger.info("this.getPassword()=" + this.getPassword());
        return ds;
    }

  
   


}
