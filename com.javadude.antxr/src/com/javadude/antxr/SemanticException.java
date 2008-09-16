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

public class SemanticException extends RecognitionException {
	private static final long serialVersionUID = 1L;

	public SemanticException(String s) {
        super(s);
    }

    /** @deprecated As of ANTXR 2.7.2 use {@see #SemanticException(char, String, int, int) } */
	@Deprecated
    public SemanticException(String s, String fileName, int line) {
        this(s, fileName, line, -1);
    }

	public SemanticException(String s, String fileName, int line, int column) {
        super(s, fileName, line, column);
    }
}
