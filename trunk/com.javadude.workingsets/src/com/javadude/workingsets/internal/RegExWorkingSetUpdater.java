/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets.internal;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import com.javadude.workingsets.DynamicWorkingSetUpdater;

/**
 * An updater for regex working sets. This defines the working set it
 * 	and a filter that specifies which projects should be included.
 * @author Scott Stanchfield
 */
public class RegExWorkingSetUpdater extends DynamicWorkingSetUpdater {
	public RegExWorkingSetUpdater() {
		super("RegEx: ");
	}
	@Override protected boolean shouldInclude(IResource resource, String workingSetId) {
		if (!(resource instanceof IProject)) {
			return false;
		}
		return (Pattern.matches(workingSetId, ((IProject) resource).getName()));
	}
}
