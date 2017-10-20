package com.vDigit.rpm.util;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vDigit.rpm.dto.NotificationContext;

@Component
public class TwilioPhoneNotification implements Notification<NotificationContext, String> {

	@Resource(name = "propertyManager")
	private com.vDigit.rpm.util.PropertyManager propertyManager;

	@PostConstruct
	public void init() {
		Twilio.init(propertyManager.getTwilioAccountSID(), propertyManager.getTwilioAuthToken());
	}

	private String convertToUSPhone(String phone) {
		if (phone.startsWith("+1"))
			return phone;
		return "+1" + phone;
	}

	@Override
	public String send(NotificationContext input) {
		// String tp = TWILIO_PHONE;
		String tp = propertyManager.getTwilioPhone();
		Message m = Message
				.creator(new PhoneNumber(convertToUSPhone(input.getTo())), new PhoneNumber(tp), input.getMessage())
				.create();
		return m.getSid();
	}

}
