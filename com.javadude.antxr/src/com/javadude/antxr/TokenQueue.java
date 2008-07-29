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

/** A private circular buffer object used by the token buffer */
class TokenQueue {
    /** Physical circular buffer of tokens */
    private Token[] buffer;
    /** buffer.length-1 for quick modulos */
    private int sizeLessOne;
    /** physical index of front token */
    private int offset;
    /** number of tokens in the queue */
    protected int nbrEntries;

    public TokenQueue(int minSize) {
        // Find first power of 2 >= to requested size
        int size;
        if ( minSize<0 ) {
            init(16); // pick some value for them
            return;
        }
        // check for overflow
        if ( minSize>=(Integer.MAX_VALUE/2) ) {
            init(Integer.MAX_VALUE); // wow that's big.
            return;
        }
        for (size = 2; size < minSize; size *= 2) {
            /* do nothing */
        }
        init(size);
    }

    /** Add token to end of the queue
     * @param tok The token to add
     */
    public final void append(Token tok) {
        if (nbrEntries == buffer.length) {
            expand();
        }
        buffer[(offset + nbrEntries) & sizeLessOne] = tok;
        nbrEntries++;
    }

    /** Fetch a token from the queue by index
     * @param idx The index of the token to fetch, where zero is the token at the front of the queue
     */
    public final Token elementAt(int idx) {
        return buffer[(offset + idx) & sizeLessOne];
    }

    /** Expand the token buffer by doubling its capacity */
    private final void expand() {
        Token[] newBuffer = new Token[buffer.length * 2];
        // Copy the contents to the new buffer
        // Note that this will store the first logical item in the
        // first physical array element.
        for (int i = 0; i < buffer.length; i++) {
            newBuffer[i] = elementAt(i);
        }
        // Re-initialize with new contents, keep old nbrEntries
        buffer = newBuffer;
        sizeLessOne = buffer.length - 1;
        offset = 0;
    }

    /** Initialize the queue.
     * @param size The initial size of the queue
     */
    private final void init(int size) {
        // Allocate buffer
        buffer = new Token[size];
        // Other initialization
        sizeLessOne = size - 1;
        offset = 0;
        nbrEntries = 0;
    }

    /** Clear the queue. Leaving the previous buffer alone.
     */
    public final void reset() {
        offset = 0;
        nbrEntries = 0;
    }

    /** Remove token from front of queue */
    public final void removeFirst() {
        offset = (offset + 1) & sizeLessOne;
        nbrEntries--;
    }
}