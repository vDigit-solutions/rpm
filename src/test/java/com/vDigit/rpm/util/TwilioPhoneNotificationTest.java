package com.vDigit.rpm.util;

import org.junit.Ignore;
import org.junit.Test;

public class TwilioPhoneNotificationTest {
	@Test
	@Ignore
	public void test() {
		TwilioPhoneNotification t = new TwilioPhoneNotification();
		String x = t.sendMessage("4252837905",
				"Hi! This is siva. I'm sending a text message from our POC. Just respond yes or no - Click the link if you are interested in accepting the job - http://a4f74ebe.ngrok.io/api/pm/job/100");
		System.out.println(x);
	}
}
