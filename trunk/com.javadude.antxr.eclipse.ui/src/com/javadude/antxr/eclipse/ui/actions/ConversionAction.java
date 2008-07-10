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

import com.javadude.antxr.eclipse.core.AntxrNature;
import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

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
