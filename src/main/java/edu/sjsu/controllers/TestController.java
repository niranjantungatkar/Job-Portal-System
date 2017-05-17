package edu.sjsu.controllers;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	public TestController() {
		super();
	}
	
	@RequestMapping(value="/fileupload")
	public ResponseEntity fileupload() {
		System.out.println("got it");
		return new ResponseEntity("added",HttpStatus.OK);
	}
	
	@SuppressWarnings("restriction")
	@RequestMapping(value="/signature", method=RequestMethod.POST)
	public ResponseEntity returnSignature() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		
		String aws_secret_key="2LvzBvB3KGtYukSMsSLpy8WGNQx4ZAcXf7SwY7dZ";
		String policy_document=
			"{"+"\"expiration\": \"2020-01-01T00:00:00Z\","+
				    "\"conditions\": ["+
				        "{\"bucket\": \"angular-file-upload\"},"+
				        "[\"starts-with\", \"$key\", \"\"],"+
				        "{\"acl\": \"public-read-write\"},"+
				        "[\"starts-with\", \"$Content-Type\", \"\"],"+
				        "[\"starts-with\", \"$filename\", \"\"],"+
				        "[\"content-length-range\", 0, 524288000]"+
				    "]"+
				"}";
		
		String policy = (new BASE64Encoder()).encode(
			    policy_document.getBytes("UTF-8")).replaceAll("\n","").replaceAll("\r","");
		
		Mac hmac = Mac.getInstance("HmacSHA1");
		hmac.init(new SecretKeySpec(
		    aws_secret_key.getBytes("UTF-8"), "HmacSHA1"));
		
		String signature = (new BASE64Encoder()).encode( hmac.doFinal(policy.getBytes("UTF-8"))).replaceAll("\n", "");
		Map<String, String> data = new HashMap<>();
		data.put("policy",policy);
		data.put("signature", signature);
		return new ResponseEntity(data,HttpStatus.OK);
	}

}
