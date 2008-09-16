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
package com.javadude.antxr.collections;

import com.javadude.antxr.Token;

/** Minimal AST node interface used by ANTXR AST generation
 * and tree-walker.
 */
public interface AST {
    /** Add a (rightmost) child to this node */
    public void addChild(AST c);

    public boolean equals(AST t);

    public boolean equalsList(AST t);

    public boolean equalsListPartial(AST t);

    public boolean equalsTree(AST t);

    public boolean equalsTreePartial(AST t);

    public ASTEnumeration findAll(AST tree);

    public ASTEnumeration findAllPartial(AST subtree);

    /** Get the first child of this node; null if no children */
    public AST getFirstChild();

    /** Get	the next sibling in line after this one */
    public AST getNextSibling();

    /** Get the token text for this node */
    public String getText();

    /** Get the token type for this node */
    public int getType();

    /** @since 2.7.3 Need for error handling */
    public int getLine();

    /** @since 2.7.3 Need for error handling */
    public int getColumn();

	/** Get number of children of this node; if leaf, returns 0 */
	public int getNumberOfChildren();

    public void initialize(int t, String txt);

    public void initialize(AST t);

    public void initialize(Token t);

    /** Set the first child of a node. */
    public void setFirstChild(AST c);

    /** Set the next sibling after this one. */
    public void setNextSibling(AST n);

    /** Set the token text for this node */
    public void setText(String text);

    /** Set the token type for this node */
    public void setType(int ttype);

    public String toString();

    public String toStringList();

    public String toStringTree();
}
