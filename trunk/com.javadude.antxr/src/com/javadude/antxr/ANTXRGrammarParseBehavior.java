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

import com.javadude.antxr.collections.impl.BitSet;

public interface ANTXRGrammarParseBehavior {
    public void abortGrammar();

    public void beginAlt(boolean doAST_);

    public void beginChildList();

    // Exception handling
    public void beginExceptionGroup();

    public void beginExceptionSpec(Token label);

    public void beginRule(Token ruleId, Token lookahead, boolean ruleAutoGen);

    public void beginSubRule(Token label, Token start, boolean not);

    // Trees
    public void beginTree(Token tok) throws SemanticException;

    public void defineRuleName(Token r, String access, boolean ruleAST, String docComment) throws SemanticException;

    public void defineToken(Token tokname, Token tokliteral);

    public void endAlt();

    public void endChildList();

    public void endExceptionGroup();

    public void endExceptionSpec();

    public void endGrammar();

    public void endOptions();

    public void endRule(String r);

    public void endSubRule();

    public void endTree();

    public void hasError();

    public void noASTSubRule();

    public void oneOrMoreSubRule();

    public void optionalSubRule();

    public void refAction(Token action);

    public void refArgAction(Token action);

    public void setUserExceptions(String thr);

    public void refCharLiteral(Token lit, Token label, boolean inverted, int autoGenType, boolean lastInRule);

    public void refCharRange(Token t1, Token t2, Token label, int autoGenType, boolean lastInRule);

    public void refElementOption(Token option, Token value);

    public void refTokensSpecElementOption(Token tok, Token option, Token value);

    public void refExceptionHandler(Token exTypeAndName, Token action);

    public void refHeaderAction(Token name, Token act);

    public void refInitAction(Token action);

    public void refMemberAction(Token act);

    public void refPreambleAction(Token act);

    public void refReturnAction(Token returnAction);

    public void refRule(Token idAssign, Token r, Token label, Token arg, int autoGenType);

    public void refSemPred(Token pred);

    public void refStringLiteral(Token lit, Token label, int autoGenType, boolean lastInRule);

    public void refToken(Token assignId, Token t, Token label, Token args,
                         boolean inverted, int autoGenType, boolean lastInRule);

    public void refTokenRange(Token t1, Token t2, Token label, int autoGenType, boolean lastInRule);

    // Tree specifiers
    public void refTreeSpecifier(Token treeSpec);

    public void refWildcard(Token t, Token label, int autoGenType);

    public void setArgOfRuleRef(Token argaction);

    public void setCharVocabulary(BitSet b);

    // Options
    public void setFileOption(Token key, Token value, String filename);

    public void setGrammarOption(Token key, Token value);

    public void setRuleOption(Token key, Token value);

    public void setSubruleOption(Token key, Token value);

    public void startLexer(String file, Token name, String superClass, String doc);

    // Flow control for grammars
    public void startParser(String file, Token name, String superClass, String doc);

    public void startTreeWalker(String file, Token name, String superClass, String doc);

    public void synPred();

    public void zeroOrMoreSubRule();

    public void fixRuleName(Token token);
}
