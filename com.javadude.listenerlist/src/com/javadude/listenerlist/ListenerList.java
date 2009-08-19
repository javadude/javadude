/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ListenerList<ListenerInterface> {
	// TODO implement options
//	private boolean throwException; // should invoked notification throw exception to caller?
//	private boolean wrapException; // should invoked methods that throw exceptions be wrapped?
//	private boolean exceptionHaltsFurtherNotifications;
//	private boolean swallowException;
//	private boolean logException;
//	private boolean notifyInThreads;
//	private boolean threadTimeout;
	private List<ListenerInterface> listeners = new ArrayList<ListenerInterface>();
	private ListenerInterface dynamicProxy;
	private final Class<ListenerInterface> clazz;

	public static <ListenerInterface> ListenerList<ListenerInterface> create(Class<ListenerInterface> clazz) {
		return new ListenerList<ListenerInterface>(clazz);
	}
	private ListenerList(Class<ListenerInterface> clazz) {
		this.clazz = clazz;
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
	@SuppressWarnings("unchecked")
	public ListenerInterface fire() {
		if (dynamicProxy == null) {
			dynamicProxy = (ListenerInterface)
				Proxy.newProxyInstance(clazz.getClassLoader(),
										new Class<?>[] {clazz},
										new ListenerInvocationHandler());
		}
		return dynamicProxy;
	}
	private class ListenerInvocationHandler implements InvocationHandler {
		@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getClass().equals(ListenerList.class)) {
				return method.invoke(this, args);
			}
			List<ListenerInterface> targets = null;
			synchronized (listeners) {
				targets = new ArrayList<ListenerInterface>(listeners);
			}
			for (ListenerInterface listener : targets) {
				method.invoke(listener, args);
			}
			return null;
		}
	}
}
