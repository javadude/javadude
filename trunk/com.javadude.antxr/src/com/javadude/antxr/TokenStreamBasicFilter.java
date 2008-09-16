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

/** This object is a TokenStream that passes through all
 *  tokens except for those that you tell it to discard.
 *  There is no buffering of the tokens.
 */
public class TokenStreamBasicFilter implements TokenStream {
    /** The set of token types to discard */
    protected BitSet discardMask;

    /** The input stream */
    protected TokenStream input;

    public TokenStreamBasicFilter(TokenStream input) {
        this.input = input;
        discardMask = new BitSet();
    }

    public void discard(int ttype) {
        discardMask.add(ttype);
    }

    public void discard(BitSet mask) {
        discardMask = mask;
    }

    public Token nextToken() throws TokenStreamException {
        Token tok = input.nextToken();
        while (tok != null && discardMask.member(tok.getType())) {
            tok = input.nextToken();
        }
        return tok;
    }
}
