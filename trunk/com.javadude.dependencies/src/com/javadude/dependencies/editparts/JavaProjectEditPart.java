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
package com.javadude.dependencies.editparts;

import java.util.List;
import java.util.Map;

import com.javadude.dependencies.DependenciesPlugin;

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

public class JavaProjectEditPart extends AbstractGraphicalEditPart implements NodeEditPart, GraphContributor {
    private static final String WHITE = "white";
    private static final String BLUE = "blue";

    /**
     * Constructor of the ProjectEditPart class
     */
    public JavaProjectEditPart() {
        DependenciesPlugin.getDefault().setColor(BLUE, 0,0,255);
        DependenciesPlugin.getDefault().setColor(WHITE, 255,255,255);
    }

    @Override
    protected IFigure createFigure() {
        Figure tempFigure = new Label();
        tempFigure.setOpaque(true);
        tempFigure.setBorder(new MyRaisedBorder());
        tempFigure.setBackgroundColor(DependenciesPlugin.getDefault().getColor(BLUE));
        tempFigure.setForegroundColor(DependenciesPlugin.getDefault().getColor(WHITE));
        return tempFigure;
    }

    @Override
    protected void refreshVisuals() {
        IJavaProject javaProject = (IJavaProject) getModel();
        ((Label) getFigure()).setText(javaProject.getElementName());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List getModelSourceConnections() {
        return DependencyManager.findSourceDependencies((IJavaProject) getModel());
    }

    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    public void applyGraphResults(CompoundDirectedGraph graph, Map map) {
        Node n = (Node)map.get(this);
        getFigure().setBounds(new Rectangle(n.x, n.y, n.width, n.height));

        for (int i = 0; i < getSourceConnections().size(); i++) {
            DependencyEditPart trans = (DependencyEditPart) getSourceConnections().get(i);
            trans.applyGraphResults(graph, map);
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
    public void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map map) {
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
