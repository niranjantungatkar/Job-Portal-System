package edu.sjsu.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class JobApplication {

	
	@Id
	@GeneratedValue(generator = "jobApplicationIdGenerator")
	@GenericGenerator(name = "jobApplicationIdGenerator", strategy = "edu.sjsu.utils.JobApplicationIdGenerator")
	private String applicationId; //generate id - use existing id generation logic of ariline system.
	
	@OneToOne
	private JobSeeker applicant;
	
	@OneToOne
	private JobPosting jobPosting;
	
	private String resume;
	
	private int applicationStatus; 	// 0 - Pending, 1 - Offered, 2 - Rejected, 4 - Offer Accepted, 5 - Offer Rejected, 6 - cancelled
									// 0 - By default, 1, 2, 6 - changed by company
									// 4, 5 - changed by job seeker
	
	//private Interview interview; -- to be updated - logic not clear & it's bonus
	
	public JobApplication() {
		super();
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public JobSeeker getApplicant() {
		return applicant;
	}

	public void setApplicant(JobSeeker applicant) {
		this.applicant = applicant;
	}

	public JobPosting getJobPosting() {
		return jobPosting;
	}

	public void setJobPosting(JobPosting jobPosting) {
		this.jobPosting = jobPosting;
	}

	public int getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(int applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}
	
}
