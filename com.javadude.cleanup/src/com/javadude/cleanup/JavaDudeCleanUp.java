package com.javadude.cleanup;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.cleanup.CleanUpContext;
import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.CleanUpRequirements;
import org.eclipse.jdt.ui.cleanup.ICleanUp;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

public class JavaDudeCleanUp implements ICleanUp {

	private CleanUpOptions options;

	public JavaDudeCleanUp() {
	}

	@Override
	public RefactoringStatus checkPostConditions(IProgressMonitor monitor)
			throws CoreException {
		return new RefactoringStatus();
	}

	@Override
	public RefactoringStatus checkPreConditions(IJavaProject project,
			ICompilationUnit[] compilationUnits, IProgressMonitor monitor)
			throws CoreException {
		return new RefactoringStatus();
	}

	@Override
	public ICleanUpFix createFix(CleanUpContext context) throws CoreException {
		if (options.isEnabled(JavaDudeCleanUpOptionsInitializer.CONVERT_TABS_TO_SPACES)) {
			return new JavaDudeCleanUpFix(context.getCompilationUnit(), options);
		}
		return null;
	}

	@Override
	public CleanUpRequirements getRequirements() {
		return new CleanUpRequirements(false, false, false, null);
	}

	@Override
	public String[] getStepDescriptions() {
		if (options.isEnabled(JavaDudeCleanUpOptionsInitializer.CONVERT_TABS_TO_SPACES)) {
			return new String[] {"Convert tabs to spaces"};
		}
		return new String[] {};
	}

	@Override
	public void setOptions(CleanUpOptions options) {
		this.options = options;
	}
}
