/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.FindReplaceDocumentAdapterContentProposalProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

/**
 * A property page for regular-expression-based dynamic working sets.
 * This page displays a field for the regular expression (with
 * 	content assist, woohoo!) to filter projects for inclusion in the working set
 * @author Scott Stanchfield
 */
public class RegExWorkingSetPage extends DynamicWorkingSetPage {
	private Text regexText_ = null;
	private org.eclipse.swt.widgets.List matches_;

	public RegExWorkingSetPage() {
		this("com.hcrest.classpath.regexWorkingSetPage");
	}
	public RegExWorkingSetPage(String pageName) {
		this(pageName, "Enter project regular expression to display in this working set", Activator.getImageDescriptor("icons/logo16.gif"));
	}
	public RegExWorkingSetPage(String pageName, String title, ImageDescriptor titleImage) {
		super("com.javadude.workingsets.RegExWorkingSetPage", pageName, title, titleImage);
	}

	/**
	 * Create the fields for the property page. This includes a list displaying
	 * 	projects that match the regular expression (b/c regexs are a real pain
	 * 	to get right the first time...)
	 */
	@Override protected void createFields(Composite parent) {
		Label label = new Label(parent, SWT.NULL);
		label.setText("Project Name Regular Expression:");

		regexText_ = new Text(parent, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		regexText_.setLayoutData(gd);
		regexText_.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
				filter();
			}
		});

		// This is cool -- I'm using the regular expression content assist from the
		//	find/replace dialog. They made it nicely reusable!
		TextContentAdapter contentAdapter= new TextContentAdapter();
		FindReplaceDocumentAdapterContentProposalProvider findProposer= new FindReplaceDocumentAdapterContentProposalProvider(true);
		ContentAssistCommandAdapter contentAssistCommandAdapter = new ContentAssistCommandAdapter(
				regexText_,
				contentAdapter,
				findProposer,
				ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS,
				new char[] {'\\', '[', '('},
				true);
		contentAssistCommandAdapter.setEnabled(true);
		label = new Label(parent, SWT.NULL);
		label.setText("Matching projects:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
		matches_ = new org.eclipse.swt.widgets.List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		matches_.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		filter();
	}

	/**
	 * Filter the matching projects section of the property page
	 */
	private void filter() {
		matches_.removeAll();
		try {
			Pattern pattern = Pattern.compile(regexText_.getText());
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for (IProject project : projects) {
				if (pattern.matcher(project.getName()).matches()) {
					matches_.add(project.getName());
				}
			}
		} catch (PatternSyntaxException e) {
			return;
		}
	}
	@Override protected List<IAdaptable> getMatchingProjects() {
		List<IAdaptable> projects = new ArrayList<IAdaptable>();
		String regex = regexText_.getText();
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
            if (project.isOpen() && Pattern.matches(regex, project.getName())) {
            	projects.add(project);
            }
		}
		return projects;
	}
	@Override protected String getWorkingSetName() {
		return "RegEx: " + regexText_.getText();
	}
	@Override protected void initFields(IWorkingSet workingSet) {
		regexText_.setText(workingSet.getName().substring(7));
	}
	@Override protected boolean validate() {
		String regex = regexText_.getText();
		if ("".equals(regex.trim())) {
			updateStatus("Regular expression must be specified");
			return false;
		}
		try {
			Pattern.compile(regexText_.getText());
		} catch (PatternSyntaxException e) {
			String message = e.getMessage().split("[\n\r]")[0];
			updateStatus("Regular expression syntax: " + message);
			return false;
		}

		return true;
	}
}
