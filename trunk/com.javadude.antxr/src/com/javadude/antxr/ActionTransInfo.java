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

/**
 * This class contains information about how an action
 * was translated (using the AST conversion rules).
 */
public class ActionTransInfo {
    public boolean assignToRoot = false;	// somebody did a "#rule = "
    public String refRuleRoot = null;		// somebody referenced #rule; string is translated var
    public String followSetName = null;		// somebody referenced $FOLLOW; string is the name of the lookahead set

    @Override
    public String toString() {
        return "assignToRoot:" + assignToRoot + ", refRuleRoot:"
				+ refRuleRoot + ", FOLLOW Set:" + followSetName;
    }
}
