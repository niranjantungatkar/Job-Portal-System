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
import edu.sjsu.exceptions.JobApplicationExceptions;
import edu.sjsu.exceptions.JobPostingException;
import edu.sjsu.models.JobPosting;
import edu.sjsu.services.JobPostingService;

@RestController
public class JobPostingController {

	@Autowired
	JobPostingService jobPostingService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/jobposting", method = RequestMethod.POST)
	public ResponseEntity createJobPosting(@RequestBody Map<String, Object> paramtersMap) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			JobPosting jobPosting = jobPostingService.createJobPosting(paramtersMap);
			return new ResponseEntity(jobPosting, responseHeaders, HttpStatus.OK);
		} catch (CompanyExceptions ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobposting", method = RequestMethod.PUT)
	public ResponseEntity updateJobPosting(@RequestBody Map<String, Object> parameters) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			JobPosting jobPosting = jobPostingService.updateJobPosting(parameters);
			return new ResponseEntity(jobPosting, responseHeaders, HttpStatus.OK);
		} catch (JobPostingException | JobApplicationExceptions ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobposting/company/{companyName}", method = RequestMethod.GET)
	public ResponseEntity getJobPosting(@PathVariable("companyName") String companyName) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			List<JobPosting> jobPostings = jobPostingService.getJobsPostingbyCompany(companyName);
			return new ResponseEntity(jobPostings, responseHeaders, HttpStatus.OK);
		} catch (CompanyExceptions | JobPostingException ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobposting/{requisitionId}", method = RequestMethod.GET)
	public ResponseEntity getJobPostingById(@PathVariable("requisitionId") String requisitionId) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			JobPosting jobPosting = jobPostingService.getJobPosting(requisitionId);
			return new ResponseEntity(jobPosting, responseHeaders, HttpStatus.OK);
		} catch (JobPostingException ex) {
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
