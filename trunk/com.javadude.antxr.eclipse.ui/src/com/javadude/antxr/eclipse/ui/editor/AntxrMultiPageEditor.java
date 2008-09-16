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
package com.javadude.antxr.eclipse.ui.editor;

import java.io.Reader;
import java.io.StringReader;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.INavigationLocationProvider;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

/**
 * The overall antxr editor
 */
public class AntxrMultiPageEditor extends MultiPageEditorPart
									   implements INavigationLocationProvider, IGotoMarker {
	private static final String PREFIX = "Editor.page.";

	private static final int PAGE_EDITOR = 0;
	private static final int PAGE_OVERVIEW = 1;

	private AntxrEditor fEditor;
	private AntxrOverview fOverview;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.INavigationLocationProvider#createEmptyNavigationLocation()
	 */
	public INavigationLocation createEmptyNavigationLocation() {
		return new AntxrNavigationLocation(fEditor, false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.INavigationLocationProvider#createNavigationLocation()
	 */
	public INavigationLocation createNavigationLocation() {
		return new AntxrNavigationLocation(fEditor, true);
	}

	/**
	 * Activate the editor
	 */
	public void activateEditor() {
		if (getActivePage() != AntxrMultiPageEditor.PAGE_EDITOR) {
			setActivePage(AntxrMultiPageEditor.PAGE_EDITOR);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
	 */
	protected void createPages() {
		try {
			fEditor = new AntxrEditor(this);
			addPage(fEditor, getEditorInput());
			setPageText(AntxrMultiPageEditor.PAGE_EDITOR,
						AntxrUIPlugin.getMessage(AntxrMultiPageEditor.PREFIX + "Source"));

			fOverview = new AntxrOverview(getContainer());
			addPage(fOverview.getControl());
			setPageText(AntxrMultiPageEditor.PAGE_OVERVIEW,
						AntxrUIPlugin.getMessage(AntxrMultiPageEditor.PREFIX + "Overview"));
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
				 		 AntxrUIPlugin.getMessage("ErrorCreatingNestedEditor"),
				 		 null, e.getStatus());
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(AntxrMultiPageEditor.PAGE_EDITOR).doSave(monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(AntxrMultiPageEditor.PAGE_EDITOR);
		editor.doSaveAs();
		setInput(editor.getEditorInput());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker(IMarker aMarker) {
		setActivePage(AntxrMultiPageEditor.PAGE_EDITOR);
		IDE.gotoMarker(getEditor(AntxrMultiPageEditor.PAGE_EDITOR), aMarker);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Refresh overview editor if activated.
	 * @see org.eclipse.ui.part.MultiPageEditorPart#pageChange(int)
	 */
	protected void pageChange(int aPageIndex) {
		super.pageChange(aPageIndex);
		if (aPageIndex == AntxrMultiPageEditor.PAGE_OVERVIEW) {
			Reader reader = new StringReader(fEditor.getDocument().get());
			fOverview.show(reader);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	public boolean isDirty() {
		return fEditor != null && fEditor.isDirty();
	}

	/**
	 * Returns ANTXR content outline page from ANTXR editor page if request.
	 */
	public Object getAdapter(Class aClass) {
	    Object adapter;
		if (aClass.equals(IContentOutlinePage.class)) {
			adapter = fEditor.getAdapter(aClass);
		} else if (aClass.equals(ITextEditor.class)) {
			adapter = fEditor;
		} else {
		    adapter = super.getAdapter(aClass);
		}
		return adapter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput anInput) {
		super.setInput(anInput);
		if (anInput != null) {
			setPartName(anInput.getName());

			// If the input is a file then use the path to the input file
			// (without the filename) as content discription
			if (anInput instanceof ILocationProvider) {
				IPath path = ((ILocationProvider) anInput).getPath(anInput);
				setContentDescription(path.removeLastSegments(1).toString());
			}
		}
	}
}
