package edu.sjsu.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.sjsu.models.JobApplication;
import edu.sjsu.models.JobPosting;

public interface JobApplicationRepository extends PagingAndSortingRepository<JobApplication, String>  {

	public List<JobApplication> findByJobPosting(JobPosting jobPosting);
}
