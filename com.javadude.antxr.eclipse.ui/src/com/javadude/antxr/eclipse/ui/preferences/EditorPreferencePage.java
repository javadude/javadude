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
package com.javadude.antxr.eclipse.ui.preferences;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.IPreferencesConstants;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Color settings for editor syntax highliting.
 */
public class EditorPreferencePage extends FieldEditorPreferencePage
										 implements IWorkbenchPreferencePage {
	private final String PREFIX = "Preferences.Editor.";

	/**
	 * Create the pref page
	 */
	public EditorPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(AntxrUIPlugin.getDefault().getPreferenceStore());
        setDescription(AntxrUIPlugin.getMessage(PREFIX + "description"));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_DEFAULT,
								  AntxrUIPlugin.getMessage(PREFIX + "default"),
								  getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_KEYWORD,
								  AntxrUIPlugin.getMessage(PREFIX + "keyword"),
								  getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_STRING,
								   AntxrUIPlugin.getMessage(PREFIX + "string"),
								   getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_COMMENT,
								  AntxrUIPlugin.getMessage(PREFIX + "comment"),
								  getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_DOC_COMMENT,
							  AntxrUIPlugin.getMessage(PREFIX + "doc_comment"),
							  getFieldEditorParent()));
    }

	/** {@inheritDoc} */
	public void init(IWorkbench aWorkbench) {
		// nothing to do here
	}

	/** {@inheritDoc} */
	public boolean performOk() {
        boolean value = super.performOk();
        AntxrUIPlugin.getDefault().savePluginPreferences();
        return value;
    }
}
