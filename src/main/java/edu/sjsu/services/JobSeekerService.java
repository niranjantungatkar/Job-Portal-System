package edu.sjsu.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.exceptions.JobSeekerExceptions;
import edu.sjsu.models.Company;
import edu.sjsu.models.JobSeeker;
import edu.sjsu.repositories.CompanyRepository;
import edu.sjsu.repositories.JobSeekerRepository;
import edu.sjsu.utils.VerificationCodeGenerator;

@Transactional
@Service
public class JobSeekerService {

	@Autowired
	JobSeekerRepository jobSeekerRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	EmailService emailService;

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
		jobSeeker.setFirstname(parameters.get("lastname"));
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
			if(jobSeeker.getIsVerified()){
				throw new JobSeekerExceptions("Already Verified! Please login to continue");
			}
			jobSeeker.setIsVerified(true);
			jobSeekerRepository.save(jobSeeker);
		} else {
			throw new JobSeekerExceptions("Invalid verification Code");
		}
	}
}
