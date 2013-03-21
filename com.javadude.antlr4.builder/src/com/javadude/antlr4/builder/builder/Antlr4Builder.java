package com.javadude.antlr4.builder.builder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Map;

import org.antlr.v4.Tool;
import org.antlr.v4.tool.ANTLRMessage;
import org.antlr.v4.tool.ANTLRToolListener;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;

import com.javadude.antlr4.builder.Activator;
import com.javadude.antlr4.builder.preferences.PreferenceConstants;

public class Antlr4Builder extends IncrementalProjectBuilder {
    /** the persistent option that attaches a grammar name to a file it generated */
    public static final QualifiedName SOURCE_GRAMMAR_PROPERTY = new QualifiedName(Activator.PLUGIN_ID, "antlr4.source.grammar");
    public static final QualifiedName COMMAND_LINE_OPTIONS_PROPERTY = new QualifiedName(Activator.PLUGIN_ID, "antlr4.options");
	public static final String BUILDER_ID = "com.javadude.antlr4.builder.antlr4Builder";
	private static final String MARKER_TYPE = "com.javadude.antlr4.builder.antlrProblem";

    public static void forceFullANTLR4Build(final IWorkspaceRoot workspaceRoot, String reason) {
    	Job job = new Job("Rebuild ANTLR 4 Grammars (" + reason + ")") {
    		@Override protected IStatus run(IProgressMonitor monitor) {
    			try {
	    			for (IProject project : workspaceRoot.getProjects()) {
	    				if (Activator.getUtil().hasNature(project, Antlr4Nature.NATURE_ID)) {
	    					BuildInfo buildInfo = Antlr4Builder.setupBuild(project, monitor);
	    					project.accept(new Antlr4ResourceVisitor(buildInfo));
	    				}
	    			}
    			} catch (CoreException e) {
    				Activator.getUtil().error(300, "Error rebuilding all ANTLR grammars", e);
    			}
    			return Status.OK_STATUS;
    		}
    	};

    	// Start the Job
    	job.schedule(); 
    		
    }
    
	private static class Antlr4DeltaVisitor implements IResourceDeltaVisitor {
		private BuildInfo buildInfo;

