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

import com.javadude.antxr.collections.AST;

public abstract class ParseTree extends BaseAST {

	private static final long serialVersionUID = 1L;
	/** Walk parse tree and return requested number of derivation steps.
	 *  If steps <= 0, return node text.  If steps == 1, return derivation
	 *  string at step.
	 */
	public String getLeftmostDerivationStep(int step) {
        if ( step<=0 ) {
			return toString();
		}
		StringBuffer buf = new StringBuffer(2000);
        getLeftmostDerivation(buf, step);
		return buf.toString();
	}

	public String getLeftmostDerivation(int maxSteps) {
		StringBuffer buf = new StringBuffer(2000);
		buf.append("    "+this.toString());
		buf.append("\n");
		for (int d=1; d<maxSteps; d++) {
			buf.append(" =>");
			buf.append(getLeftmostDerivationStep(d));
			buf.append("\n");
		}
		return buf.toString();
	}

	/** Get derivation and return how many you did (less than requested for
	 *  subtree roots.
	 */
	protected abstract int getLeftmostDerivation(StringBuffer buf, int step);

	// just satisfy BaseAST interface; unused as we manually create nodes

	@Override
    public void initialize(int i, String s) {
		// nothing
	}
	@Override
    public void initialize(AST ast) {
		// nothing
	}
	@Override
    public void initialize(Token token) {
		// nothing
	}
}
