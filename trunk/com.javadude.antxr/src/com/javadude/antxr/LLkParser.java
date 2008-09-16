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

/**An LL(k) parser.
 *
 * @see com.javadude.antxr.Token
 * @see com.javadude.antxr.TokenBuffer
 */
public class LLkParser extends Parser {
    int k;

    public LLkParser(int k_) {
        k = k_;
    }

    public LLkParser(ParserSharedInputState state, int k_) {
        super(state);
		k = k_;
    }

    public LLkParser(TokenBuffer tokenBuf, int k_) {
        k = k_;
        setTokenBuffer(tokenBuf);
    }

    public LLkParser(TokenStream lexer, int k_) {
        k = k_;
        TokenBuffer tokenBuf = new TokenBuffer(lexer);
        setTokenBuffer(tokenBuf);
    }

    /**Consume another token from the input stream.  Can only write sequentially!
     * If you need 3 tokens ahead, you must consume() 3 times.
     * <p>
     * Note that it is possible to overwrite tokens that have not been matched.
     * For example, calling consume() 3 times when k=2, means that the first token
     * consumed will be overwritten with the 3rd.
     */
    @Override
    public void consume() throws TokenStreamException {
        inputState.input.consume();
    }

    @Override
    public int LA(int i) throws TokenStreamException {
        return inputState.input.LA(i);
    }

    @Override
    public Token LT(int i) throws TokenStreamException {
        return inputState.input.LT(i);
    }

    private void trace(String ee, String rname) throws TokenStreamException {
        traceIndent();
        System.out.print(ee + rname + ((inputState.guessing > 0)?"; [guessing]":"; "));
        for (int i = 1; i <= k; i++) {
            if (i != 1) {
                System.out.print(", ");
            }
            if ( LT(i)!=null ) {
                System.out.print("LA(" + i + ")==" + LT(i).getText());
            }
            else {
                System.out.print("LA(" + i + ")==null");
            }
        }
        System.out.println("");
    }

    @Override
    public void traceIn(String rname) throws TokenStreamException {
        traceDepth += 1;
        trace("> ", rname);
    }

    @Override
    public void traceOut(String rname) throws TokenStreamException {
        trace("< ", rname);
        traceDepth -= 1;
    }
}
