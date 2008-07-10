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
package com.javadude.workingsets;

import com.javadude.common.PluginUtil;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

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
		plugin = this;
        Class.forName(NatureWorkingSetUpdater.class.getName());
        Class.forName(RegExWorkingSetUpdater.class.getName());
	}

	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}
    public static PluginUtil getUtil() {
        if (util_ == null) {
            util_ = new PluginUtil(getDefault().getBundle().getSymbolicName(), getDefault().getLog());
        }
        return util_;
    }
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin("com.hcrest.classpath", path);
	}
}
