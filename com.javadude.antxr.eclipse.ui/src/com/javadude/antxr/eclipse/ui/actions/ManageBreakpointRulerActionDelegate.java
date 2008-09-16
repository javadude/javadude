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
