package edu.sjsu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.models.Skill;
import edu.sjsu.repositories.SkillRepository;

@Service
public class SkillService {

	@Autowired
	SkillRepository skillRepository;
	
	public Skill getSkill(String skillName){
		Skill skill = skillRepository.findBySkill(skillName);
		return skill;
	}
	
	public Skill createSkill(String skillName){
		Skill skill = new Skill();
		skill.setSkill(skillName);
		skillRepository.save(skill);
		return skill;
	}
	
}
