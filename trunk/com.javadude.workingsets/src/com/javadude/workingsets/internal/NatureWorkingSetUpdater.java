/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets.internal;

import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.javadude.workingsets.DynamicWorkingSetUpdater;

/**
 * An updater for nature working sets. This defines the working set id
 * 	and a filter that specifies which projects should be included.
 * @author Scott Stanchfield
 */
public class NatureWorkingSetUpdater extends DynamicWorkingSetUpdater {
	public NatureWorkingSetUpdater() {
		super("Nature: ");
	}

	public static boolean projectHasNature(IProject project, String natureList) {
		StringTokenizer stringTokenizer = new StringTokenizer(natureList, ", ");
		while (stringTokenizer.hasMoreTokens()) {
			String nature = stringTokenizer.nextToken();
			try {
				if (project.hasNature(nature)) {
					return true;
				}
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}

	@Override protected boolean shouldInclude(IResource resource, String workingSetId) {
		if (!(resource instanceof IProject)) {
			return false;
		}
		IProject project = (IProject) resource;
		return projectHasNature(project, workingSetId);
	}
}
