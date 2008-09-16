/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.workingsets;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.javadude.common.PluginUtil;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
    private static PluginUtil util_;

	// The plug-in ID
	public static final String PLUGIN_ID = "com.javadude.workingsets";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
		// do nothing
	}

	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
        Class.forName(NatureWorkingSetUpdater.class.getName());
        Class.forName(RegExWorkingSetUpdater.class.getName());
	}

	@Override
    public void stop(BundleContext context) throws Exception {
		Activator.plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return Activator.plugin;
	}
    public static PluginUtil getUtil() {
        if (Activator.util_ == null) {
            Activator.util_ = new PluginUtil(Activator.getDefault().getBundle().getSymbolicName(), Activator.getDefault().getLog());
        }
        return Activator.util_;
    }
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("com.hcrest.classpath", path);
	}
}
