package edu.sjsu.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;
import org.springframework.stereotype.Service;

import edu.sjsu.exceptions.JobApplicationExceptions;
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

	@Autowired
	EmailService emailService;

	/**
	 * Create the new Job Application
	 * 
	 * @param parameters
	 * @return JobApplication
	 * @throws JobSeekerExceptions
	 * @throws JobPostingException
	 * @throws JobApplicationExceptions
	 */
	public JobApplication createJobApplication(Map<String, Object> parameters)
			throws JobSeekerExceptions, JobPostingException, JobApplicationExceptions {

		String jobSeekerId = (String) parameters.get("applicant");
		String jobPostingId = (String) parameters.get("jobPostingId");

		JobSeeker jobSeeker = jobSeekerService.getProfile(jobSeekerId);
		JobPosting jobPosting = jobPostingService.getJobPosting(jobPostingId);

		List<JobApplication> jobApplications = jobApplicationRepository.findByApplicantAndApplicationStatus(jobSeeker,
				0);
		if (jobApplications.size() >= 5) {
			return null;
		}

		JobApplication jobApplicationExist = jobApplicationRepository.findByApplicantAndJobPosting(jobSeeker,
				jobPosting);
		if (jobApplicationExist != null) {
			throw new JobApplicationExceptions("You have already applied to this job with application Id : "
					+ jobApplicationExist.getApplicationId());
		}

		JobApplication jobApplication = new JobApplication();

		jobApplication.setApplicationStatus(0);
		jobApplication.setApplicant(jobSeeker);
		jobApplication.setJobPosting(jobPosting);

		if (parameters.containsKey("resume")) {
			jobApplication.setResume((String) parameters.get("resume"));
		}

		jobApplicationRepository.save(jobApplication);
		StringBuilder subject = new StringBuilder();
		subject.append("Thank you for your interest at " + jobPosting.getCompany().getCompanyName());

		StringBuilder message = new StringBuilder();
		message.append("Dear " + jobSeeker.getFirstname());
		message.append("\n\nWe would like to thank you for applying for the following job at "
				+ jobPosting.getCompany().getCompanyName());
		message.append("\nPosition : " + jobPosting.getTitle());
		message.append("\nRequisition Id : " + jobPosting.getRequisitionId());
		message.append(
				"\n\nOne of our representative will review your application and get back to you, should your skills matches the requirements");
		message.append("\n\n\nBest Regards,");
		message.append("\nTalent Acquisition Team");
		message.append("\n" + jobPosting.getCompany().getCompanyName());

		emailService.sendMail(jobSeeker.getEmail(), subject.toString(), message.toString());
		return jobApplication;
	}

	/**
	 * Get the JobApplication from JobPosting
	 * 
	 * @param jobPosting
	 * @return List<JobApplication>
	 */
	public List<JobApplication> getJobApplicationByJobPosting(JobPosting jobPosting) throws JobApplicationExceptions {
		List<JobApplication> jobApplications = jobApplicationRepository.findByJobPosting(jobPosting);
		if (jobApplications.size() == 0)
			throw new JobApplicationExceptions("No Job application found");
		return jobApplications;
	}

	/**
	 * Get JobApplications by job applicant
	 * 
	 * @param jobseeker
	 * @return List<JobApplication>
	 * @throws JobApplicationExceptions
	 */
	public List<JobApplication> getJobApplicationByJobSeeker(JobSeeker jobseeker) throws JobApplicationExceptions {
		List<JobApplication> jobApplications = jobApplicationRepository.findByApplicant(jobseeker);
		if (jobApplications.size() == 0) {
			throw new JobApplicationExceptions("No Job application found");
		}
		return jobApplications;
	}

	public HashMap<String, String> getErrorResponse(String errorcode, String error) {
		HashMap<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("code", errorcode);
		errorMap.put("msg", error);
		return errorMap;
	}
}
