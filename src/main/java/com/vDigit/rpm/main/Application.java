package com.vDigit.rpm.main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vDigit.rpm.dto.Address;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.io.ReadDataFromServices;
import com.vDigit.rpm.io.ReadDataFromStream;

import bsh.Interpreter;

@RestController
@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
@ComponentScan({ "com.vDigit.rpm" })
@EnableMongoRepositories(basePackages = { "com.vDigit.rpm" })
public class Application {

	private static ObjectMapper objectMapper = new ObjectMapper();

	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
	@CrossOrigin(origins = "*")
	Job test() {
		return new Job();
	}

	@RequestMapping(value = "/runScript", method = RequestMethod.GET, produces = "text/plain")

	String runScript() throws Exception {
		//System.out.println("Run Script");
		for (String x : context.getBeanDefinitionNames()) {
			// System.out.println(x);
		}

		Object o = context.getBean("propertyManagers");
		Interpreter i = new Interpreter();
		i.set("context", context);
		String script = new ReadDataFromStream().readDataFromFile("/Users/dosapats/beanshell.txt");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos, true);
		i.setOut(ps);
		i.eval(script);
		ps.flush();
		return baos.toString();
	}

	@RequestMapping(value = "/images", method = RequestMethod.GET, produces = "application/json")
	@CrossOrigin(origins = "*")
	String vehicles(@RequestParam(required = false) String webId, @RequestParam(required = false) String limit,
			@RequestParam(required = false) String offset) {
		String url = "http://invservices.prod-las.cobaltgroup.com/inventory/rest/v1.0/vehicles/search?inventoryOwner={0}&responseFields=id,year,make,model,vin,assets&dealerPhotos=true&limit={1}&offset={2}";
		System.out.println(webId + " -> " + limit + " -> " + offset);
		if (webId == null) {
			webId = "gmps-abel";
		}
		if (limit == null) {
			limit = "10";
		}
		if (offset == null) {
			offset = "0";
		}
		url = MessageFormat.format(url, webId, limit, offset);
		System.out.println(new java.sql.Time(System.currentTimeMillis()) + " -> " + url);
		ReadDataFromServices rds = new ReadDataFromServices();
		return rds.readFromProduction(url);
	}

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext c = SpringApplication.run(Application.class, args);
		context = c;
		dumpJob();
	}

	private static ConfigurableApplicationContext context;

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
