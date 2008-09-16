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

import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.viewers.ILabelProvider;

import com.javadude.antxr.eclipse.ui.AntxrColorProvider;
import com.javadude.antxr.eclipse.ui.editor.outline.AntxrOutlineLabelProvider;
import com.javadude.antxr.eclipse.ui.editor.text.AntxrCodeScanner;

/**
 * The EditorEnvironment maintains singletons used by the ANTXR editor.
 *
 * @author Torsten Juergeleit
 */
public class EditorEnvironment {

	private static AntxrColorProvider fColorProvider;
	private static ITokenScanner fCodeScanner;
	private static ILabelProvider fLabelProvider;

	private static int fRefCount = 0;

	/**
	 * A connection has occured - initialize the receiver if it is the first
	 * activation.
	 */
	public static void connect() {
		if (++EditorEnvironment.fRefCount == 1) {
			EditorEnvironment.fColorProvider = new AntxrColorProvider();
			EditorEnvironment.fCodeScanner = new AntxrCodeScanner(EditorEnvironment.fColorProvider);
			EditorEnvironment.fLabelProvider = new AntxrOutlineLabelProvider();
		}
	}

	/**
	 * A disconnection has occured - clear the receiver if it is the last
	 * deactivation.
	 */
	public static void disconnect() {
		if (--EditorEnvironment.fRefCount == 0) {
			EditorEnvironment.fColorProvider.dispose();
			EditorEnvironment.fColorProvider = null;
			EditorEnvironment.fCodeScanner = null;
			EditorEnvironment.fLabelProvider.dispose();
			EditorEnvironment.fLabelProvider = null;
		}
	}

	/**
	 * Returns the singleton color provider.
	 * @return the color provider
	 */
	public static AntxrColorProvider getColorProvider() {
		return EditorEnvironment.fColorProvider;
	}

	/**
	 * Returns the singleton scanner.
	 * @return the token scanner
	 */
	public static ITokenScanner getCodeScanner() {
		return EditorEnvironment.fCodeScanner;
	}

	/**
	 * Returns the singleton label provider.
	 * @return the label provider
	 */
	public static ILabelProvider getLabelProvider() {
		return EditorEnvironment.fLabelProvider;
	}
}
