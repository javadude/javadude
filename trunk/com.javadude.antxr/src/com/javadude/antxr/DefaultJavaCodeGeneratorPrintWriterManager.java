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
