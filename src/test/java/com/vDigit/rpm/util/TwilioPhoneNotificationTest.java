package com.vDigit.rpm.util;

import org.junit.Ignore;

import com.vDigit.rpm.dto.NotificationContext;

public class TwilioPhoneNotificationTest {
	@Ignore
	public void test() {
		TwilioPhoneNotification tn = new TwilioPhoneNotification();
		NotificationContext nc = new NotificationContext();
		nc.setMessage("Hello");
		nc.setTo("4252837905");
		tn.send(nc);
	}
}
