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

/** A GrammarAtom is either a token ref, a character ref, or string.
 * The analysis doesn't care.
 */
abstract class GrammarAtom extends AlternativeElement {
    protected String label;
    protected String atomText;
    protected int tokenType = Token.INVALID_TYPE;
    protected boolean not = false;	// ~T or ~'c' or ~"foo"
    /** Set to type of AST node to create during parse.  Defaults to what is
     *  set in the TokenSymbol.
     */
    protected String ASTNodeType = null;

    public GrammarAtom(Grammar g, Token t, int autoGenType) {
        super(g, t, autoGenType);
        atomText = t.getText();
    }

    @Override
    public String getLabel() {
        return label;
    }

    public String getText() {
        return atomText;
    }

    public int getType() {
        return tokenType;
    }

    @Override
    public void setLabel(String label_) {
        label = label_;
    }

    public String getASTNodeType() {
        return ASTNodeType;
    }

    public void setASTNodeType(String type) {
        ASTNodeType = type;
    }

    public void setOption(Token option, Token value) {
        if (option.getText().equals("AST")) {
            setASTNodeType(value.getText());
        }
        else {
            grammar.antxrTool.error("Invalid element option:" + option.getText(),
                               grammar.getFilename(), option.getLine(), option.getColumn());
        }
    }

    @Override
    public String toString() {
        String s = " ";
        if (label != null) {
            s += label + ":";
        }
        if (not) {
            s += "~";
        }
        return s + atomText;
    }
}
