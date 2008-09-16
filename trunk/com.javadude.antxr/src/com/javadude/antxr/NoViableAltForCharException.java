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

public class NoViableAltForCharException extends RecognitionException {
	private static final long serialVersionUID = 1L;
	public char foundChar;

    public NoViableAltForCharException(char c, CharScanner scanner) {
        super("NoViableAlt", scanner.getFilename(),
              scanner.getLine(), scanner.getColumn());
        foundChar = c;
    }

    /** @deprecated As of ANTXR 2.7.2 use {@see #NoViableAltForCharException(char, String, int, int) } */
    @Deprecated
    public NoViableAltForCharException(char c, String fileName, int line) {
        this(c, fileName, line, -1);
    }

    public NoViableAltForCharException(char c, String fileName, int line, int column) {
        super("NoViableAlt", fileName, line, column);
        foundChar = c;
    }

    /**
     * Returns a clean error message (no line number/column information)
     */
    @Override
    public String getMessage() {
        String mesg = "unexpected char: ";

        // I'm trying to mirror a change in the C++ stuff.
        // But java seems to lack something convenient isprint-ish..
		  // actually we're kludging around unicode and non unicode savy
		  // output stuff like most terms.. Basically one would want to
		  // be able to tweak the generation of this message.

        if ((foundChar >= ' ') && (foundChar <= '~')) {
            mesg += '\'';
            mesg += foundChar;
            mesg += '\'';
        }
        else {
           mesg += "0x"+Integer.toHexString(foundChar).toUpperCase();
        }
        return mesg;
    }
}
