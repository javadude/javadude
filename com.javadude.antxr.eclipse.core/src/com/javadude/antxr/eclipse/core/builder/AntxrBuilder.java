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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IContainer;
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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.javadude.antxr.AntxrTool;
import com.javadude.antxr.eclipse.core.AntxrCorePlugin;
import com.javadude.antxr.eclipse.core.AntxrNature;
import com.javadude.antxr.eclipse.core.properties.SettingsPersister;

/**
 * An eclipse builder to compile ANTXR grammars
 */
public class AntxrBuilder extends IncrementalProjectBuilder implements IStreamListener {
    /** the builder id */
    public static final String BUILDER_ID = AntxrCorePlugin.PLUGIN_ID + ".antxrbuilder";
    /** the builder debug id */
    public static final String DEBUG_OPTION = AntxrCorePlugin.PLUGIN_ID + "/builder/debug";
    /** are we debugging the builder? */
    public static boolean DEBUG = false;

    private IFile fFile;
    private String fOutput;
    private PrintStream fOriginalOut;
    private PrintStream fOriginalErr;

    /** the persistent option that indicates that a generated file should have warnings cleared */
    public static final QualifiedName CLEAN_WARNINGS = new QualifiedName(AntxrCorePlugin.PLUGIN_ID,
                                                                         SettingsPersister.CLEAN_WARNINGS);

    /** the persistent option that indicates that a generated file should have warnings cleared */
    public static final QualifiedName INSTALL_SMAP = new QualifiedName(AntxrCorePlugin.PLUGIN_ID,
                                                                       SettingsPersister.SMAP_PROPERTY);

    /** the persistent option that attaches a grammar name to a file it generated */
    public static final QualifiedName GRAMMAR_ECLIPSE_PROPERTY = new QualifiedName(AntxrCorePlugin.PLUGIN_ID,
                                                                                   SettingsPersister.GRAMMAR_PROPERTY);

    /** the persistent option that attaches command line options to generated file */
    public static final QualifiedName COMMAND_LINE_OPTIONS_PROPERTY = new QualifiedName(AntxrCorePlugin.PLUGIN_ID,
                                                                                        "commandLineOptions");

    /**
     * Create a builder
     */
    public AntxrBuilder() {
        AntxrBuilder.DEBUG = AntxrCorePlugin.isDebug(AntxrBuilder.DEBUG_OPTION);
    }

    /** {@inheritDoc} */
    protected void clean(IProgressMonitor monitor) throws CoreException {
        // delete all derived resources that have a GRAMMAR option on them
        getProject().accept(new CleaningVisitor(monitor, null));
    }

    /** {@inheritDoc} */
    protected IProject[] build(int aKind, Map anArgs, IProgressMonitor aMonitor) throws CoreException {
        IResourceDelta delta = (aKind != IncrementalProjectBuilder.FULL_BUILD ? getDelta(getProject()) : null);
        if (delta == null || aKind == IncrementalProjectBuilder.FULL_BUILD) {
            IProject project = getProject();
            if (AntxrCorePlugin.getUtil().hasNature(project, AntxrNature.NATURE_ID)) {
                project.accept(new Visitor(aMonitor));
            }
        } else {
            delta.accept(new DeltaVisitor(aMonitor));
        }
        return null;
    }

    /** {@inheritDoc} */
    public void streamAppended(String aText, Object aStream) {
        if (AntxrBuilder.DEBUG) {
            fOriginalOut.println("ANTXR output: " + aText);
        }
        int line = 0;
        String message = null;
        int severity = 0;

        // First check for messages with header "<file>|<line>|<column>"
        StringTokenizer st = new StringTokenizer(aText, "|");
        if (st.countTokens() > 3) {
            st.nextToken(); // file
            line = Integer.parseInt(st.nextToken());
            Integer.parseInt(st.nextToken()); // column
            message = st.nextToken();
            while (st.hasMoreTokens()) {
                message += st.nextToken();
            }
            if (message.startsWith("warning:")) {
                severity = IMarker.SEVERITY_WARNING;
                message = message.substring(8);
            } else {
                severity = IMarker.SEVERITY_ERROR;
            }
            message = message.replace('\t', ' ').trim();

            // Then check for messages without header
        } else if (aText.startsWith("panic: ")) {
            message = aText.substring(7);
            severity = IMarker.SEVERITY_ERROR;
        } else if (aText.startsWith("error: ")) {
            message = aText.substring(7);
            severity = IMarker.SEVERITY_ERROR;
        } else if (aText.startsWith("warning: ")) {
            severity = IMarker.SEVERITY_WARNING;
            message = aText.substring(9);
        } else {
            if (AntxrBuilder.DEBUG) {
                fOriginalOut.println("Unhandled ANTXR output: " + aText);
            }
        }

        // If valid error/warning message found then create problem marker
        if (message != null) {
            createProblemMarker(line, message, severity);
        }
    }

