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

public class ParseTreeRule extends ParseTree {
	private static final long serialVersionUID = 1L;

	public static final int INVALID_ALT = -1;

	protected String ruleName;
	protected int altNumber;  // unused until I modify antxr to record this

	public ParseTreeRule(String ruleName) {
		this(ruleName,ParseTreeRule.INVALID_ALT);
	}

	public ParseTreeRule(String ruleName, int altNumber) {
		this.ruleName = ruleName;
		this.altNumber = altNumber;
	}

	public String getRuleName() {
		return ruleName;
	}

	/** Do a step-first walk, building up a buffer of tokens until
	 *  you've reached a particular step and print out any rule subroots
	 *  insteads of descending.
	 */
	@Override
    protected int getLeftmostDerivation(StringBuffer buf, int step) {
		int numReplacements = 0;
		if ( step<=0 ) {
			buf.append(' ');
			buf.append(toString());
			return numReplacements;
		}
		AST child = getFirstChild();
		numReplacements = 1;
		// walk child printing them out, descending into at most one
		while ( child!=null ) {
			if ( numReplacements>=step || child instanceof ParseTreeToken ) {
				buf.append(' ');
				buf.append(child.toString());
			}
			else {
				// descend for at least one more derivation; update count
				int remainingReplacements = step-numReplacements;
				int n = ((ParseTree)child).getLeftmostDerivation(buf,
																 remainingReplacements);
				numReplacements += n;
			}
			child = child.getNextSibling();
		}
		return numReplacements;
	}

	@Override
    public String toString() {
		if ( altNumber==ParseTreeRule.INVALID_ALT ) {
            return '<'+ruleName+'>';
        }
		return '<'+ruleName+"["+altNumber+"]>";
	}
}