		public Antlr4DeltaVisitor(BuildInfo buildInfo) {
			this.buildInfo = buildInfo;
		}

		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				buildGrammar(buildInfo, resource);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				// remove corresponding generated files
				buildInfo.getGeneratedFolder().accept(new CleaningVisitor(buildInfo, resource.getName()));
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				buildGrammar(buildInfo, resource);
				break;
			}
			//return true to continue visiting children.
			return true;
		}
	}

	private static class Antlr4ResourceVisitor implements IResourceVisitor {
		private BuildInfo buildInfo;

		public Antlr4ResourceVisitor(BuildInfo buildInfo) {
			this.buildInfo = buildInfo;
		}

		public boolean visit(IResource resource) {
			buildGrammar(buildInfo, resource);
			return true;
		}
	}


	private static void addMarker(IResource file, String message, int lineNumber, int severity) {
		try {
			IMarker marker = file.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	protected IProject[] build(int kind, @SuppressWarnings("rawtypes") Map args, IProgressMonitor monitor) throws CoreException {
		BuildInfo buildInfo = setupBuild(getProject(), monitor);
		if (kind == FULL_BUILD) {
			getProject().accept(new Antlr4ResourceVisitor(buildInfo));
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				getProject().accept(new Antlr4ResourceVisitor(buildInfo));
			} else {
				delta.accept(new Antlr4DeltaVisitor(buildInfo));
			}
		}
		return null;
	}
	
	private static BuildInfo setupBuild(IProject project, IProgressMonitor monitor) throws CoreException {
		BuildInfo buildInfo = new BuildInfo(monitor, Antlr4Nature.getGeneratedFolder(project));
		
		// TODO LOAD ANTLR JAR INTO CLASSPATH
		
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
//		String antlr4JarLocation = preferenceStore.getString(PreferenceConstants.P_ANTLR4_JAR_PATH);
//		if (antlr4JarLocation == null || antlr4JarLocation.isEmpty()) {
//			// REPORT ERROR - MUST SET ANTLR4 JAR LOCATION
//			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Antlr Jar Location not set"));
//		}
//		
//		File jarFile = new File(antlr4JarLocation);
//		if (!jarFile.exists()) {
//			// REPORT ERROR - ANTLR4 JAR NOT FOUND
//			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Antlr Jar " + jarFile + " not found"));
//		}
//		if (!jarFile.isFile()) {
//			// REPORT ERROR - ANTLR4 JAR IS NOT A FILE
//			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Antlr Jar " + jarFile + " is not a file"));
//		}
//
//		// TODO LOAD ANTLR JAR ONTO CLASSPATH
		
		// setup options
		// TODO - PER PROJECT OPTIONS
		// TODO - PER GRAMMAR OPTIONS
		
		String libraryDir = preferenceStore.getString(PreferenceConstants.P_ANTLR4_LIBRARY_DIRECTORY);
		boolean generateAtn = preferenceStore.getBoolean(PreferenceConstants.P_ANTLR4_GENERATE_ATN);
		boolean longMessages = preferenceStore.getBoolean(PreferenceConstants.P_ANTLR4_LONG_MESSAGES);
		boolean generateListener = preferenceStore.getBoolean(PreferenceConstants.P_ANTLR4_GENERATE_LISTENER);
		boolean generateVisitor = preferenceStore.getBoolean(PreferenceConstants.P_ANTLR4_GENERATE_VISITOR);
		boolean generateDependencies = preferenceStore.getBoolean(PreferenceConstants.P_ANTLR4_GENERATE_DEPENDENCIES);
		boolean warningsAsErrors = preferenceStore.getBoolean(PreferenceConstants.P_ANTLR4_WARNINGS_AS_ERRORS);
		boolean launchStInspector = preferenceStore.getBoolean(PreferenceConstants.P_ANTLR4_LAUNCH_ST_INSPECTOR);
		boolean waitForStClose = preferenceStore.getBoolean(PreferenceConstants.P_ANTLR4_WAIT_FOR_ST_CLOSE);
		boolean forceAtn = preferenceStore.getBoolean(PreferenceConstants.P_ANTLR4_FORCE_ATN);

		// NOTE: output dir and package name changes per grammar file

		if (!libraryDir.isEmpty()) {
			buildInfo.getAntlrOptions().add("-lib");
			buildInfo.getAntlrOptions().add(libraryDir);
		}
		if (generateAtn)
			buildInfo.getAntlrOptions().add("-atn");
		if (longMessages)
			buildInfo.getAntlrOptions().add("-long-messages");
		if (generateListener)
			buildInfo.getAntlrOptions().add("-listener");
		else
			buildInfo.getAntlrOptions().add("-no-listener");
		if (generateVisitor)
			buildInfo.getAntlrOptions().add("-visitor");
		else
			buildInfo.getAntlrOptions().add("-no-visitor");
		if (generateDependencies)
			buildInfo.getAntlrOptions().add("-depend");
		if (warningsAsErrors)
			buildInfo.getAntlrOptions().add("-Werror");
		if (launchStInspector)
			buildInfo.getAntlrOptions().add("-XdbgST");
		if (waitForStClose)
			buildInfo.getAntlrOptions().add("-XdbgSTWait");
		if (forceAtn)
			buildInfo.getAntlrOptions().add("-Xforce-atn");
		
		// get sourcepaths
        final IJavaProject javaProject = JavaCore.create(project);
        IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
        for (IClasspathEntry classpathEntry : rawClasspath) {
            if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
            	buildInfo.getSourcePaths().add(classpathEntry.getPath());
            }
        }
        
		return buildInfo;
	}
	
	private static void buildGrammar(BuildInfo buildInfo, final IResource grammarFile) {
		try {
			boolean found = false;
			for (IPath sourcePath : buildInfo.getSourcePaths()) {
				if (sourcePath.isPrefixOf(grammarFile.getFullPath())) {
					found = true;
					break;
				} else {
				}
			}
			
			if (!found)
				return;
			
			
			if (grammarFile.getFileExtension() == null || !grammarFile.getFileExtension().equals("g4")) {
				return;
			}
			
			// delete previous markers for this builder
			grammarFile.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);

			String grammarFileName = grammarFile.getName();
			
			// delete previously-generated files for this grammar
			// TODO MAKE SURE QUALIFIED WITH SAME PATH IN CASE SAME GRAMMAR NAME IN MULTIPLE DIRS
			grammarFile.getProject().accept(new CleaningVisitor(buildInfo, grammarFileName));
	
			File tempDir = File.createTempFile("antlr-eclipse", "-build");
			tempDir.delete();
			tempDir.mkdirs();
			
			buildInfo.getAntlrOptions().add("-o");
			buildInfo.getAntlrOptions().add(tempDir.getAbsolutePath());
			buildInfo.getAntlrOptions().add(grammarFile.getLocation().toString());
			
			// figure out which sourcepath entry contains the grammarFile
			
			// build using ANTLR to temporary directory
	        Tool antlr = new Tool(buildInfo.getAntlrOptions().toArray(new String[buildInfo.getAntlrOptions().size()]));
            antlr.processGrammarsOnCommandLine();
            antlr.addListener(new ANTLRToolListener() {
				@Override public void warning(ANTLRMessage msg) {
					addMarker(grammarFile, msg.getErrorType().msg, msg.line, IStatus.WARNING);
				}
				@Override public void info(String msg) {
					addMarker(grammarFile, msg, -1, IStatus.INFO);
				}
				@Override public void error(ANTLRMessage msg) {
					addMarker(grammarFile, msg.getErrorType().msg, msg.line, IStatus.ERROR);
				}
			});

            // strip off the deepest sourcepath so we're just left with the package path structure
            IPath packagePath = grammarFile.getFullPath();
            IPath longestContainingPath = null;
            for(IPath sourcePath : buildInfo.getSourcePaths()) {
            	if (sourcePath.isPrefixOf(packagePath)) {
            		if (longestContainingPath == null) {
            			longestContainingPath = sourcePath;
            		} else {
            			if (longestContainingPath.segmentCount() < sourcePath.segmentCount())
            				longestContainingPath = sourcePath;
            		}
            	}
            }
            if (longestContainingPath == null) {
            	throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Internal Error: " + grammarFile + " somehow not inside a source directory"));
            }
            
            packagePath = packagePath.makeRelativeTo(longestContainingPath);
            packagePath = packagePath.removeLastSegments(1);
            
			// copy generated files to generated source dir and mark derived and generated by grammar
            for(File generatedFile : tempDir.listFiles()) {
            	String content = FileUtils.readFileToString(generatedFile);
            	IFolder targetFolder = buildInfo.getGeneratedFolder().getFolder(packagePath);
            	mkdirs(targetFolder, buildInfo.getMonitor());
            	IFile target = targetFolder.getFile(generatedFile.getName());
            	target.create(new ByteArrayInputStream(content.getBytes()), true, buildInfo.getMonitor());
            	target.setDerived(true, buildInfo.getMonitor());
            	target.setPersistentProperty(SOURCE_GRAMMAR_PROPERTY, grammarFileName);
            	target.setPersistentProperty(COMMAND_LINE_OPTIONS_PROPERTY, buildInfo.getAntlrOptions().toString());
            }
            delete(tempDir);
            
		} catch (RuntimeException e) {
			throw e;
		} catch (Error e) {
			throw e;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
	private static void mkdirs(IFolder folder, IProgressMonitor monitor) throws CoreException {
		if (folder.exists())
			return;
		mkdirs((IFolder) folder.getParent(), monitor);
		folder.create(true, false, monitor);
	}
	private static void delete(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles())
				delete(child);
		}
		file.delete();
	}
}
