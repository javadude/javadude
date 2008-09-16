/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield, based on ANTLR-Eclipse plugin
 *   by Torsten Juergeleit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors
 *    Torsten Juergeleit - original ANTLR Eclipse plugin
 *    Scott Stanchfield - modifications for ANTXR
 *******************************************************************************/
package com.javadude.antxr.eclipse.core.parser;

import java.io.Reader;
import java.util.Stack;

import com.javadude.antxr.ANTXRGrammarParseBehavior;
import com.javadude.antxr.ANTXRLexer;
import com.javadude.antxr.ANTXRParser;
import com.javadude.antxr.SemanticException;
import com.javadude.antxr.Token;
import com.javadude.antxr.TokenBuffer;
import com.javadude.antxr.Tool;
import com.javadude.antxr.collections.impl.BitSet;
import com.javadude.antxr.eclipse.core.AntxrCorePlugin;

/**
 * Parser used to create an ANTXR grammar overview (only terminals and
 * non-terminals; no actions, comments and type information).
 */
public class AntxrOverviewParser implements ANTXRGrammarParseBehavior {

    private StringBuffer fBuffer;
    private Stack<SubRule> fSubRules;
    private SubRule fCurrentSubRule;

    /**
     * Parse a file to generate the overview
     * @param aReader the file to parse
     * @return overview
     */
    public String parse(Reader aReader) {
        fBuffer = new StringBuffer(2000);
        fSubRules = new Stack<SubRule>();
        fCurrentSubRule = new SubRule();

        // Parse ANTXR grammar from given reader
        ANTXRLexer lexer = new ANTXRLexer(aReader);
        TokenBuffer tokenBuf = new TokenBuffer(lexer);
        ANTXRParser p = new ANTXRParser(tokenBuf, this,
                                        new OverviewTool());
        p.setFilename(".");
        try {
            p.grammar();
        }
        catch (Exception e) {
            AntxrCorePlugin.log(e);
        }

        String text = fBuffer.toString();

        fBuffer = null;
        fSubRules = null;
        fCurrentSubRule = null;

        return text;
    }

    /** {@inheritDoc} */
    public void beginAlt(boolean doAST_) {
        if (fSubRules.empty()) {
            fBuffer.append("\n\t");
        }
        if (fCurrentSubRule.alt > 0) {
            fBuffer.append("| ");
        }
        fCurrentSubRule.alt++;
    }

    /** {@inheritDoc} */
    public void beginSubRule(Token label, Token start, boolean not) {
        if (fCurrentSubRule.ref > 0 && fSubRules.empty()) {
            fBuffer.append("\n\t");
        }
        if (not) {
            fBuffer.append('~');
        }
        fBuffer.append("( ");
        fSubRules.push(fCurrentSubRule);
        fCurrentSubRule = new SubRule();
    }

    /** {@inheritDoc} */
    public void defineRuleName(Token r, String access, boolean ruleAST,
                               String docComment) throws SemanticException {
        fBuffer.append('\n').append(r.getText()).append(" :");
    }

    /** {@inheritDoc} */
    public void endRule(String r) {
        if (fCurrentSubRule.ref > 0) {
            fBuffer.append("\n\t");
        }
        if (!fCurrentSubRule.isBracketClosed) {
            fBuffer.append('\t');
        }
        fBuffer.append(";\n");
        fCurrentSubRule.alt = 0;
        fCurrentSubRule.ref = 0;
    }

    /** {@inheritDoc} */
    public void endSubRule() {
        if (!fCurrentSubRule.isBracketClosed) {
            fBuffer.append(") ");
        }
        fCurrentSubRule = fSubRules.pop();
        if (fSubRules.empty()) {
            fBuffer.append("\n\t");
            fCurrentSubRule.isBracketClosed = true;
            fCurrentSubRule.ref = 0;
        }
    }

    /** {@inheritDoc} */
    public void oneOrMoreSubRule() {
        fBuffer.append(")+ ");
        fCurrentSubRule.isBracketClosed = true;
    }

    /** {@inheritDoc} */
    public void optionalSubRule() {
        fBuffer.append(")? ");
        fCurrentSubRule.isBracketClosed = true;
    }

    /** {@inheritDoc} */
    public void zeroOrMoreSubRule() {
        fBuffer.append(")* ");
        fCurrentSubRule.isBracketClosed = true;
    }

    /** {@inheritDoc} */
    public void synPred() {
        fBuffer.append(") => ");
        fCurrentSubRule.isBracketClosed = true;
    }

    /** {@inheritDoc} */
    public void refCharLiteral(Token lit, Token label, boolean inverted,
                               int autoGenType, boolean lastInRule) {
        if (inverted) {
            fBuffer.append('~');
        }
        fBuffer.append(lit.getText()).append(' ');
        fCurrentSubRule.ref++;
    }

