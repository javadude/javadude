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

class TokenSymbol extends GrammarSymbol {
    protected int ttype;
    /** describes what token matches in "human terms" */
    protected String paraphrase = null;

    /** Set to a value in the tokens {...} section */
    protected String ASTNodeType;

    public TokenSymbol(String r) {
        super(r);
        ttype = Token.INVALID_TYPE;
    }

    public String getASTNodeType() {
        return ASTNodeType;
    }

    public void setASTNodeType(String type) {
        ASTNodeType = type;
    }

    public String getParaphrase() {
        return paraphrase;
    }

    public int getTokenType() {
        return ttype;
    }

    public void setParaphrase(String p) {
        paraphrase = p;
    }

    public void setTokenType(int t) {
        ttype = t;
    }
}
