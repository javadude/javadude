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
package com.javadude.dependencies;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class DependenciesPlugin extends AbstractUIPlugin {
    public static final String PLUGIN_ID = "com.javadude.dependencies";
    //Color registry instance to allow the view to use colors
    private ColorRegistry colorRegistry;

    //The shared instance.
    private static DependenciesPlugin plugin;

    /**
     * The constructor.
     */
    public DependenciesPlugin() {
        plugin = this;
    }

    /**
     * This method is called upon plug-in activation
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
     * This method is called when the plug-in is stopped
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
     * Returns the shared instance.
     */
    public static DependenciesPlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("com.javadude.dependencies", path);
    }

    public static void error(int code, String message, Throwable t) {
        IStatus status =
            new Status(IStatus.ERROR, PLUGIN_ID, code, message, t);
        ErrorDialog.openError(Display.getDefault().getActiveShell(), "Property Editor Error", message, status);
        getDefault().getLog().log(status);
    }

    /**
     * Get a color from the color registry
     */
    public Color getColor(String colorName) {
        if (colorRegistry == null) {
            colorRegistry = new ColorRegistry();
        }

        return colorRegistry.get(colorName);
    }

    /**
     * Set a color in the color registry
     */
    public void setColor(String colorName, int red, int green, int blue) {
        if (colorRegistry == null) {
            colorRegistry = new ColorRegistry();
        }
        colorRegistry.put(colorName, new RGB(red, green, blue));
    }
}
