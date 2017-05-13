package edu.sjsu.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import edu.sjsu.models.Company;
import edu.sjsu.models.JobPosting;

public interface JobPostingRepository extends PagingAndSortingRepository<JobPosting, String>{
	
	public JobPosting findByRequisitionId(String requisitionId);
	
	public List<JobPosting> findByCompany(Company company);
	
	public Page<JobPosting> findByStatus(int status, Pageable pageable);
}
