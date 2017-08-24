package com.vDigit.rpm.util;

import org.junit.Test;

public class PropertyManagerFactoryTest {

	@Test
	public void testPropertyManager() {
		PropertyManager pm = PropertyManagerFactory.getPropertyManager();
		System.out.println(pm.toString());
	}
}
