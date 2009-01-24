/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
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

public abstract class BaseWorkingSetPage extends WizardPage implements IWorkingSetPage {
	private IWorkingSet workingSet_;
	private Text workingSetLabelText_ = null;

	public BaseWorkingSetPage(String pageName) {
		super(pageName);
	}

	public BaseWorkingSetPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public IWorkingSet getWorkingSet() { return workingSet_; }
	@Override
	public IWorkingSet getSelection() {
		return workingSet_;
	}

	protected abstract void initFields(IWorkingSet workingSet);
	public void setSelection(IWorkingSet workingSet) {
		Assert.isNotNull(workingSet, "Working set must not be null");
		workingSet_ = workingSet;
		if (getContainer() != null && getShell() != null && workingSetLabelText_ != null) {
			workingSetLabelText_.setText(workingSet.getLabel());
			initFields(workingSet);
		}
	}

	protected abstract List<IAdaptable> getMatchingProjects();
	protected abstract String getWorkingSetName();
	protected abstract String getWorkingSetId();
	@Override
	public void finish() {
		List<IAdaptable> projects = getMatchingProjects();

		if (workingSet_ == null) {
			IWorkingSetManager workingSetManager= PlatformUI.getWorkbench().getWorkingSetManager();
			workingSet_= workingSetManager.createWorkingSet(getWorkingSetName(), projects.toArray(new IAdaptable[projects.size()]));
			workingSet_.setId(getWorkingSetId());
		} else {
			workingSet_.setName(getWorkingSetName());
			workingSet_.setElements(projects.toArray(new IAdaptable[projects.size()]));
		}
		workingSet_.setLabel(workingSetLabelText_.getText());
	}

	protected abstract boolean validate();
	protected abstract void createFields(Composite parent);
	protected void dialogChanged() {
		if ("".equals(workingSetLabelText_.getText().trim())) {
			updateStatus("Label must be specified");
			return;
		}
		if (!validate())
			return;

		updateStatus(null);
	}
	protected void updateStatus(String message) {
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
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		workingSetLabelText_.setLayoutData(gd);
		workingSetLabelText_.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		createFields(container);
		if (workingSet_ != null) {
			workingSetLabelText_.setText(workingSet_.getLabel());
			initFields(workingSet_);
		}
		dialogChanged();
		setControl(container);
	}
}
