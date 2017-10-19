package com.vDigit.rpm.persistence.model;

import java.util.Collection;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

	@Id
	private String id;

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	private String phone;

	private boolean enabled;

	private boolean isUsing2FA;

	private String secret;

	//

	private Collection<Role> roles;

	public User() {
		super();
		this.secret = Base32.random();
		this.enabled = false;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String username) {
		this.email = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(final Collection<Role> roles) {
		this.roles = roles;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isUsing2FA() {
		return isUsing2FA;
	}

	public void setUsing2FA(boolean isUsing2FA) {
		this.isUsing2FA = isUsing2FA;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User user = (User) obj;
		if (!email.equals(user.email)) {
			return false;
		}
		if (phone == null && user.phone == null) {
			return true;
		}
		if (!phone.equals(user.phone)) {
			return false;
		}
		return true;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("User [id=").append(id).append(", firstName=").append(firstName).append(", lastName=")
				.append(lastName).append(", email=").append(email).append(", password=").append(password)
				.append(", enabled=").append(enabled).append(", isUsing2FA=").append(isUsing2FA).append(", secret=")
				.append(secret).append(", roles=").append(roles).append("]");
		return builder.toString();
	}

}