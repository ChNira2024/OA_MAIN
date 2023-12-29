package com.mashreq.oa.entity;

public class Language {
	private String englishName;
	private String arabicName;
	
	
	
	public Language() {
		super();
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getArabicName() {
		return arabicName;
	}
	public void setArabicName(String arabicName) {
		this.arabicName = arabicName;
	}
	
	@Override
	public String toString() {
		return "Language [englishName=" + englishName + ", arabicName=" + arabicName +" ]";
	}

}
