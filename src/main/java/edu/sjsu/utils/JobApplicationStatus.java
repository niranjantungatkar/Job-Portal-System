package edu.sjsu.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JobApplicationStatus {

	private static final Map<Integer, String> status;
	static{
		Map<Integer, String> tempMap = new HashMap<>();
		tempMap.put(0, "PENDING");
		tempMap.put(1, "REJECTED");
		tempMap.put(2, "CANCELLED");
		tempMap.put(3, "OFFERED");
		tempMap.put(4, "OFFER_ACCEPTED");
		tempMap.put(5, "OFFER_REJECTED");
		status = Collections.unmodifiableMap(tempMap);
	}
	
	/**returns the status name from the status id
	 * 
	 * @param input
	 * @return
	 */
	public static String getStatus(int input){
		return status.get(input);
	}
}