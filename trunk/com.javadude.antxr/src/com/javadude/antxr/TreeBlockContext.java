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
