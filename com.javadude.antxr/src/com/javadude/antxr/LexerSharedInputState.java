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

import java.io.InputStream;
import java.io.Reader;

/** This object contains the data associated with an
 *  input stream of characters.  Multiple lexers
 *  share a single LexerSharedInputState to lex
 *  the same input stream.
 */
public class LexerSharedInputState {
    protected int column = 1;
    protected int line = 1;
    protected int tokenStartColumn = 1;
    protected int tokenStartLine = 1;
    protected InputBuffer input;

    /** What file (if known) caused the problem? */
    protected String filename;

    public int guessing = 0;

    public LexerSharedInputState(InputBuffer inbuf) {
        input = inbuf;
    }

    public LexerSharedInputState(InputStream in) {
        this(new ByteBuffer(in));
    }

    public LexerSharedInputState(Reader in) {
        this(new CharBuffer(in));
    }

    public void reset() {
        column = 1;
        line = 1;
        tokenStartColumn = 1;
        tokenStartLine = 1;
        guessing = 0;
        filename = null;
        input.reset();
    }
}
