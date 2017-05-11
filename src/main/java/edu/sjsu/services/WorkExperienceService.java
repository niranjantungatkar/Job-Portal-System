package edu.sjsu.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.models.WorkExperience;
import edu.sjsu.repositories.WorkExperienceRepository;

@Transactional
@Service
public class WorkExperienceService {

	@Autowired
	WorkExperienceRepository workExperienceRepository;
	
	public WorkExperience createWorkExperience(LinkedHashMap<String, String> parameters){
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("MMM-YYYY");
		
		WorkExperience workExperience = new WorkExperience();
		workExperience.setCompany(parameters.get("company"));
		workExperience.setPositionHeld(parameters.get("positionHeld"));

		String startDate = parameters.get("startDate");
		String endDate = parameters.get("endDate");
		Date start = null;
		Date end = null;

			try {
				start = formatter.parse(startDate);
				end = formatter.parse(endDate);
			} catch (ParseException e) {
			}
		workExperience.setStartDate(start);
		workExperience.setEndDate(end);
		workExperienceRepository.save(workExperience);
		return workExperience;
	}
	
	public void deleteWorkExperience(List<WorkExperience> removedList){
		workExperienceRepository.delete(removedList);
	}
	

}
