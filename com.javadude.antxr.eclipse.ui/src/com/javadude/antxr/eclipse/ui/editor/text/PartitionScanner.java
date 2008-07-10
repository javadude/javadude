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
                            IDocument.DEFAULT_CONTENT_TYPE, STRING,
                            SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT, JAVA_DOC };
    /**
     * Creates the partitioner and sets up the appropriate rules.
     */
    public PartitionScanner() {
        IToken string = new Token(STRING);
        IToken singleLineComment = new Token(SINGLE_LINE_COMMENT);
        IToken multiLineComment = new Token(MULTI_LINE_COMMENT);
        IToken javaDoc = new Token(JAVA_DOC);

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
