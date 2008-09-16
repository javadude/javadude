/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield, based on ANTLR-Eclipse plugin
 *   by Torsten Juergeleit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors
 *    Torsten Juergeleit - original ANTLR Eclipse plugin
 *    Scott Stanchfield - modifications for ANTXR
 *******************************************************************************/
package com.javadude.antxr.eclipse.core.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * Logs output content written by a thread to specified listener.
 *
 * @author Torsten Juergeleit
 */
public class MonitoredOutputStream extends OutputStream {
    private IStreamListener fListener;

    private static final int MAX_SIZE = 1024;
    private Hashtable<Thread, ByteArrayOutputStream> fBuffers = new Hashtable<Thread, ByteArrayOutputStream>();
    private boolean fSkip = false;

    /**
     * Creates a new instance of this class.
     * @param aListener The listener
     */
    public MonitoredOutputStream(IStreamListener aListener) {
        fListener = aListener;
    }

    /**
     * Write the data to the buffer and flush the buffer, if a line separator
     * is detected.
     *
     * @param aByte  data to log
     * @see OutputStream#write(int)
     */
    public void write(int aByte) throws IOException {
        final byte c = (byte)aByte;
        if (c == '\n' || c == '\r') {
            if (!fSkip) {
                processBuffer();
            }
        } else {
            ByteArrayOutputStream buffer = getBuffer();
            buffer.write(aByte);
            if (buffer.size() > MonitoredOutputStream.MAX_SIZE) {
                processBuffer();
            }
        }
        fSkip = (c == '\r');
    }

    /**
     * Writes all remaining data from buffer.
     * @see OutputStream#flush()
     */
    public void flush() throws IOException {
        if (getBuffer().size() > 0) {
            processBuffer();
        }
    }

    /**
     * Writes all remaining data from buffer.
     * @see OutputStream#close()
     */
    public void close() throws IOException {
        flush();
    }

    /**
     * Converts the buffer to a string and sends it to listener.
     * @see IStreamListener#streamAppended(String, Object)
     */
    protected void processBuffer() {
        fListener.streamAppended(getBuffer().toString(), this);
        fBuffers.remove(Thread.currentThread());
    }

    private ByteArrayOutputStream getBuffer() {
        Thread current = Thread.currentThread();
        ByteArrayOutputStream buffer = fBuffers.get(current);
        if (buffer == null) {
            buffer = new ByteArrayOutputStream();
            fBuffers.put(current, buffer);
        }
        return buffer;
    }
}
