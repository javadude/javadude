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
package com.javadude.antxr.eclipse.ui.actions;

import org.eclipse.ui.texteditor.ITextEditorActionConstants;

/**
 * Action IDs for standard actions, for groups in the menu bar, and for actions
 * in context menus of ANTXR views.
 */
public interface IAntxrActionConstants extends ITextEditorActionConstants {

	/**
	 * Navigate menu: name of standard Goto Rule action (value
	 * <code>"GotoRule"</code>).
	 */
	public static final String GOTO_RULE = "GotoRule";
	
	/**
	 * Edit menu: name of standard Code Assist global action
	 * (value <code>"ContentAssist"</code>).
	 */
	public static final String CONTENT_ASSIST = "ContentAssist";
	
	/**
	 * Source menu: name of standard Comment global action
	 * (value <code>"Comment"</code>).
	 */
	public static final String COMMENT = "Comment";
	
	/**
	 * Source menu: name of standard Uncomment global action
	 * (value <code>"Uncomment"</code>).
	 */
	public static final String UNCOMMENT = "Uncomment";
}
