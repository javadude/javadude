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
package com.javadude.antxr.eclipse.ui.properties;

import java.util.StringTokenizer;

import com.javadude.antxr.eclipse.core.builder.AntxrBuilder;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Properties page for Java files.
 * If the Java file is generated via ANTXR then the name of the according ANTXR
 * grammar file is displayed.
 */
public class CodePropertyPage extends PropertyPage {

    private static final String GRAMMAR_LABEL = "Grammar:";
    private static final String COMMAND_LINE_OPTIONS_LABEL = "Command-line Options:";

    /**
     * @see PreferencePage#createContents(Composite)
     */
    protected Control createContents(Composite aParent) {

        // Get ANTXR grammar name from file property
        String grammar = "";
        String commandLineOptions = "";
        try {
            grammar = ((IResource)getElement()).getPersistentProperty(
                                             AntxrBuilder.GRAMMAR_ECLIPSE_PROPERTY);
            commandLineOptions = ((IResource)getElement()).getPersistentProperty(
                                             AntxrBuilder.COMMAND_LINE_OPTIONS_PROPERTY);
        }
        catch (CoreException e) {
            // ignore - likely just due to upgrade
        }

        // If ANTXR grammar property found then display it
        Composite composite;
        if (grammar != null) {
            composite = new Composite(aParent, SWT.NULL);
            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            composite.setLayout(layout);

            new Label(composite, SWT.NONE).setText(GRAMMAR_LABEL);
            new Text(composite, SWT.READ_ONLY).setText(grammar);
            new Label(composite, SWT.NONE).setText(COMMAND_LINE_OPTIONS_LABEL);
            StringTokenizer tokenizer = new StringTokenizer(commandLineOptions, "|");
            if (tokenizer.hasMoreTokens()) {
	            new Text(composite, SWT.READ_ONLY).setText(tokenizer.nextToken());
            }
            while(tokenizer.hasMoreTokens()) {
                new Label(composite, SWT.NONE); // blank
                new Text(composite, SWT.READ_ONLY).setText(tokenizer.nextToken());
            }
        } else {
            composite = aParent;
        }
        noDefaultAndApplyButton();
        return composite;
    }
}