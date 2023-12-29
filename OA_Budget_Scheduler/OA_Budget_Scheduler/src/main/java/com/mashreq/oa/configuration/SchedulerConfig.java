package com.mashreq.oa.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ConfigurationProperties(prefix = "scheduler")
public class SchedulerConfig {

    private long updateBudgetDetailsInterval;
    private long processPaymentRequestInterval;
    private long sendingStatusMailInterval;
    
    
    
    public long getUpdateBudgetDetailsInterval() {
		return updateBudgetDetailsInterval;
	}



	public void setUpdateBudgetDetailsInterval(long updateBudgetDetailsInterval) {
		this.updateBudgetDetailsInterval = updateBudgetDetailsInterval;
	}



	public long getProcessPaymentRequestInterval() {
		return processPaymentRequestInterval;
	}



	public void setProcessPaymentRequestInterval(long processPaymentRequestInterval) {
		this.processPaymentRequestInterval = processPaymentRequestInterval;
	}



	public long getSendingStatusMailInterval() {
		return sendingStatusMailInterval;
	}



	public void setSendingStatusMailInterval(long sendingStatusMailInterval) {
		this.sendingStatusMailInterval = sendingStatusMailInterval;
	}



	@Bean(name = "BudgetScheduler")
	@Primary
    public TaskScheduler scheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);
        return taskScheduler;
    }
}

