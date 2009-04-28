package com.javadude.derived.warning.cleaner;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
	private static Activator defaultInstance;
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		defaultInstance = this;
	}

	public static void error(Throwable t) {
		defaultInstance.getLog().log(new Status(IStatus.ERROR, defaultInstance.getBundle().getSymbolicName(), "Error!", t));
	}
}
