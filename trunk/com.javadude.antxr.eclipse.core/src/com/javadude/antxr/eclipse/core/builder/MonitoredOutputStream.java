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
            if (buffer.size() > MAX_SIZE) {
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
