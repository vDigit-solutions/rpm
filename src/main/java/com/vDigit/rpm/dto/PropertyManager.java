package com.vDigit.rpm.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.vDigit.rpm.persistence.model.User;

@Document
public class PropertyManager {
	@Id
	private String id;
	private String phone;
	private String name;

	public PropertyManager(User user) {
		this.id = user.getId();
		this.phone = user.getPhone();
		this.name = user.getFirstName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
