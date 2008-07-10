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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.texteditor.ITextEditor;

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
