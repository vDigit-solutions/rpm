package com.vDigit.rpm.util;

import java.util.Properties;

public class PersonalPropertyManager implements PropertyManager {
	private String twilioAccountSID;
	private String twilioPhone;
	private String twilioAuthToken;
	private String phoneNotifications = "true";
	private String appUrl;

	public String getTwilioAccountSID() {
		return twilioAccountSID;
	}

	public void setTwilioAccountSID(String twilioAccountSID) {
		this.twilioAccountSID = twilioAccountSID;
	}

	public String getTwilioPhone() {
		return twilioPhone;
	}

	public void setTwilioPhone(String twilioPhone) {
		this.twilioPhone = twilioPhone;
	}

	public String getTwilioAuthToken() {
		return twilioAuthToken;
	}

	public void setTwilioAuthToken(String twilioAuthToken) {
		this.twilioAuthToken = twilioAuthToken;
	}

	public PersonalPropertyManager() {

	}

	public void setProperties(Properties p) {
		setTwilioAccountSID(p.getProperty("tas"));
		setTwilioAuthToken(p.getProperty("tat"));
		setTwilioPhone(p.getProperty("tp"));
		setPhoneNotifications(p.getProperty("phoneNotifications"));
		setAppUrl(p.getProperty("appUrl"));
	}

	@Override
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	@Override
	public String getAppUrl() {
		return appUrl;
	}

	public String toString() {
		return Util.toJSON(this);
	}

	@Override
	public String getPhoneNotifications() {
		return phoneNotifications;
	}

	@Override
	public void setPhoneNotifications(String pn) {
		this.phoneNotifications = pn;
	}
}
