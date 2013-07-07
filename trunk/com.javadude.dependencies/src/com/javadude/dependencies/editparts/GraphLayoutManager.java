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

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.CompoundDirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

class GraphLayoutManager extends AbstractLayout {
    private EditPart editPart;

    GraphLayoutManager(EditPart editPart) {
        this.editPart = editPart;
    }

    @Override
    protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
        // if (state == PLAYBACK)
        // return container.getSize();
        container.validate();
        @SuppressWarnings("unchecked")
		List<IFigure> children = container.getChildren();
        Rectangle result = new Rectangle().setLocation(container.getClientArea().getLocation());

        for (IFigure f : children) {
            result.union(f.getBounds());
        }

        result.resize(container.getInsets().getWidth(), container.getInsets().getHeight());

        return result.getSize();
    }

    @SuppressWarnings("unchecked")
    public void layout(IFigure container) {
        CompoundDirectedGraph graph = new CompoundDirectedGraph();
        Map<GraphicalEditPart,Node> nodes = new HashMap<GraphicalEditPart, Node>();

        Node dummyNode = new Node(null);
        graph.nodes.add(dummyNode);

        List<AbstractConnectionEditPart> sourceConnections = new ArrayList<AbstractConnectionEditPart>();
        for (Iterator<GraphicalEditPart> i = editPart.getChildren().iterator(); i.hasNext();) {
        	GraphicalEditPart part = i.next();
            Node n = new Node(part);
            Rectangle bounds = part.getFigure().getBounds();
            n.width = bounds.width;
            n.height = bounds.height;
            nodes.put(part, n);
            graph.nodes.add(n);

            // keep track of connections for edge setup
            if (part instanceof NodeEditPart) {
                NodeEditPart nodeEditPart = (NodeEditPart) part;
                sourceConnections.addAll(nodeEditPart.getSourceConnections());

                // add any nodes without target dependencies to the dummy node
                if (nodeEditPart.getTargetConnections().isEmpty()) {
                    graph.edges.add(new Edge(null, dummyNode, n));
                }
            }
        }

        for (Iterator<AbstractConnectionEditPart> i = sourceConnections.iterator(); i.hasNext();) {
            AbstractConnectionEditPart part = i.next();
            Node m = (Node) nodes.get(part.getSource());
            Node e = (Node) nodes.get(part.getTarget());
            graph.edges.add(new Edge(part, m, e));
        }

        // layout the graph
        new CompoundDirectedGraphLayout().visit(graph);

        int dummyY = 0;
        for (Iterator<Node> i = graph.nodes.iterator(); i.hasNext();) {
            Node node = i.next();
            if (node.data == null) {
                dummyY = node.y;
            }
        }

        int minGap = Integer.MAX_VALUE;
        for (Iterator<Node> i = graph.nodes.iterator(); i.hasNext();) {
            Node node = i.next();
            if (node.data != null) {
                minGap = Math.min(minGap, node.y - dummyY);
            }
        }

        // update the positions of the figures
        for (Iterator<Node> i = graph.nodes.iterator(); i.hasNext();) {
            Node node = i.next();

            if (node.data == null) {
                continue;
            }

            GraphicalEditPart part = (GraphicalEditPart) node.data;
            Dimension size = part.getFigure().getPreferredSize();
            Rectangle rectangle = new Rectangle(node.x, node.y - minGap, size.width, size.height);
            part.getFigure().setBounds(rectangle);
        }
    }
}