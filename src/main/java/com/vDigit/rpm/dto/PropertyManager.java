package com.vDigit.rpm.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PropertyManager {
	@Id
	private String id;
	private String phone;
	private String name;

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

	public static PropertyManager makeNewPM(String id, String name, String phone) {
		PropertyManager pm = new PropertyManager();
		pm.id = id;
		pm.name = name;
		pm.phone = phone;
		return pm;
	}

	// PropertyManager(String id, String name, String phone) {
	// this.id = id;
	// this.phone = phone;
	// this.name = name;
	// }
}
