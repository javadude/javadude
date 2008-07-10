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

class CharRangeElement extends AlternativeElement {
    String label;
    protected char begin = 0;
    protected char end = 0;
    protected String beginText;
    protected String endText;


    public CharRangeElement(LexerGrammar g, Token t1, Token t2, int autoGenType) {
        super(g);
        begin = (char)ANTXRLexer.tokenTypeForCharLiteral(t1.getText());
        beginText = t1.getText();
        end = (char)ANTXRLexer.tokenTypeForCharLiteral(t2.getText());
        endText = t2.getText();
        line = t1.getLine();
        // track which characters are referenced in the grammar
        for (int i = begin; i <= end; i++) {
            g.charVocabulary.add(i);
        }
        this.autoGenType = autoGenType;
    }

    @Override
    public void generate() {
        grammar.generator.gen(this);
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }

    @Override
    public void setLabel(String label_) {
        label = label_;
    }

    @Override
    public String toString() {
        if (label != null) {
            return " " + label + ":" + beginText + ".." + endText;
        }
        return " " + beginText + ".." + endText;
    }
}