    private void createProblemMarker(int aLine, String aMessage, int aSeverity) {
        try {
            IMarker marker = fFile.createMarker(IMarker.PROBLEM);
            marker.setAttribute(IMarker.MESSAGE, aMessage);
            marker.setAttribute(IMarker.SEVERITY, aSeverity);
            if (aLine > 0) {
                marker.setAttribute(IMarker.LINE_NUMBER, aLine);
            }
        } catch (CoreException e) {
            AntxrCorePlugin.log(e);
        }
    }

    private class Visitor implements IResourceVisitor {
        private IProgressMonitor fMonitor;

        /**
         * Create a full-build visitor
         *
         * @param aMonitor
         *            a progress monitor
         */
        public Visitor(IProgressMonitor aMonitor) {
            fMonitor = aMonitor;
        }

        /** {@inheritDoc} */
        public boolean visit(IResource aResource) {
            if (aResource instanceof IFile) {
                // see if this is it
                IFile file = (IFile) aResource;
                if (file.getName().endsWith(".antxr")) {
                    // if we're in a java project, check to see if the file is in
                    // a source directory...
                    compileFile(file, fMonitor);
                }
            }
            return true;
        }
    }

    private class DeltaVisitor implements IResourceDeltaVisitor {
        private IProgressMonitor fMonitor;

        /**
         * Create a delta visitor
         *
         * @param aMonitor
         *            A progress monitor
         */
        public DeltaVisitor(IProgressMonitor aMonitor) {
            fMonitor = aMonitor;
        }

        /** {@inheritDoc} */
        public boolean visit(IResourceDelta aDelta) {
            boolean visitChildren = false;

            IResource resource = aDelta.getResource();
            if (resource instanceof IProject) {

                // Only check projects with ANTXR nature
                IProject project = (IProject) resource;
                visitChildren = AntxrCorePlugin.getUtil().hasNature(project, AntxrNature.NATURE_ID);
            } else if (resource instanceof IFolder) {
                visitChildren = true;
            } else if (resource instanceof IFile) {

                // Only check ANTXR grammar files
                IFile file = (IFile) resource;
                String ext = file.getFileExtension();
                if (file.exists() && ext != null && ext.equals("antxr")) {
                    switch (aDelta.getKind()) {
                        case IResourceDelta.ADDED:
                        case IResourceDelta.CHANGED:
                            compileFile(file, fMonitor);
                            visitChildren = true;
                            break;

                        case IResourceDelta.REMOVED:
                            // delete the old generated files for this grammar
                            try {
                                String grammarFileName = file.getProjectRelativePath().toString();
                                file.getProject().accept(new CleaningVisitor(fMonitor, grammarFileName));
                            } catch (CoreException e) {
                                AntxrCorePlugin.log(e);
                            }
                            visitChildren = true;
                            break;
                    }
                }
            }
            return visitChildren;
        }
    }

