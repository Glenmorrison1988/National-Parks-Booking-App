package com.techelevator.campground.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Campground {
	private Integer campgroundId;
	private Integer parkId;
	private String name;
	private String openFrom;
	private String openTo;
	private BigDecimal dailyFee;
	private String openFromMonth;
	private String openToMonth;
	private String dailyFeeString;  
	private int monthNumberFrom;
	private int monthNumberTo;
	
	  
	
	
	
	private String numberToMonth(String number) {
		String monthString = "";
		switch(number){
		case "01": monthString = "January";
				break;	
		case "02": monthString = "February";
				break;	
		case "03": monthString = "March";
				break;	
		case "04": monthString = "April";
				break;	
		case "05": monthString = "May";
				break;	
		case "06": monthString = "June";
				break;	
		case "07": monthString = "July";
				break;	
		case "08": monthString = "August";
				break;	
		case "09": monthString = "September";
				break;	
		case "10": monthString = "October";
				 break;	
		case "11": monthString = "November";
				 break;	
		case "12": monthString = "December";
				 break;	
		default : monthString = "Invalid Month";
				  break;
		}
		return monthString;			
	}
	
	private int monthToNumber(String number) {
		int monthNumber = 0;
		switch(number){  
		case "01": monthNumber = 1;
				break;	
		case "02": monthNumber = 2;
				break;	
		case "03": monthNumber = 3;
				break;	
		case "04": monthNumber = 4;
				break;	
		case "05": monthNumber = 5;
				break;	
		case "06": monthNumber = 6;
				break;	
		case "07": monthNumber = 7;
				break;	
		case "08": monthNumber = 8;
				break;	
		case "09": monthNumber = 9;
				break;	
		case "10": monthNumber = 10;
				 break;	
		case "11": monthNumber = 11;
				 break;	
		case "12": monthNumber = 12;
				 break;	
		}
		return monthNumber;			
	}
	
	public String getDailyFeeString() {
		DecimalFormat df = new DecimalFormat("###,###.00");
		String dailyFee = df.format(this.dailyFee);
		return dailyFee;
	}
	public String getOpenFromMonth() {
		openFromMonth = numberToMonth(openFrom);
		return openFromMonth;
	}
	public String getOpenToMonth() {
		openToMonth = numberToMonth(openTo);
		return openToMonth;
	}
	public Integer getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(Integer campgroundId) {
		this.campgroundId = campgroundId;
	}
	public Integer getParkId() {
		return parkId;
	}
	public void setParkId(Integer parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpenFrom() {
		return openFrom;
	}
	public void setOpenFrom(String openFrom) {
		this.openFrom = openFrom;
	}
	public String getOpenTo() {
		return openTo;
	}
	public void setOpenTo(String openTo) {
		this.openTo = openTo;
	}
	public BigDecimal getDailyFee() {
	
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}

	public int getMonthNumberFrom() {
		this.monthNumberFrom = monthToNumber(openFrom);
		return monthNumberFrom;
	}
	public int getMonthNumberTo() {
		this.monthNumberTo = monthToNumber(openTo);
		return monthNumberTo;
	}

}
