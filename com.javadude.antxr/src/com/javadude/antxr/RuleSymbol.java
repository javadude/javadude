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

import java.util.ArrayList;
import java.util.List;

class RuleSymbol extends GrammarSymbol {
    RuleBlock block;	// list of alternatives
    boolean defined;	// has the rule been defined yet?
    List<RuleRefElement> references;	// list of all nodes referencing this rule
    // not strictly needed by generic symbol table
    // but we will almost always analyze/gen code
    String access;	// access specifier for this rule
    String comment;	// A javadoc comment if any.

    public RuleSymbol(String r) {
        super(r);
        references = new ArrayList<RuleRefElement>();
    }

    public void addReference(RuleRefElement e) {
        references.add(e);
    }

    public RuleBlock getBlock() {
        return block;
    }

    public RuleRefElement getReference(int i) {
        return references.get(i);
    }

    public boolean isDefined() {
        return defined;
    }

    public int numReferences() {
        return references.size();
    }

    public void setBlock(RuleBlock rb) {
        block = rb;
    }

    public void setDefined() {
        defined = true;
    }
}
