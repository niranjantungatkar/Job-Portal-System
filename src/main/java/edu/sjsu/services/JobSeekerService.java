package edu.sjsu.services;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.exceptions.EducationExceptions;
import edu.sjsu.exceptions.JobSeekerExceptions;
import edu.sjsu.exceptions.SkillExceptions;
import edu.sjsu.exceptions.WorkExperienceExceptions;
import edu.sjsu.models.Company;
import edu.sjsu.models.Education;
import edu.sjsu.models.JobSeeker;
import edu.sjsu.models.Skill;
import edu.sjsu.models.WorkExperience;
import edu.sjsu.repositories.CompanyRepository;
import edu.sjsu.repositories.JobSeekerRepository;
import edu.sjsu.repositories.WorkExperienceRepository;
import edu.sjsu.utils.VerificationCodeGenerator;

@Transactional
@Service
public class JobSeekerService {

	@Autowired
	JobSeekerRepository jobSeekerRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	WorkExperienceService workExperienceService;

	@Autowired
	EmailService emailService;

	@Autowired
	SkillService skillService;

	@Autowired
	EducationService educationService;

	/**
	 * Signup the job seeker
	 * 
	 * @param parameters
	 * @return
	 */
	public JobSeeker createJobSeeker(HashMap<String, String> parameters) throws JobSeekerExceptions {

		Company company = companyRepository.findByEmail(parameters.get("email"));
		if (company != null) {
			throw new JobSeekerExceptions("Email ID already registered by company");
		}

		JobSeeker jobSeeker = new JobSeeker();
		jobSeeker.setEmail(parameters.get("email"));
		jobSeeker.setFirstname(parameters.get("firstname"));
		jobSeeker.setLastname(parameters.get("lastname"));
		jobSeeker.setPassword(parameters.get("password"));
		jobSeeker.setIsVerified(false);

		VerificationCodeGenerator verificationCodeGenerator = new VerificationCodeGenerator();
		String verificationCode = verificationCodeGenerator.getVerificationCode();

		jobSeeker.setVerificationCode(verificationCode);
		jobSeekerRepository.save(jobSeeker);
		return jobSeeker;
	}

	/**
	 * Verify the job seeker from the verification code send in email
	 * 
	 * @param id
	 * @param verificationCode
	 * @return
	 */
	public void verifyJobSeeker(String id, String verificationCode) throws JobSeekerExceptions {

		JobSeeker jobSeeker = jobSeekerRepository.findByJobseekeridAndVerificationCode(id, verificationCode);
		if (jobSeeker != null) {
			if (jobSeeker.getIsVerified()) {
				throw new JobSeekerExceptions("Already Verified! Please login to continue");
			}
			jobSeeker.setIsVerified(true);
			jobSeekerRepository.save(jobSeeker);
		} else {
			throw new JobSeekerExceptions("Invalid verification Code");
		}
	}

