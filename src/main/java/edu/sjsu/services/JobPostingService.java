package edu.sjsu.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.sjsu.exceptions.CompanyExceptions;
import edu.sjsu.exceptions.JobApplicationExceptions;
import edu.sjsu.exceptions.JobPostingException;
import edu.sjsu.models.Company;
import edu.sjsu.models.JobApplication;
import edu.sjsu.models.JobPosting;
import edu.sjsu.models.JobSeeker;
import edu.sjsu.models.Skill;
import edu.sjsu.repositories.CompanyRepository;
import edu.sjsu.repositories.JobPostingRepository;
import edu.sjsu.repositories.SkillRepository;

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
	CompanyRepository companyRepository;

	@Autowired
	SkillRepository skillRepository;

	@Autowired
	EmailService emailService;

	public JobPosting createJobPosting(Map<String, Object> parametersMap) throws CompanyExceptions {

		JobPosting jobPosting = new JobPosting();

		jobPosting.setTitle((String) parametersMap.get("title"));

		Company company = companyService.getCompany((String) parametersMap.get("companyName"));
		jobPosting.setCompany(company);

		jobPosting.setJobDescription((String) parametersMap.get("jobDescription"));
		jobPosting.setResponsibilities((String) parametersMap.get("responsibilities"));
		jobPosting.setLocation((String) parametersMap.get("location"));

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

	/**
	 * Get all open positions
	 * 
	 * @param pageable
	 * @return
	 * @throws JobPostingException
	 */
	public Page<JobPosting> getJobPostingOpen(Pageable pageable) throws JobPostingException {
		Page<JobPosting> jobPostings = jobPostingRepository.findByStatus(0, pageable);
		if (jobPostings.getSize() == 0)
			throw new JobPostingException("No Jobs found");
		return jobPostings;
	}

	/**
	 * 
	 * @param pageable
	 * @param parameters
	 * @return
	 */
	public Page<JobPosting> getJobPostingSearch(Pageable pageable, Map<String, Object> parameters) {

		String freeText = parameters.get("freeText").toString().trim();

		int counter = 0;
		List<String> criteria;
		if (freeText.equals("")) {
			criteria = new ArrayList<>();
		} else {
			criteria = new ArrayList<>(Arrays.asList(freeText.split("\\s(,|\\s)\\s*")));
		}

		List<String> keyWord = new ArrayList<>();
		keyWord.add("at");
		keyWord.add("in");
		keyWord.add("with");
		keyWord.add("where");
		keyWord.add("near");
		keyWord.add("about");
		keyWord.add("around");
		keyWord.add("similar");
		keyWord.add("show");
		keyWord.add("on");
		keyWord.add("the");

		criteria.removeAll(keyWord);

		String companiesStr = (String) parameters.get("companies");
		if (companiesStr.equals("")) {
			counter++;
		}
		List<String> items = new ArrayList<>(Arrays.asList(companiesStr.split("\\s*(,)\\s*")));
		items.addAll(criteria);

		String locationsStr = (String) parameters.get("location");
		if (locationsStr.equals("")) {
			counter++;
		}
		List<String> locations = new ArrayList<>(Arrays.asList(locationsStr.split("\\s*(,)\\s*")));
		locations.addAll(criteria);

		int salaryMin = 0;
		int salaryMax = 99999999;
		if (!parameters.get("minSalary").toString().equals("")) {
			salaryMin = Integer.parseInt((String) parameters.get("minSalary"));
		}
		if (!parameters.get("maxSalary").toString().equals("")) {
			salaryMax = Integer.parseInt((String) parameters.get("maxSalary"));
		}
		if (parameters.get("location").equals("")) {
			List<JobPosting> temp = (List<JobPosting>) jobPostingRepository.findAll();
			for (JobPosting tempJob : temp) {
				locations.add(tempJob.getLocation());
			}
		}
		if (parameters.get("companies").equals("")) {
			List<Company> temp = (List<Company>) companyRepository.findAll();
			for (Company tempComp : temp) {
				items.add(tempComp.getCompanyName());
			}
		}

		List<Company> companies = companyRepository.findByCompanyNameIn(items);

		List<JobPosting> jobPostings = new ArrayList<>();
		List<JobPosting> jobPostings1 = new ArrayList<>();
		if (salaryMin == -1) {
			jobPostings = jobPostingRepository.findByStatusAndCompanyInAndLocationInAndSalary(0, companies, locations,
					salaryMax, pageable);

		} else {
			jobPostings = jobPostingRepository
					.findByStatusAndCompanyInAndLocationInAndSalaryGreaterThanAndSalaryLessThan(0, companies, locations,
							salaryMin, salaryMax, pageable);
		}

		if (!freeText.equals("")) {
			List<Skill> skills = skillRepository.findBySkillIn(criteria);
			Set<JobPosting> freeTextSet = new HashSet<>();
			Set<JobPosting> filterSet = new HashSet<>();
			filterSet.addAll(jobPostings);
			for (String blindSearch : criteria) {

				freeTextSet.addAll(jobPostingRepository
						.findByStatusAndSkillsInOrJobDescriptionContainingIgnoreCaseOrResponsibilitiesContainingIgnoreCaseOrTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(
								0, skills, blindSearch, blindSearch, blindSearch, blindSearch, pageable));

			}

			Set<JobPosting> output = new HashSet<>(filterSet);
			output.retainAll(freeTextSet);
			List<JobPosting> jobsList = new ArrayList<>();
			jobsList.addAll(output);
			Page<JobPosting> jobPostingsPage = new PageImpl<>(jobsList);
			return jobPostingsPage;
		}
		Page<JobPosting> jobPostingsPage = new PageImpl<>(jobPostings);
		return jobPostingsPage;
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

		if (parameters.containsKey("status")) {
			jobPosting.setStatus((Integer) parameters.get("status"));
			statusChanged = (Integer) parameters.get("status");
			try {
				List<JobApplication> jobApplications1 = jobApplicationService.getJobApplicationByJobPosting(jobPosting);
				if (statusChanged == 1) {
					Boolean markedFilled = false;
					for (JobApplication jobApp : jobApplications1) {
						if (jobApp.getApplicationStatus() == 4) {
							markedFilled = true;
						}
					}
					if (markedFilled == false) {
						throw new JobPostingException(
								"Position can not be marked as Filled unless applicant accepts the job offer");
					}
				} else if (statusChanged == 2) {
					Boolean markedAccepted = false;
					for (JobApplication jobApp : jobApplications1) {
						if (jobApp.getApplicationStatus() == 4) {
							markedAccepted = true;
						}
					}
					if (markedAccepted == true) {
						throw new JobPostingException(
								"Position can not be Cancelled as applicant has accepted the offer");
					}
				}
			} catch (JobApplicationExceptions ex) {
			}
			jobPosting.setStatus(statusChanged);
		}
		if (parameters.containsKey("jobDescription")) {
			jobPosting.setJobDescription((String) parameters.get("jobDescription"));
			changed = true;
		}
		if (parameters.containsKey("responsibilities")) {
			jobPosting.setResponsibilities((String) parameters.get("responsibilities"));
			changed = true;
		}
		if (parameters.containsKey("location")) {
			jobPosting.setLocation((String) parameters.get("location"));
			changed = true;
		}
		if (parameters.containsKey("skills")) {
			String skillInput = (String) parameters.get("skills");
			List<String> skillsInputList = Arrays.asList(skillInput.split("\\s*,\\s*"));
			List<Skill> skills = getSkillList(skillsInputList);
			jobPosting.setSkills(skills);
			changed = true;
		}
		if (parameters.containsKey("salary")) {
			jobPosting.setSalary(Integer.valueOf(String.valueOf(parameters.get("salary"))));
			changed = true;
		}
		jobPostingRepository.save(jobPosting);

		String details = null;
		if (statusChanged == 1)
			details = "\nFollowing position has been filled.";
		else if (statusChanged == 2)
			details = "\nFollowing position has been Cancelled.";

		// Send the email to all the applicant here
		if (changed || statusChanged > 0) {
			List<JobApplication> jobApplications;
			try {
				jobApplications = jobApplicationService.getJobApplicationByJobPosting(jobPosting);
				for (JobApplication jobApplication : jobApplications) {
					JobSeeker applicant = jobApplication.getApplicant();
					String subject = "Job - " + jobPosting.getTitle() + " at "
							+ jobPosting.getCompany().getCompanyName() + " has been updated";
					message.append("Dear " + applicant.getFirstname());
					if (changed)
						message.append(
								"\n\nFollowing job has been updated.\nPlease have look at updated job details on portal.");
					if (details != null)
						message.append(details);
					message.append("\n\nJob Position : " + jobPosting.getTitle());
					message.append("\nJob Requisition Id : " + jobPosting.getRequisitionId());
					message.append(
							"\n\nBest Regards\nTalent Acquisition Team\n" + jobPosting.getCompany().getCompanyName());
					emailService.sendMail(applicant.getEmail(), subject, message.toString());
				}
			} catch (JobApplicationExceptions e) {
				System.out.println("No one has applied for this job");
			}
		}
		return jobPosting;
	}

	/**
	 * Get the list of jobs by company Name
	 * 
	 * @param companyName
	 * @return
	 * @throws CompanyExceptions
	 * @throws JobPostingException
	 */
	public List<JobPosting> getJobsPostingbyCompany(String companyName) throws CompanyExceptions, JobPostingException {

		Company company = companyService.getCompany(companyName);
		List<JobPosting> jobPostings = jobPostingRepository.findByCompany(company);
		if (jobPostings.size() == 0)
			throw new JobPostingException("No Jobs found");
		return jobPostings;
	}
}
