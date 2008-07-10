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
package com.javadude.antxr;

public class Utils {
    private static boolean useSystemExit = true;
    private static boolean useDirectClassLoading = false;
    static {
        if ("true".equalsIgnoreCase(System.getProperty("ANTXR_DO_NOT_EXIT", "false"))) {
	        useSystemExit = false;
        }
        if ("true".equalsIgnoreCase(System.getProperty("ANTXR_USE_DIRECT_CLASS_LOADING", "false"))) {
	        useDirectClassLoading = true;
        }
    }
    public static Class<?> loadClass(String name) throws ClassNotFoundException {
        if (useDirectClassLoading) {
	        return Class.forName(name);
        }
        return Thread.currentThread().getContextClassLoader().loadClass(name);
    }
    public static Object createInstanceOf(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return loadClass(name).newInstance();
    }
    public static void error(String message) {
        if (useSystemExit) {
	        System.exit(1);
        }
        throw new RuntimeException("ANTXR Panic: " + message);
    }
    public static void error(String message, Throwable t) {
        if (useSystemExit) {
	        System.exit(1);
        }
        throw new RuntimeException("ANTXR Panic", t);
    }
}
