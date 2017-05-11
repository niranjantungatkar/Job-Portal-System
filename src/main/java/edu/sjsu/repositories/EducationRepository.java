package edu.sjsu.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.models.Education;

public interface EducationRepository extends CrudRepository<Education, String>{

}
