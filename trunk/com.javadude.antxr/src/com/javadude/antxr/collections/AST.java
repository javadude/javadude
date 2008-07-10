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
