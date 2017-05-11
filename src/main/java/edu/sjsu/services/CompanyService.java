package edu.sjsu.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.exceptions.CompanyExceptions;
import edu.sjsu.models.Company;
import edu.sjsu.models.JobSeeker;
import edu.sjsu.repositories.CompanyRepository;
import edu.sjsu.repositories.JobSeekerRepository;
import edu.sjsu.utils.VerificationCodeGenerator;

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
		if (jobSeeker != null) {
			throw new CompanyExceptions("Email is already registered by job seeker");
		}

		Company companyHolder = companyRepository.findByEmail(parameters.get("email"));
		if (companyHolder != null) {
			throw new CompanyExceptions("Email is already registered by company");
		}
		Company company = new Company();

		company.setEmail(parameters.get("email"));
		company.setCompanyName(parameters.get("companyName"));
		company.setPassword(parameters.get("password"));
		company.setIsVerified(false);

		VerificationCodeGenerator verificationCodeGenerator = new VerificationCodeGenerator();
		String verificationCode = verificationCodeGenerator.getVerificationCode();

		company.setVerificationCode(verificationCode);

		companyRepository.save(company);
		return company;
	}

	/**
	 * Verify the company using the verification code sent in the email
	 * 
	 * @param id
	 * @param verificationCode
	 * @throws CompanyExceptions
	 */
	public void verifyCompany(String id, String verificationCode) throws CompanyExceptions {

		Company company = companyRepository.findByCompanyNameAndVerificationCode(id, verificationCode);
		if (company != null) {
			if(company.getIsVerified()){
				throw new CompanyExceptions("Already Verified! Please login to continue");
			}
			company.setIsVerified(true);
			companyRepository.save(company);
		} else {
			throw new CompanyExceptions("Invalid Verification Code");
		}
	}
	
	/**Returns the company object
	 * 
	 * @param companyName
	 * @return Company
	 * @throws CompanyExceptions
	 */
	public Company getCompany(String companyName) throws CompanyExceptions{
		Company company = companyRepository.findByCompanyName(companyName);
		
		if(company == null){
			throw new CompanyExceptions("No Company found");
		}
		return company;
	}
	
}
