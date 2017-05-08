package edu.sjsu.models;

import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Email;


@Entity
public class Company {

	@Id
	private String companyName;
	
	@Column(unique=true)
	private String email;
	
	private URL website;
	
	private URL logoURL;
	
	private String address;
	
	private String companyDesc;
	
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
