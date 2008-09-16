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

/** This token knows what index 0..n-1 it is from beginning of stream.
 *  Designed to work with TokenStreamRewriteEngine.java
 */
public class TokenWithIndex extends CommonToken {
    /** Index into token array indicating position in input stream */
    int index;

    public TokenWithIndex() {
	super();
    }

    public TokenWithIndex(int i, String t) {
	super(i,t);
    }

	public void setIndex(int i) {
		index = i;
	}

	public int getIndex() {
		return index;
	}

	@Override
    public String toString() {
		return "["+index+":\"" + getText() + "\",<" + getType() + ">,line=" + line + ",col=" +
col + "]\n";
	}
}
