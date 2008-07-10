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
package com.javadude.antxr.eclipse.ui.actions;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.AntxrUIPluginImages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.AbstractTreeViewer;

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
