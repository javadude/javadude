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

// class implements a String-like object whose sole purpose is to be
// entered into a lexer HashTable.  It uses a lexer object to get
// information about case sensitivity.

public class ANTXRHashString {
    // only one of s or buf is non-null
    private String s;
    private char[] buf;
    private int len;
    private CharScanner lexer;
    private static final int prime = 151;


    public ANTXRHashString(char[] buf, int length, CharScanner lexer) {
        this.lexer = lexer;
        setBuffer(buf, length);
    }

    // Hash strings constructed this way are unusable until setBuffer or setString are called.
    public ANTXRHashString(CharScanner lexer) {
        this.lexer = lexer;
    }

    public ANTXRHashString(String s, CharScanner lexer) {
        this.lexer = lexer;
        setString(s);
    }

    private final char charAt(int index) {
        return (s != null) ? s.charAt(index) : buf[index];
    }

    // Return true if o is an ANTXRHashString equal to this.
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ANTXRHashString) && !(o instanceof String)) {
            return false;
        }

        ANTXRHashString string;
        if (o instanceof String) {
            string = new ANTXRHashString((String)o, lexer);
        }
        else {
            string = (ANTXRHashString)o;
        }
        int l = length();
        if (string.length() != l) {
            return false;
        }
        if (lexer.getCaseSensitiveLiterals()) {
            for (int i = 0; i < l; i++) {
                if (charAt(i) != string.charAt(i)) {
                    return false;
                }
            }
        }
        else {
            for (int i = 0; i < l; i++) {
                if (lexer.toLower(charAt(i)) != lexer.toLower(string.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashval = 0;
        int l = length();

        if (lexer.getCaseSensitiveLiterals()) {
            for (int i = 0; i < l; i++) {
                hashval = hashval * ANTXRHashString.prime + charAt(i);
            }
        }
        else {
            for (int i = 0; i < l; i++) {
                hashval = hashval * ANTXRHashString.prime + lexer.toLower(charAt(i));
            }
        }
        return hashval;
    }

    private final int length() {
        return (s != null) ? s.length() : len;
    }

    public void setBuffer(char[] buf, int length) {
        this.buf = buf;
        this.len = length;
        s = null;
    }

    public void setString(String s) {
        this.s = s;
        buf = null;
    }
}
