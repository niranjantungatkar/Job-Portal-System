package edu.sjsu.services;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

		try {
			URL websiteUrl = new URL(parameters.get("websiteUrl"));
			URL logoUrl = new URL(parameters.get("logoUrl"));
			company.setWebsite(websiteUrl);
			company.setLogoURL(logoUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		company.setCompanyDesc(parameters.get("companyDesc"));
		company.setEmail(parameters.get("email"));
		company.setCompanyName(parameters.get("companyName"));
		company.setPassword(parameters.get("password"));
		company.setAddress(parameters.get("address"));
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
			if (company.getIsVerified()) {
				throw new CompanyExceptions("Already Verified! Please login to continue");
			}
			company.setIsVerified(true);
			companyRepository.save(company);
		} else {
			throw new CompanyExceptions("Invalid Verification Code");
		}
	}

	/**Update the company information
	 * 
	 * @param parameterMap
	 * @return Company
	 * @throws CompanyExceptions
	 * @throws MalformedURLException
	 */
	public Company updateCompany(Map<String, Object> parameterMap) throws CompanyExceptions, MalformedURLException{

		Company company = companyRepository.findByCompanyName((String) parameterMap.get("companyName"));
		if (company == null) {
			throw new CompanyExceptions("Company not found");
		}
		if (parameterMap.containsKey("password")) {
			company.setPassword((String) parameterMap.get("password"));
		}
		if (parameterMap.containsKey("website")) {
			URL websiteUrl = new URL((String) parameterMap.get("website"));
			company.setWebsite(websiteUrl);
		}
		if (parameterMap.containsKey("logoUrl")) {
			URL logoUrl = new URL((String)parameterMap.get("logoUrl"));
			company.setLogoURL(logoUrl);
		}
		if (parameterMap.containsKey("address")) {
			company.setAddress((String) parameterMap.get("address"));
		}
		if (parameterMap.containsKey("companyDesc")) {
			company.setCompanyDesc((String) parameterMap.get("companyDesc"));
		}
		companyRepository.save(company);
		return company;
	}

	/**
	 * Returns the company object
	 * 
	 * @param companyName
	 * @return Company
	 * @throws CompanyExceptions
	 */
	public Company getCompany(String companyName) throws CompanyExceptions {
		Company company = companyRepository.findByCompanyName(companyName);

		if (company == null) {
			throw new CompanyExceptions("No Company found");
		}
		return company;
	}

}
