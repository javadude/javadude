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
package com.javadude.antxr;

public class Utils {
    private static boolean useSystemExit = true;
    private static boolean useDirectClassLoading = false;
    static {
        if ("true".equalsIgnoreCase(System.getProperty("ANTXR_DO_NOT_EXIT", "false"))) {
	        Utils.useSystemExit = false;
        }
        if ("true".equalsIgnoreCase(System.getProperty("ANTXR_USE_DIRECT_CLASS_LOADING", "false"))) {
	        Utils.useDirectClassLoading = true;
        }
    }
    public static Class<?> loadClass(String name) throws ClassNotFoundException {
        if (Utils.useDirectClassLoading) {
	        return Class.forName(name);
        }
        return Thread.currentThread().getContextClassLoader().loadClass(name);
    }
    public static Object createInstanceOf(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return Utils.loadClass(name).newInstance();
    }
    public static void error(String message) {
        if (Utils.useSystemExit) {
	        System.exit(1);
        }
        throw new RuntimeException("ANTXR Panic: " + message);
    }
    public static void error(String message, Throwable t) {
        if (Utils.useSystemExit) {
	        System.exit(1);
        }
        throw new RuntimeException("ANTXR Panic", t);
    }
}
