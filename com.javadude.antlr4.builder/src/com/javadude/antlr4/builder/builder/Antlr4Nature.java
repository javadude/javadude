package com.javadude.antlr4.builder.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class Antlr4Nature implements IProjectNature {
	public static final String generatedSourceDirName = ".antlr4-generated";
	public static final String NATURE_ID = "com.javadude.antlr4.builder.antlr4Nature";

	private IProject project;

	public void configure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();

		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(Antlr4Builder.BUILDER_ID)) {
				return;
			}
		}

		ICommand[] newCommands = new ICommand[commands.length + 1];
		System.arraycopy(commands, 0, newCommands, 1, commands.length);
		ICommand command = desc.newCommand();
		command.setBuilderName(Antlr4Builder.BUILDER_ID);
		newCommands[0] = command;
		desc.setBuildSpec(newCommands);
		project.setDescription(desc, null);
		
		// set up the .antlr4-generated dir 
        getGeneratedFolder(getProject());
	}

	public static IFolder getGeneratedFolder(IProject project) throws CoreException, JavaModelException {
		IFolder antlr4GeneratedFolder = project.getFolder(generatedSourceDirName);
        if (!antlr4GeneratedFolder.exists()) {
	        antlr4GeneratedFolder.create(true, true, null);
        }
        String generatedSourcePath = "/" + project.getName() + "/" + generatedSourceDirName;

        final IJavaProject javaProject = JavaCore.create(project);
        IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
        boolean generatedSourceDirFound = false;
        for (IClasspathEntry classpathEntry : rawClasspath) {
            if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                if (classpathEntry.getPath().toString().equals(generatedSourcePath)) {
                    generatedSourceDirFound = true;
                }
            }
        }

        if (!generatedSourceDirFound) {
            final IClasspathEntry newEntries[] = new IClasspathEntry[rawClasspath.length + 1];
            System.arraycopy(rawClasspath, 0, newEntries, 0, rawClasspath.length);
            newEntries[newEntries.length - 1] = JavaCore.newSourceEntry(new Path(generatedSourcePath));
            JavaCore.run(new IWorkspaceRunnable() {
                public void run(IProgressMonitor monitor) throws CoreException {
                    javaProject.setRawClasspath(newEntries, null);
                }
            }, null);
        }
        return antlr4GeneratedFolder;
	}

	public void deconfigure() throws CoreException {
		IProject project = getProject();
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(Antlr4Builder.BUILDER_ID)) {
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i,
						commands.length - i - 1);
				description.setBuildSpec(newCommands);
				project.setDescription(description, null);			
				return;
			}
		}
		
		// remove the generated source dir
        String generatedSourcePath = "/" + getProject().getName() + "/" + generatedSourceDirName;
        final IJavaProject javaProject = JavaCore.create(getProject());
        IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
        final List<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();
        for (IClasspathEntry entry : rawClasspath) {
            if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                if (entry.getPath().toString().equals(generatedSourcePath)) {
                    continue; // skip the antlr4-generated source folder
                }
            }
            newEntries.add(entry);
        }

        JavaCore.run(new IWorkspaceRunnable() {
            public void run(IProgressMonitor monitor) throws CoreException {
                 IClasspathEntry entries[] = newEntries.toArray(new IClasspathEntry[newEntries.size()]);
                 javaProject.setRawClasspath(entries, null);
                 IFolder folder = getProject().getFolder(generatedSourceDirName);
                 if (folder.exists())
                    folder.delete(true, null);
            }
        }, null);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}
}
