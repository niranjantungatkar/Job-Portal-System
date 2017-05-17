package edu.sjsu.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.models.Education;
import edu.sjsu.repositories.EducationRepository;

@Transactional
@Service
public class EducationService {

	@Autowired
	EducationRepository educationRepository;

	public Education createEducation(LinkedHashMap<String, String> educationMap) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");

		Education education = new Education();

		education.setInstitute(educationMap.get("institute"));
		education.setDegree(educationMap.get("degree"));
		String startDate = educationMap.get("startDate");
		String endDate = educationMap.get("endDate");

		Date start = null;
		Date end = null;

		try {
			start = formatter.parse(startDate);
			end = formatter.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		education.setStartDate(start);
		education.setEndDate(end);
		educationRepository.save(education);
		return education;
	}

	public void deleteEducation(List<Education> oldEducationList) {
		educationRepository.delete(oldEducationList);
	}

}
