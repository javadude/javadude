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

class JavaBlockFinishingInfo {
    String postscript;		// what to generate to terminate block
    boolean generatedSwitch;// did block finish with "default:" of switch?
    boolean generatedAnIf;

    /** When generating an if or switch, end-of-token lookahead sets
     *  will become the else or default clause, don't generate an
     *  error clause in this case.
     */
    boolean needAnErrorClause;


    public JavaBlockFinishingInfo() {
        postscript = null;
        generatedSwitch = false;
        needAnErrorClause = true;
    }

    public JavaBlockFinishingInfo(String ps, boolean genS, boolean generatedAnIf, boolean n) {
        postscript = ps;
        generatedSwitch = genS;
        this.generatedAnIf = generatedAnIf;
        needAnErrorClause = n;
    }
}
