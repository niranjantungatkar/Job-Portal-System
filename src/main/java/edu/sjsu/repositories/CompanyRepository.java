package edu.sjsu.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.models.Company;

public interface CompanyRepository extends CrudRepository<Company, String> {

	public Company findByCompanyName(String companyName);
	
	public Company findByEmailAndPassword(String email, String password);
	
	public Company findByEmail(String email);
	
	public Company findByCompanyNameAndVerificationCode(String companyName, String verificationCode);
	
	public List<Company> findByCompanyNameIn(List<String> companyName);
}
