package com.javadude.antlr4.builder.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

public class BuildInfo {
	private List<String> antlrOptions = new ArrayList<>();
	private List<IPath> sourcePaths = new ArrayList<>();
	private IFolder generatedFolder;
	private IProgressMonitor monitor;
	
	public BuildInfo(IProgressMonitor monitor, IFolder generatedFolder) {
		this.monitor = monitor;
		this.generatedFolder = generatedFolder;
	}
	public List<String> getAntlrOptions() {
		return antlrOptions;
	}
	public List<IPath> getSourcePaths() {
		return sourcePaths;
	}
	public IFolder getGeneratedFolder() {
		return generatedFolder;
	}
	public IProgressMonitor getMonitor() {
		return monitor;
	}
}