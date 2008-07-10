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
