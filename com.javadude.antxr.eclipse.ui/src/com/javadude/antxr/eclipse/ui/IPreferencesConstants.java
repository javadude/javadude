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

/**
 * Defines constants which are used to refer to values in the plugin's
 * preference bundle.
 */
public interface IPreferencesConstants {
	/** ui plugin id */
	String PREFIX = AntxrUIPlugin.PLUGIN_ID + ".";

	/** show segments action */
	String EDITOR_SHOW_SEGMENTS = IPreferencesConstants.PREFIX + "editor.showSegments";
	/** sort outline action */
	String EDITOR_OUTLINE_SORT = IPreferencesConstants.PREFIX + "editor.outline.sort";

	/** color prefix */
	String PREFIX_COLOR = IPreferencesConstants.PREFIX + "color.";

	/** color for basic grammar text */
	String COLOR_DEFAULT = IPreferencesConstants.PREFIX_COLOR + IColorConstants.DEFAULT;
	/** color for grammar keyword text */
	String COLOR_KEYWORD = IPreferencesConstants.PREFIX_COLOR + IColorConstants.KEYWORD;
	/** color for grammar string text */
	String COLOR_STRING = IPreferencesConstants.PREFIX_COLOR + IColorConstants.STRING;
	/** color for grammar comment text */
	String COLOR_COMMENT = IPreferencesConstants.PREFIX_COLOR + IColorConstants.COMMENT;
	/** color for grammar doc comment text */
	String COLOR_DOC_COMMENT = IPreferencesConstants.PREFIX_COLOR + IColorConstants.DOC_COMMENT;
}