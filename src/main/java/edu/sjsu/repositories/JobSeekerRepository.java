package edu.sjsu.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.models.JobSeeker;

public interface JobSeekerRepository extends CrudRepository<JobSeeker, String>{

	public JobSeeker findByEmailAndPassword(String email, String password);
	
	public JobSeeker findByEmail(String email);
	
	public JobSeeker findByJobseekeridAndVerificationCode(String id, String verificationCode);
}
