/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield, based on ANTLR-Eclipse plugin
 *   by Torsten Juergeleit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors
 *    Torsten Juergeleit - original ANTLR Eclipse plugin
 *    Scott Stanchfield - modifications for ANTXR
 *******************************************************************************/
package com.javadude.antxr.eclipse.ui.editor.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import com.javadude.antxr.eclipse.ui.AntxrColorProvider;
import com.javadude.antxr.eclipse.ui.IColorConstants;

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
        for (int i = 0; i < AntxrCodeScanner.ANTXR_KEYWORDS.length; i++) {
            wordRule.addWord(AntxrCodeScanner.ANTXR_KEYWORDS[i], keyword);
        }
        for (int i = 0; i < AntxrCodeScanner.JAVA_KEYWORDS.length; i++) {
            wordRule.addWord(AntxrCodeScanner.JAVA_KEYWORDS[i], keyword);
        }
        rules.add(wordRule);

        IRule[] result = new IRule[rules.size()];
        rules.toArray(result);
        setRules(result);
    }
}
