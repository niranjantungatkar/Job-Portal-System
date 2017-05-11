package edu.sjsu.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.models.Skill;

public interface SkillRepository extends CrudRepository<Skill, String> {

	public Skill findBySkill(String skill);
}
