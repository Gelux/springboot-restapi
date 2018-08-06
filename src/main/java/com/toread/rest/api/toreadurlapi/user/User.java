package com.toread.rest.api.toreadurlapi.user;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
public class User {
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column(unique=true)
	private String email;
	
	@Size(min=5, message = "Pass should have at least 5 chars")
	private String pass;
	
	@Past
	private Date regDate;
	
	@OneToMany(mappedBy="user")
	private List<Urls> urls;
	
	public User(int id, String email, String pass, Date regDate) {
		super();
		this.id = id;
		this.email = email;
		this.pass = pass;
		this.regDate = regDate;
	}
	
	public User() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPass() {
		return pass;
	}


	public void setPass(String pass) {
		this.pass = pass;
	}


	public Date getRegDate() {
		return regDate;
	}


	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public List<Urls> getUrls() {
		return urls;
	}

	public void setUrls(List<Urls> urls) {
		this.urls = urls;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", pass=" + pass + ", regDate=" + regDate + "]";
	}
	
}
