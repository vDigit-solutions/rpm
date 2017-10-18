package com.vDigit.rpm.service.captcha;

import com.vDigit.rpm.web.error.ReCaptchaInvalidException;

public interface ICaptchaService {
	void processResponse(final String response) throws ReCaptchaInvalidException;

	String getReCaptchaSite();

	String getReCaptchaSecret();
}
