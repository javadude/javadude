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
