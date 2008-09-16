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

class TreeSpecifierNode {
    private TreeSpecifierNode parent = null;
    private TreeSpecifierNode firstChild = null;
    private TreeSpecifierNode nextSibling = null;
    private Token tok;


    TreeSpecifierNode(Token tok_) {
        tok = tok_;
    }

    public TreeSpecifierNode getFirstChild() {
        return firstChild;
    }

    public TreeSpecifierNode getNextSibling() {
        return nextSibling;
    }

    // Accessors
    public TreeSpecifierNode getParent() {
        return parent;
    }

    public Token getToken() {
        return tok;
    }

    public void setFirstChild(TreeSpecifierNode child) {
        firstChild = child;
        child.parent = this;
    }

    // Structure-building
    public void setNextSibling(TreeSpecifierNode sibling) {
        nextSibling = sibling;
        sibling.parent = parent;
    }
}
