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
package com.javadude.antxr.eclipse.ui.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.IPreferencesConstants;

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
