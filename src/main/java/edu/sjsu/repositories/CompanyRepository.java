package edu.sjsu.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.models.Company;

public interface CompanyRepository extends CrudRepository<Company, String> {

	public Company findByEmailAndPassword(String email, String password);
	
	public Company findByEmail(String email);
	
	public Company findByCompanyNameAndVerificationCode(String companyName, String verificationCode);
}
