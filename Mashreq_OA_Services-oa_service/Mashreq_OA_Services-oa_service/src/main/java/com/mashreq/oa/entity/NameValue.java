package com.mashreq.oa.entity;

public class NameValue {
	private long  name;
	private long  value;
	
	public NameValue(){
		
	}
	
	 public NameValue(long name,long value){
		this.name=name;
		this.value=value;
		
	}

	public long getName() {
		return name;
	}

	public void setName(long name) {
		this.name = name;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "NameValue [name=" + name + ", value=" + value + "]";
	}
	
	

}
