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

/**
 * Superclass for common functionality on both workingset property pages.
 * @author Scott Stanchfield
 */
public abstract class DynamicWorkingSetPage extends WizardPage implements IWorkingSetPage {
	private IWorkingSet workingSet_;
	private Text workingSetLabelText_;
	private String workingSetId_;

	public DynamicWorkingSetPage(String workingSetId, String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		workingSetId_ = workingSetId;
	}

	public IWorkingSet getWorkingSet() { return workingSet_; }
	@Override public IWorkingSet getSelection() { return workingSet_; }

	// hook methods for subclasses to override
	/**
	 * Hook method (overridden by subclasses) that allows specific field initialization
	 * 	for each work set page.
	 * @param workingSet the working set containing existing data to populate fields
	 */
	protected abstract void initFields(IWorkingSet workingSet);

	/**
	 * Get a list of all items in the workspace that match the filters specified
	 * 	to define the current working set
	 * @return item list
	 */
	protected abstract List<IAdaptable> getMatchingItems();

	/**
	 * Get the display name of this working set
	 * @return the display name
	 */
	protected abstract String getWorkingSetName();

	/**
	 * Validate the current field values
	 * @return true if all fields are valid; false otherwise
	 */
	protected abstract boolean validate();

	/**
	 * Create the controls to display on the property page
	 * @param parent the composite into which the controls go
	 */
	protected abstract void createFields(Composite parent);

	public void setSelection(IWorkingSet workingSet) {
		Assert.isNotNull(workingSet, "Working set must not be null");
		workingSet_ = workingSet;
		// if the UI has already been set up, populate the fields
		if (getContainer() != null && getShell() != null && workingSetLabelText_ != null) {
			workingSetLabelText_.setText(workingSet.getLabel());
			initFields(workingSet);
		}
	}

	/**
	 * The user hit ok; update the working set with the new settings
	 */
	@Override public void finish() {
		List<IAdaptable> items = getMatchingItems();

		// if this is a new working set, create it and fill in the details
		if (workingSet_ == null) {
			IWorkingSetManager workingSetManager= PlatformUI.getWorkbench().getWorkingSetManager();
			workingSet_= workingSetManager.createWorkingSet(getWorkingSetName(), items.toArray(new IAdaptable[items.size()]));
			workingSet_.setId(workingSetId_);

		// if this is a working set update, create it and fill in the details
		} else {
			workingSet_.setName(getWorkingSetName());
			workingSet_.setElements(items.toArray(new IAdaptable[items.size()]));
		}

		// update the display text
		workingSet_.setLabel(workingSetLabelText_.getText());
	}

	/**
	 * Called whenever the data on the property page is changed
	 */
	protected void dialogChanged() {
		if ("".equals(workingSetLabelText_.getText().trim())) {
			updateStatus("Label must be specified");
			return;
		}
		if (!validate()) {
			return;
		}

		updateStatus(null);
	}


	/**
	 * Set the error status message
	 * @param message the error to display, or null for no error
	 */
	protected void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/**
	 * Create the controls on the page
	 */
	@Override public void createControl(Composite parent) {
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

		// call the subclass' createFields method to customize
		createFields(container);
		if (workingSet_ != null) {
			workingSetLabelText_.setText(workingSet_.getLabel());
			// if we're updating an existing working set,
			//   initialize the fields from the existing values
			initFields(workingSet_);
		}

		// prime the changes
		dialogChanged();
		setControl(container);
	}
}
