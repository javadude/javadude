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
package com.javadude.antxr.eclipse.smapinstaller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.javadude.antxr.eclipse.core.builder.AntxrBuilder;

/**
 * A custom builder for Java files in Eclipse that removes trailing spaces and
 *   converts leading tabs into spaces. This builder is intended to be
 *   used whenever a java file is saved, and only acts upon files that are
 *   open in the editor.
 */
public class SMapInstallerBuilder extends IncrementalProjectBuilder {
    /** the builder id for the smap installer */
    public static final String BUILDER_ID = "com.javadude.antxr.eclipse.core.smapbuilder";

    /**
     * A delta visitor that will execute against a change made on some
     *   resources. This visitor simply calls spaceConvert when a resource
     *   has changed.
     * Note that delta visitors are called for incremental compilations,
     *   not full builds of the system.
     */
    private IResourceDeltaVisitor deltaVisitor = new IResourceDeltaVisitor() {
        public boolean visit(IResourceDelta delta) throws CoreException {
            if (delta.getKind() == IResourceDelta.CHANGED) {
                installSmap(delta.getResource());
            }
            return true;
        }
    };

    /**
     * A visitor that will execute when the project is rebuilt. This visitor
     *   simply calls spaceConvert against each resource.
     */
    private IResourceVisitor visitor = new IResourceVisitor() {
        public boolean visit(IResource resource) {
            try {
                installSmap(resource);
            }
            catch (JavaModelException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
    };

    /**
     * Installs the modified smap into a generated classfile
     * @param resource
     * @throws JavaModelException
     */
    protected void installSmap(IResource resource) throws JavaModelException {
        // We only work on smap files -- skip everything else
        if (!(resource instanceof IFile)) {
	        return;
        }
        IFile smapIFile = (IFile) resource;
        if (!"smap".equalsIgnoreCase(smapIFile.getFileExtension())) {
	        return;
        }
        IJavaProject javaProject = JavaCore.create(smapIFile.getProject());

        // get the name of the corresponding java source file
        IPath smapPath = smapIFile.getFullPath();


        IClasspathEntry[] classpathEntries = javaProject.getResolvedClasspath(true);
        for (IClasspathEntry entry : classpathEntries) {
            if (entry.getEntryKind() != IClasspathEntry.CPE_SOURCE) {
	            continue;
            }
            if (!entry.getPath().isPrefixOf(smapPath)) {
	            continue;
            }

            // found the right source container
            IPath outputLocation = entry.getOutputLocation();
            if (outputLocation == null) {
	            outputLocation = javaProject.getOutputLocation();
            }
            // strip the source dir and .smap suffix
            String sourceDir = entry.getPath().toString();
            String smapName = smapPath.toString();
            String javaSourceName = smapName.substring(0,smapName.length()-5) + ".java";
            String className = smapName.substring(sourceDir.length(),smapName.length()-5) + ".class";
            IPath path = outputLocation.append(className);
            IPath workspaceLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation();
            IPath classFileLocation = workspaceLoc.append(path);
            IResource classResource = ResourcesPlugin.getWorkspace().getRoot().findMember(javaSourceName);

            File classFile = classFileLocation.toFile();
            File smapFile = smapIFile.getLocation().toFile();
            try {
                String installSmap =
                    classResource.getPersistentProperty(AntxrBuilder.INSTALL_SMAP);
                if ("true".equals(installSmap)) {
	                SDEInstaller.install(classFile, smapFile);
                }
            }
            catch (CoreException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** {@inheritDoc} */
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
            throws CoreException {
        // split the build via incremental or full, and pass the
        //   right visitor to the delta
        if (kind == IncrementalProjectBuilder.FULL_BUILD) {
               getProject().accept(visitor);
        } else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                getProject().accept(visitor);
            } else {
                delta.accept(deltaVisitor);
            }
        }
        return null;
    }
}
