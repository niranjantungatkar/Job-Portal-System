package edu.sjsu.models;

import java.util.Date;

import javax.persistence.Embeddable;

@Embeddable
public class WorkExperience {

	private String company;
	
	private String positionHeld;
	
	private Date startDate;
	
	private Date endDate;
	
	public WorkExperience() {
		super();
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPositionHeld() {
		return positionHeld;
	}

	public void setPositionHeld(String positionHeld) {
		this.positionHeld = positionHeld;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