    /**
     * Compile a grammar
     *
     * @param aFile
     *            The grammar file to compile
     * @param aMonitor
     *            A progress monitor
     */
    public void compileFile(IFile aFile, IProgressMonitor aMonitor) {
        String grammarFileName = aFile.getProjectRelativePath().toString();
        try {
            // delete the old generated files for this grammar
            aFile.getProject().accept(new CleaningVisitor(aMonitor, grammarFileName));

            // if it's in a java project, only build it if it's in a source dir
            if (AntxrCorePlugin.getUtil().hasNature(aFile.getProject(), AntxrNature.NATURE_ID)) {
                IProject project = aFile.getProject();
                IJavaProject javaProject = JavaCore.create(project);
                IPath path = aFile.getFullPath();
                boolean ok = false;
                IClasspathEntry[] resolvedClasspath = javaProject.getResolvedClasspath(true);
                for (int i = 0; i < resolvedClasspath.length; i++ ) {
                    IClasspathEntry entry = resolvedClasspath[i];

                    if (entry.getEntryKind() != IClasspathEntry.CPE_SOURCE) {
	                    continue;
                    }

                    IPath entryPath = entry.getPath();
                    if (entryPath.isPrefixOf(path)) {
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    return;
                }
            }
        } catch (CoreException e1) {
            e1.printStackTrace();
        }

        fFile = aFile;

        aMonitor
                .beginTask(
                           AntxrCorePlugin
                                          .getFormattedMessage("AntxrBuilder.compiling", aFile.getFullPath().toString()),
                           4);

        // Remove all markers from this file
        try {
            aFile.deleteMarkers(null, true, IResource.DEPTH_INFINITE);
        } catch (CoreException e) {
            AntxrCorePlugin.log(e);
        }

        // Prepare arguments for ANTXR compiler
        // read the settings for this grammar
        Map<String, Map<String, String>> map = SettingsPersister.readSettings(aFile.getProject());

        List<String> args = createArguments(map, aFile);
        if (AntxrBuilder.DEBUG) {
            System.out.println("Compiling ANTXR grammar '" + aFile.getName() + "': arguments=" + args);
        }

        // Monitor system out and err
        fOriginalOut = System.out;
        fOriginalErr = System.err;
        System.setOut(new PrintStream(new MonitoredOutputStream(this)));
        System.setErr(new PrintStream(new MonitoredOutputStream(this)));

        try {
            // Compile ANTXR grammar file
            AntxrTool tool = new AntxrTool();
            if (!tool.preprocess(args.toArray(new String[args.size()]))) {
                try {
                    aMonitor.worked(1);
                    if (!tool.parse()) {
                        aMonitor.worked(1);
                        if (!tool.generate()) {
                            aMonitor.worked(1);
                            refreshFolder(map, tool, args, aFile, grammarFileName, ResourcesPlugin.getWorkspace()
                                                                                                  .getRoot()
                                                                                                  .findMember(fOutput),
                                          aMonitor, tool.getSourceMaps());
                        } else {

                            // If errors during generate then delete all
                            // generated files
                            deleteFiles(tool, aFile.getParent(), aMonitor);
                        }
                        aMonitor.worked(1);
                    }
                } catch (CoreException e) {
                    AntxrCorePlugin.log(e);
                }
            }

        } catch (Throwable e) {
            if (!(e instanceof SecurityException)) {
                AntxrCorePlugin.log(e);
            }
        } finally {
            System.setOut(fOriginalOut);
            System.setErr(fOriginalErr);
            aMonitor.done();
        }
    }

    /**
     * Returns list of commandline arguments for ANTXR compiler.
     *
     * @param map
     *            the saved antxr-eclipse settings for this project
     * @param file
     *            The grammar file to parse
     * @return a list of command-line arguments
     */
    private List<String> createArguments(Map<String, Map<String, String>> map, IFile file) {
        List<String> args = new ArrayList<String>();

        AntxrCorePlugin.getDefault().upgradeOldSettings(file, map);

        fOutput = SettingsPersister.get(map, file, SettingsPersister.OUTPUT_PROPERTY);
        // Prepare absolute output path (-o)
        String outputPath = null;
        if (fOutput != null && fOutput.trim().length() > 0) {
            outputPath = convertRootRelatedPath(fOutput);
        }
        if (outputPath == null) {
            try {
                IJavaProject javaProject = JavaCore.create(file.getProject());
                IClasspathEntry rawClasspath[] = javaProject.getRawClasspath();
                String longestSourceDirPrefix = "";
                String grammarFilePath = file.getFullPath().toString();
                for (IClasspathEntry entry : rawClasspath) {
                    if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                        String sourceDirString = entry.getPath().toString();
                        if (grammarFilePath.startsWith(sourceDirString) && sourceDirString.length() > longestSourceDirPrefix.length()) {
                            longestSourceDirPrefix = sourceDirString;
                        }
                    }
                }

                if ("".equals(longestSourceDirPrefix)) {
                    return null;
                }
                String basePath = file.getFullPath().removeLastSegments(1).toString();
                String packageName = basePath.substring(longestSourceDirPrefix.length());
                IFolder targetDirFolder = file.getProject().getFolder("antxr-generated/" + packageName);
                if (!targetDirFolder.exists()) {
                    targetDirFolder.getRawLocation().toFile().mkdirs();
                    file.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
                }
                fOutput = targetDirFolder.getFullPath().toString();
                outputPath = targetDirFolder.getLocation().toString();
            } catch (Exception e) {
                throw new RuntimeException("Could not set up antxr-generated as output dir", e);
            }
        }

        args.add("-o");
        args.add(outputPath);

        // Get boolean options from grammar properties
        addBooleanGrammarProperty(map, file, SettingsPersister.DEBUG_PROPERTY, "-debug", null, args, false);
        addBooleanGrammarProperty(map, file, SettingsPersister.HTML_PROPERTY, "-html", null, args, false);
        addBooleanGrammarProperty(map, file, SettingsPersister.DOCBOOK_PROPERTY, "-docbook", null, args, false);
        addBooleanGrammarProperty(map, file, SettingsPersister.DIAGNOSTIC_PROPERTY, "-diagnostic", null, args, false);
        addBooleanGrammarProperty(map, file, SettingsPersister.TRACE_PROPERTY, "-trace", null, args, false);
        addBooleanGrammarProperty(map, file, SettingsPersister.TRACE_PARSER_PROPERTY, "-traceParser", null, args, false);
        addBooleanGrammarProperty(map, file, SettingsPersister.TRACE_LEXER_PROPERTY, "-traceLexer", null, args, false);
        addBooleanGrammarProperty(map, file, SettingsPersister.TRACE_TREE_PARSER_PROPERTY, "-traceTreeParser", null,
                                  args, false);
        addBooleanGrammarProperty(map, file, SettingsPersister.SMAP_PROPERTY, "-smap", null, args, true);

        // Get super grammars from grammar properties
        String superGrammars = SettingsPersister.get(map, file, SettingsPersister.SUPER_GRAMMARS_PROPERTY);

        // Prepare optional super grammar(s) (-glib)
        // Can be defined in two ways : in a property dialog OR
        // inside the grammar using a comment "// -glib <file in same folder>"
        if (superGrammars != null && superGrammars.trim().length() > 0) {
            superGrammars = convertRootRelatedPathList(superGrammars);
            if (superGrammars != null) {
                args.add("-glib");
                args.add(superGrammars);
            }
        } else {
            // Try to get // -glib parameter from comment in .g file. This
            // enables sharing in a team project without local reconfiguration
            String localSuperGrammar = extractGlibComment(fFile);
            if (localSuperGrammar != null) {
                localSuperGrammar = convertFolderRelatedPath(localSuperGrammar);
                if (localSuperGrammar != null) {
                    args.add("-glib");
                    args.add(localSuperGrammar);
                }
            }
        }

        // Prepare ANTXR grammar file which needs to be compiled
        args.add(fFile.getLocation().toOSString());
        return args;
    }

