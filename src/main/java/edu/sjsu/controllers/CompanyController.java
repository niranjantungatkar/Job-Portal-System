package edu.sjsu.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.services.CompanyService;

@RestController
public class CompanyController {

	@Autowired
	CompanyService companyService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/company", method = RequestMethod.POST)
	public ResponseEntity<?> signUp(@RequestParam("email") String email, @RequestParam("companyName") String companyName,
			@RequestParam("password") String password) {
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("companyName", companyName);
		parameters.put("email", email);
		parameters.put("password", password);
		
		try{
			companyService.createCompany(parameters);
			HashMap<String, Object> result = new HashMap<>();
			result.put("result", true);
			return new ResponseEntity(result, responseHeaders, HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
			return new ResponseEntity(getErrorResponse("400", ex.getMessage()), responseHeaders, HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings("rawtypes")
	public HashMap<String, String> getErrorResponse(String errorcode, String error) {
		HashMap<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("code", errorcode);
		errorMap.put("msg", error);
		return errorMap;
	}
}
