/*******************************************************************************
 * Copyright (c) 2010 Scott Stanchfield.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Scott Stanchfield - Work-in-progress - ANTLR 3.x AST Visualization
 *******************************************************************************/
package com.javadude.antlr3.visualizer.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.debug.DebugEventListener;
import org.antlr.runtime.debug.DebugEventSocketProxy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import com.javadude.antlr3.visualizer.Log;
import com.javadude.antlr3.visualizer.MyRemoteDebugEventSocketListener;
import com.javadude.antlr3.visualizer.MyRemoteDebugEventSocketListener.ProxyTree;

public class ANTLRVisualizationView extends ViewPart {
	private Map<String, GraphConnection> connections = new HashMap<String, GraphConnection>();
	private static final Font font = new Font(Display.getDefault(), "Arial", 12, SWT.BOLD);
	private Graph g;
	private GraphNode highlightedFrom, highlightedTo;
	private GraphConnection highlightedConn;
	private Stack<StackItem> ruleStack = new Stack<StackItem>();
	private final List<Token> tokens = new ArrayList<Token>();
	private final List<GraphNode> nodes = new ArrayList<GraphNode>();

	private class StackItem {
		public String ruleName;
		public GraphNode node;
		public StackItem(String ruleName) {
			this.ruleName = ruleName;
		}
	}
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.javadude.antlr3.visualizer.views.ANTLRVisualizationView";

