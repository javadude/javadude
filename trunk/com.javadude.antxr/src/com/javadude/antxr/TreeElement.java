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

/** A TreeElement is a block with one alternative and a root node */
class TreeElement extends AlternativeBlock {
    GrammarAtom root;

    public TreeElement(Grammar g, Token start) {
        super(g, start, false);
    }

    @Override
    public void generate() {
        grammar.generator.gen(this);
    }

    @Override
    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }

    @Override
    public String toString() {
        String s = " #(" + root;
        Alternative a = alternatives.get(0);
        AlternativeElement p = a.head;
        while (p != null) {
            s += p;
            p = p.next;
        }
        return s + " )";
    }
}
