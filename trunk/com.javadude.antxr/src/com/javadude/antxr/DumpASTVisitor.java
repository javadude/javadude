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

/** Simple class to dump the contents of an AST to the output */
public class DumpASTVisitor implements ASTVisitor {
    protected int level = 0;


    private void tabs() {
        for (int i = 0; i < level; i++) {
            System.out.print("   ");
        }
    }

    public void visit(AST node) {
        // Flatten this level of the tree if it has no children
        boolean flatten = /*true*/ false;
        AST node2;
        for (node2 = node; node2 != null; node2 = node2.getNextSibling()) {
            if (node2.getFirstChild() != null) {
                flatten = false;
                break;
            }
        }

        for (node2 = node; node2 != null; node2 = node2.getNextSibling()) {
            if (!flatten || node2 == node) {
                tabs();
            }
            if (node2.getText() == null) {
                System.out.print("nil");
            }
            else {
                System.out.print(node2.getText());
            }

            System.out.print(" [" + node2.getType() + "] ");

            if (flatten) {
                System.out.print(" ");
            }
            else {
                System.out.println("");
            }

            if (node2.getFirstChild() != null) {
                level++;
                visit(node2.getFirstChild());
                level--;
            }
        }

        if (flatten) {
            System.out.println("");
        }
    }
}
