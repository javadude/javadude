/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.javadude.listenerlist.ListenerList;
import com.javadude.listenerlist.ListenerList.Exceptions;

public class TestObservable {
	private ListenerList<TestListener> listeners;
	public TestObservable(Exceptions exceptionHandling) {
		listeners = ListenerList.create(TestListener.class, exceptionHandling);
	}
	public TestObservable(Exceptions exceptionHandling, Logger logger, Level level) {
		listeners = ListenerList.create(TestListener.class, exceptionHandling, logger, level);
	}
	public TestObservable(Exceptions exceptionHandling, Logger logger) {
		listeners = ListenerList.create(TestListener.class, exceptionHandling, logger);
	}
	public void addTestListener(TestListener listener) {
		listeners.add(listener);
	}
	public void removeTestListener(TestListener listener) {
		listeners.remove(listener);
	}
	public void fireNotify() throws TestException {
		listeners.fire().notify(new Object());
	}
}
