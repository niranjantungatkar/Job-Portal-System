package edu.sjsu.services;

import java.util.HashMap;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.exceptions.CompanyExceptions;
import edu.sjsu.models.Company;
import edu.sjsu.models.JobSeeker;
import edu.sjsu.repositories.CompanyRepository;
import edu.sjsu.repositories.JobSeekerRepository;

@Transactional
@Service
public class CompanyService {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	JobSeekerRepository jobSeekerRepository;

	/**
	 * Signup the the company
	 * 
	 * @param parameters
	 * @return
	 */
	public Company createCompany(HashMap<String, String> parameters) throws CompanyExceptions {
		
		JobSeeker jobSeeker = jobSeekerRepository.findByEmail(parameters.get("email"));
		if(jobSeeker != null){
			throw new CompanyExceptions("Email is already registered by job seeker");
		}
				
		Company companyHolder = companyRepository.findByEmail(parameters.get("email"));
		if(companyHolder != null){
			throw new CompanyExceptions("Email is already registered by company");
		}
		Company company = new Company();

		company.setEmail(parameters.get("email"));
		company.setCompanyName(parameters.get("companyName"));
		company.setPassword(parameters.get("password"));
		company.setIsVerified(false);

		companyRepository.save(company);
		return company;
	}
}
