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

import java.util.Iterator;
import java.util.List;

import com.javadude.antxr.CommonToken;

/**
 * An ANTXR token that represents an XML Tag.
 */
public class XMLToken extends CommonToken {
    private List<Attribute> attributeList;
    // list of attributes -- shouldn't be many
    // should we have an option to optimize using hashmap of namespace?
    //   is it worth it?

    /**
     * Create an XML Token
     * @param t The token's id
     * @param txt The token's text
     */
    public XMLToken(int t, String txt) {
        super(t, txt);
    }

    /**
     * Create an XML Token
     * @param t The token's id
     * @param txt The token's text
     * @param attributeList The token's attributes
     */
    public XMLToken(int t, String txt, List<Attribute> attributeList) {
        super(t, txt);
        this.attributeList = attributeList;
    }


    /**
     * Fetch an attribute by namespace and name
     * @param namespace The attribute's namespace
     * @param localName The attribute's name
     * @return The attribute's value
     */
    public String getAttribute(String namespace, String localName) {
        for (Attribute attribute : attributeList) {
            if (attribute.getNamespace().equals(namespace) &&
                attribute.getLocalName().equals(localName)) {
	            return attribute.getValue();
            }
        }
        return null;
    }

    /**
     * Get an iterator of all attributes
     * @return an iterator of all attributes
     */
    public Iterator<Attribute> getAttributes() {
        // TODO should we make it immutable? is it worth it?
        return attributeList.iterator();
    }
}