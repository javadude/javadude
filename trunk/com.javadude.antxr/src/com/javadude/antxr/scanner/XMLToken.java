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