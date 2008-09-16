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

/**All alternative blocks are "terminated" by BlockEndElements unless
 * they are rule blocks (in which case they use RuleEndElement).
 */
class BlockEndElement extends AlternativeElement {
    protected boolean[] lock;	// for analysis; used to avoid infinite loops
    protected AlternativeBlock block;// ending blocks know what block they terminate


    public BlockEndElement(Grammar g) {
        super(g);
        lock = new boolean[g.maxk + 1];
    }

    @Override
    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }

    @Override
    public String toString() {
        //return " [BlkEnd]";
        return "";
    }
}
