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
// SAS: added this class to handle Binary input w/ FileInputStream

import java.io.IOException;
import java.io.InputStream;

public class ByteBuffer extends InputBuffer {

    // char source
    transient InputStream input;


    /** Create a character buffer */
    public ByteBuffer(InputStream input_) {
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
