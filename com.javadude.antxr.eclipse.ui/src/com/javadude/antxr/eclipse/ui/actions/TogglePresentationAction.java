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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.AntxrUIPluginImages;
import com.javadude.antxr.eclipse.ui.IPreferencesConstants;

/**
 * A toolbar action which toggles the presentation model of the connected text
 * editor. The editor shows either the highlight range only or always the
 * whole document.
 */
public class TogglePresentationAction extends TextEditorAction {
	private static final String PREFIX = "Editor.TogglePresentation";

	/**
	 * Constructs and updates the action.
	 */
	public TogglePresentationAction() {
		super(AntxrUIPlugin.getDefault().getResourceBundle(), TogglePresentationAction.PREFIX, null);
        AntxrUIPluginImages.setToolImageDescriptors(this, "segment_edit.gif");
		update();
	}

	/** {@inheritDoc} */
	public void run() {
		ITextEditor editor = getTextEditor();
		if (editor != null) {
			IRegion remembered = editor.getHighlightRange();
			editor.resetHighlightRange();

			boolean showAll = !editor.showsHighlightRangeOnly();
			setChecked(showAll);
			setToolTipText(getToolTipText(showAll));

			editor.showHighlightRangeOnly(showAll);
			if (remembered != null)  {
				editor.setHighlightRange(remembered.getOffset(),
										 remembered.getLength(), true);
			}

			IPreferenceStore store = AntxrUIPlugin.getDefault().getPreferenceStore();
			store.setValue(IPreferencesConstants.EDITOR_SHOW_SEGMENTS, showAll);
		}
	}

	/** {@inheritDoc} */
	public void update() {
		ITextEditor editor = getTextEditor();
		boolean checked = (editor != null && editor.showsHighlightRangeOnly());
		setChecked(checked);
		setToolTipText(getToolTipText(checked));
		setEnabled(true);
	}

	/** {@inheritDoc} */
	public void setEditor(ITextEditor anEditor) {
		super.setEditor(anEditor);

		if (anEditor != null) {
			IPreferenceStore store = AntxrUIPlugin.getDefault().getPreferenceStore();
			boolean showSegments = store.getBoolean(
									IPreferencesConstants.EDITOR_SHOW_SEGMENTS);
			if (isChecked() != showSegments) {
				setChecked(showSegments);
				setToolTipText(getToolTipText(showSegments));
			}

			if (anEditor.showsHighlightRangeOnly() != showSegments) {
				IRegion remembered = anEditor.getHighlightRange();
				anEditor.resetHighlightRange();
				anEditor.showHighlightRangeOnly(showSegments);
				if (remembered != null) {
					anEditor.setHighlightRange(remembered.getOffset(),
											   remembered.getLength(), true);
				}
			}
		}
	}

	private String getToolTipText(boolean anIsChecked) {
		return (anIsChecked
				? AntxrUIPlugin.getMessage(TogglePresentationAction.PREFIX + ".tooltip.checked")
				: AntxrUIPlugin.getMessage(TogglePresentationAction.PREFIX + ".tooltip.unchecked"));
	}
}
