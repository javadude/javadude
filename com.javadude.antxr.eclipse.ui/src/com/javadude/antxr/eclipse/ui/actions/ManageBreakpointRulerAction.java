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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.javadude.antxr.eclipse.ui.AntxrLineBreakpointAdapter;
import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

/**
 * Toggle breakpoints action
 */
public class ManageBreakpointRulerAction extends Action {

	private IVerticalRulerInfo fRuler;
	private ITextEditor fTextEditor;
	private AntxrLineBreakpointAdapter fAntxrLineBreakpointAdapter = new AntxrLineBreakpointAdapter();

	/**
	 * Create the action
	 * @param ruler the ruler
	 * @param editor the editor
	 */
	public ManageBreakpointRulerAction(IVerticalRulerInfo ruler, ITextEditor editor) {
		super(AntxrUIPlugin.getMessage("ManageBreakpointRulerAction.label")); //$NON-NLS-1$
		fRuler= ruler;
		fTextEditor= editor;
	}

	/**
	 * Disposes this action
	 */
	public void dispose() {
		fTextEditor = null;
		fRuler = null;
	}

	/**
	 * Returns this action's vertical ruler info.
	 *
	 * @return this action's vertical ruler
	 */
	protected IVerticalRulerInfo getVerticalRulerInfo() {
		return fRuler;
	}

	/**
	 * Returns this action's editor.
	 *
	 * @return this action's editor
	 */
	protected ITextEditor getTextEditor() {
		return fTextEditor;
	}

	/**
	 * Returns the <code>IDocument</code> of the editor's input.
	 *
	 * @return the document of the editor's input
	 */
	protected IDocument getDocument() {
		IDocumentProvider provider= fTextEditor.getDocumentProvider();
		return provider.getDocument(fTextEditor.getEditorInput());
	}

	/** {@inheritDoc} */
	public void run() {
		try {
			IDocument document = getDocument();
			int lineNumber= getVerticalRulerInfo().getLineOfLastMouseButtonActivity();
			if (lineNumber >= document.getNumberOfLines()) {
				return;
			}
			try {
				IRegion line= document.getLineInformation(lineNumber);
				ITextSelection selection = new TextSelection(document, line.getOffset(), line.getLength());
				fAntxrLineBreakpointAdapter.toggleLineBreakpoints(fTextEditor, selection);
			} catch (BadLocationException e) {
				//likely document is folded so you cannot get the line information of the folded line
			}

		} catch (CoreException e) {
			// TODO ERROR DIALOG
//			AntxrUIPlugin.errorDialog(AntxrUIPlugin.getMessage("ManageBreakpointRulerAction.error.adding.message1"), e); //$NON-NLS-1$
			String message = AntxrUIPlugin.getMessage("ManageBreakpointRulerAction.error.adding.message1");
			AntxrUIPlugin.logErrorStatus(message, e.getStatus());
		}
	}

}
