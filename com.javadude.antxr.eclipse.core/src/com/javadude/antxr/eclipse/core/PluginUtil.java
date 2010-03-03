/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.antxr.eclipse.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;

public class PluginUtil {
    private ILog log_;
    private String pluginName_;

    public PluginUtil(String pluginName, ILog log) {
        pluginName_ = pluginName;
        log_ = log;
    }

    /**
     * Log an error.
     * @param message the message
     */
    public void error(int code, String message) {
        error(code, message, null);
    }
    /**
     * Log a warning.
     * @param message the message
     */
    public void warning(int code, String message) {
        warning(code, message, null);
    }
    /**
     * Log an info message.
     * @param message the message
     */
    public void info(int code, String message) {
        info(code, message, null);
    }
    /**
     * Log an error.
     * @param message the message
     * @param throwable an optional exception
     */
    public void error(int code, final String message, Throwable throwable) {
        IStatus status = new Status(IStatus.ERROR, pluginName_, code, message, throwable);
        log_.log(status);
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                MessageDialog.openWarning(null, "Error: " + message, message);
            }});
    }
    /**
     * Log a warning.
     * @param message the message
     * @param throwable an optional exception
     */
    public void warning(int code, final String message, Throwable throwable) {
        IStatus status = new Status(IStatus.WARNING, pluginName_, code, message, throwable);
        log_.log(status);
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                MessageDialog.openWarning(null, "Warning: " + message, message);
            }});
    }
    /**
     * Log an info message.
     * @param message the message
     * @param throwable an optional exception
     */
    public void info(int code, String message, Throwable throwable) {
        IStatus status = new Status(IStatus.INFO, pluginName_, code, message, throwable);
        log_.log(status);
    }

    @SuppressWarnings("unchecked")
    public void toggleNature(String natureId, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            List<String> readOnlyProblemFiles = new ArrayList<String>();
            for (Iterator<Object> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
                Object element = it.next();
                IProject project = null;
                if (element instanceof IProject) {
                    project = (IProject) element;
                } else if (element instanceof IAdaptable) {
                    project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
                }
                if (project != null) {
                    toggleNature(natureId, project, readOnlyProblemFiles);
                }
            }
            if (!readOnlyProblemFiles.isEmpty()) {
                String message = "The following files are read-only or do not exist and their projects cannot be converted:";
                for (String readOnlyFile : readOnlyProblemFiles) {
                    message += '\n' + readOnlyFile;
                }
                error(1001, message);
            }
        }
    }

    private void toggleNature(String natureId, IProject project, List<String> readOnlyProblemFiles) {
        try {
            // check if the project's .classpath and .project files are writeable
            IFile file = project.getFile(".classpath");
            if (!file.exists() || file.isReadOnly()) {
                readOnlyProblemFiles.add(file.getLocation().toOSString());
            }
            file = project.getFile(".project");
            if (!file.exists() || file.isReadOnly()) {
                readOnlyProblemFiles.add(file.getLocation().toOSString());
            }

            IProjectDescription description = project.getDescription();
            String[] natures = description.getNatureIds();

            for (int i = 0; i < natures.length; ++i) {
                if (natureId.equals(natures[i])) {
                    // Remove the nature
                    String[] newNatures = new String[natures.length - 1];
                    System.arraycopy(natures, 0, newNatures, 0, i);
                    System.arraycopy(natures, i + 1, newNatures, i,
                            natures.length - i - 1);
                    description.setNatureIds(newNatures);
                    project.setDescription(description, null);
                    return;
                }
            }

            // Add the nature
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 1, natures.length);
            newNatures[0] = natureId;
            description.setNatureIds(newNatures);
            project.setDescription(description, null);
//          PlatformUI.getWorkbench().getDecoratorManager().update("com.hcrest.classpath.decorator");
        } catch (CoreException e) {
            // do nothing
        }
    }
    public boolean hasNature(Object resource, String natureId) {
        if (resource instanceof IJavaProject) {
            resource = ((IJavaProject) resource).getProject();
        }
        if (resource instanceof IProject) {
            return hasNature((IProject) resource, natureId);
        }
        return false;
    }

    public boolean hasNature(IProject aProject, String natureId) {
        boolean hasNature;
        try {
            hasNature = aProject.isOpen() && aProject.hasNature(natureId);
        } catch (CoreException e) {
            error(1002, "Error checking nature", e);
            hasNature = false;
        }
        return hasNature;
    }

}
