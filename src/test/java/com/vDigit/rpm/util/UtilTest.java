package com.vDigit.rpm.util;

import org.junit.Test;

import bsh.Interpreter;

public class UtilTest {
	@Test
	public void testServerName() {
		System.out.println(Util.getServerName());
	}

	@Test
	public void test() throws Exception {
		Interpreter i = new Interpreter();
		System.out.println(i.eval(" new java.util.Date()"));
	}
}
