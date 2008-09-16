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

class ExceptionSpec {
    // Non-null if this refers to a labeled rule
    // Use a token instead of a string to get the line information
    protected Token label;

    // List of ExceptionHandler (catch phrases)
    protected List<ExceptionHandler> handlers;


    public ExceptionSpec(Token label_) {
        label = label_;
        handlers = new ArrayList<ExceptionHandler>();
    }

    public void addHandler(ExceptionHandler handler) {
        handlers.add(handler);
    }
}
