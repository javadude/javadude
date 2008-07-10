/*******************************************************************************
 *  Copyright 2008 Scott Stanchfield.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
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
