package edu.sjsu.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.models.JobSeeker;
import edu.sjsu.models.Company;
import edu.sjsu.repositories.CompanyRepository;
import edu.sjsu.repositories.JobSeekerRepository;

@Transactional
@Service
public class CommonService {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	JobSeekerRepository jobSeekerRepository;

	/** Checks the credentials of both the company as well as job seeker
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public HashMap<String, Object> checkCredential(String email, String password) {

		HashMap<String, Object> result = null;
		Company company = companyRepository.findByEmailAndPassword(email, password);
		if (company != null) {
			result = new HashMap<>();
			result.put("id", company.getCompanyName());
			result.put("type", "company");
			result.put("verified", company.getIsVerified());
			return result;
		}

		JobSeeker jobSeeker = jobSeekerRepository.findByEmailAndPassword(email, password);
		if (jobSeeker != null) {
			result = new HashMap<>();
			result.put("id", jobSeeker.getJobseekerid());
			result.put("type", "jobseeker");
			result.put("verified", jobSeeker.getIsVerified());
			return result;
		}
		return result;
	}

}
