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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * Guesses the start/end of an ANTXR rule from a given offset.
 */
public class AntxrTextGuesser {
	private String fText;
	private int fLine;

	/**
	 * Create an empty text guesser.
	 */
	public AntxrTextGuesser() {
		fText = "";
		fLine = -1;
	}

	/**
	 * Create a guesser
	 * @param aDocument The document being edited
	 * @param anOffset The position in the document
	 * @param aGuessEnd the last position for the guess
	 */
	public AntxrTextGuesser(IDocument aDocument, int anOffset,
							 boolean aGuessEnd) {
		try {

		 	// Guess start position
			int start = anOffset;
			while (start >= 1 && AntxrTextGuesser.isWordPart(aDocument.getChar(start - 1))) {
				start--;
			}

			// Guess end position
			int end = anOffset;
			if (aGuessEnd) {
				int len = aDocument.getLength() - 1;
				while (end < len && AntxrTextGuesser.isWordPart(aDocument.getChar(end))) {
					end++;
				}
			}
			fText = aDocument.get(start, end - start);
			fLine = aDocument.getLineOfOffset(start) + 1;
		} catch (BadLocationException e) {
			fText = "";
			fLine = -1;
		}
	}

	/**
	 * Get the text
	 * @return the text
	 */
	public String getText() {
		return fText;
	}

	/**
	 * Get the line
	 * @return the line
	 */
	public int getLine() {
		return fLine;
	}

	/**
     * Determines if the specified character may be part of a ANTXR rule
     * as other than the first character. A character may be part of
     * a ANTXR rule if and only if it is one of the following:
     * <ul>
     * <li>a letter (a..z, A..Z)
     * <li>a digit (0..9)
     * <li>a hyphen ("-")
     * <li>a connecting punctuation character ("_")
     * </ul>
     *
     * @param aChar  the character to be tested.
     * @return true if the character may be part of a ANTXR identifier;
     *          false otherwise.
     * @see java.lang.Character#isLetterOrDigit(char)
	 */
	private static final boolean isWordPart(char aChar) {
		return Character.isLetterOrDigit(aChar) || aChar == '-' ||
													aChar == '_';
	}

	/** {@inheritDoc} */
	public String toString() {
		return "text=" + fText + ", line=" + fLine;
	}
}
