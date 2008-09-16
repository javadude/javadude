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

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;


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
