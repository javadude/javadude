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

class StringLiteralElement extends GrammarAtom {
    // atomText with quotes stripped and escape codes processed
    protected String processedAtomText;


    public StringLiteralElement(Grammar g, Token t, int autoGenType) {
        super(g, t, autoGenType);
        if (!(g instanceof LexerGrammar)) {
            // lexer does not have token types for string literals
            TokenSymbol ts = grammar.tokenManager.getTokenSymbol(atomText);
            if (ts == null) {
                g.antxrTool.error("Undefined literal: " + atomText, grammar.getFilename(), t.getLine(), t.getColumn());
            }
            else {
                tokenType = ts.getTokenType();
            }
        }
        line = t.getLine();

        // process the string literal text by removing quotes and escaping chars
        // If a lexical grammar, add the characters to the char vocabulary
        processedAtomText = new String();
        for (int i = 1; i < atomText.length() - 1; i++) {
            char c = atomText.charAt(i);
            if (c == '\\') {
                if (i + 1 < atomText.length() - 1) {
                    i++;
                    c = atomText.charAt(i);
                    switch (c) {
                        case 'n':
                            c = '\n';
                            break;
                        case 'r':
                            c = '\r';
                            break;
                        case 't':
                            c = '\t';
                            break;
                    }
                }
            }
            if (g instanceof LexerGrammar) {
                ((LexerGrammar)g).charVocabulary.add(c);
            }
            processedAtomText += c;
        }
    }

    @Override
    public void generate() {
        grammar.generator.gen(this);
    }

    @Override
    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }
}
