/*******************************************************************************
 * Copyright (c) 2010 Scott Stanchfield.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Scott Stanchfield - Work-in-progress - ANTLR 3.x AST Visualization
 *******************************************************************************/
package com.javadude.antlr3.visualizer;

import org.eclipse.ui.IStartup;

public class DummyStartup implements IStartup {
	@Override public void earlyStartup() {
		// do nothing - just want early startup
	}
}
