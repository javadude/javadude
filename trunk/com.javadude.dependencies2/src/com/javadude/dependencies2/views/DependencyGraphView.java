package com.javadude.dependencies2.views;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import com.javadude.dependencies2.Activator;

public class DependencyGraphView extends ViewPart {
	private GraphViewer viewer;

	class ViewContentProvider implements IGraphEntityContentProvider {
		private Map<String, IJavaProject> projects = new HashMap<String, IJavaProject>();
		public Object[] getConnectedTo(Object obj) {
			IJavaProject javaProject = (IJavaProject) obj;
			try {
				List<IJavaProject> dependencies = new ArrayList<IJavaProject>();
				IClasspathEntry[] classpath = javaProject.getRawClasspath();
				for (IClasspathEntry classpathEntry : classpath) {
					if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
						IPath path = classpathEntry.getPath();
						dependencies.add(projects.get(path.lastSegment()));
					}
				}
				return dependencies.toArray();
			} catch (JavaModelException e) {
				Activator.error("Error getting dependencies", e);
				throw new RuntimeException(e);
			}
		}

		public Object[] getElements(Object arg0) {
			try {
				IJavaModel javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
				IJavaProject[] javaProjects = javaModel.getJavaProjects();
				for (IJavaProject project : javaProjects) {
					projects.put(project.getElementName(), project);
				}
				return javaProjects;
			} catch (JavaModelException e) {
				Activator.error("Error getting java projects", e);
				throw new RuntimeException(e);
			}
		}

		public void dispose() { /* nothing */ }
		public void inputChanged(Viewer v, Object oldInput, Object newInput) { /* nothing */ }
	}
	class ViewLabelProvider extends LabelProvider {
		@Override
		public String getText(Object obj) {
			if (obj instanceof IJavaProject)
				return ((IJavaProject) obj).getElementName();
			return null;
		}
	}

	@Override public void createPartControl(Composite parent) {
		viewer = new GraphViewer(parent, SWT.NONE);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
		viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						viewer.refresh();
						viewer.applyLayout();
					}
				});
			}
		});
		getSite().setSelectionProvider(viewer);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.javadude.dependencies2.viewer");
	}

	@Override public void setFocus() {
		viewer.getControl().setFocus();
	}
}