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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// assumes one source file for now -- may need to change if ANTXR allows
//   file inclusion in the future
// TODO optimize the output using line ranges for input/output files
//      currently this writes one mapping per line
public class PrintWriterWithSMAP extends PrintWriter {
    private int currentOutputLine = 1;
    private int currentSourceLine = 0;
    private Map<Integer, List<Integer>> sourceMap = new HashMap<Integer, List<Integer>>();

    private boolean lastPrintCharacterWasCR = false;
    private boolean mapLines = false;
    private boolean mapSingleSourceLine = false;
    private boolean anythingWrittenSinceMapping = false;

    public PrintWriterWithSMAP(OutputStream out) {
        super(out);
    }
    public PrintWriterWithSMAP(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }
    public PrintWriterWithSMAP(Writer out) {
        super(out);
    }
    public PrintWriterWithSMAP(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public void startMapping(int sourceLine) {
        mapLines = true;
        if (sourceLine != JavaCodeGenerator.CONTINUE_LAST_MAPPING) {
            currentSourceLine = sourceLine;
        }
    }

    public void startSingleSourceLineMapping(int sourceLine) {
        mapSingleSourceLine = true;
        mapLines = true;
        if (sourceLine != JavaCodeGenerator.CONTINUE_LAST_MAPPING) {
            currentSourceLine = sourceLine;
        }
    }

    public void endMapping() {
        mapLine(false);
        mapLines = false;
        mapSingleSourceLine = false;
    }

    protected void mapLine(boolean incrementOutputLineCount) {
        if (mapLines && anythingWrittenSinceMapping) {
            Integer sourceLine = new Integer(currentSourceLine);
            Integer outputLine = new Integer(currentOutputLine);
            List<Integer> outputLines = sourceMap.get(sourceLine);
            if (outputLines == null) {
                outputLines = new ArrayList<Integer>();
                sourceMap.put(sourceLine,outputLines);
            }
            if (!outputLines.contains(outputLine)) {
                outputLines.add(outputLine);
            }
        }
        if (incrementOutputLineCount) {
            currentOutputLine++;
        }
        if (!mapSingleSourceLine) {
            currentSourceLine++;
        }
        anythingWrittenSinceMapping = false;
    }

    public void dump(PrintWriter smapWriter, String targetClassName, String grammarFile) {
        smapWriter.println("SMAP");
        smapWriter.println(targetClassName + ".java");
        smapWriter.println("G");
        smapWriter.println("*S G");
        smapWriter.println("*F");
        smapWriter.println("+ 0 " + grammarFile);
        smapWriter.println(grammarFile);
        smapWriter.println("*L");
        List<Integer> sortedSourceLines = new ArrayList<Integer>(sourceMap.keySet());
        Collections.sort(sortedSourceLines);
        for (Integer sourceLine : sortedSourceLines) {
            List<Integer> outputLines = sourceMap.get(sourceLine);
            for (Integer outputLine : outputLines) {
                smapWriter.println(sourceLine + ":" + outputLine);
            }
        }
        smapWriter.println("*E");
        smapWriter.close();
    }

    @Override
    public void write(char[] buf, int off, int len) {
        int stop = off+len;
        for(int i = off; i < stop; i++) {
            checkChar(buf[i]);
        }
        super.write(buf,off,len);
    }

    // after testing, may want to inline this
    public void checkChar(int c) {
        if (lastPrintCharacterWasCR && c != '\n') {
            mapLine(true);
        } else if (c == '\n') {
            mapLine(true);
        } else if (!Character.isWhitespace((char)c)) {
            anythingWrittenSinceMapping = true;
        }

        lastPrintCharacterWasCR = (c == '\r');
    }
    @Override
    public void write(int c) {
        checkChar(c);
        super.write(c);
    }
    @Override
    public void write(String s, int off, int len) {
        int stop = off+len;
        for(int i = off; i < stop; i++) {
            checkChar(s.charAt(i));
        }
        super.write(s,off,len);
    }

//  PrintWriter delegates write(char[]) to write(char[], int, int)
//  PrintWriter delegates write(String) to write(String, int, int)

    // dependent on current impl of PrintWriter, which directly
    //   dumps a newline sequence to the target file w/o going through
    //   the other write methods.
    @Override
    public void println() {
        mapLine(true);
        super.println();
        lastPrintCharacterWasCR = false;
    }
    public Map<Integer, List<Integer>> getSourceMap() {
        return sourceMap;
    }
}
