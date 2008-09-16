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
package com.javadude.antxr.eclipse.ui;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.ui.IEditorPart;

/**
 * Creates breakpoint adapters
 */
public class BreakpointAdapterFactory implements IAdapterFactory {

	/** {@inheritDoc} */
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		IEditorPart editorPart = AntxrLineBreakpointAdapter.getEditorPart(adaptableObject);
		if (editorPart != null) {
	        return new AntxrLineBreakpointAdapter();
        }
		return null;
	}

	/** {@inheritDoc} */
	public Class[] getAdapterList() {
		return new Class[]{IToggleBreakpointsTarget.class};
	}
}
