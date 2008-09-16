/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield, based on ANTLR-Eclipse plugin
 *   by Torsten Juergeleit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors
 *    Torsten Juergeleit - original ANTLR Eclipse plugin
 *    Scott Stanchfield - modifications for ANTXR
 *******************************************************************************/
package com.javadude.antxr.eclipse.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.AbstractTreeViewer;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.AntxrUIPluginImages;

/**
 * Action that collapses all nodes in the grammar outline
 */
public class CollapseAllAction extends Action {
	private AbstractTreeViewer fViewer;

	/**
	 * Create the action
	 * @param aViewer the viewer for the action
	 */
	public CollapseAllAction(AbstractTreeViewer aViewer) {
		fViewer = aViewer;
        setText(AntxrUIPlugin.getMessage("Editor.CollapseAllAction.label"));
        setToolTipText(AntxrUIPlugin.getMessage(
										  "Editor.CollapseAllAction.tooltip"));
        AntxrUIPluginImages.setLocalImageDescriptors(this, "collapseall.gif");
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		fViewer.collapseAll();
	}
}
