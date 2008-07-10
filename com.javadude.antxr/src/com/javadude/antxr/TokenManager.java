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

import java.util.Iterator;
import java.util.List;

/** Interface that describes the set of defined tokens */
interface TokenManager {
    public Object clone();

    /** define a token symbol */
    public void define(TokenSymbol ts);

    /** Get the name of the token manager */
    public String getName();

    /** Get a token string by index */
    public String getTokenStringAt(int idx);

    /** Get the TokenSymbol for a string */
    public TokenSymbol getTokenSymbol(String sym);

    public TokenSymbol getTokenSymbolAt(int idx);

    /** Get an enumerator over the symbol table */
    public Iterator<TokenSymbol> getTokenSymbolElements();

    public Iterator<String> getTokenSymbolKeys();

    /** Get the token vocabulary (read-only).
     * @return A Vector of Strings indexed by token type */
    public List<String> getVocabulary();

    /** Is this token manager read-only? */
    public boolean isReadOnly();

    public void mapToTokenSymbol(String name, TokenSymbol sym);

    /** Get the highest token type in use */
    public int maxTokenType();

    /** Get the next unused token type */
    public int nextTokenType();

    public void setName(String n);

    public void setReadOnly(boolean ro);

    /** Is a token symbol defined? */
    public boolean tokenDefined(String symbol);
}
