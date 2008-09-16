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

abstract class AlternativeElement extends GrammarElement {
    AlternativeElement next;
    protected int autoGenType = GrammarElement.AUTO_GEN_NONE;

    protected String enclosingRuleName;

    public AlternativeElement(Grammar g) {
        super(g);
    }

    public AlternativeElement(Grammar g, Token start) {
        super(g, start);
    }

    public AlternativeElement(Grammar g, Token start, int autoGenType_) {
        super(g, start);
        autoGenType = autoGenType_;
    }

    public int getAutoGenType() {
        return autoGenType;
    }

    public void setAutoGenType(int a) {
        autoGenType = a;
    }

    public String getLabel() {
        return null;
    }

    public void setLabel(String label) {
		// nothing
    }
}
