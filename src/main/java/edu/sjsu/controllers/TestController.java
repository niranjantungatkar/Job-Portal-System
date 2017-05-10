package edu.sjsu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.services.EmailService;

@RestController
public class TestController {

	@Autowired
	private EmailService emailService;
	
	public TestController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value="/test")
	public ResponseEntity testEmail() {
		emailService.sendMail("to", "subject", "body");
		return new ResponseEntity("lalala",HttpStatus.OK);
	}
}
