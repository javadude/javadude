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

class TokenRangeElement extends AlternativeElement {
    String label;
    protected int begin = Token.INVALID_TYPE;
    protected int end = Token.INVALID_TYPE;
    protected String beginText;
    protected String endText;

    public TokenRangeElement(Grammar g, Token t1, Token t2, int autoGenType) {
        super(g, t1, autoGenType);
        begin = grammar.tokenManager.getTokenSymbol(t1.getText()).getTokenType();
        beginText = t1.getText();
        end = grammar.tokenManager.getTokenSymbol(t2.getText()).getTokenType();
        endText = t2.getText();
        line = t1.getLine();
    }

    @Override
    public void generate() {
        grammar.generator.gen(this);
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }

    @Override
    public void setLabel(String label_) {
        label = label_;
    }

    @Override
    public String toString() {
        if (label != null) {
            return " " + label + ":" + beginText + ".." + endText;
        }
        return " " + beginText + ".." + endText;
    }
}
