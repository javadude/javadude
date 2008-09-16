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
package com.javadude.antxr.scanner;

import java.util.LinkedList;
import java.util.List;

/**
 * A blocking queue, used as a transport access point between the SAX parser
 * and the XML parser. The SAX parser will place tokens into this queue; the
 * XML token stream will pull tokens out. The queue will force the token stream
 * to wait if there are no tokens ready, and the SAX parser to wait if it's
 * loading up too many tokens.
 *
 */
public class BlockingQueue <Type> {
    private List<Type> data = new LinkedList<Type>();
    private int maxQueuedElements;
    private int resumeQueuedElements;
    private Throwable enqueueException;
    private boolean useMaxMin;
    private boolean writerWaiting;
    private boolean readerWaiting;

    /**
     * Create an instance of BlockingQueue
     */
    public BlockingQueue() {
        this(-1,-1);
    }

    /**
     * Create an instance of BlockingQueue
     * @param maxQueuedElements
     * @param resumeQueuedElements
     */
    public BlockingQueue(int maxQueuedElements, int resumeQueuedElements) {
        super();
        this.maxQueuedElements = maxQueuedElements;
        this.resumeQueuedElements = resumeQueuedElements;
        if (maxQueuedElements != -1) {
            useMaxMin = true;
            if (maxQueuedElements <= resumeQueuedElements) {
	            throw new IllegalArgumentException("maxQueuedElements must be > resumeQueuedElements");
            }
        }
    }

    /**
     * Enqueue an element
     * @param o the element to enqueue
     */
    public synchronized void enqueue(Type o) {
        // if we've reached the max queued elements, wait to queue it
        if (useMaxMin && data.size() >= maxQueuedElements) {
            try {
                writerWaiting = true;
                if (readerWaiting) {
	                notifyAll(); // notify readers
                }
                wait(); // wait until enough objects have been read
            }
            catch (Throwable e) {
                enqueueException = e;
            }
            finally {
                writerWaiting = false;
            }
        }
        data.add(o);
        if (readerWaiting) {
	        notifyAll();
        }
    }

    /**
     * Dequeue an element
     * @return The next object in the queue
     * @throws InterruptedException If we were interrupted
     */
    public synchronized Type dequeue() throws InterruptedException {
        if (enqueueException != null) {
            Throwable toReport = enqueueException;
            enqueueException = null;
            throw new RuntimeException("Exception while enqueueing", toReport);
        }

        while(data.isEmpty()) {
            try {
                readerWaiting = true;
                wait();
            }
            finally {
                readerWaiting = false;
            }
        }
        Type o = data.remove(0);
        // if any sources are waiting and we've reached the "low water mark"
        //   that makes us resume, wake them
        if (writerWaiting && data.size() <= resumeQueuedElements) {
            notifyAll();
        }
        return o;
    }
}