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
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
 *******************************************************************************/
package com.javadude.antxr.debug.misc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.javadude.antxr.ASTFactory;
import com.javadude.antxr.CommonAST;
import com.javadude.antxr.collections.AST;

public class ASTFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    class MyTreeSelectionListener
        implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent event) {
            TreePath path = event.getPath();
            System.out.println("Selected: " +
                               path.getLastPathComponent());
            Object elements[] = path.getPath();
            for (int i = 0; i < elements.length; i++) {
                System.out.print("->" + elements[i]);
            }
            System.out.println();
        }
    }

    public ASTFrame(String lab, AST r) {
        super(lab);

        // Create the TreeSelectionListener
        JTreeASTPanel tp = new JTreeASTPanel(new JTreeASTModel(r), null);
        Container content = getContentPane();
        content.add(tp, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Frame f = (Frame)e.getSource();
                f.setVisible(false);
                f.dispose();
                // System.exit(0);
            }
        });
        setSize(WIDTH, HEIGHT);
    }

    public static void main(String args[]) {
        // Create the tree nodes
        ASTFactory factory = new ASTFactory();
        CommonAST r = (CommonAST)factory.create(0, "ROOT");
        r.addChild(factory.create(0, "C1"));
        r.addChild(factory.create(0, "C2"));
        r.addChild(factory.create(0, "C3"));

        ASTFrame frame = new ASTFrame("AST JTree Example", r);
        frame.setVisible(true);
    }
}
