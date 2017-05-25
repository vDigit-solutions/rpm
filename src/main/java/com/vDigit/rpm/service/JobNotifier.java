package com.vDigit.rpm.service;

import com.vDigit.rpm.dto.Job;

public interface JobNotifier {
	public void processJob(Job job);
}
