package com.vDigit.rpm.dto;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Contractor {

	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String vendorCompanyName;
	private String type;

	public static Contractor makeNameAndPhone(String firstName, String lastName, String phone, String email,
			String type, String vendorCompanyName) {
		Contractor c = new Contractor();
		c.id = UUID.randomUUID().toString();
		c.firstName = firstName;
		c.lastName = lastName;
		c.phone = phone;
		c.email = email;
		c.type = type;
		c.vendorCompanyName = vendorCompanyName;
		return c;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getType() {
		return type;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getVendorCompanyName() {
		return vendorCompanyName;
	}

	public void setVendorCompanyName(String vendorCompanyName) {
		this.vendorCompanyName = vendorCompanyName;
	}

	public void setType(String type) {
		this.type = type;
	}
}
