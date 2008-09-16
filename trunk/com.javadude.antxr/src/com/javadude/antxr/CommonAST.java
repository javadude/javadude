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

import com.javadude.antxr.collections.AST;

/** Common AST node implementation */
public class CommonAST extends BaseAST {
	private static final long serialVersionUID = 1L;
	int ttype = Token.INVALID_TYPE;
    String text;


    /** Get the token text for this node */
    @Override
    public String getText() {
        return text;
    }

    /** Get the token type for this node */
    @Override
    public int getType() {
        return ttype;
    }

    @Override
    public void initialize(int t, String txt) {
        setType(t);
        setText(txt);
    }

    @Override
    public void initialize(AST t) {
        setText(t.getText());
        setType(t.getType());
    }

    public CommonAST() {
		// nothing
    }

    public CommonAST(Token tok) {
        initialize(tok);
    }

    @Override
    public void initialize(Token tok) {
        setText(tok.getText());
        setType(tok.getType());
    }

    /** Set the token text for this node */
    @Override
    public void setText(String text_) {
        text = text_;
    }

    /** Set the token type for this node */
    @Override
    public void setType(int ttype_) {
        ttype = ttype_;
    }
}
