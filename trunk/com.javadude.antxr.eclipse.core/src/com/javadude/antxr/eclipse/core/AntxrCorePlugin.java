/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield, based on ANTLR-Eclipse plugin
 *   by Torsten Juergeleit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors
 *    Torsten Juergeleit - original ANTLR Eclipse plugin
 *    Scott Stanchfield - modifications for ANTXR
 *******************************************************************************/
package com.javadude.antxr.eclipse.core;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.BundleContext;

import com.javadude.antxr.eclipse.core.properties.SettingsPersister;
import com.javadude.common.PluginUtil;

/**
 * Central access point for the ANTXR Core plug-in
 * (id <code>"com.javadude.antxr.eclipse.core"</code>).
 *
 * @author Torsten Juergeleit
 * @author Scott Stanchfield
 */
public class AntxrCorePlugin extends Plugin {
    private static PluginUtil util_;
    static {
        // tell ANTXR not to call System.exit
        System.setProperty("ANTXR_DO_NOT_EXIT", "true");
        // tell ANTXR not to use context class loaders
        System.setProperty("ANTXR_USE_DIRECT_CLASS_LOADING", "true");
    }

    public static final String VERSION = "2.7.6";

    /** The name of the plugin version property in the settings file */
    public static final String PLUGIN_RESOURCE_NAME = "**ANTXR-ECLIPSE-PLUGIN**";

    /** ID of the ANTXR core plugin
     * (value <code>"com.javadude.antxr.eclipse.core"</code>) */
    public static final String PLUGIN_ID = "com.javadude.antxr.eclipse.core";

    /** The name of the source mapping markers */
    public static final String SOURCE_MAPPING_MARKER = AntxrCorePlugin.PLUGIN_ID + ".sourceMapMarker";

    /** The name of the source mapping grammar line attribute */
    public static final String GRAMMAR_LINE_ATTRIBUTE = "grammarLine";

    /** The name of the source mapping generated line attribute */
    public static final String GENERATED_LINE_ATTRIBUTE = "generatedLine";

    /** Name of the property holding the output path for generated files */
    public static final QualifiedName OLD_OUTPUT_PROPERTY =
                                new QualifiedName(AntxrCorePlugin.PLUGIN_ID, "AntxrOutput");

    /** Name of the property holding a list of paths to super grammar files
     * (delimited by ';') */
    public static final QualifiedName OLD_SUPER_GRAMMARS_PROPERTY =
                            new QualifiedName(AntxrCorePlugin.PLUGIN_ID, "AntxrSuperGrammars");


    /** Singleton instance of this plugin */
    private static AntxrCorePlugin plugin;

    private static final String RESOURCE_NAME = AntxrCorePlugin.PLUGIN_ID + ".messages";
    private ResourceBundle fResourceBundle;

