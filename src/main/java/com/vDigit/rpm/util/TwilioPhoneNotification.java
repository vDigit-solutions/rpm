package com.vDigit.rpm.util;

import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class TwilioPhoneNotification implements PhoneNotification {
	public static final String ACCOUNT_SID = "ACe915c626bcb287e0d3811acda79d8298";
	public static final String AUTH_TOKEN = "dfbe2db671fb8e7150f58e4cd130e6fa";
	public static final String TWILIO_PHONE = "+12534557619";

	static {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	}

	private String convertToUSPhone(String phone) {
		if (phone.startsWith("+1"))
			return phone;
		return "+1" + phone;
	}

	@Override
	public String sendMessage(String phoneNumber, String message) {
		Message m = Message
				.creator(new PhoneNumber(convertToUSPhone(phoneNumber)), new PhoneNumber(TWILIO_PHONE), message)
				.create();
		return m.getSid();

	}

	@Override
	public String receiveResponse(String phoneNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}
