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

import com.javadude.antxr.eclipse.core.parser.ISegment;
import com.javadude.antxr.eclipse.core.parser.Rule;
import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.editor.text.AntxrTextGuesser;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;

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
