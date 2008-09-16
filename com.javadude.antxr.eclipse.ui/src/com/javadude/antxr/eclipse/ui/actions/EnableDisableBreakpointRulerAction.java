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


import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.texteditor.ITextEditor;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

/**
 * An action on the breakpoint ruler that enables a breakpoint
 */
public class EnableDisableBreakpointRulerAction extends AbstractBreakpointRulerAction {

	/**
	 * Creates the action to enable/disable breakpoints
	 * @param editor the editor
	 * @param info ruler details
	 */
	public EnableDisableBreakpointRulerAction(ITextEditor editor, IVerticalRulerInfo info) {
		setInfo(info);
		setTextEditor(editor);
		setText(AntxrUIPlugin.getMessage("EnableDisableBreakpointRulerAction.&Enable_Breakpoint_1")); //$NON-NLS-1$
	}

	/** {@inheritDoc} */
	public void run() {
		if (getBreakpoint() != null) {
			try {
				getBreakpoint().setEnabled(!getBreakpoint().isEnabled());
			} catch (CoreException e) {
				ErrorDialog.openError(getTextEditor().getEditorSite().getShell(), AntxrUIPlugin.getMessage("EnableDisableBreakpointRulerAction.Enabling/disabling_breakpoints_2"), AntxrUIPlugin.getMessage("EnableDisableBreakpointRulerAction.Exceptions_occurred_enabling_disabling_the_breakpoint_3"), e.getStatus()); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	/** {@inheritDoc} */
	public void update() {
		setBreakpoint(determineBreakpoint());
		if (getBreakpoint() == null) {
			setEnabled(false);
			return;
		}
		setEnabled(true);
		try {
			boolean enabled= getBreakpoint().isEnabled();
			setText(enabled ? AntxrUIPlugin.getMessage("EnableDisableBreakpointRulerAction.&Disable_Breakpoint_4") : AntxrUIPlugin.getMessage("EnableDisableBreakpointRulerAction.&Enable_Breakpoint_5")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (CoreException ce) {
			AntxrUIPlugin.log(ce);
		}
	}
}
