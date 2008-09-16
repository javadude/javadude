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
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

/**
 * Double click strategy aware of ANTXR identifier syntax rules.
 */
public class DoubleClickStrategy implements ITextDoubleClickStrategy {

	protected ITextViewer fText;
	protected int fPos;
	protected int fStartPos;
	protected int fEndPos;

	protected static final char[] BRACKETS = { '{', '}', '(', ')', '[', ']',
	    										  '"', '"' };

	/** {@inheritDoc} */
	public void doubleClicked(ITextViewer aText) {

		fPos = aText.getSelectedRange().x;

		if (fPos >= 0) {
			fText = aText;

			if (!selectBracketBlock()) {
				selectWord();
			}
		}
	}

	/**
	 * Select the area between the selected bracket and the closing bracket.
	 * @return true if successful
	 */
	 protected boolean selectBracketBlock() {
		if (matchBracketsAt()) {

			if (fStartPos == fEndPos) {
	            fText.setSelectedRange(fStartPos, 0);
            } else {
	            fText.setSelectedRange(fStartPos + 1, fEndPos - fStartPos - 1);
            }

			return true;
		}
		return false;
	}

	/**
	 * Select the word at the current selection.
	 */
	 protected void selectWord() {
		if (matchWord()) {

			if (fStartPos == fEndPos) {
	            fText.setSelectedRange(fStartPos, 0);
            } else {
	            fText.setSelectedRange(fStartPos + 1, fEndPos - fStartPos - 1);
            }
		}
	}

	/**
	 * Match the brackets at the current selection.
	 * @return true if successful, false otherwise.
	 */
	 protected boolean matchBracketsAt() {

		char prevChar, nextChar;

		int i;
		int bracketIndex1 = DoubleClickStrategy.BRACKETS.length;
		int bracketIndex2 = DoubleClickStrategy.BRACKETS.length;

		fStartPos = -1;
		fEndPos = -1;

		// get the chars preceding and following the start position
		try {
			IDocument doc = fText.getDocument();

			prevChar = doc.getChar(fPos - 1);
			nextChar = doc.getChar(fPos);

			// is the char either an open or close bracket?
			for (i = 0; i < DoubleClickStrategy.BRACKETS.length; i += 2) {
				if (prevChar == DoubleClickStrategy.BRACKETS[i]) {
					fStartPos = fPos - 1;
					bracketIndex1 = i;
				}
			}
			for (i = 1; i < DoubleClickStrategy.BRACKETS.length; i += + 2) {
				if (nextChar == DoubleClickStrategy.BRACKETS[i]) {
					fEndPos = fPos;
					bracketIndex2 = i;
				}
			}

			if (fStartPos > -1 && bracketIndex1 < bracketIndex2) {
				fEndPos = searchForClosingBracket(fStartPos, prevChar,
												  DoubleClickStrategy.BRACKETS[bracketIndex1 + 1], doc);
				if (fEndPos > -1) {
	                return true;
                }
				fStartPos= -1;
			} else if (fEndPos > -1) {
				fStartPos= searchForOpenBracket(fEndPos, DoubleClickStrategy.BRACKETS[bracketIndex2 - 1],
												nextChar, doc);
				if (fStartPos > -1) {
	                return true;
                }
				fEndPos= -1;
			}

		} catch (BadLocationException e) {
			AntxrUIPlugin.log(e);
		}
		return false;
	}

	/**
	 * Select the word at the current selection.
	 * @return true if successful, false otherwise.
	 */
	 protected boolean matchWord() {

		IDocument doc= fText.getDocument();

		try {

			int pos = fPos;
			char c;

			while (pos >= 0) {
				c = doc.getChar(pos);
				if (!isWordPart(c)) {
	                break;
                }
				--pos;
			}

			fStartPos = pos;

			pos = fPos;
			int length = doc.getLength();

			while (pos < length) {
				c = doc.getChar(pos);
				if (!isWordPart(c)) {
	                break;
                }
				++pos;
			}

			fEndPos= pos;

			return true;
		} catch (BadLocationException e) {
			AntxrUIPlugin.log(e);
		}
		return false;
	}

	protected boolean isWordPart(char aChar) {
		return Character.isLetterOrDigit(aChar) || aChar == '_';
	}


	/**
	 * Returns the position of the closing bracket after startPosition.
	 *
	 * @param aStartPosition  the beginning position
	 * @param anOpenBracket  the character that represents the open bracket
	 * @param aCloseBracket  the character that represents the close bracket
	 * @param aDocument  the document being searched
	 * @return the location of the closing bracket.
	 * @throws BadLocationException if the document position is invalid
	 */
	 protected int searchForClosingBracket(int aStartPosition, char anOpenBracket,
				 char aCloseBracket, IDocument aDocument) throws BadLocationException {
		int stack = 1;
		int closePosition = aStartPosition + 1;
		int length = aDocument.getLength();
		char nextChar;

		while (closePosition < length && stack > 0) {
			nextChar= aDocument.getChar(closePosition);
			if (nextChar == anOpenBracket && nextChar != aCloseBracket) {
	            stack++;
            } else if (nextChar == aCloseBracket) {
	            stack--;
            }
			closePosition++;
		}

		if (stack == 0) {
	        return closePosition - 1;
        }
		return -1;
	}

	/**
	 * Returns the position of the open bracket before startPosition.
	 *
	 * @param startPosition - the beginning position
	 * @param openBracket - the character that represents the open bracket
	 * @param closeBracket - the character that represents the close bracket
	 * @param document - the document being searched
	 * @return the location of the starting bracket.
	 * @throws BadLocationException if the document position is invalid
	 */
	 protected int searchForOpenBracket(int startPosition, char openBracket, char closeBracket, IDocument document) throws BadLocationException {
		int stack= 1;
		int openPos= startPosition - 1;
		char nextChar;

		while (openPos >= 0 && stack > 0) {
			nextChar= document.getChar(openPos);
			if (nextChar == closeBracket && nextChar != openBracket) {
	            stack++;
            } else if (nextChar == openBracket) {
	            stack--;
            }
			openPos--;
		}

		if (stack == 0) {
	        return openPos + 1;
        }
		return -1;
	}
}