package edu.sjsu.services;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.exceptions.JobPostingException;
import edu.sjsu.exceptions.JobSeekerExceptions;
import edu.sjsu.models.JobApplication;
import edu.sjsu.models.JobPosting;
import edu.sjsu.models.JobSeeker;
import edu.sjsu.repositories.JobApplicationRepository;

@Service
@Transactional
public class JobApplicationService {

	@Autowired
	JobApplicationRepository jobApplicationRepository;
	
	@Autowired
	JobSeekerService jobSeekerService;
	
	@Autowired
	JobPostingService jobPostingService;
	
	public JobApplication createJobApplication(Map<String, Object> parameters) throws JobSeekerExceptions, JobPostingException{

		
		String jobSeekerId = (String)parameters.get("applicant");
		String jobPostingId = (String)parameters.get("jobPosting");
		
		JobSeeker jobSeeker = jobSeekerService.getProfile(jobSeekerId);
		JobPosting jobPosting = jobPostingService.getJobPosting(jobPostingId);
		
		JobApplication jobApplication = new JobApplication();
		
		jobApplication.setApplicationStatus(0);
		jobApplication.setApplicant(jobSeeker);
		jobApplication.setJobPosting(jobPosting);
		
		jobApplicationRepository.save(jobApplication);
		return jobApplication;
	}

	public HashMap<String, String> getErrorResponse(String errorcode, String error) {
		HashMap<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("code", errorcode);
		errorMap.put("msg", error);
		return errorMap;
	}
}
