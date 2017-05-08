package edu.sjsu.models;

import java.net.URL;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class JobSeeker {
	
	@Id
	private String jobseekerid; //To-do: generate id - use logic of existing ariline system
	
	private String firstname;
	
	private String lastname;
	
	@Column(unique=true)
	private String email;
	
	private URL picture;
	
	private String selfIntroduction;
	
	private List<WorkExperience> workExp;
	
	private List<Education> education;
	
	private List<String> skills; //open
	
	@OneToMany
	List<JobApplication> applications;
	
	@ManyToMany
	List<JobPosting> interestedJobs;
	
}
