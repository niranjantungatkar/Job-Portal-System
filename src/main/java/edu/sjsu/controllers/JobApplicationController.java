package edu.sjsu.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.exceptions.JobPostingException;
import edu.sjsu.exceptions.JobSeekerExceptions;
import edu.sjsu.models.JobApplication;
import edu.sjsu.services.JobApplicationService;

@RestController
public class JobApplicationController {

	@Autowired
	JobApplicationService jobApplicationService;

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

	public HashMap<String, String> getErrorResponse(String errorcode, String error) {
		HashMap<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("code", errorcode);
		errorMap.put("msg", error);
		return errorMap;
	}

}
