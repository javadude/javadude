/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
 *   Scott Stanchfield - Modifications for XML Parsing
 *******************************************************************************/
package com.javadude.antxr.debug.misc;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;

public class JTreeASTPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JTree tree;

    public JTreeASTPanel(TreeModel tm, TreeSelectionListener listener) {
        // use a layout that will stretch tree to panel size
        setLayout(new BorderLayout());

        // Create tree
        tree = new JTree(tm);

        // Change line style
        tree.putClientProperty("JTree.lineStyle", "Angled");

        // Add TreeSelectionListener
        if (listener != null) {
	        tree.addTreeSelectionListener(listener);
        }

        // Put tree in a scrollable pane's viewport
        JScrollPane sp = new JScrollPane();
        sp.getViewport().add(tree);

        add(sp, BorderLayout.CENTER);
    }
}
