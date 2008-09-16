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
