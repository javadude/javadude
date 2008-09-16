/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.dependencies.editparts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Display;

import com.javadude.dependencies.DependenciesPlugin;
import com.javadude.dependencies.Dependency;

public class WorkspaceRootEditPart extends AbstractGraphicalEditPart implements GraphContributor {
    public WorkspaceRootEditPart() {
        JavaCore.addElementChangedListener(new IElementChangedListener() {
            public void elementChanged(final ElementChangedEvent event) {
                Display.getDefault().asyncExec(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        //get the workspace changes from the ElementChangedEvent
                        IJavaElementDelta[] changedChildren = event.getDelta().getAffectedChildren();

                        boolean refreshProjects = false;

                        for (int i = 0; i < changedChildren.length; i++) {
                            IJavaElementDelta delta = changedChildren[i];
                            //If the changed element is a java project
                            if (delta.getElement() instanceof IJavaProject) {
                                refreshProjects = true;
                            }
                        }

                        refresh();

                        if (refreshProjects) {
                            for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
                                AbstractGraphicalEditPart editPart = (AbstractGraphicalEditPart) iter.next();
                                editPart.refresh();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    protected IFigure createFigure() {
        Figure tempFigure = new Figure();
        tempFigure.setOpaque(true);

        // set the layout manager to be a graph layout manager.  this will ensure the user
        // does not move the project or dependencies in the view.
        tempFigure.setLayoutManager(new GraphLayoutManager(this));
        return tempFigure;
    }

    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List getModelChildren() {
        List<String> geronimoLibs = new ArrayList<String>();

        // return the java projects
        IWorkspaceRoot root = (IWorkspaceRoot) getModel();
        List<IJavaProject> javaProjects = new ArrayList<IJavaProject>();
        Map<IPath, IJavaProject> projectFinder = new HashMap<IPath, IJavaProject>();
        for (int i = 0; i < root.getProjects().length; i++) {
            IProject project = root.getProjects()[i];
            try {
                if (project.isOpen() && project.hasNature(JavaCore.NATURE_ID)) {
                    IJavaProject javaProject = JavaCore.create(project);
                    javaProjects.add(javaProject);
                    projectFinder.put(javaProject.getPath(), javaProject);
                }
            } catch (CoreException e) {
                DependenciesPlugin.error(142, "Could not check project nature", e);
            }
        }

        // need to make this more efficient -- should only process deltas
        // set up dependencies
        DependencyManager.clear();

        for (Iterator iter = javaProjects.iterator(); iter.hasNext();) {
            IJavaProject project = (IJavaProject) iter.next();
            try {
                IClasspathEntry[] rawClasspath = project.getRawClasspath();
                for (int i = 0; i < rawClasspath.length; i++) {
                    IClasspathEntry entry = rawClasspath[i];
                    if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
                        IPath path = entry.getPath();
                        IJavaProject targetProject = projectFinder.get(path);
                        Dependency dependency = new Dependency(project, targetProject, entry.isExported());
                        DependencyManager.add(dependency);
                    }
                }
            } catch (JavaModelException e) {
                DependenciesPlugin.error(342, "Could not get classpath", e);
            }
        }

        List<Object> results = new ArrayList<Object>(javaProjects);
        results.addAll(geronimoLibs);
        return results;
    }

    @Override
    protected void createEditPolicies() {
        // do nothing
    }


    @SuppressWarnings("unchecked")
    public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map map) {
//        GraphAnimation.recordInitialState(getContentPane());
        Subgraph me = new Subgraph(this, s);
//      me.rowOrder = getActivity().getSortIndex();
        me.outgoingOffset = 5;
        me.incomingOffset = 5;
//        IFigure fig = getFigure();
        me.innerPadding = GraphContributor.INNER_PADDING;
        me.setPadding(GraphContributor.PADDING);
        map.put(this, me);
        graph.nodes.add(me);
        for (int i = 0; i < getChildren().size(); i++) {
            GraphContributor activity = (GraphContributor)getChildren().get(i);
            activity.contributeNodesToGraph(graph, me, map);
        }
    }
    @SuppressWarnings("unchecked")
    public void contributeEdgesToGraph(CompoundDirectedGraph graph, Map map) {
        List outgoing = getSourceConnections();
        for (int i = 0; i < outgoing.size(); i++) {
            DependencyEditPart part = (DependencyEditPart)getSourceConnections().get(i);
            part.contributeToGraph(graph, map);
        }
        for (int i = 0; i < getChildren().size(); i++) {
            GraphContributor child = (GraphContributor)children.get(i);
            child.contributeEdgesToGraph(graph, map);
        }
    }
    @SuppressWarnings("unchecked")
    protected void applyChildrenResults(CompoundDirectedGraph graph, Map map) {
        for (int i = 0; i < getChildren().size(); i++) {
            GraphContributor part = (GraphContributor)getChildren().get(i);
            part.applyGraphResults(graph, map);
        }
    }

    @SuppressWarnings("unchecked")
    public void applyGraphResults(CompoundDirectedGraph graph, Map map) {
        Node n = (Node)map.get(this);
        getFigure().setBounds(new Rectangle(n.x, n.y, n.width, n.height));

        for (int i = 0; i < getSourceConnections().size(); i++) {
            DependencyEditPart trans = (DependencyEditPart) getSourceConnections().get(i);
            trans.applyGraphResults(graph, map);
        }
        applyChildrenResults(graph, map);
    }
}
