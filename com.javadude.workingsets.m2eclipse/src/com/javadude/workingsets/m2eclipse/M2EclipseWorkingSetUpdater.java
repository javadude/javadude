/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets.m2eclipse;

import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IWorkingSet;
import org.maven.ide.eclipse.MavenPlugin;
import org.maven.ide.eclipse.embedder.ArtifactKey;
import org.maven.ide.eclipse.project.IMavenProjectChangedListener;
import org.maven.ide.eclipse.project.IMavenProjectFacade;
import org.maven.ide.eclipse.project.MavenProjectChangedEvent;
import org.maven.ide.eclipse.project.MavenProjectManager;

import com.javadude.workingsets.DynamicWorkingSetUpdater;

/**
 * An updater for regex working sets. This defines the working set it
 * 	and a filter that specifies which projects should be included.
 * @author Scott Stanchfield
 */
public class M2EclipseWorkingSetUpdater extends DynamicWorkingSetUpdater {
	public M2EclipseWorkingSetUpdater() {
		super("Maven: ");
		MavenProjectManager projectManager = MavenPlugin.getDefault().getMavenProjectManager();
		projectManager.addMavenProjectChangedListener(new IMavenProjectChangedListener() {
			@Override public void mavenProjectChanged(MavenProjectChangedEvent[] events, IProgressMonitor arg1) {
				for (MavenProjectChangedEvent event : events) {
					IProject p = event.getMavenProject().getProject();
					Set<IWorkingSet> setsContainingProject = setsContainingProject(p);
					// add to appropriate working sets
					addToWorkingSets(p, setsContainingProject);
					for (IWorkingSet workingSet : setsContainingProject) {
						removeFromWorkingSet(workingSet, p);
					}
				}
			} });
	}
	@Override protected boolean shouldInclude(IResource resource, String workingSetId) {
		if (!(resource instanceof IProject)) {
			return false;
		}
		IProject project = (IProject) resource;
		String stuff = workingSetId.substring("group=".length());
		int i = stuff.indexOf('\n');
		String group = stuff.substring(0, i);
		String artifact = stuff.substring(i + 10);
		Pattern groupPattern = null;
		Pattern artifactPattern = null;
		if (!"".equals(group) && !".*".equals(group)) {
			groupPattern = Pattern.compile(group);
		}
		if (!"".equals(artifact) && !".*".equals(artifact)) {
			artifactPattern = Pattern.compile(artifact);
		}
		try {
			if (!project.isOpen() || !project.hasNature("org.maven.ide.eclipse.maven2Nature")) {
				return false;
			}
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
		MavenProjectManager projectManager = MavenPlugin.getDefault().getMavenProjectManager();
		IMavenProjectFacade facade = projectManager.create(project, null);
		if (facade!=null) {
			ArtifactKey artifactKey = facade.getArtifactKey();
			if (groupPattern != null && !groupPattern.matcher(artifactKey.getGroupId()).matches()) {
				return false;
			}
			if (artifactPattern != null && !artifactPattern.matcher(artifactKey.getArtifactId()).matches()) {
				return false;
			}
		}
		return true;
	}
}
