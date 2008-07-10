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
package com.javadude.dependencies.editparts;

import com.javadude.dependencies.DependenciesPlugin;
import com.javadude.dependencies.Dependency;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.IWorkbenchPage;


public class WorkspaceEditPartFactory implements EditPartFactory {
    private final IWorkbenchPage page;
    public WorkspaceEditPartFactory(IWorkbenchPage page) {
        this.page = page;
    }
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart newEditPart = null;
        if (model instanceof IWorkspaceRoot) {
            newEditPart = new WorkspaceRootEditPart();
        } else if (model instanceof IJavaProject) {
            newEditPart = new JavaProjectEditPart();
        } else if (model instanceof Dependency) {
            newEditPart = new DependencyEditPart(page);
        } else {
            DependenciesPlugin.error(242, "Unknown model type: " + model.getClass(), null);
        }
        if (newEditPart != null) {
            newEditPart.setModel(model);
        }
        return newEditPart;
    }
}
