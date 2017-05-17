package edu.sjsu.controllers;

import java.net.URL;
import java.util.HashMap;
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
import edu.sjsu.models.Company;
import edu.sjsu.services.CompanyService;
import edu.sjsu.services.EmailService;

@RestController
public class CompanyController {

	@Autowired
	CompanyService companyService;

	@Autowired
	EmailService emailService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/company", method = RequestMethod.POST)
	public ResponseEntity signUp(@RequestBody Map<String, Object> parameterMap) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("companyName", (String) parameterMap.get("companyName"));
		parameters.put("email", (String) parameterMap.get("email"));
		parameters.put("password", (String) parameterMap.get("password"));
		parameters.put("websiteUrl", (String) parameterMap.get("website"));
		parameters.put("logoUrl", (String) parameterMap.get("logoURL"));
		parameters.put("address", (String) parameterMap.get("address"));
		parameters.put("companyDesc", (String) parameterMap.get("companyDesc"));
		
		try {
			Company company = companyService.createCompany(parameters);
			emailService.sendMail(company.getEmail(), "One More step - Verification!!", "Please use the code below to verify your account: \n " + company.getVerificationCode());
			HashMap<String, Object> result = new HashMap<>();
			result.put("result", true);
			result.put("id", company.getCompanyName());
			result.put("type", "company");
			result.put("verified", company.getIsVerified());
			return new ResponseEntity(result, responseHeaders, HttpStatus.OK);
		} catch (CompanyExceptions ex) {
			return new ResponseEntity(getErrorResponse("400", ex.getMessage()), responseHeaders,
					HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("400", ex.getMessage()), responseHeaders,
					HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/company", method = RequestMethod.PUT)
	public ResponseEntity updateCompany(@RequestBody Map<String, Object> parametersMap) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			Company company = companyService.updateCompany(parametersMap);
			return new ResponseEntity(company, responseHeaders, HttpStatus.OK);
		} catch (CompanyExceptions ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", ex.getMessage()), responseHeaders,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/company/{companyName}", method = RequestMethod.GET)
	public ResponseEntity getCompany(@PathVariable("companyName") String companyName) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			Company company = companyService.getCompany(companyName);
			return new ResponseEntity(company, responseHeaders, HttpStatus.OK);
		} catch (CompanyExceptions ex) {
			return new ResponseEntity(getErrorResponse("404", ex.getMessage()), responseHeaders, HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity(getErrorResponse("500", "Error occurred while updating the company information"), responseHeaders,
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
