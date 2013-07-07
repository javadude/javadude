/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.dependencies.editparts;

import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.jdt.core.IJavaProject;

import com.javadude.dependencies.DependenciesPlugin;

public class JavaProjectEditPart extends AbstractGraphicalEditPart implements NodeEditPart, GraphContributor {
    private static final String WHITE = "white";
    private static final String BLUE = "blue";

    /**
     * Constructor of the ProjectEditPart class
     */
    public JavaProjectEditPart() {
        DependenciesPlugin.getDefault().setColor(JavaProjectEditPart.BLUE, 0,0,255);
        DependenciesPlugin.getDefault().setColor(JavaProjectEditPart.WHITE, 255,255,255);
    }

    @Override
    protected IFigure createFigure() {
        Figure tempFigure = new Label();
        tempFigure.setOpaque(true);
        tempFigure.setBorder(new MyRaisedBorder());
        tempFigure.setBackgroundColor(DependenciesPlugin.getDefault().getColor(JavaProjectEditPart.BLUE));
        tempFigure.setForegroundColor(DependenciesPlugin.getDefault().getColor(JavaProjectEditPart.WHITE));
        return tempFigure;
    }

    @Override
    protected void refreshVisuals() {
        IJavaProject javaProject = (IJavaProject) getModel();
        ((Label) getFigure()).setText(javaProject.getElementName());
    }

    @SuppressWarnings("rawtypes")
	@Override
    protected List getModelSourceConnections() {
        return DependencyManager.findSourceDependencies((IJavaProject) getModel());
    }

    @SuppressWarnings("rawtypes")
	@Override
    protected List getModelTargetConnections() {
        return DependencyManager.findTargetDependencies((IJavaProject) getModel());
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new NonResizableEditPolicy());
    }

    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }
    public void applyGraphResults(CompoundDirectedGraph graph, @SuppressWarnings("rawtypes") Map map) {
        Node n = (Node)map.get(this);
        getFigure().setBounds(new Rectangle(n.x, n.y, n.width, n.height));

        for (int i = 0; i < getSourceConnections().size(); i++) {
            DependencyEditPart trans = (DependencyEditPart) getSourceConnections().get(i);
            trans.applyGraphResults(graph, map);
        }
    }

    public void contributeEdgesToGraph(CompoundDirectedGraph graph, @SuppressWarnings("rawtypes") Map map) {
        @SuppressWarnings("rawtypes")
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
    public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, @SuppressWarnings("rawtypes") Map map) {
        Node n = new Node(this, s);
        n.outgoingOffset = getAnchorOffset();
        n.incomingOffset = getAnchorOffset();
        n.width = getFigure().getPreferredSize().width;
        n.height = getFigure().getPreferredSize().height;
        n.setPadding(new Insets(10,8,10,12));
        map.put(this, n);
        graph.nodes.add(n);
    }
    int getAnchorOffset() {
        return 9;
    }
}
