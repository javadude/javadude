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
package com.javadude.antxr.eclipse.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Bundle of images used by ANTXR UI plugin.
 */
public class AntxrUIPluginImages {

	private static final String NAME_PREFIX = AntxrUIPlugin.PLUGIN_ID;
	private static final int NAME_PREFIX_LENGTH = AntxrUIPluginImages.NAME_PREFIX.length();

	private static URL fgIconBaseURL = null;

	// Determine display depth. If depth > 4 then we use high color images.
	// Otherwise low color images are used
	static {
		// Don't consider the default display since accessing it throws an
		// SWTException anyway.
		Display display = Display.getCurrent();
		String pathSuffix;
		if (display != null && display.getIconDepth() > 4) {
			pathSuffix = "icons/full/";
		} else {
		    pathSuffix = "icons/basic/";
		}
		try {
			AntxrUIPluginImages.fgIconBaseURL = new URL(AntxrUIPlugin.getInstallURL(),
									pathSuffix);
		} catch (MalformedURLException e) {
			AntxrUIPlugin.log(e);
		}
	}

	// The plugin registry
	private static final ImageRegistry IMAGE_REGISTRY = new ImageRegistry();

	// Available cached Images in the ANTXR UI plugin image registry.
	/** class */
	public static final String IMG_OBJS_CLASS= AntxrUIPluginImages.NAME_PREFIX + "class_obj.gif";

	/** public */
	public static final String IMG_MISC_PUBLIC = AntxrUIPluginImages.NAME_PREFIX + "methpub_obj.gif";
	/** protected */
	public static final String IMG_MISC_PROTECTED = AntxrUIPluginImages.NAME_PREFIX + "methpro_obj.gif";
	/** private */
	public static final String IMG_MISC_PRIVATE = AntxrUIPluginImages.NAME_PREFIX + "methpri_obj.gif";
	/** default */
	public static final String IMG_MISC_DEFAULT = AntxrUIPluginImages.NAME_PREFIX + "methdef_obj.gif";

	// Set of predefined Image Descriptors.
	private static final String T_OBJ = "obj16";
//	private static final String T_CLCL = "clcl16";
	private static final String T_CTOOL = "ctool16";

	/** class */
	public static final ImageDescriptor DESC_OBJS_CLASS = AntxrUIPluginImages.createManaged(AntxrUIPluginImages.T_OBJ, AntxrUIPluginImages.IMG_OBJS_CLASS);

	/** public */
	public static final ImageDescriptor DESC_MISC_PUBLIC = AntxrUIPluginImages.createManaged(AntxrUIPluginImages.T_OBJ, AntxrUIPluginImages.IMG_MISC_PUBLIC);
	/** protected */
	public static final ImageDescriptor DESC_MISC_PROTECTED = AntxrUIPluginImages.createManaged(AntxrUIPluginImages.T_OBJ, AntxrUIPluginImages.IMG_MISC_PROTECTED);
	/** private */
	public static final ImageDescriptor DESC_MISC_PRIVATE = AntxrUIPluginImages.createManaged(AntxrUIPluginImages.T_OBJ, AntxrUIPluginImages.IMG_MISC_PRIVATE);
	/** default */
	public static final ImageDescriptor DESC_MISC_DEFAULT = AntxrUIPluginImages.createManaged(AntxrUIPluginImages.T_OBJ, AntxrUIPluginImages.IMG_MISC_DEFAULT);

	/** show segments */
	public static final ImageDescriptor DESC_TOOL_SHOW_SEGMENTS = AntxrUIPluginImages.create(AntxrUIPluginImages.T_CTOOL, "segment_edit.gif");
	/** go to next error */
	public static final ImageDescriptor DESC_TOOL_GOTO_NEXT_ERROR= AntxrUIPluginImages.create(AntxrUIPluginImages.T_CTOOL, "next_error_nav.gif"); 	//$NON-NLS-1$
	/** go to previous error */
	public static final ImageDescriptor DESC_TOOL_GOTO_PREV_ERROR= AntxrUIPluginImages.create(AntxrUIPluginImages.T_CTOOL, "prev_error_nav.gif"); 	//$NON-NLS-1$

	/**
	 * Returns the image managed under the given key in this registry.
	 *
	 * @param aKey the image's key
	 * @return the image managed under the given key
	 */
	public static Image get(String aKey) {
		return AntxrUIPluginImages.IMAGE_REGISTRY.get(aKey);
	}

	/**
	 * Sets the three image descriptors for enabled, disabled, and hovered to
	 * an action. The actions are retrieved from the *tool16 folders.
	 * @param anAction The action for the image
	 * @param anIconName the icon name
	 */
	public static void setToolImageDescriptors(IAction anAction,
												 String anIconName) {
		AntxrUIPluginImages.setImageDescriptors(anAction, "tool16", anIconName);
	}

	/**
	 * Sets the three image descriptors for enabled, disabled, and hovered to
	 * an action. The actions are retrieved from the *lcl16 folders.
	 * @param anAction The action for the image
	 * @param anIconName the icon name
	 */
	public static void setLocalImageDescriptors(IAction anAction,
												  String anIconName) {
		AntxrUIPluginImages.setImageDescriptors(anAction, "lcl16", anIconName);
	}

	//---- Helper methods to access icons on the file system -----------------

	private static void setImageDescriptors(IAction anAction, String aType,
											  String aRelPath) {
		try {
			ImageDescriptor id = ImageDescriptor.createFromURL(
									  AntxrUIPluginImages.makeIconFileURL("d" + aType, aRelPath));
			if (id != null) {
				anAction.setDisabledImageDescriptor(id);
			}
		} catch (MalformedURLException e) {
			AntxrUIPlugin.log(e);
		}

		try {
			ImageDescriptor id = ImageDescriptor.createFromURL(
									  AntxrUIPluginImages.makeIconFileURL("c" + aType, aRelPath));
			if (id != null) {
				anAction.setHoverImageDescriptor(id);
			}
		} catch (MalformedURLException e) {
			AntxrUIPlugin.log(e);
		}

		anAction.setImageDescriptor(AntxrUIPluginImages.create("e" + aType, aRelPath));
	}

	private static ImageDescriptor createManaged(String aPrefix,
												   String aName) {
		ImageDescriptor result;
		try {
			result = ImageDescriptor.createFromURL(AntxrUIPluginImages.makeIconFileURL(aPrefix,
										aName.substring(AntxrUIPluginImages.NAME_PREFIX_LENGTH)));
			AntxrUIPluginImages.IMAGE_REGISTRY.put(aName, result);
		} catch (MalformedURLException e) {
			result = ImageDescriptor.getMissingImageDescriptor();
		}
		return result;
	}

	private static ImageDescriptor create(String aPrefix, String aName) {
		ImageDescriptor result;
		try {
			result = ImageDescriptor.createFromURL(AntxrUIPluginImages.makeIconFileURL(aPrefix,
																   aName));
		} catch (MalformedURLException e) {
			result = ImageDescriptor.getMissingImageDescriptor();
		}
		return result;
	}

	private static URL makeIconFileURL(String aPrefix, String aName)
											    throws MalformedURLException {
		if (AntxrUIPluginImages.fgIconBaseURL == null) {
			throw new MalformedURLException();
		}

		StringBuffer buffer = new StringBuffer(aPrefix);
		buffer.append('/');
		buffer.append(aName);
		return new URL(AntxrUIPluginImages.fgIconBaseURL, buffer.toString());
	}
}
