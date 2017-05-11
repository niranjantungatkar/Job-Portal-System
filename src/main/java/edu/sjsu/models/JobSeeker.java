package edu.sjsu.models;

import java.net.URL;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class JobSeeker {

	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	private String jobseekerid; 
	
	private String firstname;

	private String lastname;

	@Column(unique = true)
	private String email;

	private String password;

	private URL picture;

	private String selfIntroduction;

	@OneToMany
	private List<WorkExperience> workExp;

	@Embedded
	private List<Education> education;

	@ManyToMany
	private List<Skill> skills; // open

	@OneToMany
	List<JobApplication> applications;

	@ManyToMany
	List<JobPosting> interestedJobs;

	private Boolean isVerified;

	private String verificationCode;

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Education> getEducation() {
		return education;
	}

	public void setEducation(List<Education> education) {
		this.education = education;
	}

	public String getJobseekerid() {
		return jobseekerid;
	}

	public void setJobseekerid(String jobseekerid) {
		this.jobseekerid = jobseekerid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public URL getPicture() {
		return picture;
	}

	public void setPicture(URL picture) {
		this.picture = picture;
	}

	public String getSelfIntroduction() {
		return selfIntroduction;
	}

	public void setSelfIntroduction(String selfIntroduction) {
		this.selfIntroduction = selfIntroduction;
	}

	public List<WorkExperience> getWorkExp() {
		return workExp;
	}

	public void setWorkExp(List<WorkExperience> workExp) {
		this.workExp = workExp;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public List<JobApplication> getApplications() {
		return applications;
	}

	public void setApplications(List<JobApplication> applications) {
		this.applications = applications;
	}

	public List<JobPosting> getInterestedJobs() {
		return interestedJobs;
	}

	public void setInterestedJobs(List<JobPosting> interestedJobs) {
		this.interestedJobs = interestedJobs;
	}

}
