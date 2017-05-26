package edu.sjsu.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.exceptions.CompanyExceptions;
import edu.sjsu.exceptions.InterviewException;
import edu.sjsu.exceptions.JobApplicationExceptions;
import edu.sjsu.exceptions.JobPostingException;
import edu.sjsu.exceptions.JobSeekerExceptions;
import edu.sjsu.models.JobApplication;
import edu.sjsu.models.JobPosting;
import edu.sjsu.models.JobSeeker;
import edu.sjsu.services.InterviewService;
import edu.sjsu.services.JobApplicationService;
import edu.sjsu.services.JobPostingService;
import edu.sjsu.services.JobSeekerService;

@RestController
public class JobApplicationController {

	@Autowired
	JobApplicationService jobApplicationService;

	@Autowired
	JobPostingService jobPostingService;

	@Autowired
	JobSeekerService jobSeekerService;

	@Autowired
	InterviewService interviewService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobapplication", method = RequestMethod.POST)
	public ResponseEntity createJobApplication(@RequestBody Map<String, Object> parameters) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			JobApplication jobApplication = jobApplicationService.createJobApplication(parameters);
			if (jobApplication == null) {
				HashMap<String, String> result = new HashMap<>();
				result.put("msg", "You already have 5 applied jobs with status PENDING");
				result.put("code", "200");
				return new ResponseEntity(result, responseHeaders, HttpStatus.OK);
			}
			return new ResponseEntity(jobApplication, responseHeaders, HttpStatus.OK);
		} catch (JobSeekerExceptions | JobPostingException ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (JobApplicationExceptions ex) {
			return new ResponseEntity(getErrorResponse("200", ex.getMessage()), responseHeaders, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobapplication/jobposting/{requisitionId}", method = RequestMethod.GET)
	public ResponseEntity getApplicationByJob(@PathVariable("requisitionId") String requisitionId)
			throws JobPostingException, JobApplicationExceptions {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			JobPosting jobPosting = jobPostingService.getJobPosting(requisitionId);
			List<JobApplication> jobApplications = jobApplicationService.getJobApplicationByJobPosting(jobPosting);
			return new ResponseEntity(jobApplications, responseHeaders, HttpStatus.OK);
		} catch (JobPostingException | JobApplicationExceptions ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobapplication/jobseeker/{id}", method = RequestMethod.GET)
	public ResponseEntity getApplicationByApplicant(@PathVariable("id") String id) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			JobSeeker jobSeeker = jobSeekerService.getProfile(id);
			List<JobApplication> jobApplications = jobApplicationService.getJobApplicationByJobSeeker(jobSeeker);
			return new ResponseEntity(jobApplications, responseHeaders, HttpStatus.OK);
		} catch (JobSeekerExceptions | JobApplicationExceptions ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobapplication/company/updatestatus", method = RequestMethod.POST)
	public ResponseEntity updateStatusByCompany(@RequestBody Map<String, Object> parameters) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			JobApplication jobApplication = jobApplicationService.updateStatusByCompany(parameters);
			return new ResponseEntity(jobApplication, responseHeaders, HttpStatus.OK);
		} catch (JobApplicationExceptions ex) {
			return new ResponseEntity(getErrorResponse("200", ex.getMessage()), responseHeaders, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("501", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobapplication/jobseeker/updatestatus", method = RequestMethod.POST)
	public ResponseEntity updateStatusByApplicant(@RequestBody Map<String, Object> parameters) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			JobApplication jobApplication = jobApplicationService.updateStatusByApplicant(parameters);
			return new ResponseEntity(jobApplication, responseHeaders, HttpStatus.OK);
		} catch (JobApplicationExceptions ex) {
			return new ResponseEntity(getErrorResponse("200", ex.getMessage()), responseHeaders, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobapplication/interview/schedule", method = RequestMethod.POST)
	public ResponseEntity scheduleInterview(@RequestBody Map<String, Object> params) throws JobApplicationExceptions {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			JobApplication jobApplication = interviewService.scheduleInterview(params);
			if (jobApplication != null) {
				interviewService.sendInterviewInvitation(params);
			}
			return new ResponseEntity(jobApplication, responseHeaders, HttpStatus.OK);
		} catch (JobApplicationExceptions ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobapplication/interview/update", method = RequestMethod.POST)
	public ResponseEntity getInterviews(@RequestBody Map<String, Object> params) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			interviewService.updateInterview(params);
			HashMap<String, String> result = new HashMap<>();
			result.put("code", "200");
			result.put("msg", "true");
			return new ResponseEntity(result, responseHeaders, HttpStatus.OK);
		} catch (InterviewException ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobapplication/interview/companysearch", method = RequestMethod.POST)
	public ResponseEntity getInterviewByCompany(@RequestBody Map<String, Object> params) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			List<JobApplication> allApplications = interviewService.getApplicationsByCompany(params);
			return new ResponseEntity(allApplications, responseHeaders, HttpStatus.OK);
		} catch (CompanyExceptions ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public HashMap<String, String> getErrorResponse(String errorcode, String error) {
		HashMap<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("code", errorcode);
		errorMap.put("msg", error);
		return errorMap;
	}

}
