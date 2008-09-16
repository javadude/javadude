/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
 *   Scott Stanchfield - Modifications for XML Parsing
 *******************************************************************************/
package com.javadude.antxr;

/**
 * Aborted recognition of current token. Try to get one again.
 * Used by TokenStreamSelector.retry() to force nextToken()
 * of stream to re-enter and retry.
 */
public class TokenStreamRetryException extends TokenStreamException {
	private static final long serialVersionUID = 1L;

	public TokenStreamRetryException() {
		// nothing
    }
}
