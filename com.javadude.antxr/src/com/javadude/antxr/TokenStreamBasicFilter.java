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
