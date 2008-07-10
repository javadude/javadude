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
import java.util.List;
import java.util.Map;

/**
 * Defines a strategy that can be used to manage the printwriter
 *   being used to write JavaCodeGenerator output
 *
 * TODO generalize so all code gens could use?
 */
public interface JavaCodeGeneratorPrintWriterManager {
    public PrintWriter setupOutput(Tool tool, Grammar grammar) throws IOException;
    public PrintWriter setupOutput(Tool tool, String fileName) throws IOException;
    public void startMapping(int sourceLine);
    public void startSingleSourceLineMapping(int sourceLine);
    public void endMapping();
    public void finishOutput() throws IOException;
    public Map<String, Map<Integer, List<Integer>>> getSourceMaps();
}
