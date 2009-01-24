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
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IWorkingSetPage;

public class NatureWorkingSetPage extends WizardPage implements IWorkingSetPage {

	private IWorkingSet workingSet_;
	private String natureId_ = null;
	private Text workingSetLabelText_ = null;

	public NatureWorkingSetPage() {
		super("com.hcrest.classpath.natureWorkingSetPage", "Enter nature to display in this working set", Activator.getImageDescriptor("icons/logo16.gif"));
	}
	public NatureWorkingSetPage(String pageName) {
		super(pageName);
	}

	public NatureWorkingSetPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public IWorkingSet getSelection() {
		return workingSet_;
	}

	public void setSelection(IWorkingSet workingSet) {
		Assert.isNotNull(workingSet, "Working set must not be null");
		workingSet_ = workingSet;
		if (getContainer() != null && getShell() != null && workingSetLabelText_ != null) {
			natureId_ = workingSet.getName().substring(8);
			workingSetLabelText_.setText(workingSet.getLabel());
		}
	}

	@Override
	public void finish() {
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

		if (workingSet_ == null) {
			IWorkingSetManager workingSetManager= PlatformUI.getWorkbench().getWorkingSetManager();
			workingSet_= workingSetManager.createWorkingSet("Nature: " + natureId_, projects.toArray(new IAdaptable[projects.size()]));
			workingSet_.setId("com.javadude.workingsets.NatureWorkingSetPage");
		} else {
			workingSet_.setName("Nature: " + natureId_);
			workingSet_.setElements(projects.toArray(new IAdaptable[projects.size()]));
		}
		workingSet_.setLabel(workingSetLabelText_.getText());
	}


	private void dialogChanged() {
		if ("".equals(workingSetLabelText_.getText().trim())) {
			updateStatus("Label must be specified");
			return;
		}
		if (natureId_ == null) {
			updateStatus("At least one nature must be selected");
			return;
		}

		updateStatus(null);
	}
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText("Working Set Label:");

		workingSetLabelText_ = new Text(container, SWT.BORDER | SWT.SINGLE);
		if (workingSet_ != null) {
			workingSetLabelText_.setText(workingSet_.getLabel());
			natureId_ = workingSet_.getName().substring(8);
		}
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		workingSetLabelText_.setLayoutData(gd);
		workingSetLabelText_.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("Registered Natures:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
		int style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.CHECK;
		final CheckboxTableViewer table = CheckboxTableViewer.newCheckList(container, style);
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
		dialogChanged();
		setControl(container);
	}
}
