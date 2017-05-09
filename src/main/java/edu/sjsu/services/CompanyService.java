package edu.sjsu.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.models.Company;
import edu.sjsu.repositories.CompanyRepository;

@Transactional
@Service
public class CompanyService {

	@Autowired
	CompanyRepository companyRepository;

	/** Signup the the company
	 * 
	 * @param parameters
	 * @return
	 */
	public Company createCompany(HashMap<String, String> parameters) {
		Company company = new Company();

		company.setEmail(parameters.get("email"));
		company.setCompanyName(parameters.get("companyName"));
		company.setPassword(parameters.get("password"));
		company.setIsVerified(false);

		companyRepository.save(company);
		return company;
	}
}
