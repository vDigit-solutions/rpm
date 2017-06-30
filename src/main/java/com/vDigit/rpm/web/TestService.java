package com.vDigit.rpm.web;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vDigit.rpm.dto.Address;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("com.vDigit.rpm")
@EnableMongoRepositories(basePackages = { "com.vDigit.rpm" })
public class TestService {
	private static ObjectMapper objectMapper = new ObjectMapper();

	@RequestMapping("/test")
	@CrossOrigin(origins = "*")
	String test() {
		return "Hello world";
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestService.class, args);
		dumpJob();
	}

	private static void dumpJob() throws Exception {
		JobRequest request = new JobRequest();
		Job job = new Job();
		request.setPropertyManagerId("100");
		job.setDescription("Clean the apartment in Lynnwood");
		job.setDesiredDateOfBegin(new Date());
		job.setType("Cleaning");
		request.setJob(job);
		job.setPropertyManagerId("100");
		job.setAddress(makeAddress());
		System.out.println(objectMapper.writeValueAsString(request));
	}

	private static Address makeAddress() {
		Address a = new Address();
		a.setStreet1("605 5th Ave South");
		a.setStreet2("Suite 800");
		a.setCity("Seattle");
		a.setState("WA");
		a.setZip("98104");
		a.setCountry("USA");
		return a;
	}

}
