/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets.m2eclipse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.text.FindReplaceDocumentAdapterContentProposalProvider;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.ArtifactKey;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.IMavenProjectRegistry;
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

import com.javadude.workingsets.DynamicWorkingSetPage;

/**
 * A property page for regular-expression-based dynamic working sets.
 * This page displays a field for the regular expression (with
 * 	content assist, woohoo!) to filter projects for inclusion in the working set
 * @author Scott Stanchfield
 */
public class M2EclipseWorkingSetPage extends DynamicWorkingSetPage {
	private Text groupText_ = null;
	private Text artifactText_ = null;
	private org.eclipse.swt.widgets.List matches_;

	public M2EclipseWorkingSetPage() {
		super("com.javadude.workingsets.m2eclipse.M2EclipseWorkingSetPage",
				"com.javadude.workingsets.m2eclipse.m2eclipseWorkingSetPage",
				"Enter group/artifact regular expressions",
				Activator.getImageDescriptor("icons/logo16.gif"));
	}

	/**
	 * Create the fields for the property page. This includes a list displaying
	 * 	projects that match the regular expression (b/c regexs are a real pain
	 * 	to get right the first time...)
	 */
	@Override protected void createFields(Composite parent) {
		Label label = new Label(parent, SWT.NULL);
		label.setText("Group ID Regular Expression:");

		groupText_ = new Text(parent, SWT.BORDER | SWT.SINGLE);
		groupText_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		label = new Label(parent, SWT.NULL);
		label.setText("Artifact ID Regular Expression:");

		artifactText_ = new Text(parent, SWT.BORDER | SWT.SINGLE);
		artifactText_.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		ModifyListener listener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
				filter();
			}
		};
		groupText_.addModifyListener(listener);
		artifactText_.addModifyListener(listener);

		// This is cool -- I'm using the regular expression content assist from the
		//	find/replace dialog. They made it nicely reusable!
		TextContentAdapter contentAdapter = new TextContentAdapter();
		FindReplaceDocumentAdapterContentProposalProvider findProposer= new FindReplaceDocumentAdapterContentProposalProvider(true);
		new ContentAssistCommandAdapter(
				groupText_,
				contentAdapter,
				findProposer,
				ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS,
				new char[] {'\\', '[', '('},
				true).setEnabled(true);
		new ContentAssistCommandAdapter(
				artifactText_,
				contentAdapter,
				findProposer,
				ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS,
				new char[] {'\\', '[', '('},
				true).setEnabled(true);

		label = new Label(parent, SWT.NULL);
		label.setText("Matching projects:");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
		matches_ = new org.eclipse.swt.widgets.List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		matches_.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		filter();
	}

	/**
	 * Filter the matching projects section of the property page
	 * @throws CoreException
	 */
	private void filter() {
		matches_.removeAll();
		List<IAdaptable> matchingProjects = getMatchingItems();
		for (IAdaptable adaptable : matchingProjects) {
			IProject project = (IProject) adaptable;
			matches_.add(project.getName());
		}
	}

	@Override protected List<IAdaptable> getMatchingItems() {
		try {
			String groupIdText = groupText_.getText().trim();
			String artifactIdText = artifactText_.getText().trim();
			Pattern groupPattern = null;
			Pattern artifactPattern = null;
			if (!"".equals(groupIdText) && !".*".equals(groupIdText)) {
				groupPattern = Pattern.compile(groupIdText);
			}
			if (!"".equals(artifactIdText) && !".*".equals(artifactIdText)) {
				artifactPattern = Pattern.compile(artifactIdText);
			}
			List<IAdaptable> projects = new ArrayList<IAdaptable>();
			for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
				try {
					if (!project.isOpen() || !project.hasNature("org.maven.ide.eclipse.maven2Nature")) {
						continue;
					}
				} catch (CoreException e) {
					throw new RuntimeException(e);
				}
				IMavenProjectRegistry mavenProjectRegistry = MavenPlugin.getMavenProjectRegistry();
				IMavenProjectFacade facade = mavenProjectRegistry.create(project, null);
				if (facade!=null) {
					ArtifactKey artifactKey = facade.getArtifactKey();
					if (groupPattern != null && !groupPattern.matcher(artifactKey.getGroupId()).matches()) {
							continue;
					}
					if (artifactPattern != null && !artifactPattern.matcher(artifactKey.getArtifactId()).matches()) {
						continue;
					}
				}
				projects.add(project);
			}
			return projects;
		} catch (PatternSyntaxException e) {
			return Collections.emptyList();
		}
	}
	@Override protected String getWorkingSetName() {
		return "Maven: group=" + groupText_.getText() + "\nartifact=" + artifactText_.getText();
	}
	@Override protected void initFields(IWorkingSet workingSet) {
		String stuff = workingSet.getName().substring(13);
		int i = stuff.indexOf('\n');
		String group = stuff.substring(0, i);
		String artifact = stuff.substring(i + 10);
		groupText_.setText(group);
		artifactText_.setText(artifact);
	}
	@Override protected boolean validate() {
		String group = groupText_.getText().trim();
		String artifact = artifactText_.getText().trim();
		if (("".equals(group) || ".*".equals(group)) &&
			("".equals(artifact) || ".*".equals(artifact))) {
			updateStatus("Group or Artifact pattern must be specified and not .*");
			return false;
		}
		try {
			Pattern.compile(groupText_.getText());
			Pattern.compile(artifactText_.getText());
		} catch (PatternSyntaxException e) {
			String message = e.getMessage().split("[\n\r]")[0];
			updateStatus("Regular expression syntax: " + message);
			return false;
		}

		return true;
	}
}
