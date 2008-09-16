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

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * A ANTXR aware word detector.
 *
 * @author Torsten Juergeleit
 */
public class WordDetector implements IWordDetector {

	/**
     * Determines if the specified character is
     * permissible as the first character in a ANTXR identifier.
     * A character may start a ANTXR identifier if and only if
     * it is one of the following:
     * <ul>
     * <li>a letter
     * <li>a connecting punctuation character ("_")
     * </ul>
     *
     * @param aChar  the character to be tested.
     * @return true if the character may start a ANTXR identifier;
     *          false otherwise.
     * @see java.lang.Character#isLetter(char)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart
	 */
	public boolean isWordStart(char aChar) {
		return Character.isLetter(aChar) || aChar == '_';
	}

	/**
     * Determines if the specified character may be part of a ANTXR
     * identifier as other than the first character.
     * A character may be part of a ANTXR identifier if and only if
     * it is one of the following:
     * <ul>
     * <li>a letter
     * <li>a digit
     * <li>a connecting punctuation character ("_").
     * </ul>
     *
     * @param aChar  the character to be tested.
     * @return true if the character may be part of a ANTXR identifier;
     *          false otherwise.
     * @see java.lang.Character#isLetterOrDigit(char)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart
	 */
	public boolean isWordPart(char aChar) {
		return Character.isLetterOrDigit(aChar) || aChar == '_';
	}
}