    private void addBooleanGrammarProperty(Map map, IResource grammar, String propertyName, String option,
                                           String option2, List<String> args, boolean defaultValue) {
        boolean value;
        String stringValue = SettingsPersister.get(map, grammar, propertyName);
        if (stringValue == null) {
	        value = defaultValue;
        } else {
	        value = "true".equalsIgnoreCase(stringValue);
        }
        if (value) {
            args.add(option);
            if (option2 != null) {
	            args.add(option2);
            }
        }
    }

    /**
     * Converts given path (related to workspace root) to an absolute path.
     *
     * @param aPath
     *            The path to convert
     * @return The converted path
     */
    private String convertRootRelatedPath(String aPath) {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(aPath);
        if (resource == null) {
            createProblemMarker(0, AntxrCorePlugin.getFormattedMessage("AntxrTool.error.noGrammarFile", aPath),
                                IMarker.SEVERITY_ERROR);
        }
        return (resource != null ? resource.getLocation().toOSString() : null);
    }

    /**
     * Converts given list of paths (related to workspace root) to a new list with absolute paths (delimited by ';').
     *
     * @param aPathList
     *            The paths to convert
     * @return The converted paths
     */
    private String convertRootRelatedPathList(String aPathList) {
        StringBuffer list = new StringBuffer();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        StringTokenizer tokenizer = new StringTokenizer(aPathList, ";");
        while (tokenizer.hasMoreTokens()) {
            String grammar = tokenizer.nextToken();
            IResource resource = root.findMember(grammar);
            if (resource != null) {
                list.append(resource.getLocation().toOSString());
                if (tokenizer.hasMoreTokens()) {
                    list.append(';');
                }
            } else {
                createProblemMarker(0, AntxrCorePlugin.getFormattedMessage("AntxrTool.error.noGrammarFile", grammar),
                                    IMarker.SEVERITY_ERROR);
            }
        }
        return list.toString();
    }

