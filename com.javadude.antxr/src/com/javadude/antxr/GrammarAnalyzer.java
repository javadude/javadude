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

/**A GrammarAnalyzer computes lookahead from Grammar (which contains
 * a grammar symbol table) and can then answer questions about the
 * grammar.
 *
 * To access the RuleBlock for a rule name, the grammar symbol table
 * is consulted.
 *
 * There should be no distinction between static & dynamic analysis.
 * In other words, some of the easy analysis can be done statically
 * and then the part that is hard statically can be deferred to
 * parse-time.  Interestingly, computing LL(k) for k>1 lookahead
 * statically is O(|T|^k) where T is the grammar vocabulary, but,
 * is O(k) at run-time (ignoring the large constant associated with
 * the size of the grammar).  In English, the difference can be
 * described as "find the set of all possible k-sequences of input"
 * versus "does this specific k-sequence match?".
 */
public interface GrammarAnalyzer {
    /**The epsilon token type is an imaginary type used
     * during analysis.  It indicates an incomplete look() computation.
     * Must be kept consistent with Token constants to be between
     * MIN_USER_TYPE and INVALID_TYPE.
     */
    // public static final int EPSILON_TYPE = 2;
    public static final int NONDETERMINISTIC = Integer.MAX_VALUE; // lookahead depth
    public static final int LOOKAHEAD_DEPTH_INIT = -1;
}
