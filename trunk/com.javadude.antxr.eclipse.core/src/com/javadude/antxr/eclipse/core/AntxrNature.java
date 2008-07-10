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
package com.javadude.antxr.eclipse.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.javadude.antxr.eclipse.core.builder.AntxrBuilder;
import com.javadude.antxr.eclipse.core.builder.WarningCleanerBuilder;
import com.javadude.antxr.eclipse.smapinstaller.SMapInstallerBuilder;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * An eclipse nature that represents a project that contains ANTXR grammars
 */
public class AntxrNature implements IProjectNature {
    /** The antxr-eclipse nature id */
    public static final String NATURE_ID = AntxrCorePlugin.PLUGIN_ID + ".antxrnature";
    /** The antxr-eclipse plugin debugging optiojn */
    public static final String DEBUG_OPTION = AntxrCorePlugin.PLUGIN_ID + "/nature/debug";

    /** are we in debug mode? */
    public static boolean DEBUG = false;

    private IProject fProject;

    /**
     * Create an instance of the nature
     */
    public AntxrNature() {
        DEBUG = AntxrCorePlugin.isDebug(DEBUG_OPTION);
    }

    public IProject getProject() {
        return fProject;
    }

    public void setProject(IProject aProject) {
        fProject = aProject;
    }

    public void configure() throws CoreException {
        if (DEBUG) {
            System.out.println("configuring ANTXR nature");
        }
        IProject project = getProject();
        IProjectDescription projectDescription = project.getDescription();
        List<ICommand> commands = new ArrayList<ICommand>(Arrays.asList(projectDescription.getBuildSpec()));

        ICommand antxrBuilderCommand = projectDescription.newCommand();
        antxrBuilderCommand.setBuilderName(AntxrBuilder.BUILDER_ID);
        ICommand warningCleanerBuilderCommand = projectDescription.newCommand();
        warningCleanerBuilderCommand.setBuilderName(WarningCleanerBuilder.BUILDER_ID);
        ICommand smapBuilderCommand = projectDescription.newCommand();
        smapBuilderCommand.setBuilderName(SMapInstallerBuilder.BUILDER_ID);

        if (!commands.contains(antxrBuilderCommand)) {
	        commands.add(0, antxrBuilderCommand); // add at start
        }
        if (!commands.contains(warningCleanerBuilderCommand)) {
	        commands.add(warningCleanerBuilderCommand); // add at end
        }
        if (!commands.contains(smapBuilderCommand)) {
	        commands.add(smapBuilderCommand); // add at end
        }

        // Commit the spec change into the project
        projectDescription.setBuildSpec(commands.toArray(new ICommand[commands.size()]));
        getProject().setDescription(projectDescription, null);

//        // add the antxr.jar file to a lib dir
//        IFolder folder = getProject().getFolder("lib");
//        if (!folder.exists()) {
//            folder.create(true, true, null);
//        }
//        IPath rawLocation = folder.getRawLocation();
//        URL antxrJarUrl = AntxrCorePlugin.getDefault().getBundle().getEntry("lib/antxr.jar");
//
//        BufferedOutputStream bout = null;
//        BufferedInputStream bin = null;
//        int b;
//        try {
//            try {
//                bout = new BufferedOutputStream(new FileOutputStream(new File(rawLocation.toFile(), "antxr.jar")));
//                bin = new BufferedInputStream(antxrJarUrl.openStream());
//                while ((b = bin.read()) != -1) {
//                    bout.write(b);
//                }
//            } finally {
//                if (bout != null) {
//                    bout.close();
//                }
//                if (bin != null) {
//                    bin.close();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        folder.refreshLocal(IResource.DEPTH_ONE, null);
//
//        // add the jar to the classpath if not present
//        String jarPath = "/" + getProject().getName() + "/lib/antxr.jar";

        IFolder antxr3GeneratedFolder = getProject().getFolder("antxr-generated");
        if (!antxr3GeneratedFolder.exists()) {
	        antxr3GeneratedFolder.create(true, true, null);
        }
        String generatedSourcePath = "/" + getProject().getName() + "/antxr-generated";

        final IJavaProject javaProject = JavaCore.create(getProject());
        IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
        boolean generatedSourceDirFound = false;
        for (IClasspathEntry classpathEntry : rawClasspath) {
//            if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
//                if (classpathEntry.getPath().toString().equals(jarPath)) {
//                    found = true;
//                }
//            }
            if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                if (classpathEntry.getPath().toString().equals(generatedSourcePath)) {
                    generatedSourceDirFound = true;
                }
            }
        }

