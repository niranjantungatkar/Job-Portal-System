package edu.sjsu.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Interview {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int interviewNo;

	@JsonFormat(pattern = "yyyy-MM-dd:hh-mm-ss")
	private Date startTime;

	@JsonFormat(pattern = "yyyy-MM-dd:hh-mm-ss")
	private Date endTime;

	private int status; // 0-Pending, 1- Accepted, 2-Rejected, 4-Cancelled

	public int getInterviewNo() {
		return interviewNo;
	}

	public void setInterviewNo(int interviewNo) {
		this.interviewNo = interviewNo;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
