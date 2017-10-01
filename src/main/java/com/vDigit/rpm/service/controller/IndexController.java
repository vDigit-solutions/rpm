package com.vDigit.rpm.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	@RequestMapping("/")
	public String jobRequest() {
		return "jobRequest";
	}

	@RequestMapping("/angular")
	public String index() {
		return "quickstart/src/index";
	}
	
	@RequestMapping("/vehicleImages")	
	public String images(){
		return "images";
	}
}
