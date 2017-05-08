package edu.sjsu.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.sjsu.models.JobApplication;

public interface JobApplicationRepository extends PagingAndSortingRepository<JobApplication, String>  {

}
