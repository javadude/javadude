/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkingSet;

import com.javadude.workingsets.DynamicWorkingSetUpdater;

/**
 * An updater for regex working sets. This defines the working set it
 * 	and a filter that specifies which projects should be included.
 * @author Scott Stanchfield
 */
public class RegExWorkingSetUpdater extends DynamicWorkingSetUpdater {
	private static final Map<String, IWorkingSet> workingSets_ = Collections.synchronizedMap(new HashMap<String, IWorkingSet>());
	@Override protected String getId(IWorkingSet workingSet) {
		String id = workingSet.getName();
		return id.substring("RegEx: ".length());
	}
	@Override protected boolean shouldInclude(IProject project, String workingSetId) {
		return (Pattern.matches(workingSetId, project.getName()));
	}
	@Override
	protected Map<String, IWorkingSet> getAllWorkingSetsOfThisType() {
		// TODO Auto-generated method stub
		return workingSets_;
	}
}
