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

public class ListenerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private List<Throwable> causes;

	public ListenerException(List<Throwable> causes) {
		super("Error notifying listeners");
		this.causes = causes;
	}

	public List<Throwable> getCauses() {
		return Collections.unmodifiableList(causes);
	}

	// TODO add causes to stack trace
}
