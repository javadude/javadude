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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.javadude.antxr.eclipse.core.AntxrNature;
import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

/**
 * A popup menu action that defines toggling the ANTXR nature of a project
 */
public class ConversionAction implements IObjectActionDelegate {

    private ISelection selection_;

    public void run(IAction action) {
        AntxrUIPlugin.getUtil().toggleNature(AntxrNature.NATURE_ID, selection_);
    }

    public void selectionChanged(IAction action, ISelection selection) {
        selection_ = selection;
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // do nothing
    }
}
