package edu.sjsu.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.models.JobSeeker;
import edu.sjsu.repositories.JobSeekerRepository;


@Transactional
@Service
public class JobSeekerService {

	@Autowired
	JobSeekerRepository jobSeekerRepository;
	
	/** Signup the job seeker
	 * 
	 * @param parameters
	 * @return
	 */
	public JobSeeker createJobSeeker(HashMap<String, String> parameters){
		JobSeeker jobSeeker = new JobSeeker();
		jobSeeker.setEmail(parameters.get("email"));
		jobSeeker.setFirstname(parameters.get("firstname"));
		jobSeeker.setFirstname(parameters.get("lastname"));
		jobSeeker.setPassword(parameters.get("password"));
		jobSeeker.setIsVerified(false);
		
		jobSeekerRepository.save(jobSeeker);
		return jobSeeker;
	}
	
}
