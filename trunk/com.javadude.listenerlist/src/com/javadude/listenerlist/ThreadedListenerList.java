/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadedListenerList<ListenerInterface> extends ListenerList<ListenerInterface> {
	private Executor executor;

	private class Runner implements Runnable {
		private ListenerInterface listener;
		private Method method;
		private Object[] args;

		public Runner(ListenerInterface listener, Method method, Object[] args) {
			this.listener = listener;
			this.method = method;
			this.args = args;
		}

		@Override public void run() {
			try {
				ThreadedListenerList.super.invoke(listener, method, args, null);
			} catch (ThreadDeath t) {
				throw t;
			} catch (Throwable t) {
				throw new AssertionError("Cannot happen - if threaded invoker is specified exceptions must be swallowed, logged, or collected");
			}
		}
	}

	public static <ListenerInterface> ThreadedListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling) {
		return new ThreadedListenerList<ListenerInterface>(clazz, exceptionHandling, null, null);
	}
	public static <ListenerInterface> ThreadedListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Logger logger) {
		return new ThreadedListenerList<ListenerInterface>(clazz, exceptionHandling, logger, Level.SEVERE);
	}
	public static <ListenerInterface> ThreadedListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Logger logger, Level logLevel) {
		return new ThreadedListenerList<ListenerInterface>(clazz, exceptionHandling, logger, logLevel);
	}
	private ThreadedListenerList(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Logger logger, Level logLevel) {
		super(clazz, exceptionHandling, logger, logLevel);
		if (exceptionHandling == Exceptions.THROW) {
			throw new IllegalArgumentException("Cannot use Exceptions.THROW for exception handling in ThreadedListenerList");
		}
	}
	@Override protected void invoke(ListenerInterface listener, Method method, Object[] args, List<Throwable> exceptions) throws Throwable {
		// exceptions should be null; there's no caller to return the exceptions to
		executor.execute(new Runner(listener, method, args));
	}
	@Override public String paramString() {
		return "executor=" + executor;
	}
}
