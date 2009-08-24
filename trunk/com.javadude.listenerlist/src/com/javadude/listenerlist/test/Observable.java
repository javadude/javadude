/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist.test;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.javadude.listenerlist.ListenerList;
import com.javadude.listenerlist.ListenerList.Exceptions;

public class Observable {
	private ListenerList<SunListener> listeners;
	public Observable(Exceptions exceptionHandling) {
		listeners = ListenerList.create(SunListener.class, exceptionHandling);
	}
	public Observable(Exceptions exceptionHandling, Class<? extends Throwable> wrapper) {
		listeners = ListenerList.create(SunListener.class, exceptionHandling, wrapper);
	}
	public Observable(Exceptions exceptionHandling, Logger logger, Level level) {
		listeners = ListenerList.create(SunListener.class, exceptionHandling, logger, level);
	}
	public Observable(Exceptions exceptionHandling, Class<? extends Throwable> wrapper, Logger logger, Level level) {
		listeners = ListenerList.create(SunListener.class, exceptionHandling, wrapper, logger, level);
	}
	public Observable(Exceptions exceptionHandling, Logger logger) {
		listeners = ListenerList.create(SunListener.class, exceptionHandling, logger);
	}
	public Observable(Exceptions exceptionHandling, Class<? extends Throwable> wrapper, Logger logger) {
		listeners = ListenerList.create(SunListener.class, exceptionHandling, wrapper, logger);
	}
	public void addSunListener(SunListener listener) {
		listeners.add(listener);
	}
	public void removeSunListener(SunListener listener) {
		listeners.remove(listener);
	}
	public void fireSunRose() throws SomeException {
		listeners.fire().sunRose(new Date());
	}
}
