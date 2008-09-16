/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetUpdater;

public class RegExWorkingSetUpdater implements IWorkingSetUpdater {
	private static final Map<String, IWorkingSet> workingSets_ = Collections.synchronizedMap(new HashMap<String, IWorkingSet>());
	static {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				try {
					if (event.getDelta() == null) {
						return;
					}
					event.getDelta().accept(new IResourceDeltaVisitor() {
						@Override
						public boolean visit(IResourceDelta delta) throws CoreException {
							if (delta.getResource() instanceof IProject &&
								(delta.getKind() == IResourceDelta.ADDED ||
								 delta.getKind() == IResourceDelta.REMOVED ||
								 (delta.getFlags() & IResourceDelta.OPEN) != 0)) {
								IProject project = (IProject) delta.getResource();
								Set<IWorkingSet> setsContainingProject = setsContainingProject(project);
								addToWorkingSets(project, setsContainingProject);
								for (IWorkingSet workingSet : setsContainingProject) {
									removeFromWorkingSet(workingSet, project);
                                }
								return false;
							}
							return true;
						}
					});
				} catch (CoreException e) {
					Activator.getUtil().error(2, "Error walking delta", e);
				}
			}

			private void removeFromWorkingSet(IWorkingSet workingSet, IProject project) {
				IAdaptable[] elements = workingSet.getElements();
				if (elements.length == 0) {
					return;
				}
				List<IAdaptable> newElements = new ArrayList<IAdaptable>();
				boolean found = false;
				for (IAdaptable adaptable : elements) {
					if (adaptable != project) {
						newElements.add(adaptable);
					} else {
						found = true;
					}
				}
				if (!found) {
					return;
				}
				workingSet.setElements(newElements.toArray(new IAdaptable[newElements.size()]));
			}
			private Set<IWorkingSet> setsContainingProject(IProject project) {
				Set<IWorkingSet> workingSetsContainingProject = new HashSet<IWorkingSet>();
				for (IWorkingSet workingSet : RegExWorkingSetUpdater.workingSets_.values()) {
					IAdaptable[] elements = workingSet.getElements();
					for (IAdaptable element : elements) {
	                    if (element.equals(project)) {
	                    	workingSetsContainingProject.add(workingSet);
	                    }
	                }
				}
				return workingSetsContainingProject;
			}
			private void addToWorkingSets(IProject project, Set<IWorkingSet> setsContainingProject) {
				if (!project.isOpen()) {
					return;
				}
				for (Map.Entry<String, IWorkingSet> entry : RegExWorkingSetUpdater.workingSets_.entrySet()) {
					if (Pattern.matches(entry.getKey(), project.getName())) {
						// add project to working set
						IWorkingSet workingSet = entry.getValue();
						if (setsContainingProject != null) {
                            setsContainingProject.remove(workingSet);
                        }
						IAdaptable[] elements = workingSet.getElements();
						IAdaptable[] newElements = new IAdaptable[elements.length + 1];
						System.arraycopy(elements, 0, newElements, 0, elements.length);
						newElements[elements.length] = project;
						workingSet.setElements(newElements);
					}
				}
			}});

	}

	public RegExWorkingSetUpdater() {
		// assign working sets based on reading projects in workspace

	}

	private String regex(IWorkingSet workingSet) {
		String id = workingSet.getName();
		return id.substring("RegEx: ".length());
	}

	@Override
	public void add(IWorkingSet workingSet) {
		RegExWorkingSetUpdater.workingSets_.put(regex(workingSet), workingSet);
	}

	@Override
	public boolean contains(IWorkingSet workingSet) {
		return RegExWorkingSetUpdater.workingSets_.values().contains(workingSet);
	}

	@Override
	public void dispose() {
		RegExWorkingSetUpdater.workingSets_.clear();
	}

	@Override
	public boolean remove(IWorkingSet workingSet) {
		return RegExWorkingSetUpdater.workingSets_.remove(regex(workingSet)) != null;
	}
}
