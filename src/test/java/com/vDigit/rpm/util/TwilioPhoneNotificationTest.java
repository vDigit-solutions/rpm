package com.vDigit.rpm.util;

import org.junit.Test;

import com.vDigit.rpm.dto.NotificationContext;

public class TwilioPhoneNotificationTest {

	@Test
	public void test() {
		TwilioPhoneNotification p = new TwilioPhoneNotification();
		NotificationContext context = new NotificationContext();
		context.setTo("4252837905");
		context.setMessage("Trying after removing the number");
		p.send(context);
	}
}
