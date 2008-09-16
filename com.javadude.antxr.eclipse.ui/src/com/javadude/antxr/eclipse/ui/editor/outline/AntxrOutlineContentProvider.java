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

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.javadude.antxr.eclipse.core.parser.IModel;
import com.javadude.antxr.eclipse.ui.editor.AntxrEditor;

/**
 * Supplies a model for an outline view
 */
public class AntxrOutlineContentProvider implements ITreeContentProvider {

	private AntxrEditor fEditor;

	/**
	 * Create the model
	 * @param anEditor the target editor
	 */
	public AntxrOutlineContentProvider(AntxrEditor anEditor) {
	    fEditor = anEditor;
	}

	/** {@inheritDoc} */
	public void inputChanged(Viewer aViewer, Object anOldInput,
							  Object aNewInput) {
		// nothing to do here
    }

	/** {@inheritDoc} */
	public void dispose() {
		// nothing to do here
    }

	/** {@inheritDoc} */
	public Object[] getElements(Object inputElement) {
        return fEditor.getRootElements();
    }

	/** {@inheritDoc} */
	public Object[] getChildren(Object anElement) {
        return (anElement instanceof IModel) ?
						((IModel)anElement).getChildren() : IModel.NO_CHILDREN;
    }

	/** {@inheritDoc} */
	public Object getParent(Object anElement) {
		return (anElement instanceof IModel) ?
										((IModel)anElement).getParent() : null;
    }

    /** {@inheritDoc} */
    public boolean hasChildren(Object anElement) {
        return (anElement instanceof IModel) ?
									 ((IModel)anElement).hasChildren() : false;
    }
}
