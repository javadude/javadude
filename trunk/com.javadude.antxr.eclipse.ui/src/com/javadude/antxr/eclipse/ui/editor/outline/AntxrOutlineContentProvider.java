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
package com.javadude.antxr.eclipse.ui.editor.outline;

import com.javadude.antxr.eclipse.core.parser.IModel;
import com.javadude.antxr.eclipse.ui.editor.AntxrEditor;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

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
