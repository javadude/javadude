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

class RuleRefElement extends AlternativeElement {
    protected String targetRule; // which rule is being called?
    protected String args = null;		 // were any args passed to rule?
    protected String idAssign = null;	 // is the return type assigned to a variable?
    protected String label;


    public RuleRefElement(Grammar g, Token t, int autoGenType_) {
        super(g, t, autoGenType_);
        targetRule = t.getText();
        //		if ( Character.isUpperCase(targetRule.charAt(0)) ) { // lexer rule?
        if (t.type == ANTXRTokenTypes.TOKEN_REF) { // lexer rule?
            targetRule = CodeGenerator.encodeLexerRuleName(targetRule);
        }
    }

//	public RuleRefElement(Grammar g, String t, int line, int autoGenType_) {
//		super(g, autoGenType_);
//		targetRule = t;
//		if ( Character.isUpperCase(targetRule.charAt(0)) ) { // lexer rule?
//			targetRule = CodeGenerator.lexerRuleName(targetRule);
//		}
//		this.line = line;
//	}

    @Override
    public void generate() {
        grammar.generator.gen(this);
    }

    public String getArgs() {
        return args;
    }

    public String getIdAssign() {
        return idAssign;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }

    public void setArgs(String a) {
        args = a;
    }

    public void setIdAssign(String id) {
        idAssign = id;
    }

    @Override
    public void setLabel(String label_) {
        label = label_;
    }

    @Override
    public String toString() {
        if (args != null) {
            return " " + targetRule + args;
        }
        return " " + targetRule;
    }
}
