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

/**A list of alternatives */
public class AlternativeBlock extends AlternativeElement {
    protected String initAction = null;	// string for init action {...}
    protected List<Alternative> alternatives;	// Contains Alternatives

    protected String label;			// can label a looping block to break out of it.

    protected int alti, altj;		// which alts are being compared at the moment with
    // deterministic()?
    protected int analysisAlt;		// which alt are we computing look on?  Must be alti or altj

    protected boolean hasAnAction = false;	// does any alt have an action?
    protected boolean hasASynPred = false;	// does any alt have a syntactic predicate?

    protected int ID = 0;				// used to generate unique variables
    protected static int nblks;	// how many blocks have we allocated?
    boolean not = false;				// true if block is inverted.

    boolean greedy = true;			// Blocks are greedy by default
    boolean greedySet = false;		// but, if not explicitly greedy, warning might be generated

    protected boolean doAutoGen = true;	// false if no AST (or text) to be generated for block

    protected boolean warnWhenFollowAmbig = true; // warn when an empty path or exit path

    protected boolean generateAmbigWarnings = true;  // the general warning "shut-up" mechanism
    // conflicts with alt of subrule.
    // Turning this off will suppress stuff
    // like the if-then-else ambig.

    public AlternativeBlock(Grammar g) {
        super(g);
        alternatives = new ArrayList<Alternative>();
        this.not = false;
        AlternativeBlock.nblks++;
        ID = AlternativeBlock.nblks;
    }

    public AlternativeBlock(Grammar g, Token start, boolean not) {
        super(g, start);
        alternatives = new ArrayList<Alternative>();
//		this.line = start.getLine();
//		this.column = start.getColumn();
        this.not = not;
        AlternativeBlock.nblks++;
        ID = AlternativeBlock.nblks;
    }

    public void addAlternative(Alternative alt) {
        alternatives.add(alt);
    }

    @Override
    public void generate() {
        grammar.generator.gen(this);
    }

    public Alternative getAlternativeAt(int i) {
        return alternatives.get(i);
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    public boolean getAutoGen() {
        return doAutoGen;
    }

    public String getInitAction() {
        return initAction;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }

    public void prepareForAnalysis() {
        for (Alternative a : alternatives) {
            // deterministic() uses an alternative cache and sets lookahead depth
            a.cache = new Lookahead[grammar.maxk + 1];
            a.lookaheadDepth = GrammarAnalyzer.LOOKAHEAD_DEPTH_INIT;
        }
    }

    /**Walk the syntactic predicate and, for a rule ref R, remove
     * the ref from the list of FOLLOW references for R (stored
     * in the symbol table.
     */
    public void removeTrackingOfRuleRefs(Grammar g) {
        for (Alternative alt : alternatives) {
            AlternativeElement elem = alt.head;
            while (elem != null) {
                if (elem instanceof RuleRefElement) {
                    RuleRefElement rr = (RuleRefElement)elem;
                    RuleSymbol rs = (RuleSymbol)g.getSymbol(rr.targetRule);
                    if (rs == null) {
                        grammar.antxrTool.error("rule " + rr.targetRule + " referenced in (...)=>, but not defined");
                    }
                    else {
                        rs.references.remove(rr);
                    }
                }
                else if (elem instanceof AlternativeBlock) {// recurse into subrules
                    ((AlternativeBlock)elem).removeTrackingOfRuleRefs(g);
                }
                elem = elem.next;
            }
        }
    }

    public void setAlternatives(List<Alternative> v) {
        alternatives = v;
    }

    public void setAutoGen(boolean doAutoGen_) {
        doAutoGen = doAutoGen_;
    }

    public void setInitAction(String initAction_) {
        initAction = initAction_;
    }

    @Override
    public void setLabel(String label_) {
        label = label_;
    }

    public void setOption(Token key, Token value) {
        if (key.getText().equals("warnWhenFollowAmbig")) {
            if (value.getText().equals("true")) {
                warnWhenFollowAmbig = true;
            }
            else if (value.getText().equals("false")) {
                warnWhenFollowAmbig = false;
            }
            else {
                grammar.antxrTool.error("Value for warnWhenFollowAmbig must be true or false", grammar.getFilename(), key.getLine(), key.getColumn());
            }
        }
        else if (key.getText().equals("generateAmbigWarnings")) {
            if (value.getText().equals("true")) {
                generateAmbigWarnings = true;
            }
            else if (value.getText().equals("false")) {
                generateAmbigWarnings = false;
            }
            else {
                grammar.antxrTool.error("Value for generateAmbigWarnings must be true or false", grammar.getFilename(), key.getLine(), key.getColumn());
            }
        }
        else if (key.getText().equals("greedy")) {
            if (value.getText().equals("true")) {
                greedy = true;
                greedySet = true;
            }
            else if (value.getText().equals("false")) {
                greedy = false;
                greedySet = true;
            }
            else {
                grammar.antxrTool.error("Value for greedy must be true or false", grammar.getFilename(), key.getLine(), key.getColumn());
            }
        }
        else {
            grammar.antxrTool.error("Invalid subrule option: " + key.getText(), grammar.getFilename(), key.getLine(), key.getColumn());
        }
    }

    @Override
    public String toString() {
        String s = " (";
        if (initAction != null) {
            s += initAction;
        }
        for (int i = 0; i < alternatives.size(); i++) {
            Alternative alt = getAlternativeAt(i);
            Lookahead cache[] = alt.cache;
            int k = alt.lookaheadDepth;
            // dump lookahead set
            if (k == GrammarAnalyzer.LOOKAHEAD_DEPTH_INIT) {
                // nothing
            }
            else if (k == GrammarAnalyzer.NONDETERMINISTIC) {
                s += "{?}:";
            }
            else {
                s += " {";
                for (int j = 1; j <= k; j++) {
                    s += cache[j].toString(",", grammar.tokenManager.getVocabulary());
                    if (j < k && cache[j + 1] != null) {
                        s += ";";
                    }
                }
                s += "}:";
            }
            // dump alternative including pred (if any)
            AlternativeElement p = alt.head;
            String pred = alt.semPred;
            if (pred != null) {
                s += pred;
            }
            while (p != null) {
                s += p;
                p = p.next;
            }
            if (i < (alternatives.size() - 1)) {
                s += " |";
            }
        }
        s += " )";
        return s;
    }

}
