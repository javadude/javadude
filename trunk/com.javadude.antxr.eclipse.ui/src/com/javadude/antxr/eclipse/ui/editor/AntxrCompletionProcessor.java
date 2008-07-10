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
package com.javadude.antxr.eclipse.ui.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.javadude.antxr.eclipse.ui.editor.text.AntxrTextGuesser;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

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
        Collections.sort(proposals, PROPOSAL_COMPARATOR);
        return proposals.toArray(new ICompletionProposal[proposals.size()]);
    }

    /** {@inheritDoc} */
    public IContextInformation[] computeContextInformation(ITextViewer viewer,
                                                          int documentOffset) {
        return null;
    }

    /** {@inheritDoc} */
    public char[] getCompletionProposalAutoActivationCharacters() {
        return AUTO_ACTIVATION_CHARS;
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
