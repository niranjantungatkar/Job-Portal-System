package edu.sjsu.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.sjsu.models.JobPosting;

public interface JobPostingRepository extends PagingAndSortingRepository<JobPosting, String>{

}
