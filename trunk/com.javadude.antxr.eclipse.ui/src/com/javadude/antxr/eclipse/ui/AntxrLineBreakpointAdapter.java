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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Adapter to create breakpoints in ANTXR grammar files.
 */
public class AntxrLineBreakpointAdapter implements IToggleBreakpointsTarget {
	/** {@inheritDoc} */
	public void toggleLineBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		IEditorPart editorPart = AntxrLineBreakpointAdapter.getEditorPart(part);
		if (editorPart != null) {
			IResource resource = (IResource)editorPart.getEditorInput().getAdapter(IResource.class);
			ITextSelection textSelection = (ITextSelection)selection;
			int lineNumber = textSelection.getStartLine();
			IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints();
			for (int i = 0; i < breakpoints.length; i++) {
				IBreakpoint breakpoint = breakpoints[i];
				if (resource.equals(breakpoint.getMarker().getResource())) {
					if (((ILineBreakpoint)breakpoint).getLineNumber() == (lineNumber + 1)) {
						// remove
						breakpoint.delete();
						return; // done with toggle -- removed it
					}
				}
			}

			// didn't remove one, so we must be adding one

			// create line breakpoint (antxr line numbers start at 0, so add 1)
			JDIDebugModel.createStratumBreakpoint(resource, "G", resource.getName(), null, null, lineNumber + 1, -1, -1, 0, true, null);
		}
	}

	/** {@inheritDoc} */
	public boolean canToggleLineBreakpoints(IWorkbenchPart part, ISelection selection) {
		return AntxrLineBreakpointAdapter.getEditorPart(part) != null;
	}

	/**
	 * Convert an adaptable to an edit part
	 * @param adaptableObject the adaptable
	 * @return the editor
	 */
	public static IEditorPart getEditorPart(Object adaptableObject) {
		if (adaptableObject instanceof IEditorPart) {
			IEditorPart editorPart = (IEditorPart)adaptableObject;
			IResource resource = (IResource)editorPart.getEditorInput().getAdapter(IResource.class);
			if (resource != null) {
				String extension = resource.getFileExtension();
				if (extension != null && extension.equals("g")) {
					return editorPart;
				}
			}
		}
		return null;
	}

	// TODO Add method breakpoints and watchpoints!
	/** {@inheritDoc} */
	public void toggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		// method breakpoints are not currently supported
	}

	/** {@inheritDoc} */
	public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
		// method breakpoints are not currently supported
		return false;
	}

	/** {@inheritDoc} */
	public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		// watchpoints are not currently supported
	}

	/** {@inheritDoc} */
	public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
		// watchpoints are not currently supported
		return false;
	}
}
