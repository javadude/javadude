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

/**A Stream of characters fed to the lexer from a InputStream that can
 * be rewound via mark()/rewind() methods.
 * <p>
 * A dynamic array is used to buffer up all the input characters.  Normally,
 * "k" characters are stored in the buffer.  More characters may be stored during
 * guess mode (testing syntactic predicate), or when LT(i>k) is referenced.
 * Consumption of characters is deferred.  In other words, reading the next
 * character is not done by conume(), but deferred until needed by LA or LT.
 * <p>
 *
 * @see com.javadude.antxr.CharQueue
 */

import java.io.IOException;
import java.io.Reader;

// SAS: Move most functionality into InputBuffer -- just the file-specific
//      stuff is in here

public class CharBuffer extends InputBuffer {
    // char source
    transient Reader input;

    /** Create a character buffer */
    public CharBuffer(Reader input_) { // SAS: for proper text i/o
        super();
        input = input_;
    }

    /** Ensure that the character buffer is sufficiently full */
    @Override
    public void fill(int amount) throws CharStreamException {
        try {
            syncConsume();
            // Fill the buffer sufficiently to hold needed characters
            while (queue.nbrEntries < amount + markerOffset) {
                // Append the next character
                queue.append((char)input.read());
            }
        }
        catch (IOException io) {
            throw new CharStreamIOException(io);
        }
    }
}
