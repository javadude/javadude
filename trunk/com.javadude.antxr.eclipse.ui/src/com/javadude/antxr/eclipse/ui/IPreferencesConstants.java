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

/**
 * Defines constants which are used to refer to values in the plugin's
 * preference bundle.
 */
public interface IPreferencesConstants {
	/** ui plugin id */
	String PREFIX = AntxrUIPlugin.PLUGIN_ID + ".";

	/** show segments action */
	String EDITOR_SHOW_SEGMENTS = PREFIX + "editor.showSegments";
	/** sort outline action */
	String EDITOR_OUTLINE_SORT = PREFIX + "editor.outline.sort";

	/** color prefix */
	String PREFIX_COLOR = PREFIX + "color.";

	/** color for basic grammar text */
	String COLOR_DEFAULT = PREFIX_COLOR + IColorConstants.DEFAULT;
	/** color for grammar keyword text */
	String COLOR_KEYWORD = PREFIX_COLOR + IColorConstants.KEYWORD;
	/** color for grammar string text */
	String COLOR_STRING = PREFIX_COLOR + IColorConstants.STRING;
	/** color for grammar comment text */
	String COLOR_COMMENT = PREFIX_COLOR + IColorConstants.COMMENT;
	/** color for grammar doc comment text */
	String COLOR_DOC_COMMENT = PREFIX_COLOR + IColorConstants.DOC_COMMENT;
}