package com.vDigit.rpm.dto;

import org.apache.commons.lang.StringUtils;

public class Address {
	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	private String street1;
	private String street2;
	private String city;
	private String zip;
	private String state;
	private String country;

	public String toString() {
		StringBuilder address = new StringBuilder(street1);
		if (StringUtils.isNotBlank(street2)) {
			address.append(" ").append(street2);
		}
		address.append("\n");
		if (StringUtils.isNotBlank(city)) {
			address.append(city).append(",");
		}
		if (StringUtils.isNotBlank(state)) {
			address.append(state).append(" ");
		}
		if (StringUtils.isNotBlank(zip)) {
			address.append(zip).append("\n");
		}
		return address.toString();
	}

}
