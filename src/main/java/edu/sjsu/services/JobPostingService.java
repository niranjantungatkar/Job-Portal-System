package edu.sjsu.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.exceptions.CompanyExceptions;
import edu.sjsu.models.Company;
import edu.sjsu.models.JobPosting;
import edu.sjsu.models.Skill;
import edu.sjsu.repositories.JobPostingRepository;

@Service
@Transactional
public class JobPostingService {

	@Autowired
	JobPostingRepository jobPostingRepository;

	@Autowired
	CompanyService companyService;

	@Autowired
	SkillService skillService;

	public JobPosting createJobPosting(Map<String, Object> parametersMap) throws CompanyExceptions {

		JobPosting jobPosting = new JobPosting();
		
		jobPosting.setTitle((String) parametersMap.get("title"));
		
		Company company = companyService.getCompany((String) parametersMap.get("companyName"));
		jobPosting.setCompany(company);
		
		jobPosting.setJobDescription((String) parametersMap.get("jobDescription"));
		jobPosting.setResponsibilities((String) parametersMap.get("responsibilities"));

		String skillInput = (String) parametersMap.get("skills");
		List<String> skillsInputList = Arrays.asList(skillInput.split("\\s*,\\s*"));
		List<Skill> skills = new ArrayList<>();

		for (String singleSkill : skillsInputList) {
			Skill skill = skillService.getSkill(singleSkill);
			if(skill == null){
				skill = skillService.createSkill(singleSkill);
			}
			skills.add(skill);
		}
		jobPosting.setSkills(skills);

		jobPosting.setSalary((Integer) parametersMap.get("salary"));
		jobPosting.setStatus(0);	// 0 status- Open position
		jobPostingRepository.save(jobPosting);
		return jobPosting;
	
	}

}
