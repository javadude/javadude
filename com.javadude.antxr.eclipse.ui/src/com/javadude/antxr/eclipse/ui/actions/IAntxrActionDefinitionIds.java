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
