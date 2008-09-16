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
package com.javadude.antxr.eclipse.ui.actions;

import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.AntxrUIPluginImages;
import com.javadude.antxr.eclipse.ui.editor.AntxrEditor;

/**
 * Action that moves us to next/previous errors in the grammar editor
 */
public class GotoErrorAction extends TextEditorAction {
	private boolean fIsForward;

	/**
	 * Create the action
	 * @param anIsForward ?
	 */
	public GotoErrorAction(boolean anIsForward) {
		super(AntxrUIPlugin.getDefault().getResourceBundle(), (anIsForward ?
						"Editor.NextError." : "Editor.PreviousError.") , null);
		setImageDescriptor(anIsForward ?
						   AntxrUIPluginImages.DESC_TOOL_GOTO_NEXT_ERROR :
						   AntxrUIPluginImages.DESC_TOOL_GOTO_PREV_ERROR);
		fIsForward = anIsForward;
	}

	/** {@inheritDoc} */
	public void run() {
		ITextEditor editor = getTextEditor();
		if (editor != null && editor instanceof AntxrEditor) {
			((AntxrEditor)editor).gotoError(fIsForward);
		}
	}

	/** {@inheritDoc} */
	public void update() {
		setEnabled(true);
	}
}