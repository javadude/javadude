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

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordPatternRule;

/**
 * This scanner recognizes the ANTXR comments.
 */
public class PartitionScanner extends RuleBasedPartitionScanner {
    /** string type */
    public final static String STRING = "__antxr_string";
    /** single-line comment type */
    public final static String SINGLE_LINE_COMMENT = "__antxr_single_line_comment";
    /** multi-line comment type */
    public final static String MULTI_LINE_COMMENT = "__antxr_multi_line_comment";
    /** javadoc comment type */
    public final static String JAVA_DOC = "__java_doc";

    /**
     * types of partitions in an ANTXR grammar
     */
    public static final String[] PARTITION_TYPES = new String[] {
                            IDocument.DEFAULT_CONTENT_TYPE, PartitionScanner.STRING,
                            PartitionScanner.SINGLE_LINE_COMMENT, PartitionScanner.MULTI_LINE_COMMENT, PartitionScanner.JAVA_DOC };
    /**
     * Creates the partitioner and sets up the appropriate rules.
     */
    public PartitionScanner() {
        IToken string = new Token(PartitionScanner.STRING);
        IToken singleLineComment = new Token(PartitionScanner.SINGLE_LINE_COMMENT);
        IToken multiLineComment = new Token(PartitionScanner.MULTI_LINE_COMMENT);
        IToken javaDoc = new Token(PartitionScanner.JAVA_DOC);

        List<IRule> rules = new ArrayList<IRule>();

        // Add rule for strings and character constants.
        rules.add(new SingleLineRule("\"", "\"", string, '\\'));
        rules.add(new SingleLineRule("'", "'", string, '\\'));

        // Add special empty comment word rule
        rules.add(new WordPatternRule(new EmptyCommentDetector(), "/**/",
                                      null, multiLineComment));
        // Add rules for multi-line comments
        rules.add(new MultiLineRule("/**", "*/", javaDoc));
        rules.add(new MultiLineRule("/*", "*/", multiLineComment));

        // Add special empty comment word rules
        rules.add(new WordPatternRule(new EmptyCommentDetector(), "/**/",
                                      null, multiLineComment));
        rules.add(new WordPatternRule(new EmptyCommentDetector(), "/***/",
                                      null, javaDoc));
        // Add rule for single line comments
        rules.add(new EndOfLineRule("//", singleLineComment));

        IPredicateRule[] result = new IPredicateRule[rules.size()];
        rules.toArray(result);
        setPredicateRules(result);
    }
}
