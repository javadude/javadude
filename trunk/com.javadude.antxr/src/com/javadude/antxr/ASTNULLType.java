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
package com.javadude.antxr;

import com.javadude.antxr.collections.AST;
import com.javadude.antxr.collections.ASTEnumeration;

/** There is only one instance of this class **/
public class ASTNULLType implements AST {
    public void addChild(AST c) {
		// nothing
    }

    public boolean equals(AST t) {
        return false;
    }

    public boolean equalsList(AST t) {
        return false;
    }

    public boolean equalsListPartial(AST t) {
        return false;
    }

    public boolean equalsTree(AST t) {
        return false;
    }

    public boolean equalsTreePartial(AST t) {
        return false;
    }

    public ASTEnumeration findAll(AST tree) {
        return null;
    }

    public ASTEnumeration findAllPartial(AST subtree) {
        return null;
    }

    public AST getFirstChild() {
        return this;
    }

    public AST getNextSibling() {
        return this;
    }

    public String getText() {
        return "<ASTNULL>";
    }

    public int getType() {
        return Token.NULL_TREE_LOOKAHEAD;
    }

    public int getLine() {
        return 0;
    }

    public int getColumn() {
        return 0;
    }

	public int getNumberOfChildren() {
		return 0;
	}

    public void initialize(int t, String txt) {
		// nothing
    }

    public void initialize(AST t) {
		// nothing
    }

    public void initialize(Token t) {
		// nothing
    }

    public void setFirstChild(AST c) {
		// nothing
    }

    public void setNextSibling(AST n) {
		// nothing
    }

    public void setText(String text) {
		// nothing
    }

    public void setType(int ttype) {
		// nothing
    }

    @Override
    public String toString() {
        return getText();
    }

    public String toStringList() {
        return getText();
    }

    public String toStringTree() {
        return getText();
    }
}
