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

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.AntxrUIPluginImages;
import com.javadude.antxr.eclipse.ui.IPreferencesConstants;
import com.javadude.antxr.eclipse.ui.editor.outline.AntxrOutlineSorter;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerSorter;

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
        setText(AntxrUIPlugin.getMessage(PREFIX + "label"));
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
        fViewer.setSorter(aValue ? SORTER : null);
        setToolTipText(aValue ?
			AntxrUIPlugin.getMessage(PREFIX + "tooltip.checked") :
			AntxrUIPlugin.getMessage(PREFIX + "tooltip.unchecked"));
        setDescription(aValue ?
			AntxrUIPlugin.getMessage(PREFIX + "description.checked") :
			AntxrUIPlugin.getMessage(PREFIX + "description.unchecked"));
        if (aDoStore) {
	        Preferences prefs = AntxrUIPlugin.getDefault().getPluginPreferences();
	        prefs.setValue(IPreferencesConstants.EDITOR_OUTLINE_SORT, aValue);
        }
    }
}