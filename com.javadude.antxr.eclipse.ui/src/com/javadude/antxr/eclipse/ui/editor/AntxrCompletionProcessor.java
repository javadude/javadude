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
package com.javadude.antxr.eclipse.ui.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import com.javadude.antxr.eclipse.ui.editor.text.AntxrTextGuesser;

/**
 * Provides code completion support
 */
public class AntxrCompletionProcessor implements IContentAssistProcessor {

    private static final char[] AUTO_ACTIVATION_CHARS = new char[] {
                                                               '(', '|', ':' };
    private AntxrEditor fEditor;

    private static final Comparator<CompletionProposal> PROPOSAL_COMPARATOR = new Comparator<CompletionProposal>() {
        public int compare(CompletionProposal aProposal1, CompletionProposal aProposal2) {
            String text1 = aProposal1.getDisplayString();
            String text2 = aProposal2.getDisplayString();
            return text1.compareTo(text2);
        }

        public boolean equals(Object aProposal) {
            return false;
        }
    };

    /**
     * Create the instance
     * @param anEditor the editor we're completing for
     */
    public AntxrCompletionProcessor(AntxrEditor anEditor) {
        fEditor = anEditor;
    }

    /** {@inheritDoc} */
    public ICompletionProposal[] computeCompletionProposals(
                                           ITextViewer aViewer, int anOffset) {
        List<CompletionProposal> proposals = new ArrayList<CompletionProposal>();
        String prefix = new AntxrTextGuesser(aViewer.getDocument(), anOffset,
                                             false).getText();
        String[] rules = fEditor.getRules(prefix);
        for (int i = 0; i < rules.length; i++) {
            if (rules[i].startsWith(prefix)) {
                proposals.add(new CompletionProposal(rules[i],
                              anOffset - prefix.length(), prefix.length(),
                              rules[i].length(), null, rules[i], null, null));
            }
        }
        Collections.sort(proposals, AntxrCompletionProcessor.PROPOSAL_COMPARATOR);
        return proposals.toArray(new ICompletionProposal[proposals.size()]);
    }

    /** {@inheritDoc} */
    public IContextInformation[] computeContextInformation(ITextViewer viewer,
                                                          int documentOffset) {
        return null;
    }

    /** {@inheritDoc} */
    public char[] getCompletionProposalAutoActivationCharacters() {
        return AntxrCompletionProcessor.AUTO_ACTIVATION_CHARS;
    }

    /** {@inheritDoc} */
    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    /** {@inheritDoc} */
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }

    /** {@inheritDoc} */
    public String getErrorMessage() {
        return null;
    }
}
