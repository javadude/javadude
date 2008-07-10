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
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.PropertyDialogAction;

/**
 * Presents the standard properties dialog to configure
 * the attibutes of a Java Breakpoint.
 */
public class JavaBreakpointPropertiesAction implements IObjectActionDelegate {

    private IWorkbenchPart fPart;
    private IJavaBreakpoint fBreakpoint;

    /** {@inheritDoc} */
    public void run(IAction action) {
        IShellProvider shellProvider = new IShellProvider() {
            public Shell getShell() {
                return AntxrUIPlugin.getActiveWorkbenchShell();
            }
        };
        PropertyDialogAction propertyAction=
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
        propertyAction.run();
    }

    /** {@inheritDoc} */
    public void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection ss= (IStructuredSelection)selection;
            if (ss.isEmpty() || ss.size() > 1) {
                return;
            }
            Object element= ss.getFirstElement();
            if (element instanceof IJavaBreakpoint) {
                setBreakpoint((IJavaBreakpoint)element);
            }
        }
    }

    protected IWorkbenchPart getActivePart() {
        return fPart;
    }

    protected void setActivePart(IWorkbenchPart part) {
        fPart = part;
    }

    protected IJavaBreakpoint getBreakpoint() {
        return fBreakpoint;
    }

    /**
     * Set the breakpoint that we're managing
     * @param breakpoint the breakpoint
     */
    public void setBreakpoint(IJavaBreakpoint breakpoint) {
        fBreakpoint = breakpoint;
    }
    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        setActivePart(targetPart);
    }
}
