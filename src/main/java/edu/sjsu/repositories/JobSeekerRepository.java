package edu.sjsu.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.sjsu.models.JobSeeker;

@Repository
public interface JobSeekerRepository extends CrudRepository<JobSeeker, String>{

	public JobSeeker findByEmailAndPassword(String email, String password);
	
	public JobSeeker findByEmail(String email);
	
	public JobSeeker findByJobseekerid(String jobseekerid);
	
	public JobSeeker findByJobseekeridAndVerificationCode(String id, String verificationCode);
}
