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

import java.util.Map;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Subgraph;

@SuppressWarnings("unchecked")
public interface GraphContributor {
    static final Insets PADDING = new Insets(8, 6, 8, 6);
    static final Insets INNER_PADDING = new Insets(0);
    void contributeEdgesToGraph(CompoundDirectedGraph graph, Map map);
    void applyGraphResults(CompoundDirectedGraph graph, Map map);
    void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map map);
}
