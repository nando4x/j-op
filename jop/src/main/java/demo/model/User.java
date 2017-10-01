package demo.model;

import java.util.Date;

public class User implements IntegrationServiceData {
	private String name;
	private String surename;
	private String email;
	private Date birthdate;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the surename
	 */
	public String getSurename() {
		return surename;
	}
	/**
	 * @param surename the surename to set
	 */
	public void setSurename(String surename) {
		this.surename = surename;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the birthdate
	 */
	public Date getBirthdate() {
		return birthdate;
	}
	/**
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

}
