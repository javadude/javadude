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
