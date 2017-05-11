package edu.sjsu.controllers;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.exceptions.JobSeekerExceptions;
import edu.sjsu.exceptions.SkillExceptions;
import edu.sjsu.exceptions.WorkExperienceExceptions;
import edu.sjsu.models.JobSeeker;
import edu.sjsu.services.JobSeekerService;
import edu.sjsu.services.EmailService;

@SuppressWarnings("unused")
@RestController
public class JobSeekerController {

	@Autowired
	JobSeekerService jobSeekerService;

	@Autowired
	EmailService emailService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/jobseeker", method = RequestMethod.POST)
	public ResponseEntity signUp(@RequestBody Map<String, Object> parameterMap) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("email", (String) parameterMap.get("email"));
		parameters.put("firstname", (String) parameterMap.get("firstname"));
		parameters.put("lastname", (String) parameterMap.get("lastname"));
		parameters.put("password", (String) parameterMap.get("password"));

		try {
			JobSeeker jobSeeker = jobSeekerService.createJobSeeker(parameters);
			emailService.sendMail(jobSeeker.getEmail(), "Test Email", jobSeeker.getVerificationCode());
			HashMap<String, Object> response = new HashMap<>();
			response.put("result", true);
			response.put("id", jobSeeker.getJobseekerid());
			response.put("type", "jobseeker");
			response.put("verified", jobSeeker.getIsVerified());
			return new ResponseEntity(response, responseHeaders, HttpStatus.OK);
		} catch (JobSeekerExceptions ex) {
			return new ResponseEntity(getErrorResponse("400", ex.getMessage()), responseHeaders,
					HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			if (ex.getMessage().contains("ConstraintViolationException")) {
				return new ResponseEntity(getErrorResponse("400", "Email ID already registered by job seeker"),
						responseHeaders, HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity(getErrorResponse("400", ex.getMessage()), responseHeaders,
					HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobseeker", method = RequestMethod.PUT)
	public ResponseEntity updateProfile(@RequestBody Map<String, Object> parameterMap) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			JobSeeker jobSeeker = jobSeekerService.updateProfile(parameterMap);
			return new ResponseEntity(jobSeeker, responseHeaders, HttpStatus.OK);
		} catch (JobSeekerExceptions ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (WorkExperienceExceptions ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (SkillExceptions ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}catch( Exception ex){
			return new ResponseEntity(getErrorResponse("500", "Error occurred while updating the profile. Try again later"), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jobseeker/{id}", method = RequestMethod.GET)
	public ResponseEntity getProfile(@PathVariable("id") String id) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		JobSeeker jobSeeker = jobSeekerService.getProfile(id);
		return new ResponseEntity(jobSeeker, responseHeaders, HttpStatus.OK);
	}

	public HashMap<String, String> getErrorResponse(String errorcode, String error) {
		HashMap<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("code", errorcode);
		errorMap.put("msg", error);
		return errorMap;
	}
}