    /** {@inheritDoc} */
    public void refCharRange(Token t1, Token t2, Token label, int autoGenType,
                             boolean lastInRule) {
        fBuffer.append(t1.getText()).append("..").append(t2.getText()).
                                                                   append(' ');
        fCurrentSubRule.ref++;
    }

    /** {@inheritDoc} */
    public void refRule(Token idAssign, Token r, Token label, Token arg,
                        int autoGenType) {
        fBuffer.append(r.getText()).append(' ');
        fCurrentSubRule.ref++;
    }

    /** {@inheritDoc} */
    public void refSemPred(Token pred) {
        fBuffer.append("{").append(pred.getText()).append("}? ");
        fCurrentSubRule.ref++;
    }

    /** {@inheritDoc} */
    public void refStringLiteral(Token lit, Token label, int autoGenType,
                                 boolean lastInRule) {
        fBuffer.append(lit.getText()).append(' ');
        fCurrentSubRule.ref++;
    }

    /** {@inheritDoc} */
    public void refToken(Token assignId, Token t, Token label, Token args,
                        boolean inverted, int autoGenType, boolean lastInRule) {
        if (inverted) {
            fBuffer.append('~');
        }
        fBuffer.append(t.getText()).append(' ');
        fCurrentSubRule.ref++;
    }

    /** {@inheritDoc} */
    public void refTokenRange(Token t1, Token t2, Token label, int autoGenType,
                              boolean lastInRule) {
        fBuffer.append(t1.getText()).append("..").append(t2.getText()).
                                                                   append(' ');
        fCurrentSubRule.ref++;
    }

    /** {@inheritDoc} */
    public void refWildcard(Token t, Token label, int autoGenType) {
        fBuffer.append(". ");
        fCurrentSubRule.ref++;
    }

    /** {@inheritDoc} */
    public void startLexer(String file, Token name, String superClass,
                           String doc) {
        startClass(file, name, superClass, doc, "Lexer");
    }

    /** {@inheritDoc} */
    public void startParser(String file, Token name, String superClass,
                            String doc) {
        startClass(file, name, superClass, doc, "Parser");
    }

    /** {@inheritDoc} */
    public void startTreeWalker(String file, Token name, String superClass,
                                String doc) {
        startClass(file, name, superClass, doc, "TreeParser");
    }

    private void startClass(String file, Token name, String superClass,
                            String doc, String className) {
        if (fBuffer.length() > 0) {
            fBuffer.append("\n\n\n");
        }
        if (doc != null) {
            fBuffer.append(doc).append("\n\n");
        }
        fBuffer.append("class ").append(name.getText()).append(" extends ").
                                             append(className).append(";\n\n");
    }

    /** {@inheritDoc} */
    public void abortGrammar() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void beginChildList() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void beginExceptionGroup() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void beginExceptionSpec(Token label) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void beginTree(Token tok) throws SemanticException {
        // do nothing
    }

    /** {@inheritDoc} */
    public void defineToken(Token tokname, Token tokliteral) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void endAlt() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void endChildList() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void endExceptionGroup() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void endExceptionSpec() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void endGrammar() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void endOptions() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void endTree() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void hasError() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void noASTSubRule() {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refAction(Token action) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refArgAction(Token action) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void setUserExceptions(String thr) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refElementOption(Token option, Token value) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refTokensSpecElementOption(Token tok, Token option,
                                           Token value) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refExceptionHandler(Token exTypeAndName, Token action) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refHeaderAction(Token name, Token act) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refInitAction(Token action) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refMemberAction(Token act) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refPreambleAction(Token act) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refReturnAction(Token returnAction) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void refTreeSpecifier(Token treeSpec) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void setArgOfRuleRef(Token argaction) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void setCharVocabulary(BitSet b) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void setFileOption(Token key, Token value, String filename) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void setGrammarOption(Token key, Token value) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void setRuleOption(Token key, Token value) {
        // do nothing
    }

    /** {@inheritDoc} */
    public void setSubruleOption(Token key, Token value) {
        // do nothing
    }

    private class OverviewTool extends Tool {

        /** {@inheritDoc} */
        public void error(String s, String file, int line, int column) {
            // do nothing
        }

        /** {@inheritDoc} */
        public void error(String s) {
            // do nothing
        }

        /** {@inheritDoc} */
        public void warning(String s, String file, int line, int column) {
            // do nothing
        }

        /** {@inheritDoc} */
        public void warning(String s) {
            // do nothing
        }

        /** {@inheritDoc} */
        public void warning(String[] s, String file, int line, int column) {
            // do nothing
        }
    }

    private class SubRule {
        int ref = 0;
        int alt = 0;
        boolean isBracketClosed = false;
    }

    /** {@inheritDoc} */
    public void beginRule(Token ruleId, Token lookahead, boolean ruleAutoGen) {
        // do nothing
    }

    public void fixRuleName(Token token) {
        // do nothing -- keep the overview as is
    }
}
