package edu.sjsu.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.exceptions.CompanyExceptions;
import edu.sjsu.exceptions.JobApplicationExceptions;
import edu.sjsu.exceptions.JobPostingException;
import edu.sjsu.models.Company;
import edu.sjsu.models.JobApplication;
import edu.sjsu.models.JobPosting;
import edu.sjsu.models.JobSeeker;
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

	@Autowired
	JobApplicationService jobApplicationService;

	@Autowired
	EmailService emailService;

	public JobPosting createJobPosting(Map<String, Object> parametersMap) throws CompanyExceptions {

		JobPosting jobPosting = new JobPosting();

		jobPosting.setTitle((String) parametersMap.get("title"));

		Company company = companyService.getCompany((String) parametersMap.get("companyName"));
		jobPosting.setCompany(company);

		jobPosting.setJobDescription((String) parametersMap.get("jobDescription"));
		jobPosting.setResponsibilities((String) parametersMap.get("responsibilities"));

		String skillInput = (String) parametersMap.get("skills");
		List<String> skillsInputList = Arrays.asList(skillInput.split("\\s*,\\s*"));
		List<Skill> skills = getSkillList(skillsInputList);

		jobPosting.setSkills(skills);
		jobPosting.setSalary(Integer.valueOf(String.valueOf(parametersMap.get("salary"))));
		jobPosting.setStatus(0); // 0 status- Open position
		jobPostingRepository.save(jobPosting);
		return jobPosting;
	}

	/**
	 * Get the list of Skill object from skill name
	 * 
	 * @param skillsInputList
	 * @return List of Skill
	 */
	public List<Skill> getSkillList(List<String> skillsInputList) {
		List<Skill> skills = new ArrayList<>();

		for (String singleSkill : skillsInputList) {
			Skill skill = skillService.getSkill(singleSkill);
			if (skill == null) {
				skill = skillService.createSkill(singleSkill);
			}
			skills.add(skill);
		}
		return skills;
	}

	/**
	 * Get the JobPosting Object using requisitionId
	 * 
	 * @param requisitionId
	 * @return JobPosting
	 * @throws JobPostingException
	 */
	public JobPosting getJobPosting(String requisitionId) throws JobPostingException {

		JobPosting jobPosting = jobPostingRepository.findByRequisitionId(requisitionId);
		if (jobPosting == null) {
			throw new JobPostingException("Job Posting not found");
		}
		return jobPosting;
	}
	
	
	public List<JobPosting> getJobPostingOpen() throws JobPostingException{
		List<JobPosting> jobPostings = jobPostingRepository.findByStatus(0);
		if(jobPostings.size() == 0)
			throw new JobPostingException("No Jobs found");
		return jobPostings;
	}

	/**
	 * Update the JobPosting
	 * 
	 * @param parameters
	 * @return JobPosting
	 * @throws JobPostingException
	 * @throws JobApplicationExceptions 
	 */
	public JobPosting updateJobPosting(Map<String, Object> parameters) throws JobPostingException {

		JobPosting jobPosting = getJobPosting((String) parameters.get("requisitionId"));

		Boolean changed = false;
		int statusChanged = -1;
		StringBuilder message = new StringBuilder();
		
		if (parameters.containsKey("jobDescription")) {
			jobPosting.setJobDescription((String) parameters.get("jobDescription"));
			if (changed)
				message.append(", ");
			changed = true;
			message.append("job descrption ");
		}
		if (parameters.containsKey("responsibilities")) {
			jobPosting.setResponsibilities((String) parameters.get("responsibilities"));
			if (changed)
				message.append(", ");
			changed = true;
			message.append("job responsibilities ");
		}
		if (parameters.containsKey("skills")) {
			String skillInput = (String) parameters.get("skills");
			List<String> skillsInputList = Arrays.asList(skillInput.split("\\s*,\\s*"));
			List<Skill> skills = getSkillList(skillsInputList);
			jobPosting.setSkills(skills);
			if (changed)
				message.append(", ");
			changed = true;
			message.append("required skills ");
		}
		if (parameters.containsKey("status")) {
			jobPosting.setStatus((Integer) parameters.get("status"));
			if (changed)
				message.append(", ");
			changed = true;
			statusChanged = (Integer) parameters.get("status");
			message.append("job status ");
		}
		if (parameters.containsKey("salary")) {
			jobPosting.setSalary(Integer.valueOf(String.valueOf(parameters.get("salary"))));
			if (changed)
				message.append(", ");
			changed = true;
			message.append("salary ");
		}
		jobPostingRepository.save(jobPosting);

		String details = null;
		if(statusChanged == 1)
			details = "Position has been filled.Thanks for applying";
		else if(statusChanged == 2)
			details = "Position has been Cancelled.Thanks for applying";
		
		// Send the email to all the applicant here
		if (changed) {
			List<JobApplication> jobApplications;
			try {
				jobApplications = jobApplicationService.getJobApplicationByJobPosting(jobPosting);
				for (JobApplication jobApplication : jobApplications) {
					JobSeeker applicant = jobApplication.getApplicant();
					String subject = "Job Id " + jobPosting.getRequisitionId() + " has been updated";
					message.append("has been updated\n");
					if(details != null)
						message.append(details);
					emailService.sendMail(applicant.getEmail(), subject, message.toString());
				}
			} catch (JobApplicationExceptions e) {
				System.out.println("No one has applied for this job");
			}
		}
		return jobPosting;
	}
	
	/**Get the list of jobs by company Name
	 * 
	 * @param companyName
	 * @return
	 * @throws CompanyExceptions
	 * @throws JobPostingException
	 */
	public List<JobPosting> getJobsPostingbyCompany(String companyName) throws CompanyExceptions, JobPostingException{
		
		Company company = companyService.getCompany(companyName);
		List<JobPosting> jobPostings = jobPostingRepository.findByCompany(company);
		if(jobPostings.size() == 0)
			throw new JobPostingException("No Jobs found");
		return jobPostings;
	}
}
