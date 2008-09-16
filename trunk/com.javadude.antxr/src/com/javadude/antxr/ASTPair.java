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

/** ASTPair:  utility class used for manipulating a pair of ASTs
 * representing the current AST root and current AST sibling.
 * This exists to compensate for the lack of pointers or 'var'
 * arguments in Java.
 */
public class ASTPair {
    public AST root;		// current root of tree
    public AST child;		// current child to which siblings are added

    /** Make sure that child is the last sibling */
    public final void advanceChildToEnd() {
        if (child != null) {
            while (child.getNextSibling() != null) {
                child = child.getNextSibling();
            }
        }
    }

    /** Copy an ASTPair.  Don't call it clone() because we want type-safety */
    public ASTPair copy() {
        ASTPair tmp = new ASTPair();
        tmp.root = root;
        tmp.child = child;
        return tmp;
    }

    @Override
    public String toString() {
        String r = root == null ? "null" : root.getText();
        String c = child == null ? "null" : child.getText();
        return "[" + r + "," + c + "]";
    }
}
