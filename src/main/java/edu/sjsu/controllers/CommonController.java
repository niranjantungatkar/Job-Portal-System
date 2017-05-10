package edu.sjsu.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.exceptions.CompanyExceptions;
import edu.sjsu.exceptions.JobSeekerExceptions;
import edu.sjsu.services.CommonService;
import edu.sjsu.services.CompanyService;
import edu.sjsu.services.JobSeekerService;

@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30)
@RestController
public class CommonController {

	@Autowired
	CommonService commonService;

	@Autowired
	JobSeekerService jobSeekerService;

	@Autowired
	CompanyService companyService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity login(HttpSession session, @RequestParam("email") String email,
			@RequestParam("password") String password) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			HashMap<String, Object> response = commonService.checkCredential(email, password);
			if (response != null) {
				session.setAttribute("id", response.get("id"));
				session.setAttribute("type", response.get("type"));
				session.setAttribute("verified", response.get("verified"));
				return new ResponseEntity(response, responseHeaders, HttpStatus.OK);
			}
			return new ResponseEntity(getErrorResponse("401", "Bad username and password"), responseHeaders,
					HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", "Internal Server error"), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ResponseEntity logout(HttpSession session) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			session.invalidate();
			HashMap<String, Object> result = new HashMap<>();
			result.put("result", true);
			return new ResponseEntity(result, responseHeaders, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", "Failed to logout"), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public ResponseEntity verifyAccount(@RequestParam(value = "id") String id, @RequestParam("type") String type,
			@RequestParam("verificationCode") String verificationCode) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		try {
			if (type.equals("jobseeker")) {
				jobSeekerService.verifyJobSeeker(id, verificationCode);
				HashMap<String, Object> result = new HashMap<>();
				result.put("rersult", true);
				return new ResponseEntity(result, responseHeaders, HttpStatus.OK);
			} else {
				companyService.verifyCompany(id, verificationCode);
				HashMap<String, Object> result = new HashMap<>();
				result.put("result", true);
				return new ResponseEntity(result, responseHeaders, HttpStatus.OK);
			}
		} catch (JobSeekerExceptions ex) {
			if (ex.getMessage().contains("Already Verified")) {
				return new ResponseEntity(getErrorResponse("200", ex.getMessage()), responseHeaders, HttpStatus.OK);
			}
			return new ResponseEntity(getErrorResponse("401", ex.getMessage()), responseHeaders,
					HttpStatus.UNAUTHORIZED);
		} catch (CompanyExceptions ex) {
			if (ex.getMessage().contains("Already Verified")) {
				return new ResponseEntity(getErrorResponse("200", ex.getMessage()), responseHeaders, HttpStatus.OK);
			}
			return new ResponseEntity(getErrorResponse("401", ex.getMessage()), responseHeaders,
					HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			return new ResponseEntity(getErrorResponse("500", "Internal server error"), responseHeaders,
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
