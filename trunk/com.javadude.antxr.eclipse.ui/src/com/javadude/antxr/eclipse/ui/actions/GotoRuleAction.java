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

import java.util.ResourceBundle;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.editor.AntxrEditor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;

/**
 * An action that moves the editor position to a rule
 */
public class GotoRuleAction extends TextEditorAction {

	/**
	 * Create the action
	 * @param aBundle messages
	 * @param aPrefix prefix
	 * @param anEditor edior
	 */
	public GotoRuleAction(ResourceBundle aBundle, String aPrefix,
						   ITextEditor anEditor) {
		super(aBundle, aPrefix, anEditor);
	}

	/** {@inheritDoc} */
	@Override
    public void run() {
		AntxrEditor editor = (AntxrEditor)getTextEditor();
		ITextSelection selection = (ITextSelection)
								  editor.getSelectionProvider().getSelection();
		if (!selection.isEmpty() && selection instanceof TextSelection) {
			IDocument doc = editor.getDocument();
			int offset = selection.getOffset();
			int length = selection.getLength();
			try {
				if (length != 0) {

					// Use selected text as rule name
					if (length < 0) {
						length = -length;
						offset -= length;
					}
					editor.gotoRule(doc.get(offset, length));
				} else {

					// Use Java identifier under cursor as rule name
					int start = offset;
					while (start > 0) {
					    char c = doc.getChar(start - 1);
						if (c == '<' || c == '>' || Character.isJavaIdentifierPart(c)) {
							start--;
						} else {
							break;
						}
					}
					length = offset - start;
					int max = doc.getLength();
					while (offset < max) {
					    char c = doc.getChar(offset);
						if (c == '<' || c == '>' || Character.isJavaIdentifierPart(c)) {
							offset++;
							length++;
						} else {
							break;
						}
					}
					if (length > 0) {
						editor.gotoRule(doc.get(start, length));
					}
				}
			} catch (BadLocationException e) {
				AntxrUIPlugin.log(e);
			}
		}
	}
}