/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkingSet;

public class RegExWorkingSetUpdater extends BaseWorkingSetUpdater {
	@Override protected String getId(IWorkingSet workingSet) {
		String id = workingSet.getName();
		return id.substring("RegEx: ".length());
	}
	@Override protected boolean shouldInclude(IProject project, String workingSetId) {
		return (Pattern.matches(workingSetId, project.getName()));
	}
}
