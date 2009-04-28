package com.javadude.derived.warning.cleaner.builder;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.javadude.derived.warning.cleaner.Activator;

public class DerivedWarningCleanerBuilder extends IncrementalProjectBuilder {

	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				deleteWarnings(resource);
				break;
			case IResourceDelta.REMOVED:
				break;
			case IResourceDelta.CHANGED:
				deleteWarnings(resource);
				break;
			}
			//return true to continue visiting children.
			return true;
		}
	}

	class SampleResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) {
			try {
				deleteWarnings(resource);
			} catch (CoreException e) {
				Activator.error(e);
			}
			return true;
		}
	}

	public static final String BUILDER_ID = "com.javadude.derived.warning.cleaner.derivedWarningCleanerBuilder";

	@Override
	@SuppressWarnings("unchecked")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		try {
			if (kind == FULL_BUILD) {
				getProject().accept(new SampleResourceVisitor());
			} else {
				IResourceDelta delta = getDelta(getProject());
				if (delta == null) {
					getProject().accept(new SampleResourceVisitor());
				} else {
					delta.accept(new SampleDeltaVisitor());
				}
			}
		} catch (CoreException e) {
			Activator.error(e);
		}
		return null;
	}

	void deleteWarnings(IResource resource) throws CoreException {
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			if (file.isDerived()) {
				IMarker[] markers = file.findMarkers("org.eclipse.core.resources.problemmarker", true, IFile.DEPTH_ZERO);
				for (IMarker marker : markers) {
					if (IMarker.SEVERITY_ERROR != ((Integer) marker.getAttribute(IMarker.SEVERITY)).intValue())
						marker.delete();
				}
			}
		}
	}
}
