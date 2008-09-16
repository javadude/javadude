/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.dependencies.actions;

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.javadude.dependencies.DependenciesPlugin;

public class DOTReportAction implements IWorkbenchWindowActionDelegate {
	public DOTReportAction() {
		// do nothing
	}
	private String fixName(String name) {
		return name.replace('.', '_');
	}
	public void run(IAction action) {
		// create DOT definition
		String NL = System.getProperty("line.separator");
		String dot = "digraph dependencies {" + NL;
		try {
			for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
	            if (project.isOpen() && project.hasNature(JavaCore.NATURE_ID)) {
	            	IJavaProject javaProject = JavaCore.create(project);
	            	for (IClasspathEntry entry : javaProject.getRawClasspath()) {
	            		if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
	            			dot += '\t' + fixName(project.getName()) + " -> " + fixName(entry.getPath().lastSegment()) + ';' + NL;
	            		}
	            	}
	            }
			}
			dot += "}";
			try {
	            FileWriter fileWriter = new FileWriter("/dependencies.dot");
	            fileWriter.write(dot);
	            fileWriter.close();
            } catch (IOException e) {
            	DependenciesPlugin.error(111, "Error writing dot file /dependencies.dot", e);
            }
		} catch (CoreException e) {
			DependenciesPlugin.error(222, "Error determining dependencies", e);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// do nothing
	}
	public void dispose() {
		// do nothing
	}
	public void init(IWorkbenchWindow window) {
		// do nothing
	}
}