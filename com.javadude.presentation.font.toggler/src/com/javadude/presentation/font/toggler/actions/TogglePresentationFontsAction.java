/*******************************************************************************
 * Copyright (c) 2009 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.presentation.font.toggler.actions;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.javadude.presentation.font.toggler.Activator;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class TogglePresentationFontsAction implements IWorkbenchWindowActionDelegate {
//	private IWorkbenchWindow window;
	private static final String PRESENTATION = "1|Consolas|15.75|1|WINDOWS|1|-21|0|0|0|700|0|0|0|0|3|2|1|49|Consolas";
	private static final String PROGRAMMING = "1|Consolas|9.75|0|WINDOWS|1|-13|0|0|0|400|0|0|0|0|3|2|1|49|Consolas";

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		try {
			ScopedPreferenceStore store = new ScopedPreferenceStore(new InstanceScope(), "org.eclipse.ui.workbench");
			String value = store.getString("org.eclipse.jface.textfont");
			if (PRESENTATION.equals(value))
				value = PROGRAMMING;
			else
				value = PRESENTATION;
			store.setValue("org.eclipse.jface.textfont", value);
			store.save();
		} catch (Exception e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error switching font", e));
		}
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// do nothing
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
		// do nothing
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
//		this.window = window;
	}
}