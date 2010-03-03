/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets.internal;
import org.eclipse.ui.IStartup;

/**
 * A dummy startup class to allow us to start when eclipse starts.
 * @author Scott Stanchfield
 */
public class DummyStartup implements IStartup {
	public void earlyStartup() {
		// do nothing
	}
}
