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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.javadude.dependencies.DependenciesPlugin;
import com.javadude.dependencies.Dependency;
import com.javadude.dependencies.commands.DeleteCommand;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

public class DependencyEditPart extends AbstractConnectionEditPart {
    private static final String GREEN = "green";
    private static final String RED = "red";
    private static final String YELLOW = "yellow";
    private static final String BLACK = "black";
    private static final String BLUE = "blue";
    private static final String WHITE = "white";
    private static final String PINK = "pink";
    private String lineColor = BLACK;
    private IWorkbenchPage page;

    /**
     * Constructor of the ProjectEditPart class
     */
    public DependencyEditPart(IWorkbenchPage page) {
        this.page = page;
        DependenciesPlugin.getDefault().setColor(GREEN, 0,255,0);
        DependenciesPlugin.getDefault().setColor(PINK, 255,20,147);
        DependenciesPlugin.getDefault().setColor(YELLOW, 250,250,210);
        DependenciesPlugin.getDefault().setColor(RED, 255,0,0);
        DependenciesPlugin.getDefault().setColor(BLACK, 0,0,0);
        DependenciesPlugin.getDefault().setColor(BLUE, 0,0,255);
        DependenciesPlugin.getDefault().setColor(WHITE, 255,255,255);
    }

    private ISelectionListener listener = new ISelectionListener() {
        @SuppressWarnings("unchecked")
        public void selectionChanged(IWorkbenchPart part, ISelection selection) {
            if (selection instanceof IStructuredSelection) {
                Dependency dependency = (Dependency) getModel();

                lineColor = BLACK;
                
                if (DependencyManager.indirectPathExists(dependency, dependency.getSource(), dependency.getTarget())) {
                	lineColor = WHITE;
            	} else {
            		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            		boolean isSource = false;
            		boolean isTarget = false;
            		for (Iterator i = structuredSelection.iterator(); i.hasNext();) {
            			Object o = i.next();
            			if (o instanceof JavaProjectEditPart) {
            				EditPart editPart = (EditPart) o;
            				o = editPart.getModel();
            			}
            			if (o instanceof IJavaProject) {
            				if (dependency.getSource().equals(o)) {
            					isSource = true;
            				} else if (dependency.getTarget().equals(o)) {
            					isTarget = true;
            				}
            			}
            		}
            		if (isSource && isTarget) {
	                    lineColor = BLUE;
	                } else if (isSource) {
	                    lineColor = GREEN;
	                } else if (isTarget) {
	                    lineColor = RED;
	                } else {
	                    lineColor = BLACK;
	                }
            	}
                refreshVisuals();
            }
        }
    };
    /**
     * Creates a PolylineConnection that represents a dependency
     */
    @Override
    protected IFigure createFigure() {
        PolylineConnection connection = new PolylineConnection();

        // set the target decoration to be an arrow
        connection.setTargetDecoration(new PolygonDecoration());

        return connection;
    }

    @Override
    protected void refreshVisuals() {
        Dependency dependency = (Dependency) getModel();
        if (!dependency.isExported()) {
        	((PolylineConnection) getFigure()).setLineStyle(Graphics.LINE_DOT);
        	lineColor = PINK;
        }
        if (DependencyManager.indirectPathExists(dependency, dependency.getSource(), dependency.getTarget())) {
        	lineColor = WHITE;
    	}
        figure.setForegroundColor(DependenciesPlugin.getDefault().getColor(lineColor));
    }

    /**
     * Create edit policies for the DependencyEditPart. Allows a dependency to
     * be selected and deleted.
     */
    @Override
    protected void createEditPolicies() {
        // Selection handle edit policy. Makes the connection show a feedback,
        // when selected by the user.
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
                new ConnectionEndpointEditPolicy());

        // Edit policy to allow the removal of a dependency. To support the
        // removal of a dependency,
        // a DeleteCommand instance is created
        installEditPolicy(EditPolicy.CONNECTION_ROLE,
                new ConnectionEditPolicy() {
                    @Override
                    protected Command getDeleteCommand(GroupRequest request) {
                        return new DeleteCommand((Dependency) getModel());
                    }
                });
    }

    @Override
    public void activate() {
        super.activate();
        page.addSelectionListener(listener);
    }

    @Override
    public void deactivate() {
        page.removeSelectionListener(listener);
        super.deactivate();
    }

    @SuppressWarnings("unchecked")
    public void contributeToGraph(CompoundDirectedGraph graph, Map map) {
        Node source = (Node)map.get(getSource());
        Node target = (Node)map.get(getTarget());
        Edge e = new Edge(this, source, target);
        e.weight = 2;
        graph.edges.add(e);
        map.put(this, e);
    }
    @SuppressWarnings("unchecked")
    protected void applyGraphResults(CompoundDirectedGraph graph, Map map) {
        Edge e = (Edge)map.get(this);
        NodeList nodes = e.vNodes;
        PolylineConnection conn = (PolylineConnection)getConnectionFigure();
        conn.setTargetDecoration(new PolygonDecoration());
        if (nodes != null) {
            List<AbsoluteBendpoint> bends = new ArrayList<AbsoluteBendpoint>();
            for (int i = 0; i < nodes.size(); i++) {
                Node vn = nodes.getNode(i);
                int x = vn.x;
                int y = vn.y;
                if (e.isFeedback()) {
                    bends.add(new AbsoluteBendpoint(x, y + vn.height));
                    bends.add(new AbsoluteBendpoint(x, y));
                } else {
                    bends.add(new AbsoluteBendpoint(x, y));
                    bends.add(new AbsoluteBendpoint(x, y + vn.height));
                }
            }
            conn.setRoutingConstraint(bends);
        } else {
            conn.setRoutingConstraint(Collections.EMPTY_LIST);
        }
    }

}
