package edu.sjsu.controllers;

import java.util.HashMap;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30)
@RestController
public class HttpSessionController {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getSession", method = RequestMethod.GET)
	public ResponseEntity getSession(HttpSession session) {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		HashMap<String, Object> response = new HashMap<>();
		if(session.getAttribute("id") != null){
			response.put("id", session.getAttribute("id"));
			response.put("type", session.getAttribute("type"));
			response.put("verified", session.getAttribute("verified"));
			return new ResponseEntity(response, responseHeaders, HttpStatus.OK);
		}else{
			return null;
		}
	}
	
	
	public HashMap<String, String> getErrorResponse(String errorcode, String error) {
		HashMap<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("code", errorcode);
		errorMap.put("msg", error);
		return errorMap;
	}

}
