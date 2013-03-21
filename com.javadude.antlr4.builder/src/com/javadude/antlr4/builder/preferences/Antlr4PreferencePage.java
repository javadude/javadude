package com.javadude.antlr4.builder.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import com.javadude.antlr4.builder.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class Antlr4PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public Antlr4PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("ANTLR 4 Settings");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new FileFieldEditor(PreferenceConstants.P_ANTLR4_JAR_PATH, "Antlr 4 Jar location:", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceConstants.P_ANTLR4_LIBRARY_DIRECTORY, "Library Directory", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_ANTLR4_GENERATE_ATN, "Generate ATN", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_ANTLR4_LONG_MESSAGES, "Show exception details in messages", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_ANTLR4_GENERATE_LISTENER, "Generate parse tree listener", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_ANTLR4_GENERATE_VISITOR, "Generate parse tree visitor", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_ANTLR4_GENERATE_DEPENDENCIES, "Generate file dependencies", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_ANTLR4_WARNINGS_AS_ERRORS, "Treat warnings as errors", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_ANTLR4_LAUNCH_ST_INSPECTOR, "Launch StringTemplate visualizer on generated code", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_ANTLR4_WAIT_FOR_ST_CLOSE, "Wait for STViz to close before continuing", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_ANTLR4_FORCE_ATN, "Force ATN simulator for all predictions", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}
	
	
}