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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**A list of alternatives and info contained in
 * the rule definition.
 */
public class RuleBlock extends AlternativeBlock {
    protected String ruleName;
    protected String argAction = null;	// string for rule arguments [...]
    protected String throwsSpec = null;
    protected String returnAction = null;// string for rule return type(s) <...>
    protected RuleEndElement endNode;	// which node ends this rule?

    // Generate literal-testing code for lexer rule?
    protected boolean testLiterals = false;

    List<AlternativeElement> labeledElements;	// List of labeled elements found in this rule
    // This is a list of AlternativeElement (or subclass)

    protected boolean[] lock;	// for analysis; used to avoid infinite loops
    // 1..k
    protected Lookahead cache[];// Each rule can cache it's lookahead computation.

    // This cache contains an epsilon
    // imaginary token if the FOLLOW is required.  No
    // FOLLOW information is cached here.
    // The FIRST(rule) is stored in this cache; 1..k
    // This set includes FIRST of all alts.

    Map<String, ExceptionSpec> exceptionSpecs;		// table of String-to-ExceptionSpec.

    // grammar-settable options
    protected boolean defaultErrorHandler = true;
    protected String ignoreRule = null;

    /** Construct a named rule. */
    public RuleBlock(Grammar g, String r) {
        super(g);
        ruleName = r;
        labeledElements = new ArrayList<AlternativeElement>();
        cache = new Lookahead[g.maxk + 1];
        exceptionSpecs = new HashMap<String, ExceptionSpec>();
        setAutoGen(g instanceof ParserGrammar);
    }

    /** Construct a named rule with line number information */
    public RuleBlock(Grammar g, String r, int line, boolean doAutoGen_) {
        this(g, r);
        this.line = line;
        setAutoGen(doAutoGen_);
    }

    public void addExceptionSpec(ExceptionSpec ex) {
        if (findExceptionSpec(ex.label) != null) {
            if (ex.label != null) {
                grammar.antxrTool.error("Rule '" + ruleName + "' already has an exception handler for label: " + ex.label);
            }
            else {
                grammar.antxrTool.error("Rule '" + ruleName + "' already has an exception handler");
            }
        }
        else {
            exceptionSpecs.put((ex.label == null ? "" : ex.label.getText()), ex);
        }
    }

    public ExceptionSpec findExceptionSpec(Token theLabel) {
        return exceptionSpecs.get(theLabel == null ? "" : theLabel.getText());
    }

    public ExceptionSpec findExceptionSpec(String theLabel) {
        return exceptionSpecs.get(theLabel == null ? "" : theLabel);
    }

    @Override
    public void generate() {
        grammar.generator.gen(this);
    }

    public boolean getDefaultErrorHandler() {
        return defaultErrorHandler;
    }

    public RuleEndElement getEndElement() {
        return endNode;
    }

    public String getIgnoreRule() {
        return ignoreRule;
    }

    public String getRuleName() {
        return ruleName;
    }

    public boolean getTestLiterals() {
        return testLiterals;
    }

    public boolean isLexerAutoGenRule() {
        return ruleName.equals("nextToken");
    }

