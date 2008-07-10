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
package com.javadude.dependencies;

import com.javadude.dependencies.editparts.WorkspaceEditPartFactory;

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
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), deleteAction);
		keyHandler.put(KeyStroke.getPressed('y', 121, 0), redoAction);
		keyHandler.put(KeyStroke.getPressed('z', 122, 0), undoAction);

		// create the GraphicalViewerKeyHandler
		GraphicalViewerKeyHandler overallHandler = new GraphicalViewerKeyHandler(viewer);
		overallHandler.setParent(keyHandler);
		viewer.setKeyHandler(overallHandler);

		// set the selection provider
//		getSite().setSelectionProvider(viewer);
		getViewSite().getPage().addSelectionListener(selectionListener_);
	}
	private ISelectionListener selectionListener_ = new ISelectionListener() {
		@Override
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
    public Object getAdapter(Class adapter) {
		if (adapter == CommandStack.class) {
			return viewer.getEditDomain().getCommandStack();
		}

		return super.getAdapter(adapter);
	}
}
