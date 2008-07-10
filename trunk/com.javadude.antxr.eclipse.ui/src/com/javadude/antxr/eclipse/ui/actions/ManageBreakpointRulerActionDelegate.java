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


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.AbstractRulerActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Create ruler breakpoint toggle actions
 */
public class ManageBreakpointRulerActionDelegate extends AbstractRulerActionDelegate {

	private ManageBreakpointRulerAction targetAction;
	private IEditorPart activeEditor;
	
	/** {@inheritDoc} */
	protected IAction createAction(ITextEditor editor, IVerticalRulerInfo rulerInfo) {
		targetAction = new ManageBreakpointRulerAction(rulerInfo, editor);
		return targetAction;
	}

	/** {@inheritDoc} */
	public void setActiveEditor(IAction callerAction, IEditorPart targetEditor) {
		if (activeEditor != null) {
			if (targetAction != null) {
				targetAction.dispose();
				targetAction = null;
			}
		}
		activeEditor = targetEditor;
		super.setActiveEditor(callerAction, targetEditor);
	}
	
	/** {@inheritDoc} */
	public void mouseDoubleClick(MouseEvent e) {
		targetAction.run();
	}
}
