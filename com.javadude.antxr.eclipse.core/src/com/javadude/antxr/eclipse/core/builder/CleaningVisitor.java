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
package com.javadude.antxr.eclipse.core.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A resource visitor that deletes ANTXR-generated files
 */
public class CleaningVisitor implements IResourceVisitor {
	private IProgressMonitor monitor;
	private String grammarFile;

	/**
	 * Create the visitor
	 * @param monitor progress monitor
	 * @param grammarFile the grammar file that owns the output
	 */
	public CleaningVisitor(IProgressMonitor monitor, String grammarFile) {
		this.monitor = monitor;
		this.grammarFile = grammarFile;
	}

	/** {@inheritDoc} */
	public boolean visit(IResource resource) throws CoreException {
		if (resource.isDerived()) {
			if (resource instanceof IFile) {
				IFile file = (IFile)resource;
				if (file.getPersistentProperty(AntxrBuilder.GRAMMAR_ECLIPSE_PROPERTY) != null) {
					if (grammarFile == null ||
						file.getPersistentProperty(AntxrBuilder.GRAMMAR_ECLIPSE_PROPERTY).equals(grammarFile)) {
	                    resource.delete(true, monitor);
                    }
				}
			}
		}
		return true;
	}
}
