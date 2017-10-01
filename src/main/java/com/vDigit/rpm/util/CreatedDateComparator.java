package com.vDigit.rpm.util;

import java.util.Comparator;
import java.util.Date;

import com.vDigit.rpm.dto.Job;

public class CreatedDateComparator implements Comparator<Job> {

	@Override
	public int compare(Job o1, Job o2) {
		Date createdDate1 = o1.getCreatedDate();
		Date createdDate2 = o2.getCreatedDate();
		if (createdDate1 == null || createdDate2 == null) {
			return -1;
		}
		return createdDate2.compareTo(createdDate1);
	}

}
