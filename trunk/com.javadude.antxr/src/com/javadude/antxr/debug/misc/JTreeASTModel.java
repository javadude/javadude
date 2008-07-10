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
