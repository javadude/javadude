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

public class RecognitionException extends ANTXRException {
	private static final long serialVersionUID = 1L;
	public String fileName;		// not used by treeparsers
    public int line;
    public int column;

    public RecognitionException() {
        super("parsing error");
        fileName = null;
        line = -1;
        column = -1;
    }

    /**
     * RecognitionException constructor comment.
     * @param s java.lang.String
     */
    public RecognitionException(String s) {
        super(s);
        fileName = null;
        line = -1;
        column = -1;
    }

    /** @deprecated As of ANTXR 2.7.2 use {@see #RecognitionException(char, String, int, int) } */
    @Deprecated
    public RecognitionException(String s, String fileName_, int line_) {
        this(s, fileName_, line_, -1);
    }

    /**
     * RecognitionException constructor comment.
     * @param s java.lang.String
     */
    public RecognitionException(String s, String fileName_, int line_, int column_) {
        super(s);
        fileName = fileName_;
        line = line_;
        column = column_;
    }

    public String getFilename() {
        return fileName;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    /** @deprecated As of ANTXR 2.7.0 */
    @Deprecated
    public String getErrorMessage() {
        return getMessage();
    }

    @Override
    public String toString() {
        return FileLineFormatter.getFormatter().
            getFormatString(fileName, line, column) + getMessage();
    }
}
