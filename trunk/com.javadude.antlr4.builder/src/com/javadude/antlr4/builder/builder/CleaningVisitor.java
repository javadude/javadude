package com.javadude.antlr4.builder.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class CleaningVisitor implements IResourceVisitor {
	private String grammarFile;
	private BuildInfo buildInfo;

	public CleaningVisitor(BuildInfo buildInfo, String grammarFile) {
		this.buildInfo = buildInfo;
		this.grammarFile = grammarFile;
	}

	public boolean visit(IResource resource) throws CoreException {
		if (resource.isDerived()) {
			if (resource instanceof IFile) {
				IFile file = (IFile)resource;
				if (file.getPersistentProperty(Antlr4Builder.SOURCE_GRAMMAR_PROPERTY) != null) {
					if (grammarFile == null ||
						file.getPersistentProperty(Antlr4Builder.SOURCE_GRAMMAR_PROPERTY).equals(grammarFile)) {
	                    resource.delete(true, buildInfo.getMonitor());
                    }
				}
			}
		}
		return true;
	}
}
