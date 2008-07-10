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
