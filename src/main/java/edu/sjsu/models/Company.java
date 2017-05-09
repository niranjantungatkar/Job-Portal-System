package edu.sjsu.models;

import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Company {

	@Id
	private String companyName;

	@Column(unique = true)
	private String email;

	private String password;

	private URL website;

	private URL logoURL;

	private String address;

	private String companyDesc;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Company() {
		super();
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public URL getWebsite() {
		return website;
	}

	public void setWebsite(URL website) {
		this.website = website;
	}

	public URL getLogoURL() {
		return logoURL;
	}

	public void setLogoURL(URL logoURL) {
		this.logoURL = logoURL;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

}
