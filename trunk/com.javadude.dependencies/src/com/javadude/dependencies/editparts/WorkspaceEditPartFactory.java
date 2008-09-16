/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.dependencies.editparts;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.IWorkbenchPage;

import com.javadude.dependencies.DependenciesPlugin;
import com.javadude.dependencies.Dependency;


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
