package com.vDigit.rpm.dto;

public class Address {
	private String street1;
	private String street2;
	private String city;
	private String zip;
	private String state;
	private String country;

	public String toString() {
		return street1 + " " + street2 + "\n" + city + ", " + state + " " + zip;
	}

}
