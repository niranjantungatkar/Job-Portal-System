package edu.sjsu.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.sjsu.models.WorkExperience;

@Repository
public interface WorkExperienceRepository extends CrudRepository<WorkExperience, String>{

}
