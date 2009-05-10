package com.javadude.dependencies2.views;


import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.javadude.dependencies2.Activator;


public class DependencyTableView extends ViewPart {
	private TableViewer viewer;

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) { /* nothing */ }
		public void dispose() { /* nothing */ }
		public Object[] getElements(Object parent) {
			try {
				IJavaModel javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
				return javaModel.getJavaProjects();
			} catch (JavaModelException e) {
				Activator.error("Error getting java projects", e);
				throw new RuntimeException(e);
			}
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			switch (index) {
				case 0: return ((IJavaProject) obj).getElementName();
				case 1:
					IJavaProject javaProject = (IJavaProject) obj;
					try {
						String dependencies = "";
						IClasspathEntry[] classpath = javaProject.getRawClasspath();
						for (IClasspathEntry classpathEntry : classpath) {
							if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
								IPath path = classpathEntry.getPath();
								dependencies += ',' + path.lastSegment();
							}
						}
						if (dependencies.length() > 0)
							return dependencies.substring(1);
						return "";
					} catch (JavaModelException e) {
						e.printStackTrace();
					}
			}
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}
	class NameSorter extends ViewerSorter { /* nothing */ }

	@Override public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		TableColumn column = new TableColumn((Table) viewer.getControl(), SWT.LEFT);
		column.setText("Project");
		column.setResizable(true);
		column.setWidth(200);
		column = new TableColumn((Table) viewer.getControl(), SWT.LEFT);
		column.setText("Dependencies");
		column.setResizable(true);
		column.setWidth(400);
		((Table) viewer.getControl()).setHeaderVisible(true);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		getSite().setSelectionProvider(viewer);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.javadude.dependencies2.viewer");
	}

	@Override public void setFocus() {
		viewer.getControl().setFocus();
	}
}