    @Override
    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }

    @Override
    public void prepareForAnalysis() {
        super.prepareForAnalysis();
        lock = new boolean[grammar.maxk + 1];
    }

    // rule option values
    public void setDefaultErrorHandler(boolean value) {
        defaultErrorHandler = value;
    }

    public void setEndElement(RuleEndElement re) {
        endNode = re;
    }

    @Override
    public void setOption(Token key, Token value) {
        if (key.getText().equals("defaultErrorHandler")) {
            if (value.getText().equals("true")) {
                defaultErrorHandler = true;
            }
            else if (value.getText().equals("false")) {
                defaultErrorHandler = false;
            }
            else {
                grammar.antxrTool.error("Value for defaultErrorHandler must be true or false", grammar.getFilename(), key.getLine(), key.getColumn());
            }
        }
        else if (key.getText().equals("testLiterals")) {
            if (!(grammar instanceof LexerGrammar)) {
                grammar.antxrTool.error("testLiterals option only valid for lexer rules", grammar.getFilename(), key.getLine(), key.getColumn());
            }
            else {
                if (value.getText().equals("true")) {
                    testLiterals = true;
                }
                else if (value.getText().equals("false")) {
                    testLiterals = false;
                }
                else {
                    grammar.antxrTool.error("Value for testLiterals must be true or false", grammar.getFilename(), key.getLine(), key.getColumn());
                }
            }
        }
        else if (key.getText().equals("ignore")) {
            if (!(grammar instanceof LexerGrammar)) {
                grammar.antxrTool.error("ignore option only valid for lexer rules", grammar.getFilename(), key.getLine(), key.getColumn());
            }
            else {
                ignoreRule = value.getText();
            }
        }
        else if (key.getText().equals("paraphrase")) {
            if (!(grammar instanceof LexerGrammar)) {
                grammar.antxrTool.error("paraphrase option only valid for lexer rules", grammar.getFilename(), key.getLine(), key.getColumn());
            }
            else {
                // find token def associated with this rule
                TokenSymbol ts = grammar.tokenManager.getTokenSymbol(ruleName);
                if (ts == null) {
                    grammar.antxrTool.fatalError("panic: cannot find token associated with rule " + ruleName);
                    return;
                }
                ts.setParaphrase(value.getText());
            }
        }
        else if (key.getText().equals("generateAmbigWarnings")) {
            if (value.getText().equals("true")) {
                generateAmbigWarnings = true;
            }
            else if (value.getText().equals("false")) {
                generateAmbigWarnings = false;
            }
            else {
                grammar.antxrTool.error("Value for generateAmbigWarnings must be true or false", grammar.getFilename(), key.getLine(), key.getColumn());
            }
        }

        else if ("xmlTag".equals(key.getText())) {
            // strip off quotes
            String text = value.getText();
            int begin = 0;
            int end = text.length();
            if (text.startsWith("\"")) {
                begin++;
            }
            if (text.startsWith("<")) {
                begin++;
            }
            if (text.endsWith("\"")) {
                end--;
            }
            if (text.endsWith(">")) {
                end--;
            }

            text = text.substring(begin,end);
            grammar.xmlRuleTagMap.put(this, resolveXMLName(grammar, text));
        }

        else {
            grammar.antxrTool.error("Invalid rule option: " + key.getText(), grammar.getFilename(), key.getLine(), key.getColumn());
        }
    }

    /**
     * Resolve an XML tag name by looking it up in the namespace mapping
     * @param grammar The grammar being compiled
     * @param tagName The xml tag name
     * @return The fully-qualified tagname with the prefix replaced
     */
    public String resolveXMLName(Grammar theGrammar, String tagName) {
        int colon = tagName.lastIndexOf(':');
        if (colon != -1 && colon != tagName.length()-1) {
            String prefix = tagName.substring(0,colon);
            String tag = tagName.substring(colon+1);
            String namespace = theGrammar.namespaceMap.get(prefix);
            if (namespace != null) {
                return namespace + ":" + tag;
            }
        }
        return tagName;
    }

    @Override
    public String toString() {
        String s = " FOLLOW={";
        Lookahead lookaheadCache[] = endNode.cache;
        int k = grammar.maxk;
        boolean allNull = true;
        for (int j = 1; j <= k; j++) {
            if (lookaheadCache[j] == null) {
                continue;
            }
            s += lookaheadCache[j].toString(",", grammar.tokenManager.getVocabulary());
            allNull = false;
            if (j < k && lookaheadCache[j + 1] != null) {
                s += ";";
            }
        }
        s += "}";
        if (allNull) {
            s = "";
        }
        return ruleName + ": " + super.toString() + " ;" + s;
    }
}
