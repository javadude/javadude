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
package com.javadude.antxr.eclipse.core.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.javadude.antxr.eclipse.core.AntxrCorePlugin;
import com.javadude.antxr.scanner.BasicCrimsonXMLTokenStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

/**
 * Utility class to manager persistence of an antxr properties file
 */
public class SettingsPersister {
    /** Name of the property specifying normal output turned on */
    public static final String PLUGIN_VERSION_PROPERTY = "pluginVersion";

    /** Name of the property specifying normal output turned on */
    public static final String NORMAL_PROPERTY = "normalOuput";

    /** Name of the property specifying if the -html option is turned on */
    public static final String HTML_PROPERTY = "htmlOutput";

    /** Name of the property specifying if the -docbook option is turned on */
    public static final String DOCBOOK_PROPERTY = "docbookOutput";

    /** Name of the property specifying if the -diagnostic option is turned on */
    public static final String DIAGNOSTIC_PROPERTY = "diagnosticOutput";

    /** Name of the property specifying no trace options are turned on */
    public static final String NOTRACE_PROPERTY = "noTrace";

    /** Name of the property specifying if the -trace option is turned on */
    public static final String TRACE_PROPERTY = "trace";

    /** Name of the property specifying if the -traceParser option is turned on */
    public static final String TRACE_PARSER_PROPERTY = "traceParser";

    /** Name of the property specifying if the -traceLexer option is turned on */
    public static final String TRACE_LEXER_PROPERTY = "traceLexer";

    /** Name of the property specifying if the -traceTreeParser option is turned on */
    public static final String TRACE_TREE_PARSER_PROPERTY = "traceTreeParser";

    /** Name of the property specifying if the -smap option is turned on */
    public static final String SMAP_PROPERTY = "installSmap";

    /** Name of the property holding the ANTXR grammar a file is generated
     * from */
    public static final String GRAMMAR_PROPERTY = "grammar";

    /** Name of the property holding the debug option for generated files */
    public static final String DEBUG_PROPERTY = "debug";

    /** Name of the property holding the clean warnings option for generated files */
    public static final String CLEAN_WARNINGS = "cleanWarnings";

    /** Name of the property holding the output path for generated files */
    public static final String OUTPUT_PROPERTY = "output";

    /** Name of the property holding a list of paths to super grammar files
     * (delimited by ';') */
    public static final String SUPER_GRAMMARS_PROPERTY = "superGrammars";

    /**
     * Read the .antxr-eclipse file to retrieve grammar settings
     * @param project The project containing the grammars
     * @return A Map of grammar options
     */
    public static Map<String, Map<String, String>> readSettings(IProject project) {
        BufferedReader in = null;
        try {
            IFile file = project.getFile(".antxr-eclipse");
            IPath location = file.getLocation();
            File antxrSettingsFile = location.toFile();
            in = new BufferedReader(new FileReader(antxrSettingsFile));
            BasicCrimsonXMLTokenStream scanner =
                new BasicCrimsonXMLTokenStream(in, AntxrSettingsParser.class, false, false);
            AntxrSettingsParser parser = new AntxrSettingsParser(scanner);
            try {
                return parser.document();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        catch (FileNotFoundException e) {
            // ignore! don't report as error
            return new HashMap<String, Map<String, String>>();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    // ignore - don't log
                    return new HashMap<String, Map<String, String>>();
                }
            }
        }
    }

    /**
     * Write all grammar options for a project to an .antxr-eclipse file
     * @param project The project containing the grammars
     * @param settings The grammar settings
     */
    public static void writeSettings(IProject project, Map<String, Map<String, String>> settings) {
        PrintWriter out = null;
        try {
            set(settings, PLUGIN_VERSION_PROPERTY, AntxrCorePlugin.VERSION);
            IFile file = project.getFile(".antxr-eclipse");
            IPath location = file.getLocation();
            File antxrSettingsFile = location.toFile();

            out = new PrintWriter(new FileWriter(antxrSettingsFile));
            out.println("<?xml version='1.0' ?> ");
            out.println("<settings>");
            for (Iterator i = settings.keySet().iterator(); i.hasNext();) {
                String resourceName = (String)i.next();
                out.println("  <resource name='" + resourceName + "'>");
                Map<String, String> properties = settings.get(resourceName);
                for (Iterator j = properties.keySet().iterator(); j.hasNext();) {
                    String propertyName = (String)j.next();
                    String propertyValue = properties.get(propertyName);
                    out.println("    <property name='" + propertyName + "' value='" + propertyValue + "' />");
                }
                out.println("  </resource>");
            }
            out.println("</settings>");
        }
        catch (IOException e) {
            AntxrCorePlugin.log(e);
        }
        finally {
            if (out != null) {
	            out.close();
            }
        }
    }

    /**
     * Set a grammar option
     * @param projectSettings The settings map
     * @param resource The grammar
     * @param property The property name to set
     * @param value The value of the property
     */
    public static void set(Map<String, Map<String, String>> projectSettings, IResource resource, String property, String value) {
        String resourceName = resource.getProjectRelativePath().toString();
        Map<String, String> resourceSettings = projectSettings.get(resourceName);
        if (resourceSettings == null) {
            resourceSettings = new HashMap<String, String>();
            projectSettings.put(resourceName, resourceSettings);
        }
        resourceSettings.put(property, value);
    }

    /**
     * Set a plugin option
     * @param projectSettings The settings map
     * @param property The property name to set
     * @param value The value of the property
     */
    public static void set(Map<String, Map<String, String>> projectSettings, String property, String value) {
        Map<String, String> resourceSettings = projectSettings.get(AntxrCorePlugin.PLUGIN_RESOURCE_NAME);
        if (resourceSettings == null) {
            resourceSettings = new HashMap<String, String>();
            projectSettings.put(AntxrCorePlugin.PLUGIN_RESOURCE_NAME, resourceSettings);
        }
        resourceSettings.put(property, value);
    }

    /**
     * Get a plugin option
     * @param projectSettings The settings map
     * @param property The property name to set
     * @return The value of the property
     */
    public static String get(Map projectSettings, String property) {
        Map resourceSettings = (Map)projectSettings.get(AntxrCorePlugin.PLUGIN_RESOURCE_NAME);
        if (resourceSettings == null) {
	        return null;
        }

        return (String)resourceSettings.get(property);
    }
    /**
     * Get a grammar option
     * @param projectSettings The settings map
     * @param resource The grammar
     * @param property The property name to set
     * @return The value of the property
     */
    public static String get(Map projectSettings, IResource resource, String property) {
        String resourceName = resource.getProjectRelativePath().toString();
        Map resourceSettings = (Map)projectSettings.get(resourceName);
        if (resourceSettings == null) {
	        return null;
        }

        return (String)resourceSettings.get(property);
    }
}
