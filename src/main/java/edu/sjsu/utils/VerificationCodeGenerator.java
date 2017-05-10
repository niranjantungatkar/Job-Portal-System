package edu.sjsu.utils;

import java.util.UUID;

public class VerificationCodeGenerator {

	public String getVerificationCode(){
		
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-","");
	}
	
}
