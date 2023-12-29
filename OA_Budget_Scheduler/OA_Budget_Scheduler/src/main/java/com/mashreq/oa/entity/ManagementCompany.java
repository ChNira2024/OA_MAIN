package com.mashreq.oa.entity;

import org.springframework.stereotype.Component;

@Component
public class ManagementCompany {
	
	private Integer id;
	private Language name;
	
	
	public ManagementCompany() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Language getName() {
		return name;
	}
	public void setName(Language name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "ManagementCompany [id=" + id + ", name=" + name + "]";
	}
	
}
