/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListenerList<ListenerInterface> {
	public enum Exceptions {
		SWALLOW,
		THROW,
		COLLECT
	}
	private Exceptions exceptionHandling;

	private Constructor<? extends Throwable> wrapperConstructorNoMessage;
	private Constructor<? extends Throwable> wrapperConstructorWithMessage;

	// options for ExceptionHandling.LOG
	private Logger logger;
	private Level logLevel;

	private List<ListenerInterface> listeners = new ArrayList<ListenerInterface>();
	private ListenerInterface dynamicProxy;
	private final Class<ListenerInterface> clazz;

	public static <ListenerInterface> ListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling) {
		return new ListenerList<ListenerInterface>(clazz, exceptionHandling, null, null, null);
	}
	public static <ListenerInterface> ListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Class<? extends Throwable> wrapperClass) {
		return new ListenerList<ListenerInterface>(clazz, exceptionHandling, wrapperClass, null, null);
	}
	public static <ListenerInterface> ListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Logger logger) {
		return new ListenerList<ListenerInterface>(clazz, exceptionHandling, null, logger, Level.SEVERE);
	}
	public static <ListenerInterface> ListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Logger logger, Level logLevel) {
		return new ListenerList<ListenerInterface>(clazz, exceptionHandling, null, logger, logLevel);
	}
	public static <ListenerInterface> ListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Class<? extends Throwable> wrapperClass, Logger logger) {
		return new ListenerList<ListenerInterface>(clazz, exceptionHandling, wrapperClass, logger, Level.SEVERE);
	}
	public static <ListenerInterface> ListenerList<ListenerInterface> create(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Class<? extends Throwable> wrapperClass, Logger logger, Level logLevel) {
		return new ListenerList<ListenerInterface>(clazz, exceptionHandling, wrapperClass, logger, logLevel);
	}
	protected ListenerList(Class<ListenerInterface> clazz, Exceptions exceptionHandling, Class<? extends Throwable> wrapperClass, Logger logger, Level logLevel) {
		if (wrapperClass != null && exceptionHandling != Exceptions.THROW && exceptionHandling != Exceptions.COLLECT) {
			throw new IllegalArgumentException("Exception handling must be THROW or COLLECT to wrap exceptions");
		}
		this.clazz = clazz;
		this.exceptionHandling = exceptionHandling;
		this.logger = logger;
		this.logLevel = logLevel;
		if (wrapperClass != null) {
			if (!RuntimeException.class.isAssignableFrom(wrapperClass)) {
				throw new IllegalArgumentException("Wrapper class must extend RuntimeException (or a subclass of RuntimeException)");
			}
			try {
				this.wrapperConstructorWithMessage = wrapperClass.getConstructor(String.class, Throwable.class);
			} catch (ThreadDeath t) {
				throw t;
			} catch (SecurityException e) {
				throw new IllegalArgumentException("Wrapper class contains a public constructor that takes (String,Throwable) as an argument but it cannot be accessed", e);
			} catch (NoSuchMethodException e) {
				try {
					this.wrapperConstructorNoMessage = wrapperClass.getConstructor(Throwable.class);
				} catch (ThreadDeath t) {
					throw t;
				} catch (SecurityException t) {
					throw new IllegalArgumentException("Wrapper class contains a public constructor that takes (Throwable) as an argument but it cannot be accessed", t);
				} catch (Throwable t) {
					throw new IllegalArgumentException("Wrapper class must contain a public constructor that takes (Throwable) or (String, Throwable) as an argument", t);
				}
			}
		}
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
				if (wrapperConstructorWithMessage != null) {
					throw wrapperConstructorWithMessage.newInstance(t.getClass().getName() + ":" + t.getMessage(), t);
				} else if (wrapperConstructorNoMessage != null) {
					throw wrapperConstructorNoMessage.newInstance(t);
				} else if (exceptionHandling == Exceptions.THROW || exceptionHandling == Exceptions.COLLECT) {
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
