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

import java.lang.reflect.InvocationTargetException;

import com.javadude.antxr.eclipse.core.builder.AntxrBuilder;
import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * A popup menu action to compile a java grammar
 */
public class CompileAction implements IObjectActionDelegate {
    private IFile fGrammarFile;

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction anAction, IWorkbenchPart aTargetPart) {
        // nothing to do here...
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction anAction, ISelection aSelection) {
        IFile file = null;
        if (aSelection instanceof IStructuredSelection) {
            Object obj = ((IStructuredSelection)aSelection).getFirstElement();
            if (obj != null && obj instanceof IFile) {
                file = (IFile)obj;
            }
        }
        fGrammarFile = file;
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction anAction) {
        Assert.isNotNull(fGrammarFile);
        Shell shell = AntxrUIPlugin.getActiveWorkbenchShell();
        ProgressMonitorDialog pmd = new ProgressMonitorDialog(shell);
        try {
            pmd.run(true, false, new Operation(fGrammarFile));	// cancel button disabled
        } catch (InterruptedException e) {
            AntxrUIPlugin.log(e);
        } catch (InvocationTargetException e) {
            AntxrUIPlugin.log(e);
        }
    }

    private class Operation extends WorkspaceModifyOperation {
        IFile fFile;

        /**
         * Create an operation, holding a file
         * @param aFile the file to hold
         */
        public Operation(IFile aFile) {
            fFile = aFile;
        }

        /**
         * @see WorkspaceModifyOperation#execute(IProgressMonitor)
         */
        protected void execute(IProgressMonitor aMonitor) throws CoreException,
                                 InvocationTargetException, InterruptedException {
            new AntxrBuilder().compileFile(fFile, aMonitor);
        }
    }
}
