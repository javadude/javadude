package com.javadude.antlr4.builder.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.javadude.antlr4.builder.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_ANTLR4_JAR_PATH, "");
		store.setDefault(PreferenceConstants.P_ANTLR4_LIBRARY_DIRECTORY, "");
		store.setDefault(PreferenceConstants.P_ANTLR4_GENERATE_ATN, false);
		store.setDefault(PreferenceConstants.P_ANTLR4_LONG_MESSAGES, false);
		store.setDefault(PreferenceConstants.P_ANTLR4_GENERATE_LISTENER, true);
		store.setDefault(PreferenceConstants.P_ANTLR4_GENERATE_VISITOR, false);
		store.setDefault(PreferenceConstants.P_ANTLR4_GENERATE_DEPENDENCIES, false);
		store.setDefault(PreferenceConstants.P_ANTLR4_WARNINGS_AS_ERRORS, false);
		store.setDefault(PreferenceConstants.P_ANTLR4_LAUNCH_ST_INSPECTOR, false);
		store.setDefault(PreferenceConstants.P_ANTLR4_WAIT_FOR_ST_CLOSE, false);
		store.setDefault(PreferenceConstants.P_ANTLR4_FORCE_ATN, false);
	}
}
