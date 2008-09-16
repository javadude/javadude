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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;

import com.javadude.antxr.eclipse.core.parser.ISegment;
import com.javadude.antxr.eclipse.core.parser.Rule;
import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.editor.text.AntxrTextGuesser;

/**
 * Text hover support
 */
public class AntxrTextHover implements ITextHover {

	private AntxrEditor fEditor;

	/**
	 * Create an instance
	 * @param anEditor the editor to add hovers to
	 */
	public AntxrTextHover(AntxrEditor anEditor) {
		fEditor = anEditor;
	}

	public String getHoverInfo(ITextViewer aTextViewer, IRegion aRegion) {
		return getRule(aRegion);
	}

	public IRegion getHoverRegion(ITextViewer aTextViewer, int anOffset) {
		return new Region(anOffset, 0);
	}

	private String getRule(IRegion aRegion) {
		if (aRegion != null) {
			IDocument doc = fEditor.getDocument();
    		AntxrTextGuesser guess = new AntxrTextGuesser(doc,
    												aRegion.getOffset(), true);
			// Look through model for a rule with guessed text
			ISegment segment = fEditor.getSegment(guess.getText());
			if (segment != null && segment instanceof Rule) {
				int startLine = segment.getStartLine() - 1;
				int endLine = segment.getEndLine() - 1;
				if ((endLine - startLine) > 10) {
					endLine = startLine + 10;
				}
				try {
					int offset = doc.getLineOffset(startLine);
					return doc.get(offset,
									doc.getLineOffset(endLine) - offset +
											   doc.getLineLength(endLine) - 1);
				} catch(BadLocationException e) {
					AntxrUIPlugin.log(e);
				}
			}
		}
		return null;
	}
}
