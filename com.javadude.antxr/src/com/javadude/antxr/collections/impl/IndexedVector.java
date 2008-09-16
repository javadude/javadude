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
package com.javadude.antxr.collections.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple indexed vector: a normal vector except that you must
 * specify a key when adding an element.  This allows fast lookup
 * and allows the order of specification to be preserved.
 */
public class IndexedVector <Type> implements Iterable<Type> {
    protected List<Type> elements;
    protected Map<String, Type> index;


    /**
     * IndexedVector constructor comment.
     */
    public IndexedVector() {
        elements = new ArrayList<Type>();
        index = new HashMap<String, Type>();
    }

    /**
     * IndexedVector constructor comment.
     * @param size int
     */
    public IndexedVector(int size) {
        elements = new ArrayList<Type>();
        index = new HashMap<String, Type>();
    }

    public synchronized void appendElement(String key, Type value) {
        elements.add(value);
        index.put(key, value);
    }

    /**
     * Returns the element at the specified index.
     * @param index the index of the desired element
     * @exception ArrayIndexOutOfBoundsException If an invalid
     * index was given.
     */
    public Object elementAt(int i) {
        return elements.get(i);
    }

    public Iterator<Type> iterator() {
        return elements.iterator();
    }

    public Object getElement(String key) {
        Object o = index.get(key);
        return o;
    }

    /** remove element referred to by key NOT value; return false if not found. */
    public synchronized boolean removeElement(Type key) {
        Object value = index.get(key);
        if (value == null) {
            return false;
        }
        index.remove(key);
        elements.remove(value);
        return false;
    }

    public int size() {
        return elements.size();
    }
}
