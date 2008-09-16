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

import com.javadude.antxr.collections.AST;

public class ASTIterator {
    protected AST cursor = null;
    protected AST original = null;


    public ASTIterator(AST t) {
        original = cursor = t;
    }

    /** Is 'sub' a subtree of 't' beginning at the root? */
    public boolean isSubtree(AST t, AST sub) {
        AST sibling;

        // the empty tree is always a subset of any tree.
        if (sub == null) {
            return true;
        }

        // if the tree is empty, return true if the subtree template is too.
        if (t == null) {
            return false;
        }

        // Otherwise, start walking sibling lists.  First mismatch, return false.
        for (sibling = t;
             sibling != null && sub != null;
             sibling = sibling.getNextSibling(), sub = sub.getNextSibling()) {
            // as a quick optimization, check roots first.
            if (sibling.getType() != sub.getType()) {
	            return false;
            }
            // if roots match, do full match test on children.
            if (sibling.getFirstChild() != null) {
                if (!isSubtree(sibling.getFirstChild(), sub.getFirstChild())) {
	                return false;
                }
            }
        }
        return true;
    }

    /** Find the next subtree with structure and token types equal to
     * those of 'template'.
     */
    public AST next(AST template) {
        AST t = null;

        if (cursor == null) {	// do nothing if no tree to work on
            return null;
        }

        // Start walking sibling list looking for subtree matches.
        for (; cursor != null; cursor = cursor.getNextSibling()) {
            // as a quick optimization, check roots first.
            if (cursor.getType() == template.getType()) {
                // if roots match, do full match test on children.
                if (cursor.getFirstChild() != null) {
                    if (isSubtree(cursor.getFirstChild(), template.getFirstChild())) {
                        return cursor;
                    }
                }
            }
        }
        return t;
    }
}
