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

/** A token is minimally a token type.  Subclasses can add the text matched
 *  for the token and line info.
 */
public class Token implements Cloneable {
    // constants
    public static final int MIN_USER_TYPE = 4;
    public static final int NULL_TREE_LOOKAHEAD = 3;
    public static final int INVALID_TYPE = 0;
    public static final int EOF_TYPE = 1;
    public static final int SKIP = -1;

    // each Token has at least a token type
    protected int type = Token.INVALID_TYPE;

    // the illegal token object
    public static Token badToken = new Token(Token.INVALID_TYPE, "<no text>");

    public Token() {
		// nothing
    }

    public Token(int t) {
        type = t;
    }

    public Token(int t, String txt) {
        type = t;
        setText(txt);
    }

    public int getColumn() {
        return 0;
    }

    public int getLine() {
        return 0;
    }

	public String getFilename() {
		return null;
	}

	public void setFilename(String name) {
		// nothing
	}

    public String getText() {
        return "<no text>";
    }

	public void setText(String t) {
		// nothing
    }

    public void setColumn(int c) {
		// nothing
    }

    public void setLine(int l) {
		// nothing
    }

	public int getType() {
        return type;
    }

    public void setType(int t) {
        type = t;
    }

    @Override
    public String toString() {
        return "[\"" + getText() + "\",<" + getType() + ">]";
    }
}
