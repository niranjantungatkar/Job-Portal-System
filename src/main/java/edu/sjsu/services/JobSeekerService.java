package edu.sjsu.services;

import java.util.HashMap;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.exceptions.JobSeekerExceptions;
import edu.sjsu.models.Company;
import edu.sjsu.models.JobSeeker;
import edu.sjsu.repositories.CompanyRepository;
import edu.sjsu.repositories.JobSeekerRepository;
import javassist.expr.Instanceof;


@Transactional
@Service
public class JobSeekerService {

	@Autowired
	JobSeekerRepository jobSeekerRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	/** Signup the job seeker
	 * 
	 * @param parameters
	 * @return
	 */
	public JobSeeker createJobSeeker(HashMap<String, String> parameters) throws JobSeekerExceptions{
		JobSeeker jobSeeker = new JobSeeker();
		jobSeeker.setEmail(parameters.get("email"));
		jobSeeker.setFirstname(parameters.get("firstname"));
		jobSeeker.setFirstname(parameters.get("lastname"));
		jobSeeker.setPassword(parameters.get("password"));
		jobSeeker.setIsVerified(false);
		
		Company company = companyRepository.findByEmail(parameters.get("email"));
		if(company != null){
			throw new JobSeekerExceptions("Email ID already registered with company");
		}
		jobSeekerRepository.save(jobSeeker);	
		return jobSeeker;
	}
	
}
