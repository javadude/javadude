package com.javadude.antlr4.builder;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.javadude.antlr4.builder.builder.Antlr4Builder;
import com.javadude.antlr4.builder.builder.PluginUtil;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.javadude.antlr4.builder"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private static PluginUtil util;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		plugin.getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent event) {
				getUtil().info(107, "ANTLR 4 Preferences changed; rebuilding all ANTLR 4 grammars in projects with ANTLR Natures");
				Antlr4Builder.forceFullANTLR4Build(ResourcesPlugin.getWorkspace().getRoot(), "ANTLR 4 preferences changed");
			}}); 
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
    public static PluginUtil getUtil() {
        if (util == null) {
            util = new PluginUtil(getDefault().getBundle().getSymbolicName(), getDefault().getLog());
        }
        return util;
    }
}
