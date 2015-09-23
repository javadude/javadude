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

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextSelectionNavigationLocation;

/**
 * The navigation location
 *
 */
public class AntxrNavigationLocation extends TextSelectionNavigationLocation {

	/**
	 * Create an instance
	 * @param anEditor the editor
	 * @param anInitialize should we initialize
	 */
	public AntxrNavigationLocation(ITextEditor anEditor, boolean anInitialize) {
		super(anEditor, anInitialize);
	}

	/**
	 * Redirect from MultiPageEditor to associated ITextEditor.
	 * @see org.eclipse.ui.NavigationLocation#getEditorPart()
	 */
	protected IEditorPart getEditorPart() {
		IEditorPart editor = super.getEditorPart();
		if (editor != null) {
			editor = editor.getAdapter(ITextEditor.class);
		}
		return editor;
	}
}