	/** Update the profile of jobSeeker
	 * 
	 * @param parameters
	 * @return UpdatedJobseeker
	 * @throws JobSeekerExceptions
	 * @throws WorkExperienceExceptions
	 * @throws SkillExceptions
	 * @throws EducationExceptions
	 */
	@SuppressWarnings({ "unchecked" })
	public JobSeeker updateProfile(Map<String, Object> parameters)
			throws JobSeekerExceptions, WorkExperienceExceptions, SkillExceptions, EducationExceptions {
		JobSeeker jobSeeker = jobSeekerRepository.findByJobseekerid((String) parameters.get("id"));

		if (jobSeeker == null) {
			throw new JobSeekerExceptions("No profile found");
		}

		if (parameters.containsKey("firstname")) {
			jobSeeker.setFirstname((String) parameters.get("firstname"));
		}
		if (parameters.containsKey("lastname")) {
			jobSeeker.setLastname((String) parameters.get("lastname"));
		}
		if (parameters.containsKey("password")) {
			jobSeeker.setPassword((String) parameters.get("password"));
		}
		if (parameters.containsKey("selfIntroduction")) {
			jobSeeker.setSelfIntroduction((String) parameters.get("selfIntroduction"));
		}
		if (parameters.containsKey("picture")) {
			jobSeeker.setPicture((URL) parameters.get("picture"));
		}

		// Update the skills
		if (parameters.containsKey("skills")) {
			try {
				List<Skill> skillsList = updateSkillsList(parameters);
				jobSeeker.setSkills(skillsList);
			} catch (Exception ex) {
				throw new SkillExceptions("Error occurred while updating the skills. Try again later");
			}

		}

		// update the workExperience
		if (parameters.containsKey("workExperience")) {
			List<WorkExperience> oldWorkExp = jobSeeker.getWorkExp();
			try {
				workExperienceService.deleteWorkExperience(oldWorkExp);
				List<WorkExperience> workExpList = updateWorkExperience(parameters);
				jobSeeker.setWorkExp(workExpList);
			} catch (Exception ex) {
				throw new WorkExperienceExceptions(
						"Error occurred while updating the work experience. Try again later.");
			}
		}

		//Update the education
		if (parameters.containsKey("education")) {
			List<Education> oldEducationList = jobSeeker.getEducation();
			try {
				educationService.deleteEducation(oldEducationList);
				List<Education> educatonList = updateEducation(parameters);
				jobSeeker.setEducation(educatonList);
			} catch (Exception ex) {
				throw new EducationExceptions("Error occurred while updating the work education. Try again later");
			}

		}

		jobSeekerRepository.save(jobSeeker);
		return jobSeeker;
	}

	/**
	 * Get the jobseeker's profile
	 * 
	 * @param id
	 * @return JobSeeker
	 */
	public JobSeeker getProfile(String id) throws JobSeekerExceptions {
		JobSeeker jobSeeker = jobSeekerRepository.findByJobseekerid(id);
		if (jobSeeker == null) {
			throw new JobSeekerExceptions("No profile found.");
		}
		return jobSeeker;
	}

	/**
	 * Update the skills
	 * 
	 * @param parameters
	 * @return List of updated Skill
	 */
	@SuppressWarnings("unchecked")
	public List<Skill> updateSkillsList(Map<String, Object> parameters) {
		List<Skill> skillsList = new ArrayList<>();
		List<LinkedHashMap<String, String>> skillsInput = (List<LinkedHashMap<String, String>>) parameters
				.get("skills");
		for (LinkedHashMap<String, String> skillMap : skillsInput) {
			Skill skill = null;
			try {
				skill = skillService.createSkill(skillMap.get("skill"));
				skillsList.add(skill);
			} catch (Exception ex) {
				System.out.println("Skills already exists, so not adding again");
			}
		}
		return skillsList;
	}

	/**
	 * Update the Work Experience.
	 * 
	 * @param parameters
	 * @return List of Updated WorkExperience
	 */
	@SuppressWarnings("unchecked")
	public List<WorkExperience> updateWorkExperience(Map<String, Object> parameters) {
		List<WorkExperience> workExpList = new ArrayList<>();
		List<LinkedHashMap<String, String>> workExpInput = (List<LinkedHashMap<String, String>>) parameters
				.get("workExperience");
		for (LinkedHashMap<String, String> workExp : workExpInput) {
			WorkExperience workExperience = workExperienceService.createWorkExperience(workExp);
			workExpList.add(workExperience);
		}
		return workExpList;
	}

	/** Update thh education
	 * 
	 * @param parameters
	 * @return List of Updated education
	 */
	@SuppressWarnings("unchecked")
	public List<Education> updateEducation(Map<String, Object> parameters) {
		List<Education> educationList = new ArrayList<>();
		List<LinkedHashMap<String, String>> educationInput = (List<LinkedHashMap<String, String>>) parameters
				.get("education");
		for (LinkedHashMap<String, String> edu : educationInput) {
			Education newEdu = educationService.createEducation(edu);
			educationList.add(newEdu);
		}
		return educationList;
	}
}
