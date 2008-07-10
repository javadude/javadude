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

import org.eclipse.jdt.debug.core.IJavaBreakpoint;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.texteditor.ITextEditor;


/**
 * @author scott
 *
 */
/**
 * Presents the standard properties dialog to configure
 * the attibutes of a Java Breakpoint from the ruler popup menu of a
 * text editor.
 */
public class JavaBreakpointPropertiesRulerAction extends AbstractBreakpointRulerAction {

    /**
     * Creates the action to enable/disable breakpoints
     * @param editor the editor
     * @param info ruler details
     */
    public JavaBreakpointPropertiesRulerAction(ITextEditor editor, IVerticalRulerInfo info) {
        setInfo(info);
        setTextEditor(editor);
        setText(AntxrUIPlugin.getMessage("JavaBreakpointPropertiesRulerAction.Breakpoint_&Properties_1")); //$NON-NLS-1$
    }

    /** {@inheritDoc} */
    public void run() {
        if (getBreakpoint() != null) {
            IShellProvider shellProvider = new IShellProvider() {
                public Shell getShell() {
                    return getTextEditor().getEditorSite().getShell();
                }
            };
            PropertyDialogAction action=
                new PropertyDialogAction(shellProvider, new ISelectionProvider() {
                    public void addSelectionChangedListener(ISelectionChangedListener listener) {
                        // nothing to do here
                    }
                    public ISelection getSelection() {
                        return new StructuredSelection(getBreakpoint());
                    }
                    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
                        // nothing to do here
                    }
                    public void setSelection(ISelection selection) {
                        // nothing to do here
                    }
                });
            action.run();
        }
    }

    /** {@inheritDoc} */
    public void update() {
        setBreakpoint(determineBreakpoint());
        if (getBreakpoint() == null || !(getBreakpoint() instanceof IJavaBreakpoint)) {
            setBreakpoint(null);
            setEnabled(false);
            return;
        }
        setEnabled(true);
    }
}
