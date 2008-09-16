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

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * Used to determine if a character is whitespace
 */
public class WhitespaceDetector implements IWhitespaceDetector {
	/** {@inheritDoc} */
	public boolean isWhitespace(char aChar) {
		return Character.isWhitespace(aChar);
	}
}
