/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Manages a set of listeners on behalf of another class.</p>
 *
 *  TODO more detail and examples
 *
 * @param <ListenerInterface> The type of listeners we will track in this list. This must be an interface.
 */
public class ListenerList<ListenerInterface> {
	public enum Exceptions {
		/** Swallow all exceptions. All listeners will be notified and no exception will be thrown if any
		 *  listeners throw exceptions. Exceptions will be logged if a logger is passed in when creating the
		 *  listener list. */
		SWALLOW,
		/** Throw all exceptions. Exceptions thrown by any listener will immediately be thrown to the caller.
		 *  Note that any listeners that have not yet been notified when an exception is thrown will
		 *  not be notified.
		 */
		THROW,
		/** Collect all exceptions. If any listeners throw an exception when being notified, the exception will
		 *  be added to a list. All listeners will be notified, and zero or more may throw exceptions.
		 *  After all listeners have been notified, a ListenerException will be thrown that contains all of the
		 *  exceptions thrown.
		 */
		COLLECT
	}
	private Exceptions exceptionHandling;

	// options for ExceptionHandling.LOG
	private Logger logger;
	private Level logLevel;

	private List<ListenerInterface> listeners = new ArrayList<ListenerInterface>();
	private ListenerInterface dynamicProxy;
	private final Class<ListenerInterface> clazz;

	/**
	 * Create a listener list without wrapping thrown exceptions. This list can be created using any exception mode.
	 * @param <ListenerInterface> The listener interface that we will be tracking.
	 * @param clazz The listener interface type
	 * @param exceptionHandling The exception handling mode. Can be SWALLOW, THROW or COLLECT.
	 * @return a new listener list instance
	 * @see Exceptions
	 */
	public static <ListenerInterface> ListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling) {
		return new ListenerList<ListenerInterface>(clazz, exceptionHandling, null, null);
	}
	public static <ListenerInterface> ListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Logger logger) {
		return new ListenerList<ListenerInterface>(clazz, exceptionHandling, logger, Level.SEVERE);
	}
	public static <ListenerInterface> ListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Logger logger, Level logLevel) {
		return new ListenerList<ListenerInterface>(clazz, exceptionHandling, logger, logLevel);
	}
	protected ListenerList(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Logger logger, Level logLevel) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException("Listener type must be an interface");
		}
		this.clazz = clazz;
		this.exceptionHandling = exceptionHandling;
		this.logger = logger;
		this.logLevel = logLevel;
		@SuppressWarnings("unchecked")
		ListenerInterface proxy = (ListenerInterface) Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class<?>[] {clazz},
				new ListenerInvocationHandler());
		dynamicProxy = proxy;
	}
	public void add(ListenerInterface listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	public void remove(ListenerInterface listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	public ListenerInterface fire() {
		return dynamicProxy;
	}
	private class ListenerInvocationHandler implements InvocationHandler {
		@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			List<Throwable> exceptions = null;
			if (exceptionHandling == Exceptions.COLLECT) {
				exceptions = new ArrayList<Throwable>();
			}
			List<ListenerInterface> targets = null;
			synchronized (listeners) {
				targets = new ArrayList<ListenerInterface>(listeners);
			}
			try {
				for (ListenerInterface listener : targets) {
					ListenerList.this.invoke(listener, method, args, exceptions);
				}
				if (exceptions != null && !exceptions.isEmpty()) {
					throw new ListenerException(exceptions);
				}
			} catch (ThreadDeath t) {
				throw t;
			} catch (Throwable t) {
				if (exceptionHandling == Exceptions.THROW || exceptionHandling == Exceptions.COLLECT) {
					throw t;
				}
				// else swallow exception
			}
			return null;
		}
	}
	protected void invoke(ListenerInterface listener, Method method, Object[] args, List<Throwable> exceptions) throws Throwable {
		try {
			try {
				method.invoke(listener, args);
			} catch (InvocationTargetException t) {
				throw t.getCause();
			}
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			if (logger != null) {
				logger.log(logLevel, "Error invoking listener method " + method.getName(), t);  // TBD NLS
			}
			if (exceptions != null) {
				exceptions.add(t);
			}
			if (exceptionHandling != Exceptions.COLLECT && exceptionHandling != Exceptions.SWALLOW) {
				throw t;
			}
			// else swallow
		}
	}
	@Override public String toString() {
		return getClass().getName() + '[' + paramString() + ']';
	}
	public String paramString() {
		return "listenerType=" + clazz + ", listeners=" + listeners;
	}
}
