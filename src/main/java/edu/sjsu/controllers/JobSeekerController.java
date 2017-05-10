package edu.sjsu.controllers;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.exceptions.JobSeekerExceptions;
import edu.sjsu.services.JobSeekerService;

@SuppressWarnings("unused")
@RestController
public class JobSeekerController {

	@Autowired
	JobSeekerService jobSeekerService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/jobseeker", method = RequestMethod.POST)
	public ResponseEntity signUp(@RequestParam("email") String email, 
								 @RequestParam("firstname") String firstname,
								 @RequestParam("lastname") String lastname,
								 @RequestParam("password") String password) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("email", email);
		parameters.put("firstname", firstname);
		parameters.put("lastname", lastname);
		parameters.put("password", password);

		try {
			jobSeekerService.createJobSeeker(parameters);
			HashMap<String, Object> reponse = new HashMap<>();
			reponse.put("result", true);
			return new ResponseEntity(reponse, responseHeaders, HttpStatus.OK);
		} catch (JobSeekerExceptions ex) {
			return new ResponseEntity(getErrorResponse("400", ex.getMessage()), responseHeaders,
					HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			if(ex.getMessage().contains("ConstraintViolationException")){
				return new ResponseEntity(getErrorResponse("400", "Email ID already registered by job seeker"), responseHeaders,
						HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity(getErrorResponse("400", ex.getMessage()), responseHeaders,
					HttpStatus.BAD_REQUEST);
		}
	}

	public HashMap<String, String> getErrorResponse(String errorcode, String error) {
		HashMap<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("code", errorcode);
		errorMap.put("msg", error);
		return errorMap;
	}
}
