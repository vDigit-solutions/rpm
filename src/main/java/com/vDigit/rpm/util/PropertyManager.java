package com.vDigit.rpm.util;

public interface PropertyManager {
	String getTwilioAccountSID();

	void setTwilioAccountSID(String accountSID);

	String getTwilioPhone();

	void setTwilioPhone(String tp);

	String getTwilioAuthToken();

	void setTwilioAuthToken(String token);
}