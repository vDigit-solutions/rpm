package com.vDigit.rpm.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.vDigit.rpm.dto.Job;

public class JobSortingTest {

	@Test
	public void sort() {
		long time = System.currentTimeMillis();
		List<Job> j = new ArrayList<>();
		Date date = new Date();
		date.setTime(time);
		Job job = new Job();
		job.setId("1");
		job.setCreatedDate(date);
		j.add(job);

		Date date1 = new Date();
		date1.setTime(time + 34000);
		Job job1 = new Job();
		job1.setId("2");
		job1.setCreatedDate(date1);
		j.add(job1);

		Date date2 = new Date();
		Job job2 = new Job();
		date2.setTime(time - 34000);
		job2.setId("3");
		job2.setCreatedDate(date2);
		j.add(job2);

		Collections.sort(j, new CreatedDateComparator());

		assertEquals("2", j.get(0).getId());
		assertEquals("1", j.get(1).getId());
		assertEquals("3", j.get(2).getId());
	}

}
