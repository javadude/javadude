/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.javadude.listenerlist.ListenerException;
import com.javadude.listenerlist.ListenerList.Exceptions;
@RunWith(Parameterized.class)
public class ListenerListTest {
	private Logger logger = Logger.getLogger(ListenerListTest.class.getName());
	private TestHandler handler = new TestHandler();

	private static class TestHandler extends Handler {
		private List<LogRecord> records = new ArrayList<LogRecord>();
		public List<LogRecord> getRecords() { return records; }
		@Override public void publish(LogRecord record) { records.add(record); }
		@Override public void flush() { }
		@Override public void close() throws SecurityException { }
	};
	@Parameters public static Collection<Object[]> data() {
    	Class<? extends Throwable> se = SomeException.class;
    	Class<? extends Throwable> le = ListenerException.class;
        return Arrays.asList(
        		new Object[][] {
        			// 	handling			throws:			l1		l2		l3		called:			l1		l2		l3		exception
        	/*00*/	{	Exceptions.SWALLOW,	new boolean[] {	},						new boolean[] {	},						null},
        	/*01*/	{	Exceptions.SWALLOW,	new boolean[] {	false },				new boolean[] {	true },					null},
        	/*02*/	{	Exceptions.SWALLOW,	new boolean[] {	true  },				new boolean[] {	true },					null},
        	/*03*/	{	Exceptions.SWALLOW,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	true,	true},	null},
        	/*04*/	{	Exceptions.SWALLOW,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	true},	null},
        	/*05*/	{	Exceptions.SWALLOW,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	null},
        	/*06*/	{	Exceptions.SWALLOW,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	true,	true},	null},
        	/*07*/	{	Exceptions.THROW,	new boolean[] {	},						new boolean[] {	},						null},
        	/*08*/	{	Exceptions.THROW,	new boolean[] {	false },				new boolean[] {	true },					null},
        	/*09*/	{	Exceptions.THROW,	new boolean[] {	true  },				new boolean[] {	true },					se},
        	/*10*/	{	Exceptions.THROW,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	false,	false},	se},
        	/*11*/	{	Exceptions.THROW,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	false},	se},
        	/*12*/	{	Exceptions.THROW,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	se},
        	/*13*/	{	Exceptions.THROW,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	false,	false},	se},
        	/*14*/	{	Exceptions.COLLECT,	new boolean[] {	},						new boolean[] {	},						null},
        	/*15*/	{	Exceptions.COLLECT,	new boolean[] {	false },				new boolean[] {	true },					null},
        	/*16*/	{	Exceptions.COLLECT,	new boolean[] {	true  },				new boolean[] {	true },					le},
        	/*17*/	{	Exceptions.COLLECT,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	true,	true},	le},
        	/*18*/	{	Exceptions.COLLECT,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	true},	le},
        	/*19*/	{	Exceptions.COLLECT,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	le},
        	/*20*/	{	Exceptions.COLLECT,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	true,	true},	le},
        		});
    }
    private Exceptions handling;
    private boolean[] listenerThrows;
    private boolean[] listenerCalled;
    private Class<? extends Throwable> expectedException;

	public ListenerListTest(Exceptions handling,
			boolean[] listenerThrows, boolean[] listenerCalled,
			Class<? extends Throwable> expectedException) {
		super();
		this.handling = handling;
		this.listenerThrows = listenerThrows;
		this.listenerCalled = listenerCalled;
		this.expectedException = expectedException;

		logger.addHandler(handler);
	}

	private interface CallCheck {
		boolean isSunRoseCalled();
	}
	private static class ListenerNoThrow implements SunListener, CallCheck {
		private boolean sunRoseCalled = false;
		public boolean isSunRoseCalled() { return sunRoseCalled; }
		@Override public void sunRose(Date date) { sunRoseCalled = true; }
	}
	private static class ListenerThrows implements SunListener, CallCheck {
		private boolean sunRoseCalled = false;
		public boolean isSunRoseCalled() { return sunRoseCalled; }
		@Override public void sunRose(Date date) { sunRoseCalled = true; throw new SomeException(); }
	}

	@Test public void testNoLog() throws Throwable {
		testBase(new Observable(handling));
	}
	@Test public void testLog() throws Throwable {
		testBase(new Observable(handling, logger));
		checkLog(Level.SEVERE);
	}
	private void checkLog(Level level) {
		int n = 0;
		for (boolean b : listenerThrows) {
			if (b) {
				LogRecord logRecord = handler.getRecords().get(n++);
				Assert.assertEquals("Error invoking listener method sunRose", logRecord.getMessage());
				Assert.assertEquals(level, logRecord.getLevel());
				Assert.assertEquals(SomeException.class, logRecord.getThrown().getClass());
				if (handling == Exceptions.THROW) {
					// only one exception will be reported
					break;
				}
			}
		}
		if (n != handler.getRecords().size()) {
			System.out.println("eeeeek");
		}
		Assert.assertEquals(n, handler.getRecords().size());
	}
	private void testLog(Level level) throws Throwable {
		testBase(new Observable(handling, logger, level));
		checkLog(level);
	}
	@Test public void testLogSevere() throws Throwable {
		testLog(Level.SEVERE);
	}
	@Test public void testLogWarn() throws Throwable {
		testLog(Level.WARNING);
	}
	@Test public void testLogInfo() throws Throwable {
		testLog(Level.INFO);
	}
	public void testBase(Observable observable) throws Throwable {
		SunListener[] listeners = new SunListener[listenerThrows.length];
		for (int i = 0; i < listenerThrows.length; i++) {
			if (listenerThrows[i]) {
				listeners[i] = new ListenerThrows();
			} else {
				listeners[i] = new ListenerNoThrow();
			}
			observable.addSunListener(listeners[i]);
		}
		try {
			observable.fireSunRose();
			if (expectedException != null) {
				Assert.fail("expected " + expectedException.getName());
			}
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			if (expectedException == null) {
				throw t;
			}
			Assert.assertEquals(expectedException, t.getClass());
		}
		for (int i = 0; i < listenerThrows.length; i++) {
			Assert.assertEquals(listenerCalled[i], ((CallCheck) listeners[i]).isSunRoseCalled());
		}
	}
}
