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

/** A token is minimally a token type.  Subclasses can add the text matched
 *  for the token and line info.
 */
public class Token implements Cloneable {
    // constants
    public static final int MIN_USER_TYPE = 4;
    public static final int NULL_TREE_LOOKAHEAD = 3;
    public static final int INVALID_TYPE = 0;
    public static final int EOF_TYPE = 1;
    public static final int SKIP = -1;

    // each Token has at least a token type
    protected int type = INVALID_TYPE;

    // the illegal token object
    public static Token badToken = new Token(INVALID_TYPE, "<no text>");

    public Token() {
		// nothing
    }

    public Token(int t) {
        type = t;
    }

    public Token(int t, String txt) {
        type = t;
        setText(txt);
    }

    public int getColumn() {
        return 0;
    }

    public int getLine() {
        return 0;
    }

	public String getFilename() {
		return null;
	}

	public void setFilename(String name) {
		// nothing
	}

    public String getText() {
        return "<no text>";
    }

	public void setText(String t) {
		// nothing
    }

    public void setColumn(int c) {
		// nothing
    }

    public void setLine(int l) {
		// nothing
    }

	public int getType() {
        return type;
    }

    public void setType(int t) {
        type = t;
    }

    @Override
    public String toString() {
        return "[\"" + getText() + "\",<" + getType() + ">]";
    }
}
