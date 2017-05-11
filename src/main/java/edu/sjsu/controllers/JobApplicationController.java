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

import edu.sjsu.exceptions.JobApplicationExceptions;
import edu.sjsu.exceptions.JobPostingException;
import edu.sjsu.exceptions.JobSeekerExceptions;
import edu.sjsu.models.JobApplication;
import edu.sjsu.models.JobPosting;
import edu.sjsu.services.JobApplicationService;
import edu.sjsu.services.JobPostingService;

@RestController
public class JobApplicationController {

	@Autowired
	JobApplicationService jobApplicationService;

	@Autowired
	JobPostingService jobPostingService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobapplication", method = RequestMethod.POST)
	public ResponseEntity createJobApplication(@RequestBody Map<String, Object> parameters) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			JobApplication jobApplication = jobApplicationService.createJobApplication(parameters);
			return new ResponseEntity(jobApplication, responseHeaders, HttpStatus.OK);
		} catch (JobSeekerExceptions ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (JobPostingException ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobapplication/{requisitionId}", method = RequestMethod.GET)
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

	public HashMap<String, String> getErrorResponse(String errorcode, String error) {
		HashMap<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("code", errorcode);
		errorMap.put("msg", error);
		return errorMap;
	}

}