	/**
	 * The constructor.
	 */
	public ANTLRVisualizationView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(6, false));
		final Button connectButton = new Button(parent, SWT.PUSH);
		connectButton.setText("Connect to parser");
		connectButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		connectButton.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent event) {
				try {
					clearGraph();
					clearData();
					remoteListener = new MyRemoteDebugEventSocketListener(new MyDebugEventListener(), "localhost", DebugEventSocketProxy.DEFAULT_DEBUGGER_PORT);
					remoteListener.start();
				} catch (IOException e) {
					Log.error(42, "Error starting socket listener", e);
				}
			} });
		
		g = new Graph(parent, SWT.NONE);
		g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		g.setFont(new Font(Display.getDefault(), "Arial", 24, SWT.BOLD));
		g.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED | ZestStyles.CONNECTIONS_DOT);
		TreeLayoutAlgorithm layoutAlgorithm = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		g.setLayoutAlgorithm(layoutAlgorithm, true);
		layoutAlgorithm.setComparator(new MyComparator());
	}
	private MyRemoteDebugEventSocketListener remoteListener;
	private static class MyComparator implements Comparator<LayoutEntity> {
		public int compare(LayoutEntity o1, LayoutEntity o2) {
			GraphNode n1 = (GraphNode) o1.getGraphData();
			GraphNode n2 = (GraphNode) o2.getGraphData();
			int order1 = (Integer) n1.getData("order");
			int order2 = (Integer) n2.getData("order");
			return order1 - order2;
		}
	}

	@Override
	public void setFocus() {
		g.setFocus();
	}

	@Override public void dispose() {
		g.dispose();
		super.dispose();
	}



	private class MyDebugEventListener implements DebugEventListener {
	
		public void addChild(Object root, Object child) {
			addConnection(root, child);
		}
	
		public void becomeRoot(Object newRoot, Object oldRoot) {
			swapRoot(newRoot, oldRoot);
		}
	
		public void consumeToken(Token token) {
			if (token.getType() != Token.EOF) {
				set(tokens, token.getTokenIndex(), token);
			}
		}
	
		public void createNode(Object node) {
			addASTNode(node);
		}
	
		public void createNode(Object node, org.antlr.runtime.Token token) {
			addASTNode(node, token);
		}
	
		public void enterRule(String grammarFileName, String ruleName) {
			ruleStack.push(new StackItem(ruleName));
		}
	
		public void enterSubRule(int decisionNumber) {
			ruleStack.push(new StackItem(ruleStack.peek().ruleName + "-" + decisionNumber));
		}
	
		public void errorNode(Object t) {
			addASTNode(t);
		}
	
		public void exitRule(String grammarFileName, String ruleName) {
			popStack();
		}
	
		public void exitSubRule(int decisionNumber) {
			popStack();
		}
	
		public void nilNode(Object node) {
			addNilNode(node);
		}
	
		public void terminate() {
			clearData();
			remoteListener.stop();
		}

		public void LT(int i, org.antlr.runtime.Token t) {}
		public void LT(int i, Object t) {}
		public void beginBacktrack(int level) {}
		public void beginResync() {}
		public void commence() {}
		public void consumeHiddenToken(org.antlr.runtime.Token t) {}
		public void consumeNode(Object t) {}
		public void endBacktrack(int level, boolean successful) {}
		public void endResync() {}
		public void enterAlt(int alt) {}
		public void enterDecision(int decisionNumber) {}
		public void exitDecision(int decisionNumber) {}
		public void location(int line, int pos) {}
		public void mark(int marker) {}
		public void recognitionException(RecognitionException e) {}
		public void rewind() {}
		public void rewind(int marker) {}
		public void semanticPredicate(boolean result, String predicate) {}
		public void setTokenBoundaries(Object t, int tokenStartIndex, int tokenStopIndex) {}
	}
	
	private void clearGraph() {
		clearHighlights();
		for (GraphConnection c : connections.values()) {
			c.dispose();
		}
		for (GraphNode n : nodes) {
			n.dispose();
		}
	}
	private void clearData() {
		connections.clear();
		nodes.clear();
		ruleStack.clear();
		tokens.clear();
	}
	private void clearHighlights() {
		if (highlightedFrom != null) {
			if (!highlightedFrom.isDisposed())
				highlightedFrom.unhighlight();
			highlightedFrom = null;
		}
		if (highlightedTo != null) {
			if (!highlightedTo.isDisposed())
				highlightedTo.unhighlight();
			highlightedTo = null;
		}
		if (highlightedConn != null) {
			if (!highlightedConn.isDisposed())
				highlightedConn.unhighlight();
			highlightedConn = null;
		}
	}
	private void highlight(GraphNode from, GraphNode to, GraphConnection conn) {
		clearHighlights();
		from.highlight();
		to.highlight();
		conn.highlight();
		highlightedFrom = from;
		highlightedTo = to;
		highlightedConn = conn;
	}
	private void addASTNode(Object o) {
		ProxyTree ast = (ProxyTree) o;
		addNode(ast.ID, ast, ast.getText(), false);
	}
	private void addASTNode(Object o, Token token) {
		ProxyTree ast = (ProxyTree) o;
		Token realToken = tokens.get(token.getTokenIndex());
		addNode(ast.ID, ast, realToken.getText(), false);
	}
	private void addNilNode(Object o) {
		ProxyTree ast = (ProxyTree) o;
		addNode(ast.ID, ruleStack.peek().ruleName, ruleStack.peek().ruleName, true);
	}
	private void popStack() {
		g.getDisplay().syncExec(new Runnable() {
			public void run() {
				StackItem stackItem = ruleStack.pop();
				// leave the top-level rule node
				if (!ruleStack.isEmpty() && stackItem.node != null) {
					stackItem.node.dispose();
				}
			}});
	}
	private void addNode(final int index, final Object data, final String text, final boolean updateStackItem) {
		g.getDisplay().syncExec(new Runnable() {
			public void run() {
				GraphNode node = new GraphNode(g, ZestStyles.NODES_FISHEYE, data);
				node.setText(text);
				node.setFont(font);
				set(nodes, index, node);
				if (updateStackItem)
					ruleStack.peek().node = node;
				g.applyLayout();
			}});
	}
	private void swapRoot(final Object newRoot, final Object oldRoot) {
		g.getDisplay().syncExec(new Runnable() {
			public void run() {
				ProxyTree newRootAST = (ProxyTree) newRoot;
				ProxyTree oldRootAST = (ProxyTree) oldRoot;
				GraphNode newRootNode = nodes.get(newRootAST.ID);
				GraphNode oldRootNode = nodes.get(oldRootAST.ID);
				@SuppressWarnings("unchecked")
				List<GraphConnection> sourceConnections = oldRootNode.getSourceConnections();
				// for each connection from the old node, add the target to the new node and then dispose it
				for (GraphConnection connection : sourceConnections) {
					GraphNode destination = connection.getDestination();
					doAddConnection(newRootNode, destination, newRootAST, (ProxyTree) connection.getData("child"));
					connection.dispose();
				}
				oldRootNode.dispose();
				g.applyLayout();
			}});
	}
	private GraphConnection doAddConnection(GraphNode from, GraphNode to, ProxyTree fromAST, ProxyTree toAST) {
		GraphConnection conn = new GraphConnection(g, SWT.NONE, from, to);
		int num = from.getSourceConnections().size();
		conn.setData("parent", fromAST);
		conn.setData("child", toAST);
		String name = fromAST.ID + "->" + toAST.ID;
		connections.put(name, conn);
		to.setData("order", num);
		return conn;
	}
	private void addConnection(final Object root, final Object child) {
		g.getDisplay().syncExec(new Runnable() {
			public void run() {
				ProxyTree fromAST = (ProxyTree) root;
				ProxyTree toAST = (ProxyTree) child;
				String name = fromAST.ID + "->" + toAST.ID;
		
				GraphNode fromNode = nodes.get(fromAST.ID);
				GraphNode toNode = nodes.get(toAST.ID);
				GraphConnection conn = connections.get(name);
				if (conn == null) {
					conn = doAddConnection(fromNode, toNode, fromAST, toAST);
				}
				highlight(fromNode, toNode, conn);
				g.applyLayout();
			}});
	}

	private static <T> void set(List<T> list, int index, T value) {
		int size = list.size();
		int missing = index - size;
		if (missing >= 0) {
			for(; missing > 0; missing--)
				list.add(null);
			list.add(value);
		} else {
			list.set(index, value);
		}
	}
}