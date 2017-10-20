package com.vDigit.rpm.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PropertyManagerConfig {
	private static Logger logger = LoggerFactory.getLogger(PropertyManagerConfig.class);

	@Bean
	public PropertyManager propertyManager() {
		PersonalPropertyManager pm = new PersonalPropertyManager();
		String propertiesFile = "environment/" + Util.getServerName() + ".properties";
		Properties p = new Properties();
		try {
			p.load(new ClassPathResource(propertiesFile).getInputStream());
		} catch (IOException e) {
			logger.error("Could not find property file -> " + propertiesFile
					+ " in resources folder. Hence using environment.default.properties. Please change it to use your machine specific property file");
			return new PropertyManagerImpl();
		}
		pm.setProperties(p);
		return pm;
	}

}
