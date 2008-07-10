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
package com.javadude.antxr.preprocessor;

import java.io.IOException;
import java.io.PrintWriter;

import com.javadude.antxr.Tool;
import com.javadude.antxr.collections.impl.IndexedVector;

/** Stores header action, grammar preamble, file options, and
 *  list of grammars in the file
 */
public class GrammarFile {
    protected String fileName;
    protected String headerAction = "";
    protected IndexedVector<Option> options;
    protected IndexedVector<Grammar> grammars;
    protected boolean expanded = false;	// any grammars expanded within?
    protected Tool tool;

    public GrammarFile(Tool tool, String f) {
        fileName = f;
        grammars = new IndexedVector<Grammar>();
        this.tool = tool;
    }

    public void addGrammar(Grammar g) {
        grammars.appendElement(g.getName(), g);
    }

    public void generateExpandedFile() throws IOException {
        if (!expanded) {
            return;	// don't generate if nothing got expanded
        }
        String expandedFileName = nameForExpandedGrammarFile(this.getName());

        // create the new grammar file with expanded grammars
        PrintWriter expF = tool.openOutputFile(expandedFileName);
        expF.println(toString());
        expF.close();
    }

    public IndexedVector<Grammar> getGrammars() {
        return grammars;
    }

    public String getName() {
        return fileName;
    }

    public String nameForExpandedGrammarFile(String f) {
        if (expanded) {
            // strip path to original input, make expanded file in current dir
            return "expanded" + tool.fileMinusPath(f);
        }
        return f;
    }

    public void setExpanded(boolean exp) {
        expanded = exp;
    }

    public void addHeaderAction(String a) {
        headerAction += a + System.getProperty("line.separator");
    }

    public void setOptions(IndexedVector<Option> o) {
        options = o;
    }

    @Override
    public String toString() {
        String h = headerAction == null ? "" : headerAction;
        String o = options == null ? "" : Hierarchy.optionsToString(options);

        StringBuffer s = new StringBuffer(10000); s.append(h); s.append(o);
        for (Grammar g : grammars) {
            s.append(g.toString());
        }
        return s.toString();
    }
}
