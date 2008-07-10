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
 *******************************************************************************/
package com.javadude.antxr.eclipse.ui.editor.text;

import java.util.ArrayList;
import java.util.List;

import com.javadude.antxr.eclipse.ui.AntxrColorProvider;
import com.javadude.antxr.eclipse.ui.IColorConstants;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

/**
 * An ANTXR and Java aware code scanner.
 */
public class AntxrCodeScanner extends RuleBasedScanner {
    /** ANTXR keywords we care about */
    public static final String[] ANTXR_KEYWORDS = new String[] {
                       "header", "options", "tokens", "returns", "exception" };


    /** Java keywords we care about */
    public static final String[] JAVA_KEYWORDS = new String[] {
            "abstract", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else",
            "extends", "false", "final", "finally", "float", "for", "goto",
            "if", "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "null", "package", "private", "protected",
            "public", "return", "short", "static", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "true",
            "try", "void", "volatile", "while" };

    /**
     * Create an instance of a code scanner for syntax highlighting
     * @param aColorProvider The color mapping
     */
    public AntxrCodeScanner(AntxrColorProvider aColorProvider) {

        IToken keyword = new Token(new TextAttribute(aColorProvider.getColor(
                                    IColorConstants.KEYWORD), null, SWT.BOLD));
        IToken other = new Token(new TextAttribute(aColorProvider.getColor(
                                                    IColorConstants.DEFAULT)));
        List<IRule> rules = new ArrayList<IRule>();

        // Add generic whitespace rule
        rules.add(new WhitespaceRule(new WhitespaceDetector()));

        // Add word rule for ANTXR keywords
        WordRule wordRule = new WordRule(new WordDetector(), other);
        for (int i = 0; i < ANTXR_KEYWORDS.length; i++) {
            wordRule.addWord(ANTXR_KEYWORDS[i], keyword);
        }
        for (int i = 0; i < JAVA_KEYWORDS.length; i++) {
            wordRule.addWord(JAVA_KEYWORDS[i], keyword);
        }
        rules.add(wordRule);

        IRule[] result = new IRule[rules.size()];
        rules.toArray(result);
        setRules(result);
    }
}
