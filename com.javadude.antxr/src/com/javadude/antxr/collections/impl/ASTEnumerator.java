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

import java.util.Iterator;
import java.util.List;

import com.javadude.antxr.collections.AST;
import com.javadude.antxr.collections.ASTEnumeration;

public class ASTEnumerator implements ASTEnumeration {
    /** The list of root nodes for subtrees that match */
    Iterator<AST> nodes;
    int i = 0;


    public ASTEnumerator(List<AST> v) {
        nodes = v.iterator();
    }

    public boolean hasMoreNodes() {
        return nodes.hasNext();
    }

    public AST nextNode() {
        return nodes.next();
    }
}
