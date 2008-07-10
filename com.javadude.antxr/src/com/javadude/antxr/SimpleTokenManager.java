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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class SimpleTokenManager implements TokenManager, Cloneable {
    protected int maxToken = Token.MIN_USER_TYPE;
    // Token vocabulary is Vector of String's
    protected List<String> vocabulary;
    // Hash table is a mapping from Strings to TokenSymbol
    private Map<String, TokenSymbol> table;
    // the ANTXR tool
    protected Tool antxrTool;
    // Name of the token manager
    protected String name;

    protected boolean readOnly = false;

    SimpleTokenManager(String name_, Tool tool_) {
        antxrTool = tool_;
        name = name_;
        // Don't make a bigger vector than we need, because it will show up in output sets.
        vocabulary = new ArrayList<String>();
        table = new HashMap<String, TokenSymbol>();

        // define EOF symbol
        TokenSymbol ts = new TokenSymbol("EOF");
        ts.setTokenType(Token.EOF_TYPE);
        define(ts);

        // define <null-tree-lookahead> but only in the vocabulary vector
        ensureCapacity(vocabulary, Token.NULL_TREE_LOOKAHEAD);
        vocabulary.set(Token.NULL_TREE_LOOKAHEAD, "NULL_TREE_LOOKAHEAD");
    }
    private void ensureCapacity(List<?> list, int  index) {
        while (list.size() <= index) {
            list.add(null);
        }
    }
    @Override
    public Object clone() {
        SimpleTokenManager tm;
        try {
            tm = (SimpleTokenManager)super.clone();
            tm.vocabulary = new ArrayList<String>(vocabulary);
            tm.table = new HashMap<String, TokenSymbol>(table);
            tm.maxToken = this.maxToken;
            tm.antxrTool = this.antxrTool;
            tm.name = this.name;
        }
        catch (CloneNotSupportedException e) {
            antxrTool.fatalError("panic: cannot clone token manager");
            return null;
        }
        return tm;
    }

    /** define a token */
    public void define(TokenSymbol ts) {
        // Add the symbol to the vocabulary vector
        ensureCapacity(vocabulary, ts.getTokenType());
        vocabulary.set(ts.getTokenType(), ts.getId());
        // add the symbol to the hash table
        mapToTokenSymbol(ts.getId(), ts);
    }

    /** Simple token manager doesn't have a name -- must be set externally */
    public String getName() {
        return name;
    }

    /** Get a token symbol by index */
    public String getTokenStringAt(int idx) {
        return vocabulary.get(idx);
    }

    /** Get the TokenSymbol for a string */
    public TokenSymbol getTokenSymbol(String sym) {
        return table.get(sym);
    }

    /** Get a token symbol by index */
    public TokenSymbol getTokenSymbolAt(int idx) {
        return getTokenSymbol(getTokenStringAt(idx));
    }

    /** Get an enumerator over the symbol table */
    public Iterator<TokenSymbol> getTokenSymbolElements() {
        return table.values().iterator();
    }

    public Iterator<String> getTokenSymbolKeys() {
        return table.keySet().iterator();
    }

    /** Get the token vocabulary (read-only).
     * @return A Vector of TokenSymbol
     */
    public List<String> getVocabulary() {
        return vocabulary;
    }

    /** Simple token manager is not read-only */
    public boolean isReadOnly() {
        return false;
    }

    /** Map a label or string to an existing token symbol */
    public void mapToTokenSymbol(String theName, TokenSymbol sym) {
        // System.out.println("mapToTokenSymbol("+name+","+sym+")");
        table.put(theName, sym);
    }

    /** Get the highest token type in use */
    public int maxTokenType() {
        return maxToken - 1;
    }

    /** Get the next unused token type */
    public int nextTokenType() {
        return maxToken++;
    }

    /** Set the name of the token manager */
    public void setName(String name_) {
        name = name_;
    }

    public void setReadOnly(boolean ro) {
        readOnly = ro;
    }

    /** Is a token symbol defined? */
    public boolean tokenDefined(String symbol) {
        return table.containsKey(symbol);
    }
}
