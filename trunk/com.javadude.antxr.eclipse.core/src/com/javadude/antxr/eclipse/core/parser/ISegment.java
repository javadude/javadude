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
 * A segment in the file to track in the outline
 */
public interface ISegment {

	/**
	 * Get the id of the segment
	 * @return the segment id
	 */
	String getUniqueID();

	/**
	 * Where does the segment start
	 * @return the start line of the segment
	 */
	int getStartLine();

	/**
	 * Where does the line end
	 * @return The end line of the segment
	 */
	int getEndLine();

	/**
	 * Visitor design pattern.
	 * @param aVisitor the visitor to accept
	 * @return true if we should visit children
	 * @see ISegmentVisitor#visit(ISegment)
	 */
	boolean accept(ISegmentVisitor aVisitor);
}
