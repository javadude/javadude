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
package com.javadude.antxr.debug.misc;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.javadude.antxr.collections.AST;

public class JTreeASTModel implements TreeModel {

    AST root = null;

    public JTreeASTModel(AST t) {
        if (t == null) {
            throw new IllegalArgumentException("root is null");
        }
        root = t;
    }

    public void addTreeModelListener(TreeModelListener l) {
    	/* do nothing */
    }

    public Object getChild(Object parent, int index) {
        if (parent == null) {
            return null;
        }
        AST p = (AST)parent;
        AST c = p.getFirstChild();
        if (c == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        int i = 0;
        while (c != null && i < index) {
            c = c.getNextSibling();
            i++;
        }
        return c;
    }

    public int getChildCount(Object parent) {
        if (parent == null) {
            throw new IllegalArgumentException("root is null");
        }
        AST p = (AST)parent;
        AST c = p.getFirstChild();
        int i = 0;
        while (c != null) {
            c = c.getNextSibling();
            i++;
        }
        return i;
    }

    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) {
            throw new IllegalArgumentException("root or child is null");
        }
        AST p = (AST)parent;
        AST c = p.getFirstChild();
        if (c == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        int i = 0;
        while (c != null && c != child) {
            c = c.getNextSibling();
            i++;
        }
        if (c == child) {
            return i;
        }
        throw new java.util.NoSuchElementException("node is not a child");
    }

    public Object getRoot() {
        return root;
    }

    public boolean isLeaf(Object node) {
        if (node == null) {
            throw new IllegalArgumentException("node is null");
        }
        AST t = (AST)node;
        return t.getFirstChild() == null;
    }

    public void removeTreeModelListener(TreeModelListener l) {
    	/* do nothing */
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("heh, who is calling this mystery method?");
    }
}
