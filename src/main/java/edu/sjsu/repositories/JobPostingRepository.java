package edu.sjsu.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import edu.sjsu.models.Company;
import edu.sjsu.models.JobPosting;
import edu.sjsu.models.Skill;

public interface JobPostingRepository extends PagingAndSortingRepository<JobPosting, String> {

	public JobPosting findByRequisitionId(String requisitionId);

	public List<JobPosting> findByCompany(Company company);

	public Page<JobPosting> findByStatus(int status, Pageable pageable);

	public Page<JobPosting> findByStatusAndCompanyInAndLocationInAndSalaryGreaterThanAndSalaryLessThan(int status,
			List<Company> company, List<String> locations, int salaryMin, int salaryMax, Pageable pageable);

	public Page<JobPosting> findByStatusAndCompanyInAndLocationInAndSalary(int status, List<Company> company,
			List<String> locations, int salary, Pageable pageable);

	public List<JobPosting> findByStatusAndCompanyInAndLocationInAndSkillsInAndJobDescriptionContainingIgnoreCaseOrResponsibilitiesContainingIgnoreCaseOrTitleContainingIgnoreCase(
			int status, List<Company> company, List<String> locations, List<Skill> skills, String jobDescription,
			String responsibilities, String title);

	public List<JobPosting> findByStatusAndCompanyInAndLocationIn(int status, List<Company> company,
			List<String> locations);

	public List<JobPosting> findByStatusAndSkillsInOrJobDescriptionContainingIgnoreCaseOrResponsibilitiesContainingIgnoreCaseOrTitleContainingIgnoreCase(
			int status, List<Skill> skills, String jobDescription, String responsibilities, String title, Pageable pageable);
}
