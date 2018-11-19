package com.techelevator.campground.model;

public class Site {

	private Integer siteId;
	private Integer campgroundId;
	private Integer siteNumber;
	private Integer maxOccupancy;
	private boolean accessible = false;
	private Integer maxRvLength;
	private boolean utilities;
	
	
	public Integer getSiteId() {
		return siteId;
	}
	public Integer getCampgroundId() {
		return campgroundId;
	}
	public Integer getSiteNumber() {
		return siteNumber;
	}
	public Integer getMaxOccupancy() {
		return maxOccupancy;
	}
	public String isAccessible() {
		if(accessible) {
			return "Yes";
		}
		return "No";
		
	}
	public String getMaxRvLength() {
		if(maxRvLength == 0) {
			return "N/A";
		}
		return maxRvLength.toString();
	}
	public String isUtilities() {
		if(utilities) {
			return "Yes";
		}
		return "N/A";
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public void setCampgroundId(Integer campgroundId) {
		this.campgroundId = campgroundId;
	}
	public void setSiteNumber(Integer siteNumber) {
		this.siteNumber = siteNumber;
	}
	public void setMaxOccupancy(Integer maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	public void setMaxRvLength(Integer maxRvLength) {
		this.maxRvLength = maxRvLength;
	}
	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}
	
	
	
}
