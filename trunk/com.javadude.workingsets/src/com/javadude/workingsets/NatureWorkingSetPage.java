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
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNatureDescriptor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkingSet;

public class NatureWorkingSetPage extends BaseWorkingSetPage {
	private String natureId_ = null;

	public NatureWorkingSetPage() {
		super("com.hcrest.classpath.natureWorkingSetPage", "Enter nature to display in this working set", Activator.getImageDescriptor("icons/logo16.gif"));
	}
	public NatureWorkingSetPage(String pageName) {
		super(pageName);
	}
	public NatureWorkingSetPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override protected void createFields(Composite parent) {
		Label label = new Label(parent, SWT.NULL);
		label.setText("Registered Natures:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
		int style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.CHECK;
		final CheckboxTableViewer table = CheckboxTableViewer.newCheckList(parent, style);
		List<String> natures = new ArrayList<String>();
		for (IProjectNatureDescriptor nature : ResourcesPlugin.getWorkspace().getNatureDescriptors()) {
			natures.add(nature.getNatureId());
		}
		Collections.sort(natures);
		for (String nature : natures) {
			table.add(nature);
		}
		if (natureId_ != null) {
			String[] split = natureId_.split("[,\\s]");
			for (String nature : split) {
				table.setChecked(nature, true);
			}
		}
		table.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.addCheckStateListener(new ICheckStateListener() {
			@Override public void checkStateChanged(CheckStateChangedEvent event) {
				natureId_ = null;
				for (Object o : table.getCheckedElements()) {
					if (natureId_ == null)
						natureId_ = (String) o;
					else
						natureId_ += ',' + (String) o;
				}
				dialogChanged();
			} });
	}
	@Override protected List<IAdaptable> getMatchingProjects() {
		List<IAdaptable> projects = new ArrayList<IAdaptable>();
		try {
			for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
	            if (project.isOpen() && NatureWorkingSetUpdater.projectHasNature(project, natureId_)) {
	            	projects.add(project);
	            }
			}
		} catch (CoreException e) {
			Activator.getUtil().error(42, "Error checking natures", e);
		}
		return projects;
	}
	@Override protected String getWorkingSetName() { return "Nature: " + natureId_; }
	@Override protected String getWorkingSetId() { return "com.javadude.workingsets.NatureWorkingSetPage"; }
	@Override protected void initFields(IWorkingSet workingSet) {
		natureId_ = workingSet.getName().substring(8);
	}
	@Override protected boolean validate() {
		if (natureId_ == null) {
			updateStatus("At least one nature must be selected");
			return false;
		}
		return true;
	}
}
