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
 * General model interface for nodes in the outline view
 */
public interface IModel {
	/** A constant array of zero children (many nodes have no kids */
    public static final Object[] NO_CHILDREN = new Object[0];

	/**
	 * Get the name of the current node
	 * @return the name of the current node
	 */
	String getName();

	/**
	 * Get the parent node
	 * @return the parent node
	 */
	Object getParent();

	/**
	 * Do we have children
	 * @return true if child nodes exist
	 */
	boolean hasChildren();

	/**
	 * Get the child nodes
	 * @return the child nodes
	 */
	Object[] getChildren();
}
