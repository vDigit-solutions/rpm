package com.vDigit.rpm.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class PropertyManagerFactory {
	private static Logger logger = LoggerFactory.getLogger(PropertyManagerFactory.class);
	private static PropertyManager pm = makePropertyManager();

	private static PropertyManager makePropertyManager() {
		PropertyManagerImpl pm = new PropertyManagerImpl();
		String propertiesFile = "environment/" + Util.getServerName() + ".properties";
		Properties p = new Properties();
		try {
			p.load(new ClassPathResource(propertiesFile).getInputStream());
		} catch (IOException e) {
			logger.error("Could not find property file -> " + propertiesFile
					+ " in resources folder. Hence using environment.default.properties. Please change it to use your machine specific property file");
			try {
				p.load(new ClassPathResource("environment/default.properties").getInputStream());
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}

		}
		pm.setProperties(p);
		return pm;
	}

	public static PropertyManager getPropertyManager() {
		return pm;
	}

}