    /**
     * Converts given path (related to folder of grammar file) to an absolute path.
     *
     * @param aPath
     *            The path to convert
     * @return The converted paths
     */
    private String convertFolderRelatedPath(String aPath) {
        IResource resource = fFile.getParent().findMember(aPath.toString());
        if (resource == null) {
            createProblemMarker(0, AntxrCorePlugin.getFormattedMessage("AntxrTool.error.noGrammarFile", aPath),
                                IMarker.SEVERITY_ERROR);
        }
        return (resource != null ? resource.getLocation().toOSString() : null);
    }

    private void deleteFiles(AntxrTool aTool, IContainer aFolder, IProgressMonitor aMonitor) {
        Iterator files = aTool.files();
        while (files.hasNext()) {
            String fileName = (String) files.next();
            if (AntxrBuilder.DEBUG) {
                fOriginalOut.println("Deleting ANTXR generated file '" + fileName + "'");
            }
            IResource file = aFolder.findMember(fileName);
            if (file != null) {
                aMonitor.subTask(AntxrCorePlugin.getFormattedMessage("AntxrBuilder.deleting", fileName));
                try {
                    file.delete(true, aMonitor);
                } catch (CoreException e) {
                    AntxrCorePlugin.log(e);
                }
            }
        }
    }

    private void refreshFolder(Map map, AntxrTool aTool, List args, IFile grammarFile, String grammarFileName,
                               IResource aFolder, IProgressMonitor aMonitor, Map sourceMaps) {
        aMonitor.subTask(AntxrCorePlugin.getFormattedMessage("AntxrBuilder.refreshing", aFolder.getFullPath()
                                                                                               .toString()));
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IContainer folder = root.getContainerForLocation(aFolder.getLocation());
        if (AntxrBuilder.DEBUG) {
            fOriginalOut.println("Refreshing output folder '" + folder.getFullPath() + "'");
        }
        try {
            StringBuffer argString = new StringBuffer();
            for (Iterator i = args.iterator(); i.hasNext();) {
                String arg = (String) i.next();
                argString.append(arg);
                argString.append("|");
            }
            List<IResource> compilationUnitFiles = new ArrayList<IResource>();
            Iterator files = aTool.files();
            while (files.hasNext()) {
                String fileName = (String) files.next();
                if (AntxrBuilder.DEBUG) {
                    fOriginalOut.println("ANTXR generated file '" + fileName + "'");
                }
                IResource file = folder.getFile(new Path(fileName));
                file.refreshLocal(IResource.DEPTH_ZERO, aMonitor);
                // SASSAS check if file is being edited - if so, refresh
                file.setDerived(true);
                String cleanWarnings = SettingsPersister.get(map, grammarFile, SettingsPersister.CLEAN_WARNINGS);
                String installSmap = SettingsPersister.get(map, grammarFile, SettingsPersister.SMAP_PROPERTY);
                file.setPersistentProperty(AntxrBuilder.CLEAN_WARNINGS, cleanWarnings);
                file.setPersistentProperty(AntxrBuilder.INSTALL_SMAP, installSmap);
                file.setPersistentProperty(AntxrBuilder.GRAMMAR_ECLIPSE_PROPERTY, grammarFileName);
                file.setPersistentProperty(AntxrBuilder.COMMAND_LINE_OPTIONS_PROPERTY, argString.toString());
                if (fileName.endsWith(".java")) {
                    compilationUnitFiles.add(file);
                    Map sourceMap = (Map) sourceMaps.get(file.getName());

                    // add source mappings to the generated file
                    if (sourceMap != null) {
	                    for (Iterator i = sourceMap.keySet().iterator(); i.hasNext();) {
                            Integer sourceLine = (Integer) i.next();
                            List targetLines = (List) sourceMap.get(sourceLine);
                            for (Iterator j = targetLines.iterator(); j.hasNext();) {
                                Integer targetLine = (Integer) j.next();
                                IMarker marker = file.createMarker(AntxrCorePlugin.SOURCE_MAPPING_MARKER);
                                marker.setAttribute(AntxrCorePlugin.GRAMMAR_LINE_ATTRIBUTE, sourceLine);
                                marker.setAttribute(AntxrCorePlugin.GENERATED_LINE_ATTRIBUTE, targetLine);
                            }
                        }
                    }
                }
            }
            // WORK-IN-PROGRESS -- NON FUNCTIONAL
            // if (!compilationUnitFiles.isEmpty()) {
            // // find "end of imports" marker in generated code:
            // // $$END-OF-ANTXR-GENERATED-IMPORTS$$
            // int[] oldImportEnds = findEndOfImports(compilationUnitFiles);
            //
            // MultiStatus status = new MultiStatus(AntxrCorePlugin.PLUGIN_ID, 0, "", null);
            // new ImportOrganizer().organizeImports(compilationUnitFiles, status, aMonitor);
            // if (status.getSeverity() != IStatus.OK)
            // AntxrCorePlugin.log(status);
            // for (Iterator i = compilationUnitFiles.iterator(); i.hasNext();) {
            // IFile file = (IFile)i.next();
            // file.setDerived(true);
            // }
            //
            // // find "end of imports" markers after the organize imports
            // int[] newImportEnds = findEndOfImports(compilationUnitFiles);
            //
            // // adjust the smap files based on the offsets
            // adjustSmapFiles(compilationUnitFiles, oldImportEnds, newImportEnds);
            // }
        } catch (OperationCanceledException e) {
            // TODO what to do with cancel?
        } catch (Exception e) {
            AntxrCorePlugin.log(e);
        }
    }

