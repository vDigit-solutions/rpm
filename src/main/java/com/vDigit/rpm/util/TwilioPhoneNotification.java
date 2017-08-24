package com.vDigit.rpm.util;

import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vDigit.rpm.dto.NotificationContext;

@Component
public class TwilioPhoneNotification implements Notification<NotificationContext, String> {

	// public static final String ACCOUNT_SID =
	// "AC41706b179954da498f88f7b09ae75e1c";
	// public static final String AUTH_TOKEN =
	// "84315fbfbfffb90d6b22a54a4250d545";
	// public static final String TWILIO_PHONE = "+12062045272";

	// RPM.CVA
	public static final String ACCOUNT_SID = "ACd7fe4c566d7c57cf8fe662d77fe58b20";
	public static final String AUTH_TOKEN = "f95f2d853df8a0b84ef9248d6e5fe3d6";
	public static final String TWILIO_PHONE = "+13608136499";

	// Paid numbers
	// public static final String ACCOUNT_SID =
	// "AC8ffaef9921bf75d1b8dae345bf073b31";
	// public static final String AUTH_TOKEN =
	// "d4b90652fe91f2821751702fa0c90d7f";
	// public static final String TWILIO_PHONE = "+12062027301";

	private static PropertyManager pm = PropertyManagerFactory.getPropertyManager();

	static {
		// Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		Twilio.init(pm.getTwilioAccountSID(), pm.getTwilioAuthToken());
	}

	private String convertToUSPhone(String phone) {
		if (phone.startsWith("+1"))
			return phone;
		return "+1" + phone;
	}

	@Override
	public String send(NotificationContext input) {
		// String tp = TWILIO_PHONE;
		String tp = pm.getTwilioPhone();
		Message m = Message
				.creator(new PhoneNumber(convertToUSPhone(input.getTo())), new PhoneNumber(tp), input.getMessage())
				.create();
		return m.getSid();
	}

}
