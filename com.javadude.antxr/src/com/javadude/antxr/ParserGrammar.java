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

import java.io.IOException;


/** Parser-specific grammar subclass */
class ParserGrammar extends Grammar {


    ParserGrammar(String className_, Tool tool_, String superClass) {
        super(className_, tool_, superClass);
    }

    /** Top-level call to generate the code for this grammar */
    @Override
    public void generate() throws IOException {
        generator.gen(this);
    }

    // Get name of class from which generated parser/lexer inherits
    @Override
    protected String getSuperClass() {
        // if debugging, choose the debugging version of the parser
        if (debuggingOutput) {
            return "debug.LLkDebuggingParser";
        }
        return "LLkParser";
    }

    /**Process command line arguments.
     * -trace			have all rules call traceIn/traceOut
     * -traceParser		have parser rules call traceIn/traceOut
     * -debug			generate debugging output for parser debugger
     */
    @Override
    public void processArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-trace")) {
                traceRules = true;
                antxrTool.setArgOK(i);
            }
            else if (args[i].equals("-traceParser")) {
                traceRules = true;
                antxrTool.setArgOK(i);
            }
            else if (args[i].equals("-debug")) {
                debuggingOutput = true;
                antxrTool.setArgOK(i);
            }
        }
    }

    /** Set parser options -- performs action on the following options:
     */
    @Override
    public boolean setOption(String key, Token value) {
        String s = value.getText();
        if (key.equals("buildAST")) {
            if (s.equals("true")) {
                buildAST = true;
            }
            else if (s.equals("false")) {
                buildAST = false;
            }
            else {
                antxrTool.error("buildAST option must be true or false", getFilename(), value.getLine(), value.getColumn());
            }
            return true;
        }
        if (key.equals("interactive")) {
            if (s.equals("true")) {
                interactive = true;
            }
            else if (s.equals("false")) {
                interactive = false;
            }
            else {
                antxrTool.error("interactive option must be true or false", getFilename(), value.getLine(), value.getColumn());
            }
            return true;
        }
        if (key.equals("ASTLabelType")) {
            super.setOption(key, value);
            return true;
        }
        if (key.equals("className")) {
            super.setOption(key, value);
            return true;
        }
        if (super.setOption(key, value)) {
            return true;
        }
        antxrTool.error("Invalid option: " + key, getFilename(), value.getLine(), value.getColumn());
        return false;
    }
}
