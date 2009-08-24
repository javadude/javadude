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
    	Class<? extends Throwable> w1 = ExceptionWrapper1.class;
    	Class<? extends Throwable> w2 = ExceptionWrapper2.class;
    	Class<? extends Throwable> w3 = ExceptionWrapper3.class;
    	Class<? extends Throwable> le = ListenerException.class;
    	String err1 = "error";
    	String err2 = SomeException.class.getName() + ":error";
    	String err3 = "Error notifying listeners";
    	String err4 = ListenerException.class.getName() + ":Error notifying listeners";
        return Arrays.asList(
        		new Object[][] {
        			// 	wrapper	handling			throws:			l1		l2		l3		called:			l1		l2		l3		exception	message
        	/*00*/	{	null, 	Exceptions.SWALLOW,	new boolean[] {	},						new boolean[] {	},						null, 		null},
        	/*01*/	{	null, 	Exceptions.SWALLOW,	new boolean[] {	false },				new boolean[] {	true },					null, 		null},
        	/*02*/	{	null, 	Exceptions.SWALLOW,	new boolean[] {	true  },				new boolean[] {	true },					null, 		null},
        	/*03*/	{	null, 	Exceptions.SWALLOW,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	true,	true},	null, 		null},
        	/*04*/	{	null, 	Exceptions.SWALLOW,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	true},	null, 		null},
        	/*05*/	{	null, 	Exceptions.SWALLOW,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	null, 		null},
        	/*06*/	{	null, 	Exceptions.SWALLOW,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	true,	true},	null, 		null},
        	/*07*/	{	null, 	Exceptions.THROW,	new boolean[] {	},						new boolean[] {	},						null, 		null},
        	/*08*/	{	null, 	Exceptions.THROW,	new boolean[] {	false },				new boolean[] {	true },					null, 		null},
        	/*09*/	{	null, 	Exceptions.THROW,	new boolean[] {	true  },				new boolean[] {	true },					se, 		err1},
        	/*10*/	{	null, 	Exceptions.THROW,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	false,	false},	se, 		err1},
        	/*11*/	{	null, 	Exceptions.THROW,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	false},	se, 		err1},
        	/*12*/	{	null, 	Exceptions.THROW,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	se, 		err1},
        	/*13*/	{	null, 	Exceptions.THROW,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	false,	false},	se, 		err1},
        	/*14*/	{	w1, 	Exceptions.THROW,	new boolean[] {	},						new boolean[] {	},						null, 		null},
        	/*15*/	{	w1, 	Exceptions.THROW,	new boolean[] {	false },				new boolean[] {	true },					null, 		null},
        	/*16*/	{	w1, 	Exceptions.THROW,	new boolean[] {	true  },				new boolean[] {	true },					w1,			null},
        	/*17*/	{	w1, 	Exceptions.THROW,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	false,	false},	w1, 		null},
        	/*18*/	{	w1, 	Exceptions.THROW,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	false},	w1, 		null},
        	/*19*/	{	w1, 	Exceptions.THROW,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	w1, 		null},
        	/*20*/	{	w1, 	Exceptions.THROW,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	false,	false},	w1, 		null},
        	/*21*/	{	w2, 	Exceptions.THROW,	new boolean[] {	},						new boolean[] {	},						null, 		null},
        	/*22*/	{	w2, 	Exceptions.THROW,	new boolean[] {	false },				new boolean[] {	true },					null, 		null},
        	/*23*/	{	w2, 	Exceptions.THROW,	new boolean[] {	true  },				new boolean[] {	true },					w2,			err2},
        	/*24*/	{	w2, 	Exceptions.THROW,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	false,	false},	w2, 		err2},
        	/*25*/	{	w2, 	Exceptions.THROW,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	false},	w2, 		err2},
        	/*26*/	{	w2, 	Exceptions.THROW,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	w2, 		err2},
        	/*27*/	{	w2, 	Exceptions.THROW,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	false,	false},	w2, 		err2},
        	/*28*/	{	w3, 	Exceptions.THROW,	new boolean[] {	},						new boolean[] {	},						null, 		null},
        	/*29*/	{	w3, 	Exceptions.THROW,	new boolean[] {	false },				new boolean[] {	true },					null, 		null},
        	/*30*/	{	w3, 	Exceptions.THROW,	new boolean[] {	true  },				new boolean[] {	true },					w3,			err2},
        	/*31*/	{	w3, 	Exceptions.THROW,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	false,	false},	w3, 		err2},
        	/*32*/	{	w3, 	Exceptions.THROW,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	false},	w3, 		err2},
        	/*33*/	{	w3, 	Exceptions.THROW,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	w3, 		err2},
        	/*34*/	{	w3, 	Exceptions.THROW,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	false,	false},	w3, 		err2},
        	/*35*/	{	null, 	Exceptions.COLLECT,	new boolean[] {	},						new boolean[] {	},						null, 		null},
        	/*36*/	{	null, 	Exceptions.COLLECT,	new boolean[] {	false },				new boolean[] {	true },					null, 		null},
        	/*37*/	{	null, 	Exceptions.COLLECT,	new boolean[] {	true  },				new boolean[] {	true },					le, 		err3},
        	/*38*/	{	null, 	Exceptions.COLLECT,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	true,	true},	le, 		err3},
        	/*39*/	{	null, 	Exceptions.COLLECT,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	true},	le, 		err3},
        	/*40*/	{	null, 	Exceptions.COLLECT,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	le, 		err3},
        	/*41*/	{	null, 	Exceptions.COLLECT,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	true,	true},	le, 		err3},
        	/*42*/	{	w1, 	Exceptions.COLLECT,	new boolean[] {	},						new boolean[] {	},						null, 		null},
        	/*43*/	{	w1, 	Exceptions.COLLECT,	new boolean[] {	false },				new boolean[] {	true },					null, 		null},
        	/*44*/	{	w1, 	Exceptions.COLLECT,	new boolean[] {	true  },				new boolean[] {	true },					w1,			null},
        	/*45*/	{	w1, 	Exceptions.COLLECT,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	true,	true},	w1, 		null},
        	/*46*/	{	w1, 	Exceptions.COLLECT,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	true},	w1, 		null},
        	/*47*/	{	w1, 	Exceptions.COLLECT,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	w1, 		null},
        	/*48*/	{	w1, 	Exceptions.COLLECT,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	true,	true},	w1, 		null},
        	/*49*/	{	w2, 	Exceptions.COLLECT,	new boolean[] {	},						new boolean[] {	},						null, 		null},
        	/*50*/	{	w2, 	Exceptions.COLLECT,	new boolean[] {	false },				new boolean[] {	true },					null, 		null},
        	/*51*/	{	w2, 	Exceptions.COLLECT,	new boolean[] {	true  },				new boolean[] {	true },					w2,			err4},
        	/*52*/	{	w2, 	Exceptions.COLLECT,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	true,	true},	w2, 		err4},
        	/*53*/	{	w2, 	Exceptions.COLLECT,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	true},	w2, 		err4},
        	/*54*/	{	w2, 	Exceptions.COLLECT,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	w2, 		err4},
        	/*55*/	{	w2, 	Exceptions.COLLECT,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	true,	true},	w2, 		err4},
        	/*56*/	{	w3, 	Exceptions.COLLECT,	new boolean[] {	},						new boolean[] {	},						null, 		null},
        	/*57*/	{	w3, 	Exceptions.COLLECT,	new boolean[] {	false },				new boolean[] {	true },					null, 		null},
        	/*58*/	{	w3, 	Exceptions.COLLECT,	new boolean[] {	true  },				new boolean[] {	true },					w3,			err4},
        	/*59*/	{	w3, 	Exceptions.COLLECT,	new boolean[] {	true,	false,	false},	new boolean[] {	true,	true,	true},	w3, 		err4},
        	/*60*/	{	w3, 	Exceptions.COLLECT,	new boolean[] {	false,	true,	false},	new boolean[] {	true,	true,	true},	w3, 		err4},
        	/*61*/	{	w3, 	Exceptions.COLLECT,	new boolean[] {	false,	false,	true},	new boolean[] {	true,	true,	true},	w3, 		err4},
        	/*62*/	{	w3, 	Exceptions.COLLECT,	new boolean[] {	true,	true,	true},	new boolean[] {	true,	true,	true},	w3, 		err4},
        		});
    }
    private Class<? extends Throwable> exceptionWrapper;
    private Exceptions handling;
    private boolean[] listenerThrows;
    private boolean[] listenerCalled;
    private Class<? extends Throwable> expectedException;
    private String message;

	public ListenerListTest(
			Class<? extends Throwable> exceptionWrapper, Exceptions handling,
			boolean[] listenerThrows, boolean[] listenerCalled,
			Class<? extends Throwable> expectedException, String message) {
		super();
		this.exceptionWrapper = exceptionWrapper;
		this.handling = handling;
		this.listenerThrows = listenerThrows;
		this.listenerCalled = listenerCalled;
		this.expectedException = expectedException;
		this.message = message;

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
		Observable observable;
		if (exceptionWrapper != null) {
			observable = new Observable(handling, exceptionWrapper);
		} else {
			observable = new Observable(handling);
		}
		testBase(observable);
	}
	@Test public void testLog() throws Throwable {
		Observable observable;
		if (exceptionWrapper != null) {
			observable = new Observable(handling, exceptionWrapper, logger);
		} else {
			observable = new Observable(handling, logger);
		}
		testBase(observable);
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
		Observable observable;
		if (exceptionWrapper != null) {
			observable = new Observable(handling, exceptionWrapper, logger, level);
		} else {
			observable = new Observable(handling, logger, level);
		}
		testBase(observable);
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
			if (message != null) {
				Assert.assertEquals(message, t.getMessage());
			}
			Assert.assertEquals(expectedException, t.getClass());
		}
		for (int i = 0; i < listenerThrows.length; i++) {
			Assert.assertEquals(listenerCalled[i], ((CallCheck) listeners[i]).isSunRoseCalled());
		}
	}
}
