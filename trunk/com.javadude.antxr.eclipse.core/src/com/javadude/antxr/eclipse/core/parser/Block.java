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
 * Describes a block for the outline view
 */
public class Block extends AbstractModel {
    /** antxr header */
    public static final int HEADER = 0;
    /** antxr options */
    public static final int OPTIONS = 1;
    /** antxr tokens */
    public static final int TOKENS = 2;
    /** antxr preamble */
    public static final int PREAMBLE = 3;
    /** antxr comment */
    public static final int COMMENT = 4;
    /** antxr action code */
    public static final int ACTION = 5;
    /** antxr exception */
    public static final int EXCEPTION = 6;
	private static final String[] TYPES = { "Header", "Options", "Tokens",
	    									"Preamble", "Comment", "Action",
	    									"Exception" };
	private int fType;

	/**
	 * Define a block
	 * @param aParent the parent block
	 * @param aType the type of block
	 * @param aStartLine the start line
	 * @param anEndLine the end line
	 */
	public Block(IModel aParent, int aType, int aStartLine, int anEndLine) {
	    super(Block.TYPES[aType], aParent);
	    fType = aType;
	    setStartLine(aStartLine);
	    setEndLine(anEndLine);
	}

	/** {@inheritDoc} */
	public boolean hasChildren() {
	    return false;
	}

	/** {@inheritDoc} */
	public Object[] getChildren() {
	    return IModel.NO_CHILDREN;
	}

	/** {@inheritDoc} */
	public String getUniqueID() {
		return ((ISegment)getParent()).getUniqueID() + "/Block:" +
				Block.TYPES[fType];
	}

	/** {@inheritDoc} */
	public boolean accept(ISegmentVisitor aVisitor) {
		return aVisitor.visit(this);
	}

	/**
	 * Get the type of block
	 * @return the block type
	 */
	public int getType() {
	    return fType;
	}

	/** {@inheritDoc} */
	public String toString() {
	    return getUniqueID() + " [" + getStartLine() + ":" +
	    		getEndLine() + "]";
	}
}
