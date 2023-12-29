package com.mashreq.oa.configuration;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jmx.support.RegistrationPolicy;

import com.zaxxer.hikari.HikariDataSource;


@Configuration
@ConfigurationProperties(prefix = "datasource.oracle.jie")
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class JIEDataSourceConfig {
	
	private static final Logger log = LoggerFactory.getLogger(JIEDataSourceConfig.class);
    private String username;
    private String password;
    private String url;
    private String driver;

    
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Bean(name="jieOracleDataSource")
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(16);
        ds.setDriverClassName(this.getDriver());
        ds.setJdbcUrl(this.getUrl());
        ds.setUsername(this.getUsername());
        ds.setPassword(this.getPassword());
        ds.setAutoCommit(false);
        ds.setPoolName("hikariP00l-1" + Math.random());
        ds.setInitializationFailFast(false);
        log.info("HikariDataSource=" + ds);
        
        return ds;
    }

    @Bean(name = "jieOracleTranscationManager")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("jieOracleDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean(name = "jieOracleJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("jieOracleDataSource") DataSource ds)  {
        return new JdbcTemplate(ds);
    }
}
