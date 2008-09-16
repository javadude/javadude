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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultJavaCodeGeneratorPrintWriterManager implements JavaCodeGeneratorPrintWriterManager {
    private Grammar grammar;
    private PrintWriterWithSMAP smapOutput;
    private PrintWriter currentOutput;
    private Tool tool;
    private Map<String, Map<Integer, List<Integer>>> sourceMaps = new HashMap<String, Map<Integer,List<Integer>>>();
    private String currentFileName;

    public PrintWriter setupOutput(Tool tool, Grammar grammar) throws IOException {
        return setupOutput(tool, grammar, null);
    }

    public PrintWriter setupOutput(Tool tool, String fileName) throws IOException {
        return setupOutput(tool, null, fileName);
    }

    public PrintWriter setupOutput(Tool tool, Grammar grammar, String fileName) throws IOException {
        this.tool = tool;
        this.grammar = grammar;

        if (fileName == null) {
	        fileName = grammar.getClassName();
        }

            smapOutput = new PrintWriterWithSMAP(tool.openOutputFile(fileName + ".java"));
        currentFileName = fileName + ".java";
            currentOutput = smapOutput;
        return currentOutput;
    }

    public void startMapping(int sourceLine) {
            smapOutput.startMapping(sourceLine);
    }

    public void startSingleSourceLineMapping(int sourceLine) {
            smapOutput.startSingleSourceLineMapping(sourceLine);
    }

    public void endMapping() {
            smapOutput.endMapping();
    }

    public void finishOutput() throws IOException {
        currentOutput.close();
        if (grammar != null) {
            PrintWriter smapWriter;
            smapWriter = tool.openOutputFile(grammar.getClassName() + ".smap");
            String grammarFile = grammar.getFilename();
            grammarFile = grammarFile.replace('\\', '/');
            int lastSlash = grammarFile.lastIndexOf('/');
               if (lastSlash != -1) {
	            grammarFile = grammarFile.substring(lastSlash+1);
            }
            smapOutput.dump(smapWriter, grammar.getClassName(), grammarFile);
            sourceMaps.put(currentFileName, smapOutput.getSourceMap());
        }
        currentOutput = null;
    }

    public Map<String, Map<Integer, List<Integer>>> getSourceMaps() {
        return sourceMaps;
    }
}
