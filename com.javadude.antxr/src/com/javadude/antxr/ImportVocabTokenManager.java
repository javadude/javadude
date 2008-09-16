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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/** Static implementation of the TokenManager, used for importVocab option  */
class ImportVocabTokenManager extends SimpleTokenManager implements Cloneable {
    private String filename;
    protected Grammar grammar;

    // FIXME: it would be nice if the path to the original grammar file was
    // also searched.
    ImportVocabTokenManager(Grammar grammar, String filename_, String name_, Tool tool_) {
        // initialize
        super(name_, tool_);

        this.grammar = grammar;
        this.filename = filename_;

        // Figure out exactly where the file lives.  Check $PWD first,
        // and then search in -o <output_dir>.
        //
        File grammarFile = new File(filename);

        if (!grammarFile.exists()) {
            grammarFile = new File(antxrTool.getOutputDirectory(), filename);

            if (!grammarFile.exists()) {
                antxrTool.fatalError("panic: Cannot find importVocab file '" + filename + "'");
            }
        }

        setReadOnly(true);

        // Read a file with lines of the form ID=number
        try {
            Reader fileIn = new BufferedReader(new FileReader(grammarFile));
            ANTXRTokdefLexer tokdefLexer = new ANTXRTokdefLexer(fileIn);
            ANTXRTokdefParser tokdefParser = new ANTXRTokdefParser(tokdefLexer);
            tokdefParser.setTool(antxrTool);
            tokdefParser.setFilename(filename);
            tokdefParser.file(this);
        }
        catch (FileNotFoundException fnf) {
            antxrTool.fatalError("panic: Cannot find importVocab file '" + filename + "'");
        }
        catch (RecognitionException ex) {
            antxrTool.fatalError("panic: Error parsing importVocab file '" + filename + "': " + ex.toString());
        }
        catch (TokenStreamException ex) {
            antxrTool.fatalError("panic: Error reading importVocab file '" + filename + "'");
        }
    }

    @Override
    public Object clone() {
        ImportVocabTokenManager tm;
        tm = (ImportVocabTokenManager)super.clone();
        tm.filename = this.filename;
        tm.grammar = this.grammar;
        return tm;
    }

    /** define a token. */
    @Override
    public void define(TokenSymbol ts) {
        super.define(ts);
    }

    /** define a token.  Intended for use only when reading the importVocab file. */
    public void define(String s, int ttype) {
        TokenSymbol ts = null;
        if (s.startsWith("\"")) {
            ts = new StringLiteralSymbol(s);
        }
        else {
            ts = new TokenSymbol(s);
        }
        ts.setTokenType(ttype);
        super.define(ts);
        maxToken = (ttype + 1) > maxToken ? (ttype + 1) : maxToken;	// record maximum token type
    }

    /** importVocab token manager is read-only if output would be same as input */
    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    /** Get the next unused token type. */
    @Override
    public int nextTokenType() {
        return super.nextTokenType();
    }
}
