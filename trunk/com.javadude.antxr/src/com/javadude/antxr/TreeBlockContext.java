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

/**The context needed to add root,child elements to a Tree.  There
 * is only one alternative (i.e., a list of children).  We subclass to
 * specialize. MakeGrammar.addElementToCurrentAlt will work correctly
 * now for either a block of alts or a Tree child list.
 *
 * The first time addAlternativeElement is called, it sets the root element
 * rather than adding it to one of the alternative lists.  Rather than have
 * the grammar duplicate the rules for grammar atoms etc... we use the same
 * grammar and same refToken behavior etc...  We have to special case somewhere
 * and here is where we do it.
 */
class TreeBlockContext extends BlockContext {
    protected boolean nextElementIsRoot = true;


    @Override
    public void addAlternativeElement(AlternativeElement e) {
        TreeElement tree = (TreeElement)block;
        if (nextElementIsRoot) {
            tree.root = (GrammarAtom)e;
            nextElementIsRoot = false;
        }
        else {
            super.addAlternativeElement(e);
        }
    }
}
