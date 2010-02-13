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

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.javadude.antlr3.visualizer";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	MyRemoteDebugEventSocketListener remoteListener;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
//		DebugEventListener listener = (DebugEventListener) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] {DebugEventListener.class}, new InvocationHandler() {
//			@Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//				Log.info(42, "ANTLR Event: " + method + " : " + args);
//				return null;
//			}});
//		remoteListener = new MyRemoteDebugEventSocketListener(listener, "localhost", DebugEventSocketProxy.DEFAULT_DEBUGGER_PORT);
//		remoteListener.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
//		remoteListener.stop();
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}
}
