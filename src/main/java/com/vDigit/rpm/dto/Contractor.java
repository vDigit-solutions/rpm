package com.vDigit.rpm.dto;

public class Contractor {
	private String id;
	private String name;
	private String email;
	private String phone;

	public static Contractor makeNameAndPhone(String id, String name, String phone, String email) {
		Contractor c = new Contractor();
		c.id = id;
		c.name = name;
		c.phone = phone;
		c.email = email;
		return c;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
