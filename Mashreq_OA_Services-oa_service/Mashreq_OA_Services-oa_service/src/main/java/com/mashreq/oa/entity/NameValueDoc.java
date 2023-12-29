package com.mashreq.oa.entity;

public class NameValueDoc {
	
	private long name;
	private String value;
	
	public NameValueDoc(long name, String value) {
		
		this.name = name;
		this.value = value;
	}
	
	public long getName() {
		return name;
	}
	public void setName(long name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "NameValueDoc [name=" + name + ", value=" + value + "]";
	}
	
	

}
