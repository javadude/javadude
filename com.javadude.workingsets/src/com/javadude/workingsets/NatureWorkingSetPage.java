/*******************************************************************************
 *  Copyright 2008 Scott Stanchfield.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package com.javadude.workingsets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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

public class NatureWorkingSetPage extends WizardPage implements IWorkingSetPage {

	private IWorkingSet workingSet_;

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
	public void finish() {
		IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
		// TODO get list of all server core elements in workspace and init it
		List<IAdaptable> projects = new ArrayList<IAdaptable>();
		String natureId = natureIdText_.getText();
		try {
			for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
	            if (project.isOpen() && NatureWorkingSetUpdater.projectHasNature(project, natureId)) {
	            	projects.add(project);
	            }
			}
		} catch (CoreException e) {
			Activator.getUtil().error(42, "Error checking natures", e);
		}
		workingSet_ = workingSetManager.createWorkingSet("Nature: " + natureId, projects.toArray(new IAdaptable[projects.size()]));
		workingSet_.setLabel(workingSetLabelText_.getText());
	}

	@Override
	public IWorkingSet getSelection() {
		return workingSet_;
	}

	@Override
	public void setSelection(IWorkingSet workingSet) {
		workingSet_ = workingSet;
	}

	private Text natureIdText_ = null;
	private Text workingSetLabelText_ = null;

	private void dialogChanged() {
		String natureId = natureIdText_.getText();
		if ("".equals(natureId.trim())) {
			updateStatus("Nature must be specified");
			return;
		}
		String label = workingSetLabelText_.getText();
		if ("".equals(label.trim())) {
			updateStatus("Label must be specified");
			return;
		}
		
		// check if any projects have the nature
		Set<String> natures = new HashSet<String>();
		StringTokenizer stringTokenizer = new StringTokenizer(natureId, ", ");
		while (stringTokenizer.hasMoreTokens()) {
			String nature = stringTokenizer.nextToken();
			natures.add(nature);
		}
	projects:
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			try {
	            if (project.isOpen()) {
	            	for (Iterator<String> i = natures.iterator(); i.hasNext();) {
	                    String nature = i.next();
	                    if (project.hasNature(nature)) {
	                    	i.remove();
	                    	if (natures.isEmpty()) {
	                    		break projects;
	                    	}
	                    }
					}
	            }
            } catch (CoreException e) {
	            Activator.getUtil().error(22, "Error checking nature", e);
            }
		}
		if (!natures.isEmpty()) {
			String natureString = "";
			for (String nature : natures) {
	            natureString += "," + nature;
            }
			natureString = natureString.substring(1);
			if (natures.size() > 1) {
				updateStatus("Natures (" + natureString + ") are not used in any open projects");
			} else {
				updateStatus("Nature '" + natureString + "' is not used in any open projects");
			}
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
		label.setText("Nature:");

		natureIdText_ = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		natureIdText_.setLayoutData(gd);
		natureIdText_.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("Working Set Label:");
		
		workingSetLabelText_ = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		workingSetLabelText_.setLayoutData(gd);
		workingSetLabelText_.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

// TBD -- browse all available natures		
//		Button button = new Button(container, SWT.PUSH);
//		button.setText("Browse...");
//		button.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				handleBrowse();
//			}
//		});
		dialogChanged();
		setControl(container);
	}
}
