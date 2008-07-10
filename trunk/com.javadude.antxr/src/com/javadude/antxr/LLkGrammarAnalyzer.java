/*******************************************************************************
 *  Copyright 2008 Scott Stanchfield.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
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
