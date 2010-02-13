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
package com.javadude.antlr3.visualizer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public final class Log {
	private Log() {}
	
    /**
     * Log an error.
     * @param message the message
     */
    public static void error(int code, String message) {
        error(code, message, null);
    }
    /**
     * Log a warning.
     * @param message the message
     */
    public static void warning(int code, String message) {
        warning(code, message, null);
    }
    /**
     * Log an info message.
     * @param message the message
     */
    public static void info(int code, String message) {
        info(code, message, null);
    }
    /**
     * Log an error.
     * @param message the message
     * @param throwable an optional exception
     */
    public static void error(int code, final String message, Throwable throwable) {
        IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, code, message, throwable);
        Activator.getDefault().getLog().log(status);
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                MessageDialog.openWarning(null, "Error: " + message, message);
            }});
    }
    /**
     * Log a warning.
     * @param message the message
     * @param throwable an optional exception
     */
    public static void warning(int code, final String message, Throwable throwable) {
        IStatus status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, code, message, throwable);
        Activator.getDefault().getLog().log(status);
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                MessageDialog.openWarning(null, "Warning: " + message, message);
            }});
    }
    /**
     * Log an info message.
     * @param message the message
     * @param throwable an optional exception
     */
    public static void info(int code, String message, Throwable throwable) {
        IStatus status = new Status(IStatus.INFO, Activator.PLUGIN_ID, code, message, throwable);
        Activator.getDefault().getLog().log(status);
    }
}
