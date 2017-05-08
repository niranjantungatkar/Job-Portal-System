package edu.sjsu.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Skill {
	
	@Id
	private String skill;
	
	public Skill() {
		super();
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

}
