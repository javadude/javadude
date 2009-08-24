/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist.sample;

import java.util.Date;

import com.javadude.listenerlist.ListenerList;
import com.javadude.listenerlist.ListenerList.Exceptions;

public class WeatherStation {
	private ListenerList<SunListener> sunListeners = ListenerList.create(SunListener.class, Exceptions.THROW);
	public void addSunListener(SunListener listener)	{ sunListeners.add(listener); }
	public void removeSunListener(SunListener listener)	{ sunListeners.remove(listener); }

	public void announceSunRose() {
		sunListeners.fire().sunRose(new Date());
	}
	public void announceSunSet() {
		sunListeners.fire().sunSet(new Date());
	}
}
