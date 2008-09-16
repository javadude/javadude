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
package com.javadude.antxr.eclipse.core.builder;

/**
 * A stream listener is notified of changes to a stream.
 */
public interface IStreamListener {

	/**
	 * Notifies this listener that text has been appended to the given stream.
	 *
	 * @param aText  the appended text
	 * @param aStream  the stream to which text was appended
	 */
	public void streamAppended(String aText, Object aStream);
}
