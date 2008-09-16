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

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.AntxrUIPluginImages;
import com.javadude.antxr.eclipse.ui.IPreferencesConstants;
import com.javadude.antxr.eclipse.ui.editor.outline.AntxrOutlineSorter;

/**
 * Sorts the outline page
 */
public class LexicalSortingAction extends Action {

	private static final String PREFIX = "OutlinePage.Sort.";
	private static final ViewerSorter SORTER = new AntxrOutlineSorter();
	private StructuredViewer fViewer;

    /**
     * Constructor for LexicalSortingAction.
     * @param aViewer the viewer containing the outline
     */
    public LexicalSortingAction(StructuredViewer aViewer) {
        fViewer = aViewer;
        setText(AntxrUIPlugin.getMessage(LexicalSortingAction.PREFIX + "label"));
        AntxrUIPluginImages.setLocalImageDescriptors(this, "alphab_sort_co.gif");
        Preferences prefs = AntxrUIPlugin.getDefault().getPluginPreferences();
        boolean checked = prefs.getBoolean(IPreferencesConstants.EDITOR_OUTLINE_SORT);
        valueChanged(checked, false);
    }

    /** {@inheritDoc} */
    public void run() {
        valueChanged(isChecked(), true);
    }

    private void valueChanged(boolean aValue, boolean aDoStore) {
        setChecked(aValue);
        fViewer.setSorter(aValue ? LexicalSortingAction.SORTER : null);
        setToolTipText(aValue ?
			AntxrUIPlugin.getMessage(LexicalSortingAction.PREFIX + "tooltip.checked") :
			AntxrUIPlugin.getMessage(LexicalSortingAction.PREFIX + "tooltip.unchecked"));
        setDescription(aValue ?
			AntxrUIPlugin.getMessage(LexicalSortingAction.PREFIX + "description.checked") :
			AntxrUIPlugin.getMessage(LexicalSortingAction.PREFIX + "description.unchecked"));
        if (aDoStore) {
	        Preferences prefs = AntxrUIPlugin.getDefault().getPluginPreferences();
	        prefs.setValue(IPreferencesConstants.EDITOR_OUTLINE_SORT, aValue);
        }
    }
}