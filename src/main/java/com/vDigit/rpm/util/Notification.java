package com.vDigit.rpm.util;

public interface Notification<I, O> {
	O send(I input);
}
