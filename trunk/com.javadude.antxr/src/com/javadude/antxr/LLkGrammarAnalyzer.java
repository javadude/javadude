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

public interface LLkGrammarAnalyzer extends GrammarAnalyzer {
    public boolean deterministic(AlternativeBlock blk);
    public boolean deterministic(OneOrMoreBlock blk);
    public boolean deterministic(ZeroOrMoreBlock blk);
    public Lookahead FOLLOW(int k, RuleEndElement end);
    public Lookahead look(int k, ActionElement action);
    public Lookahead look(int k, AlternativeBlock blk);
    public Lookahead look(int k, BlockEndElement end);
    public Lookahead look(int k, CharLiteralElement atom);
    public Lookahead look(int k, CharRangeElement end);
    public Lookahead look(int k, GrammarAtom atom);
    public Lookahead look(int k, OneOrMoreBlock blk);
    public Lookahead look(int k, RuleBlock blk);
    public Lookahead look(int k, RuleEndElement end);
    public Lookahead look(int k, RuleRefElement rr);
    public Lookahead look(int k, StringLiteralElement atom);
    public Lookahead look(int k, SynPredBlock blk);
    public Lookahead look(int k, TokenRangeElement end);
    public Lookahead look(int k, TreeElement end);
    public Lookahead look(int k, WildcardElement wc);
    public Lookahead look(int k, ZeroOrMoreBlock blk);
    public Lookahead look(int k, String rule);
    public void setGrammar(Grammar g);
    public boolean subruleCanBeInverted(AlternativeBlock blk, boolean forLexer);
}
