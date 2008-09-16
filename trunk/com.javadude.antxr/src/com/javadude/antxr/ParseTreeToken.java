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

public class ParseTreeToken extends ParseTree {
	private static final long serialVersionUID = 1L;
	protected Token token;

	public ParseTreeToken(Token token) {
		this.token = token;
	}

	@Override
    protected int getLeftmostDerivation(StringBuffer buf, int step) {
		buf.append(' ');
		buf.append(toString());
		return step; // did on replacements
	}

	@Override
    public String toString() {
		if ( token!=null ) {
			return token.getText();
		}
		return "<missing token>";
	}
}
