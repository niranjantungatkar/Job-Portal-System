package edu.sjsu.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.models.Interview;

public interface InterviewRepository extends CrudRepository<Interview, String> {

	public Interview findByStatus(int status);
	
	public Interview findByInterviewNo(int interviewNo);
}