        int toAdd = 0;
        if (!generatedSourceDirFound) {
	        toAdd++ ;
        }
        if (!generatedSourceDirFound) {
            final IClasspathEntry newEntries[] = new IClasspathEntry[rawClasspath.length + toAdd];
            System.arraycopy(rawClasspath, 0, newEntries, 0, rawClasspath.length);
//            newEntries[rawClasspath.length] = JavaCore.newLibraryEntry(new Path(jarPath), Path.EMPTY, Path.ROOT);
            if (!generatedSourceDirFound) {
                newEntries[newEntries.length - 1] = JavaCore.newSourceEntry(new Path(generatedSourcePath));
            }
            JavaCore.run(new IWorkspaceRunnable() {
                public void run(IProgressMonitor monitor) throws CoreException {
                    javaProject.setRawClasspath(newEntries, null);
                }
            }, null);
        }
    }

    public void deconfigure() throws CoreException {
        if (DEBUG) {
            System.out.println("deconfiguring ANTXR nature");
        }
        IProject project = getProject();
        IProjectDescription desc = project.getDescription();
        List<ICommand> commands = new ArrayList<ICommand>(Arrays.asList(desc.getBuildSpec()));
        for (Iterator i = commands.iterator(); i.hasNext();) {
            ICommand command = (ICommand) i.next();
            if (command.getBuilderName().equals(AntxrBuilder.BUILDER_ID)
                || command.getBuilderName().equals(SMapInstallerBuilder.BUILDER_ID)
                || command.getBuilderName().equals(WarningCleanerBuilder.BUILDER_ID)) {
	            i.remove();
            }
        }

        // Commit the spec change into the project
        desc.setBuildSpec(commands.toArray(new ICommand[commands.size()]));
        project.setDescription(desc, null);

        // remove the jar from the classpath if present
        // delete the jar from the lib dir
        // if the lib dir is empty, delete it
//        String jarPath = "/" + getProject().getName() + "/lib/antxr.jar";
        String generatedSourcePath = "/" + getProject().getName() + "/antxr-generated";
        final IJavaProject javaProject = JavaCore.create(getProject());
        IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
        final List<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();
        for (IClasspathEntry entry : rawClasspath) {
//            if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
//                if (entry.getPath().toString().equals(jarPath)) {
//                    found = i;
//                    break;
//                }
            if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                if (entry.getPath().toString().equals(generatedSourcePath)) {
                    continue; // skip the antxr-generated source folder
                }
            }
            newEntries.add(entry);
        }

        JavaCore.run(new IWorkspaceRunnable() {
            public void run(IProgressMonitor monitor) throws CoreException {
                 IClasspathEntry entries[] = newEntries.toArray(new IClasspathEntry[newEntries.size()]);
                 javaProject.setRawClasspath(entries, null);
                 IFolder folder = getProject().getFolder("antxr-generated");
                 if (folder.exists() && !hasNonAntxrGeneratedFile(folder)) {
                    folder.delete(true, null);
                } else {
                    AntxrCorePlugin.getUtil().warning(4211, "Did not delete antxr-generated source directory; it contains non-generated source. You should move that source to another source dir; it really doesn't belong there...");
                }
            }
        }, null);
    }

    private boolean hasNonAntxrGeneratedFile(IFolder folder) {
        try {
            boolean hasNonGenerated = false;
            for (IResource resource : folder.members()) {
                if(resource instanceof IFolder) {
                    if (hasNonAntxrGeneratedFile((IFolder)resource)) {
                        hasNonGenerated = true;
                    } else {
                        resource.delete(true, null);
                    }
                    continue;
                }
                if(!resource.isDerived()) {
                    hasNonGenerated = true;
                    continue;
                }
                String gramarFile = resource.getPersistentProperty(AntxrBuilder.GRAMMAR_ECLIPSE_PROPERTY);
                if(gramarFile == null) {
                    hasNonGenerated = true;
                    continue;
                }
                resource.delete(true, null);
            }
            return hasNonGenerated;
        } catch (CoreException e) {
            throw new RuntimeException("Could not determine if we can delete antxr-generated dir", e);
        }
    }
}
