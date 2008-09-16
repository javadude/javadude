/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield, based on ANTLR-Eclipse plugin
 *   by Torsten Juergeleit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors
 *    Torsten Juergeleit - original ANTLR Eclipse plugin
 *    Scott Stanchfield - modifications for ANTXR
 *******************************************************************************/
package com.javadude.antxr.eclipse.ui.editor.outline;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.javadude.antxr.eclipse.core.parser.ISegment;
import com.javadude.antxr.eclipse.ui.actions.CollapseAllAction;
import com.javadude.antxr.eclipse.ui.actions.LexicalSortingAction;
import com.javadude.antxr.eclipse.ui.editor.AntxrEditor;

/**
 * A content outline page which represents the content of an ANTXR grammar file.
 */
public class AntxrOutlinePage extends ContentOutlinePage {
	private AntxrEditor fEditor;
	private Object fInput;
	private String fSelectedSegmentID;
	private AntxrOutlineLabelProvider fLabelProvider;
	private boolean fIsDisposed;

	/**
	 * Create the outline page
	 * @param anEditor the editor
	 */
	public AntxrOutlinePage(AntxrEditor anEditor) {
		fEditor = anEditor;
		fIsDisposed = true;
	}

	/** {@inheritDoc} */
	public void createControl(Composite aParent) {
		super.createControl(aParent);

		fLabelProvider = new AntxrOutlineLabelProvider();

		// Init tree viewer
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new AntxrOutlineContentProvider(fEditor));
		viewer.setLabelProvider(fLabelProvider);
		viewer.addSelectionChangedListener(this);
		if (fInput != null) {
			viewer.setInput(fInput);
		}
		fIsDisposed = false;

		// Add collapse all button to viewer's toolbar
		IToolBarManager mgr = getSite().getActionBars().getToolBarManager();
		mgr.add(new CollapseAllAction(viewer));
		mgr.add(new LexicalSortingAction(viewer));

		// Refresh outline according to initial cursor position
		update();
	}

	/** {@inheritDoc} */
	public void selectionChanged(SelectionChangedEvent anEvent) {
		super.selectionChanged(anEvent);

		ISelection selection = anEvent.getSelection();
		if (!selection.isEmpty()) {
			ISegment segment = (ISegment)
						   ((IStructuredSelection)selection).getFirstElement();
			if (fSelectedSegmentID == null || isDifferentSegment(segment)) {
				fEditor.highlightSegment(segment, true);
				fSelectedSegmentID = segment.getUniqueID();
			} else {
				fEditor.revealSegment(segment);
			}
		}
	}

	/**
	 * Select part of the grammar
	 * @param aLine the line to select
	 * @param aForceSelect should we force the selection?
	 */
	public void selectSegment(int aLine, boolean aForceSelect) {
		if (aLine > 0) {
			TreeViewer viewer = getTreeViewer();
			ISegment segment = fEditor.getSegment(aLine);
			viewer.removeSelectionChangedListener(this);
			if (segment == null) {
				if (fSelectedSegmentID != null) {
					viewer.setSelection(new StructuredSelection());
					fEditor.resetHighlightRange();
					fSelectedSegmentID = null;
				}
			} else {
				if (aForceSelect || isDifferentSegment(segment)) {
					viewer.setSelection(new StructuredSelection(segment));
					fEditor.highlightSegment(segment, false);
					fSelectedSegmentID = segment.getUniqueID();
				}
				viewer.reveal(segment);
			}
			viewer.addSelectionChangedListener(this);
		}
	}

	private boolean isDifferentSegment(ISegment aSegment) {
		return (fSelectedSegmentID == null ||
				 !fSelectedSegmentID.equals(aSegment.getUniqueID()));
	}

	/**
	 * @param aInput
	 */
	public void setInput(Object aInput) {
		fInput = aInput;
		update();
	}

	/**
	 * Updates the outline page.
	 */
	public void update() {
		TreeViewer viewer = getTreeViewer();
		if (viewer != null) {
			Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.removeSelectionChangedListener(this);
				control.setRedraw(false);
				viewer.setInput(fInput);
//				viewer.expandAll();
				control.setRedraw(true);
				selectSegment(fEditor.getCursorLine(), true);
				viewer.addSelectionChangedListener(this);
			}
		}
	}

	/** {@inheritDoc} */
	public void dispose() {
	    setInput(null);
	    if (fLabelProvider != null) {
	    	fLabelProvider.dispose();
	    	fLabelProvider = null;
	    }
	    fIsDisposed = true;
	    super.dispose();
	}

	/**
	 * Have we disposed the outline yet?
	 * @return true if disposed
	 */
	public boolean isDisposed() {
		return fIsDisposed;
	}
}
