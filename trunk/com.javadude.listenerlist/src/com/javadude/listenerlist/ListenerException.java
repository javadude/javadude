/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist;

import java.util.Collections;
import java.util.List;

/**
 * <p>Captures a list of nested exceptions. This is used to collect exceptions that
 * be thrown while notifying multiple listeners. In "collect" mode, rather than
 * stop notification when the first listener throws an exception, we collect a
 * list of exceptions that the listeners threw.</p>
 *
 * <p>TODO Add causes to stack trace</p>
 */
public class ListenerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private List<Throwable> causes;

	/**
	 * Constructor.
	 * @param causes the list of causes we collected
	 */
	public ListenerException(List<Throwable> causes) {
		super("Error notifying listeners");
		this.causes = causes;
	}

	/**
	 * @return the list of causes that were collected
	 */
	public List<Throwable> getCauses() {
		return Collections.unmodifiableList(causes);
	}
}
