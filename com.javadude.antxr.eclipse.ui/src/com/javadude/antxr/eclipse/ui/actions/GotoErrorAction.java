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
package com.javadude.antxr.eclipse.ui.actions;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.AntxrUIPluginImages;
import com.javadude.antxr.eclipse.ui.editor.AntxrEditor;

import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;

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