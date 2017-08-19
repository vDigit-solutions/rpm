package com.vDigit.rpm.service;

import com.vDigit.rpm.dto.Job;

public interface JobNotifier {
	static final String UNSUBSCRIBE = "%s/api/contractor/unsubscribe/%s";

	void processJob(Job job);
}
