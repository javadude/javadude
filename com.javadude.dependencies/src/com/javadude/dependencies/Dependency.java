/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.dependencies;

import org.eclipse.jdt.core.IJavaProject;

public class Dependency {
	private IJavaProject source;
	private IJavaProject target;
	private boolean exported;

    public IJavaProject getSource() { return source; }
	public void setSource(IJavaProject source) { this.source = source; }
	public IJavaProject getTarget() { return target; }
	public void setTarget(IJavaProject target) { this.target = target; }
	public boolean isExported() { return exported; }
	public void setExported(boolean exported) { this.exported = exported; }
	public Dependency(IJavaProject source, IJavaProject target, boolean exported) {
    	setSource(source);
    	setTarget(target);
    	setExported(exported);
    }
}
