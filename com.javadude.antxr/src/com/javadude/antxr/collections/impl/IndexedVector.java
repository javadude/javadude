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