    // WORK-IN-PROGRESS -- NON FUNCTIONAL
    // /**
    // * Adjust the smap line definitions based on line offsets
    // * @param compilationUnitFiles the generated java files
    // * @param oldImportEnds the old location of the "end of imports" marker
    // * @param newImportEnds the new location of the "end of imports" marker
    // */
    // private void adjustSmapFiles(List compilationUnitFiles, int[] oldImportEnds, int[] newImportEnds) {
    // int n = 0;
    // for (Iterator i = compilationUnitFiles.iterator(); i.hasNext(); n++) {
    // IFile file = (IFile)i.next();
    // // skip token type definition files
    // if (file.getName().endsWith("TokenTypes.java"))
    // continue;
    //
    // IPath smapLocation = file.getLocation().removeFileExtension().addFileExtension(".smap");
    // // strip ".java" from the file name and add ".smap"
    // System.out.println(smapLocation.toFile());
    // }
    // }
    //
    // /**
    // * Determine where the "end of imports" marker in the generated code is
    // * @param compilationUnitFiles the file to process
    // * @return an array of line numbers
    // */
    // private int[] findEndOfImports(List compilationUnitFiles) {
    // int[] endOfImports = new int[compilationUnitFiles.size()];
    // int n = 0;
    // for (Iterator i = compilationUnitFiles.iterator(); i.hasNext(); n++) {
    // IFile file = (IFile)i.next();
    // // skip the token type definitions -- they don't have smaps
    // if (file.getName().endsWith("TokenTypes.java"))
    // continue;
    //
    // try {
    // BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents()));
    // String line;
    // while((line = reader.readLine()) != null) {
    // if (line.startsWith("// $$END-OF-ANTXR-GENERATED-IMPORTS$$"))
    // break;
    // endOfImports[n]++;
    // }
    // }
    // catch (Exception e) {
    // AntxrCorePlugin.log(e);
    // }
    // }
    // return endOfImports;
    // }

    /**
     * Try to get -glib parameter from the first single line comment in given grammar file (// -glib <super grammar>).
     * This enables sharing in a team project without local reconfiguration.
     *
     * @param aFile
     *            The grammar file
     * @return The glib comment
     */
    private String extractGlibComment(IFile aFile) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(aFile.getContents()));
            // Skip leading white spaces
            int c = reader.read();
            while (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                c = reader.read();
            }

            // Match leading "//"
            if (c == '/' && reader.read() == '/') {

                // Skip white spaces
                c = reader.read();
                while (c == ' ' || c == '\t') {
                    c = reader.read();
                }

                // Match "-glib"
                if (c == '-' && reader.read() == 'g' && reader.read() == 'l' && reader.read() == 'i'
                    && reader.read() == 'b' && reader.read() == ' ') {
                    // Skip white spaces
                    c = reader.read();
                    while (c == ' ' || c == '\t') {
                        c = reader.read();
                    }

                    // Use rest of line as relative path to super grammar
                    StringBuffer grammar = new StringBuffer();
                    while (c != '\n' && c != '\r') {
                        grammar.append((char) c);
                        c = reader.read();
                    }
                    return grammar.toString();
                }
            }
        } catch (Exception e) {
            AntxrCorePlugin.log(e);
        }
        return null;
    }
}
