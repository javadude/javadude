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

import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;

/**
 * Defines the definition IDs for the ANTXR editor actions.
 */
public interface IAntxrActionDefinitionIds
									  extends IJavaEditorActionDefinitionIds {
	/**
	 * Action definition ID of the 'Navigate -> Go To Rule' action.
	 */
	public static final String GOTO_RULE =
										 "com.javadude.antxr.eclipse.ui.edit.goto.rule";
}
