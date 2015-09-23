/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.dependencies;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.javadude.dependencies.editparts.WorkspaceEditPartFactory;

public class DependencyView extends ViewPart {
	private ScrollingGraphicalViewer viewer;

	@Override
    public void createPartControl(Composite parent) {
		viewer = new ScrollingGraphicalViewer();
		viewer.createControl(parent);

		// set the edit part factory
		viewer.setEditPartFactory(new WorkspaceEditPartFactory(getSite().getPage()));
		viewer.setContents(ResourcesPlugin.getWorkspace().getRoot());

		// create the edit domain
		EditDomain editDomain = new EditDomain();
		editDomain.addViewer(viewer);
		viewer.setEditDomain(editDomain);

		// create and enable the delete, undo, and redo actions
		final DeleteAction deleteAction = new DeleteAction(this);
		UndoAction undoAction = new UndoAction(this);
		RedoAction redoAction = new RedoAction(this);
		deleteAction.setEnabled(true);
		redoAction.setEnabled(true);
		undoAction.setEnabled(true);

		// update the delete action when the selection changes
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				deleteAction.update();
			}
		});

		// create a key handler for the delete, redo, and undo actions
		KeyHandler keyHandler = new KeyHandler();
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, SWT.DEL, 0), deleteAction);
		keyHandler.put(KeyStroke.getPressed((char) 0x19, 0x79, SWT.CTRL), redoAction);
		keyHandler.put(KeyStroke.getPressed((char) 0x1a, 0x7a, SWT.CTRL), undoAction);

		// create the GraphicalViewerKeyHandler
		GraphicalViewerKeyHandler overallHandler = new GraphicalViewerKeyHandler(viewer);
		overallHandler.setParent(keyHandler);
		viewer.setKeyHandler(overallHandler);

		// set the selection provider
		getSite().setSelectionProvider(viewer);
		getViewSite().getPage().addSelectionListener(selectionListener_);
	}
	private ISelectionListener selectionListener_ = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				Object firstElement = structuredSelection.getFirstElement();
				if (firstElement instanceof IJavaProject) {
					IJavaProject selectedProject = (IJavaProject) firstElement;
					Object editPart = viewer.getEditPartRegistry().get(selectedProject);
					if (editPart != null) {
						viewer.setSelection(new StructuredSelection(editPart));
					}
				}
			}
		}};

	@Override
    public void dispose() {
	    super.dispose();
	    getViewSite().getPage().removeSelectionListener(selectionListener_);
    }

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
    public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * Adapter for the dependencies view. Support for the EditDomain
	 */
    @SuppressWarnings("unchecked")
	@Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == CommandStack.class) {
			return viewer.getEditDomain().getCommandStack();
		}

		return super.getAdapter(adapter);
	}
}