    /**
     *
     */
    public AntxrCorePlugin() {
        AntxrCorePlugin.plugin = this;
        try {
            fResourceBundle = ResourceBundle.getBundle(AntxrCorePlugin.RESOURCE_NAME);
        } catch (MissingResourceException e) {
            AntxrCorePlugin.log(e);
            fResourceBundle = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        super.start(context);
        // Add ANTXR grammar files to JDT builder's copy exclusion filter
        Hashtable options = JavaCore.getOptions();
        String filter = (String) options.get(JavaCore.CORE_JAVA_BUILD_RESOURCE_COPY_FILTER);
        StringTokenizer st = new StringTokenizer(filter, ",");
        boolean found = false;
        while (st.hasMoreTokens()) {
            if (st.nextToken().equals("*.antxr")) {
                found = true;
                break;
            }
        }
        if (!found) {
            if (AntxrCorePlugin.isDebug()) {
                System.out.println("Adding '*.antxr' to JDT builder's " +
                                   "resource filter (" + filter + ")");
            }
            options.put(JavaCore.CORE_JAVA_BUILD_RESOURCE_COPY_FILTER, filter + ",*.antxr");
            JavaCore.setOptions(options);
        }
    }

    /**
     * Returns the shared instance.
     * @return the shared instance
     */
    public static AntxrCorePlugin getDefault() {
        return AntxrCorePlugin.plugin;
    }

    /**
     * Get the resource bundle for the app
     * @return the resource bundle
     */
    public ResourceBundle getResourceBundle() {
        return fResourceBundle;
    }

    /**
     * Get the workspace
     * @return The workspace
     */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    /**
     * Log an error message
     * @param aStatus The status information to log
     */
    public static void log(IStatus aStatus) {
        AntxrCorePlugin.getDefault().getLog().log(aStatus);
    }

    /**
     * Log an exception
     * @param aThrowable The exception to log
     */
    public static void log(Throwable aThrowable) {
        AntxrCorePlugin.log(new Status(IStatus.ERROR, AntxrCorePlugin.PLUGIN_ID, IStatus.OK,
                        AntxrCorePlugin.getMessage("AntxrCorePlugin.internal_error"),
                        aThrowable));
    }

    /**
     * Log an error message
     * @param aMessage the error message to log
     */
    public static void logErrorMessage(String aMessage) {
        AntxrCorePlugin.log(new Status(IStatus.ERROR, AntxrCorePlugin.PLUGIN_ID, IStatus.OK, aMessage,
            null));
    }

    /**
     * Log a message and a status
     * @param aMessage The message
     * @param aStatus The status
     */
    public static void logErrorStatus(String aMessage, IStatus aStatus) {
        if (aStatus == null) {
            AntxrCorePlugin.logErrorMessage(aMessage);
        } else {
            MultiStatus multi = new MultiStatus(AntxrCorePlugin.PLUGIN_ID, IStatus.OK,
                                                aMessage, null);
            multi.add(aStatus);
            AntxrCorePlugin.log(multi);
        }
    }

    /**
     * Are we in debug mode?
     * @return true if in debug mode; false otherwise
     */
    public static boolean isDebug() {
        return AntxrCorePlugin.getDefault().isDebugging();
    }

    /**
     * Is the specified option a valid debug option?
     * @param anOption The option to test
     * @return true if it's a debug option; false otherwise
     */
    public static boolean isDebug(String anOption) {
        boolean debug;
        if (AntxrCorePlugin.isDebug()) {
            String value = Platform.getDebugOption(anOption);
            debug = (value != null && value.equalsIgnoreCase("true") ?
                     true : false);
        } else {
            debug = false;
        }
        return debug;
    }

    /**
     * Translate a key into message text
     * @param aKey The key to translate
     * @return The message text
     */
    public static String getMessage(String aKey) {
        String bundleString;
        ResourceBundle bundle = AntxrCorePlugin.getDefault().getResourceBundle();
        if (bundle != null) {
            try {
                bundleString = bundle.getString(aKey);
            } catch (MissingResourceException e) {
                AntxrCorePlugin.log(e);
                bundleString = "!" + aKey + "!";
            }
        } else {
            bundleString = "!" + aKey + "!";
        }
        return bundleString;
    }

    /**
     * Translate a message with an argument
     * @param aKey The message key
     * @param anArg The argument to replace
     * @return The formatted message
     */
    public static String getFormattedMessage(String aKey, String anArg) {
        return AntxrCorePlugin.getFormattedMessage(aKey, new String[] { anArg });
    }

    /**
     * Translate a message with arguments
     * @param aKey The message key
     * @param anArgs the replacement strings
     * @return The formatted message
     */
    public static String getFormattedMessage(String aKey, String[] anArgs) {
        return MessageFormat.format(AntxrCorePlugin.getMessage(aKey), (Object[]) anArgs);
    }

    /**
     * Upgrade from the old antxr settings to the new ones
     * @param resource the resource to upgrade
     * @param map the new map of settings
     */
    public void upgradeOldSettings(IResource resource, Map<String, Map<String, String>> map) {
        boolean upgraded = false;

        // check for old settings, delete them, and overwrite the defaults
        String oldValue;
        try {
            oldValue = resource.getPersistentProperty(AntxrCorePlugin.OLD_OUTPUT_PROPERTY);
            resource.setPersistentProperty(AntxrCorePlugin.OLD_OUTPUT_PROPERTY, null);
            if (oldValue != null) {
                upgraded = true;
                SettingsPersister.set(map, resource, SettingsPersister.OUTPUT_PROPERTY, oldValue);
            }
        }
        catch (CoreException e) {
            e.printStackTrace();
        }
        try {
            oldValue = resource.getPersistentProperty(AntxrCorePlugin.OLD_SUPER_GRAMMARS_PROPERTY);
            resource.setPersistentProperty(AntxrCorePlugin.OLD_SUPER_GRAMMARS_PROPERTY, null);
            if (oldValue != null) {
                upgraded = true;
                SettingsPersister.set(map, resource, SettingsPersister.SUPER_GRAMMARS_PROPERTY, oldValue);
            }
        }
        catch (CoreException e) {
            e.printStackTrace();
        }

        // if the version # changed, mark the properties with the new plugin version
        // we're not using the version # now, but we may need it for compat later
        if (!AntxrCorePlugin.VERSION.equals(SettingsPersister.get(map, SettingsPersister.PLUGIN_VERSION_PROPERTY))) {
            upgraded = true;
            SettingsPersister.set(map, SettingsPersister.PLUGIN_VERSION_PROPERTY, AntxrCorePlugin.VERSION);
        }

        if (upgraded) {
            SettingsPersister.writeSettings(resource.getProject(), map);
        }
    }
    public static PluginUtil getUtil() {
        if (AntxrCorePlugin.util_ == null) {
            AntxrCorePlugin.util_ = new PluginUtil(AntxrCorePlugin.getDefault().getBundle().getSymbolicName(), AntxrCorePlugin.getDefault().getLog());
        }
        return AntxrCorePlugin.util_;
    }
}
