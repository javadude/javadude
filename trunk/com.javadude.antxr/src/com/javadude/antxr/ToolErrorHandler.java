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

interface ToolErrorHandler {


    /** Issue a warning about ambiguity between a alternates
     * @param blk  The block being analyzed
     * @param lexicalAnalysis  true for lexical rule
     * @param depth  The depth of the ambiguity
     * @param sets  An array of bitsets containing the ambiguities
     * @param altIdx1  The zero-based index of the first ambiguous alternative
     * @param altIdx2  The zero-based index of the second ambiguous alternative
     */
    public void warnAltAmbiguity(
        Grammar grammar,
        AlternativeBlock blk,
        boolean lexicalAnalysis,
        int depth,
        Lookahead[] sets,
        int altIdx1,
        int altIdx2
        );

    /** Issue a warning about ambiguity between an alternate and exit path.
     * @param blk  The block being analyzed
     * @param lexicalAnalysis  true for lexical rule
     * @param depth  The depth of the ambiguity
     * @param sets  An array of bitsets containing the ambiguities
     * @param altIdx  The zero-based index of the ambiguous alternative
     */
    public void warnAltExitAmbiguity(
        Grammar grammar,
        BlockWithImpliedExitPath blk,
        boolean lexicalAnalysis,
        int depth,
        Lookahead[] sets,
        int altIdx
        );
}
