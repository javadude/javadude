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

abstract class BlockWithImpliedExitPath extends AlternativeBlock {
    protected int exitLookaheadDepth;	// lookahead needed to handle optional path
    /** lookahead to bypass block; set
     * by deterministic().  1..k of Lookahead
     */
    protected Lookahead[] exitCache = new Lookahead[grammar.maxk + 1];

    public BlockWithImpliedExitPath(Grammar g) {
        super(g);
    }

    public BlockWithImpliedExitPath(Grammar g, Token start) {
        super(g, start, false);
    }
}
