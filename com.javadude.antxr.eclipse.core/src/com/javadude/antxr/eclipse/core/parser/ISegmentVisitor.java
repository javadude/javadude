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
 * Visitor design pattern.
 * @see ISegment#accept(ISegmentVisitor)
 */
public interface ISegmentVisitor {

	/**
	 * Visit a specific segment
	 * @param aSegment the segment to visit
	 * @return true if we should visit children
	 */
	boolean visit(ISegment aSegment);
}
