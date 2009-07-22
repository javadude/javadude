package com.javadude.cleanup;

import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUpOptionsInitializer;

public class JavaDudeCleanUpOptionsInitializer implements
		ICleanUpOptionsInitializer {
	public static String CONVERT_TABS_TO_SPACES = "com.javadude.cleanup.convertTabsToSpaces";
	public static String TAB_SIZE = "com.javadude.cleanup.tabSize";
	public JavaDudeCleanUpOptionsInitializer() {
	}

	@Override
	public void setDefaultOptions(CleanUpOptions options) {
		options.setOption(CONVERT_TABS_TO_SPACES, CleanUpOptions.TRUE);
		options.setOption(TAB_SIZE, "4");
	}
}
