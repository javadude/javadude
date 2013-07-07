/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.dependencies.editparts;

import java.util.Map;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Subgraph;

public interface GraphContributor {
    static final Insets PADDING = new Insets(8, 6, 8, 6);
    static final Insets INNER_PADDING = new Insets(0);
    void contributeEdgesToGraph(CompoundDirectedGraph graph, @SuppressWarnings("rawtypes") Map map);
    void applyGraphResults(CompoundDirectedGraph graph, @SuppressWarnings("rawtypes") Map map);
    void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, @SuppressWarnings("rawtypes") Map map);
}
