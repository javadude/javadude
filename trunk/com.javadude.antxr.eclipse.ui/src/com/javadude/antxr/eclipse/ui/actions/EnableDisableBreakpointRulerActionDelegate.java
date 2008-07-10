/**
 * <small>
 * <p><i>Copyright (C) 2005 Torsten Juergeleit, 
 * All rights reserved. </i></p>
 * 
 * <p>USE OF THIS CONTENT IS GOVERNED BY THE TERMS AND CONDITIONS OF THIS
 * AGREEMENT AND/OR THE TERMS AND CONDITIONS OF LICENSE AGREEMENTS OR NOTICES
 * INDICATED OR REFERENCED BELOW. BY USING THE CONTENT, YOU AGREE THAT YOUR USE
 * OF THE CONTENT IS GOVERNED BY THIS AGREEMENT AND/OR THE TERMS AND CONDITIONS
 * OF ANY APPLICABLE LICENSE AGREEMENTS OR NOTICES INDICATED OR REFERENCED
 * BELOW. IF YOU DO NOT AGREE TO THE TERMS AND CONDITIONS OF THIS AGREEMENT AND
 * THE TERMS AND CONDITIONS OF ANY APPLICABLE LICENSE AGREEMENTS OR NOTICES
 * INDICATED OR REFERENCED BELOW, THEN YOU MAY NOT USE THE CONTENT.</p>
 * 
 * <p>This Content is Copyright (C) 2005 Torsten Juergeleit, 
 * and is provided to you under the terms and conditions of the Common Public 
 * License Version 1.0 ("CPL"). A copy of the CPL is provided with this Content 
 * and is also available at 
 *     <a href="http://www.eclipse.org/legal/cpl-v10.html">
 *         http://www.eclipse.org/legal/cpl-v10.html </a>.
 * 
 * For purposes of the CPL, "Program" will mean the Content.</p>
 * 
 * <p>Content includes, but is not limited to, source code, object code,
 * documentation and any other files in this distribution.</p>
 * 
 * </small>
 */
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


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.texteditor.AbstractRulerActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Creates instances of the breakpoint ruler action
 */
public class EnableDisableBreakpointRulerActionDelegate extends AbstractRulerActionDelegate {
	
	/**
	 * @see AbstractRulerActionDelegate#createAction(ITextEditor, IVerticalRulerInfo)
	 */
	protected IAction createAction(ITextEditor editor, IVerticalRulerInfo rulerInfo) {
		return new EnableDisableBreakpointRulerAction(editor, rulerInfo);
	}
}
