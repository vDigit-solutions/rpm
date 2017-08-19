package com.vDigit.rpm.util;

import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vDigit.rpm.dto.NotificationContext;

@Component
public class TwilioPhoneNotification implements Notification<NotificationContext, String> {
	//public static final String ACCOUNT_SID = "ACe915c626bcb287e0d3811acda79d8298";
	//public static final String AUTH_TOKEN = "dfbe2db671fb8e7150f58e4cd130e6fa";
	//public static final String TWILIO_PHONE = "+12534557619";

	//Paid numbers
	public static final String ACCOUNT_SID = "AC8ffaef9921bf75d1b8dae345bf073b31";
	public static final String AUTH_TOKEN = "d4b90652fe91f2821751702fa0c90d7f";
	public static final String TWILIO_PHONE = "+12062027301";

	static {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	}

	private String convertToUSPhone(String phone) {
		if (phone.startsWith("+1"))
			return phone;
		return "+1" + phone;
	}

	@Override
	public String send(NotificationContext input) {
		Message m = Message.creator(new PhoneNumber(convertToUSPhone(input.getTo())), new PhoneNumber(TWILIO_PHONE),
				input.getMessage()).create();
		return m.getSid();
	}

}
