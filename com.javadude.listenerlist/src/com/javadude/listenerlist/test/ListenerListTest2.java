package com.javadude.listenerlist.test;

import junit.framework.Assert;

import org.junit.Test;

import com.javadude.listenerlist.ListenerList.Exceptions;
public class ListenerListTest2 {
	public static class BadException1 extends Exception {
		private static final long serialVersionUID = 1L;
		public BadException1() { super(); }
		public BadException1(String message, Throwable cause) { super(message, cause); }
		public BadException1(String message) { super(message); }
		public BadException1(Throwable cause) { super(cause); }
	}
	// test using a wrapper that doesn't extend RuntimException
	@Test public void badWrapper1() throws Throwable {
		try {
			new Observable(Exceptions.THROW, BadException1.class);
			Assert.fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Wrapper class must extend RuntimeException (or a subclass of RuntimeException)", e.getMessage());
		}
	}

	// test using a wrapper that doesn't have (Throwable) or (String,Throwable) constructor
	public static class BadException2 extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public BadException2() { super(); }
		public BadException2(String message) { super(message); }
	}
	@Test public void badWrapper2() throws Throwable {
		try {
			new Observable(Exceptions.THROW, BadException2.class);
			Assert.fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Wrapper class must contain a public constructor that takes (Throwable) or (String, Throwable) as an argument", e.getMessage());
		}
	}

	// test using a wrapper that doesn't expose (Throwable) or (String,Throwable) constructor
	public static class BadException3 extends RuntimeException {
		private static final long serialVersionUID = 1L;
		private BadException3(Throwable t) { super(t); }
	}
	@Test public void badWrapper3() throws Throwable {
		try {
			new Observable(Exceptions.THROW, BadException3.class);
			Assert.fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Wrapper class must contain a public constructor that takes (Throwable) or (String, Throwable) as an argument", e.getMessage());
		}
	}

	// test using a wrapper that doesn't expose (Throwable) or (String,Throwable) constructor
	public static class BadException4 extends RuntimeException {
		private static final long serialVersionUID = 1L;
		private BadException4(String message, Throwable t) { super(message, t); }
	}
	@Test public void badWrapper4() throws Throwable {
		try {
			new Observable(Exceptions.THROW, BadException4.class);
			Assert.fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Wrapper class must contain a public constructor that takes (Throwable) or (String, Throwable) as an argument", e.getMessage());
		}
	}
}
