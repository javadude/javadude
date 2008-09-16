/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield, based on ANTLR-Eclipse plugin
 *   by Torsten Juergeleit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors
 *    Torsten Juergeleit - original ANTLR Eclipse plugin
 *    Scott Stanchfield - modifications for ANTXR
 *******************************************************************************/
package com.javadude.antxr.eclipse.core.parser;

/**
 * General model for outline view
 */
public abstract class AbstractModel implements IModel, ISegment {
	private String fName;
	private IModel fParent;
	private int fStartLine;
	private int fEndLine;

	protected AbstractModel(String aName, IModel aParent) {
	    fName = aName;
	    fParent = aParent;
	}

	/**
	 * @see IModel#getName()
	 */
	public String getName() {
	    return fName;
	}

	/**
	 * @see IModel#getParent()
	 */
	public Object getParent() {
	    return fParent;
	}

	/**
	 * Set the start line
	 * @param aLine the start line
	 */
	public void setStartLine(int aLine) {
	    fStartLine = aLine;
	}

    /**
     * @see ISegment#getStartLine()
     */
	public int getStartLine() {
	    return fStartLine;
	}

	/**
	 * Set the end line
	 * @param aLine the end line
	 */
	public void setEndLine(int aLine) {
	    fEndLine = aLine;
	}

    /**
     * @see ISegment#getEndLine()
     */
	public int getEndLine() {
	    return fEndLine;
	}
}
