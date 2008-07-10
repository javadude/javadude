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
package com.javadude.antxr.eclipse.ui;

import java.net.URL;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.javadude.common.PluginUtil;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Central access point for the ANTXR UI plug-in
 * (id <code>"com.javadude.antxr.eclipse.ui"</code>).
 *
 * @author Torsten Juergeleit
 */
public class AntxrUIPlugin extends AbstractUIPlugin {
    private static PluginUtil util_;

    /** The id of the ANTXR plugin
     * (value <code>"com.javadude.antxr.eclipse.ui"</code>). */
    public static final String PLUGIN_ID = "com.javadude.antxr.eclipse.ui";

    /** Singleton instance of this plugin */
    private static AntxrUIPlugin plugin;

    private static final String RESOURCE_NAME = PLUGIN_ID + ".messages";
    private ResourceBundle resourceBundle;

    /**
     * Create the plugin instance
     */
    public AntxrUIPlugin() {
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME);
        } catch (MissingResourceException e) {
            log(e);
            resourceBundle = null;
        }
    }

    /**
     * Returns the shared instance.
     * @return the shared instance
     */
    public static AntxrUIPlugin getDefault() {
        return plugin;
    }

    /**
     * Get the resource bundle
     * @return the resource bundle
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Get the workspace
     * @return the workspace
     */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    /**
     * Get the active workbench shell
     * @return the shell
     */
    public static Shell getActiveWorkbenchShell() {
        IWorkbenchWindow window = getActiveWorkbenchWindow();
        return (window != null ? window.getShell() : null);
    }

    /**
     * Get the active window
     * @return the active window
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }


    /**
     * Get the active page
     * @return the active page
     */
    public static IWorkbenchPage getActiveWorkbenchPage() {
        return getDefault().getWorkbench().getActiveWorkbenchWindow().
                                                               getActivePage();
    }

    /**
     * Get my installation URL
     * @return my installation URL
     */
    public static URL getInstallURL() {
        return Platform.getBundle((PLUGIN_ID)).getEntry("/");
    }

    /**
     * Get my plugin id
     * @return my plugin id
     */
    public static String getUniqueIdentifier() {
        return Platform.getBundle((PLUGIN_ID)).getSymbolicName();
    }

    /**
     * Log a status
     * @param aStatus the status to log
     */
    public static void log(IStatus aStatus) {
        getDefault().getLog().log(aStatus);
    }

    /**
     * log an exception
     * @param aThrowable the exception to log
     */
    public static void log(Throwable aThrowable) {
        log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK,
                        getMessage("Plugin.internal_error"),
                        aThrowable));
    }

    /**
     * log an error message
     * @param aMessage the message to log
     */
    public static void logErrorMessage(String aMessage) {
        log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, aMessage, null));
    }


    /**
     * log an error status
     * @param aMessage the message
     * @param aStatus the status
     */
    public static void logErrorStatus(String aMessage, IStatus aStatus) {
        if (aStatus == null) {
            logErrorMessage(aMessage);
        } else {
            MultiStatus multi = new MultiStatus(PLUGIN_ID, IStatus.OK,
                                                aMessage, null);
            multi.add(aStatus);
            log(multi);
        }
    }

    /**
     * Are we in debug mode?
     * @return true for debug mode; false otherwise
     */
    public static boolean isDebug() {
        return getDefault().isDebugging();
    }

    /**
     * Translate a key to a bundled message
     * @param aKey the key
     * @return the message
     */
    public static String getMessage(String aKey) {
        String bundleString;
        ResourceBundle bundle = getDefault().getResourceBundle();
        if (bundle != null) {
            try {
                bundleString = bundle.getString(aKey);
            } catch (MissingResourceException e) {
                log(e);
                bundleString = "!" + aKey + "!";
            }
        } else {
            bundleString = "!" + aKey + "!";
        }
        return bundleString;
    }

    /**
     * Translate a message with an argument
     * @param aKey the key
     * @param anArg the argument
     * @return the formatted messsage
     */
    public static String getFormattedMessage(String aKey, String anArg) {
        return getFormattedMessage(aKey, new String[] { anArg });
    }

    /**
     * Translate a message with arguments
     * @param aKey the key
     * @param anArgs the arguments
     * @return the formatted messsage
     */
    public static String getFormattedMessage(String aKey, String[] anArgs) {
        return MessageFormat.format(getMessage(aKey), (Object[]) anArgs);
    }

    /**
     * This method is called upon plug-in activation
     * @param context the bundle description
     * @throws Exception something evil happened
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
     * This method is called when the plug-in is stopped
     * @param context the bundle description
     * @throws Exception something evil happened
     */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
        resourceBundle = null;
    }

    public static PluginUtil getUtil() {
        if (util_ == null) {
            util_ = new PluginUtil(getDefault().getBundle().getSymbolicName(), getDefault().getLog());
        }
        return util_;
    }
}
