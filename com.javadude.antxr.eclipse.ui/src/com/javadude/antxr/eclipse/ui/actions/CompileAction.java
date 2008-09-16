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

import java.lang.reflect.InvocationTargetException;

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

import com.javadude.antxr.eclipse.core.builder.AntxrBuilder;
import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

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
