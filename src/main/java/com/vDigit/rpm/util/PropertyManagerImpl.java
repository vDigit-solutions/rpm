package com.vDigit.rpm.util;

import java.util.Properties;

public class PropertyManagerImpl implements PropertyManager {
	private String twilioAccountSID;
	private String twilioPhone;
	private String twilioAuthToken;
	private String phoneNotifications="true";

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

	public PropertyManagerImpl() {

	}

	public void setProperties(Properties p) {
		setTwilioAccountSID(p.getProperty("tas"));
		setTwilioAuthToken(p.getProperty("tat"));
		setTwilioPhone(p.getProperty("tp"));
		setPhoneNotifications(p.getProperty("phoneNotifications"));
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