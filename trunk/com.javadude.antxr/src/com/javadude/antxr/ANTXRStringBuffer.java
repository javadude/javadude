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

// Implementation of a StringBuffer-like object that does not have the
// unfortunate side-effect of creating Strings with very large buffers.

public class ANTXRStringBuffer {
    protected char[] buffer = null;
    protected int length = 0;		// length and also where to store next char


    public ANTXRStringBuffer() {
        buffer = new char[50];
    }

    public ANTXRStringBuffer(int n) {
        buffer = new char[n];
    }

    public final void append(char c) {
        // This would normally be  an "ensureCapacity" method, but inlined
        // here for speed.
        if (length >= buffer.length) {
            // Compute a new length that is at least double old length
            int newSize = buffer.length;
            while (length >= newSize) {
                newSize *= 2;
            }
            // Allocate new array and copy buffer
            char[] newBuffer = new char[newSize];
            for (int i = 0; i < length; i++) {
                newBuffer[i] = buffer[i];
            }
            buffer = newBuffer;
        }
        buffer[length] = c;
        length++;
    }

    public final void append(String s) {
        for (int i = 0; i < s.length(); i++) {
            append(s.charAt(i));
        }
    }

    public final char charAt(int index) {
        return buffer[index];
    }

    final public char[] getBuffer() {
        return buffer;
    }

    public final int length() {
        return length;
    }

    public final void setCharAt(int index, char ch) {
        buffer[index] = ch;
    }

    public final void setLength(int newLength) {
        if (newLength < length) {
            length = newLength;
        }
        else {
            while (newLength > length) {
                append('\0');
            }
        }
    }

    @Override
    public final String toString() {
        return new String(buffer, 0, length);
    }
}
