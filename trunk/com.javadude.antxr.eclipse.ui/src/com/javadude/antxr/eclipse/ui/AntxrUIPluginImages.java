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
	private static final int NAME_PREFIX_LENGTH = NAME_PREFIX.length();

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
			fgIconBaseURL = new URL(AntxrUIPlugin.getInstallURL(),
									pathSuffix);
		} catch (MalformedURLException e) {
			AntxrUIPlugin.log(e);
		}
	}

	// The plugin registry
	private static final ImageRegistry IMAGE_REGISTRY = new ImageRegistry();

	// Available cached Images in the ANTXR UI plugin image registry.
	/** class */
	public static final String IMG_OBJS_CLASS= NAME_PREFIX + "class_obj.gif";

	/** public */
	public static final String IMG_MISC_PUBLIC = NAME_PREFIX + "methpub_obj.gif";
	/** protected */
	public static final String IMG_MISC_PROTECTED = NAME_PREFIX + "methpro_obj.gif";
	/** private */
	public static final String IMG_MISC_PRIVATE = NAME_PREFIX + "methpri_obj.gif";
	/** default */
	public static final String IMG_MISC_DEFAULT = NAME_PREFIX + "methdef_obj.gif";
	
	// Set of predefined Image Descriptors.
	private static final String T_OBJ = "obj16";
//	private static final String T_CLCL = "clcl16";
	private static final String T_CTOOL = "ctool16";

	/** class */
	public static final ImageDescriptor DESC_OBJS_CLASS = createManaged(T_OBJ, IMG_OBJS_CLASS);

	/** public */
	public static final ImageDescriptor DESC_MISC_PUBLIC = createManaged(T_OBJ, IMG_MISC_PUBLIC);
	/** protected */
	public static final ImageDescriptor DESC_MISC_PROTECTED = createManaged(T_OBJ, IMG_MISC_PROTECTED);
	/** private */
	public static final ImageDescriptor DESC_MISC_PRIVATE = createManaged(T_OBJ, IMG_MISC_PRIVATE);
	/** default */
	public static final ImageDescriptor DESC_MISC_DEFAULT = createManaged(T_OBJ, IMG_MISC_DEFAULT);

	/** show segments */
	public static final ImageDescriptor DESC_TOOL_SHOW_SEGMENTS = create(T_CTOOL, "segment_edit.gif");
	/** go to next error */
	public static final ImageDescriptor DESC_TOOL_GOTO_NEXT_ERROR= create(T_CTOOL, "next_error_nav.gif"); 	//$NON-NLS-1$
	/** go to previous error */
	public static final ImageDescriptor DESC_TOOL_GOTO_PREV_ERROR= create(T_CTOOL, "prev_error_nav.gif"); 	//$NON-NLS-1$

	/**
	 * Returns the image managed under the given key in this registry.
	 * 
	 * @param aKey the image's key
	 * @return the image managed under the given key
	 */ 
	public static Image get(String aKey) {
		return IMAGE_REGISTRY.get(aKey);
	}

	/**
	 * Sets the three image descriptors for enabled, disabled, and hovered to
	 * an action. The actions are retrieved from the *tool16 folders.
	 * @param anAction The action for the image
	 * @param anIconName the icon name
	 */
	public static void setToolImageDescriptors(IAction anAction,
												 String anIconName) {
		setImageDescriptors(anAction, "tool16", anIconName);
	}
	
	/**
	 * Sets the three image descriptors for enabled, disabled, and hovered to
	 * an action. The actions are retrieved from the *lcl16 folders.
	 * @param anAction The action for the image
	 * @param anIconName the icon name
	 */
	public static void setLocalImageDescriptors(IAction anAction,
												  String anIconName) {
		setImageDescriptors(anAction, "lcl16", anIconName);
	}

	//---- Helper methods to access icons on the file system -----------------

	private static void setImageDescriptors(IAction anAction, String aType,
											  String aRelPath) {
		try {
			ImageDescriptor id = ImageDescriptor.createFromURL(
									  makeIconFileURL("d" + aType, aRelPath));
			if (id != null) {
				anAction.setDisabledImageDescriptor(id);
			}
		} catch (MalformedURLException e) {
			AntxrUIPlugin.log(e);
		}
	
		try {
			ImageDescriptor id = ImageDescriptor.createFromURL(
									  makeIconFileURL("c" + aType, aRelPath));
			if (id != null) {
				anAction.setHoverImageDescriptor(id);
			}
		} catch (MalformedURLException e) {
			AntxrUIPlugin.log(e);
		}
	
		anAction.setImageDescriptor(create("e" + aType, aRelPath));
	}

	private static ImageDescriptor createManaged(String aPrefix,
												   String aName) {
		ImageDescriptor result;
		try {
			result = ImageDescriptor.createFromURL(makeIconFileURL(aPrefix,
										aName.substring(NAME_PREFIX_LENGTH)));
			IMAGE_REGISTRY.put(aName, result);
		} catch (MalformedURLException e) {
			result = ImageDescriptor.getMissingImageDescriptor();
		}
		return result;
	}
	
	private static ImageDescriptor create(String aPrefix, String aName) {
		ImageDescriptor result;
		try {
			result = ImageDescriptor.createFromURL(makeIconFileURL(aPrefix,
																   aName));
		} catch (MalformedURLException e) {
			result = ImageDescriptor.getMissingImageDescriptor();
		}
		return result;
	}
	
	private static URL makeIconFileURL(String aPrefix, String aName)
											    throws MalformedURLException {
		if (fgIconBaseURL == null) {
			throw new MalformedURLException();
		}
			
		StringBuffer buffer = new StringBuffer(aPrefix);
		buffer.append('/');
		buffer.append(aName);
		return new URL(fgIconBaseURL, buffer.toString());
	}	
}
