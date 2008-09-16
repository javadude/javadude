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
package com.javadude.antxr.eclipse.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Color provider to syntax highlight ANTXR grammars
 */
public class AntxrColorProvider implements IColorConstants {

    // Default colors
    private static final RGB RGB_DEFAULT = new RGB(0, 0, 0);
    private static final RGB RGB_KEYWORD = new RGB(127, 0, 85);
    private static final RGB RGB_STRING = new RGB(42, 0, 255);
    private static final RGB RGB_COMMENT = new RGB(63, 127, 95);
    private static final RGB RGB_DOC_COMMENT = new RGB(63, 95, 191);

    protected Map<String, Color> fColorTable = new HashMap<String, Color>(10);

    /**
     * Set default colors in given preference store.
     * @param aStore the pref store
     */
    public static void initializeDefaults(IPreferenceStore aStore) {
        PreferenceConverter.setDefault(aStore,
                             IPreferencesConstants.COLOR_DEFAULT, AntxrColorProvider.RGB_DEFAULT);
        PreferenceConverter.setDefault(aStore,
                             IPreferencesConstants.COLOR_KEYWORD, AntxrColorProvider.RGB_KEYWORD);
        PreferenceConverter.setDefault(aStore,
                               IPreferencesConstants.COLOR_STRING, AntxrColorProvider.RGB_STRING);
        PreferenceConverter.setDefault(aStore,
                             IPreferencesConstants.COLOR_COMMENT, AntxrColorProvider.RGB_COMMENT);
        PreferenceConverter.setDefault(aStore,
                     IPreferencesConstants.COLOR_DOC_COMMENT, AntxrColorProvider.RGB_DOC_COMMENT);
    }

    /**
     * Returns specified color that is stored in the color table. If color not
     * found in color table then a new instance is created from according
     * preferences value and stored in color table.
     * @param aName the name of the color
     * @return the color instance
     */
    public Color getColor(String aName) {
        Color color = fColorTable.get(aName);
        if (color == null) {
            IPreferenceStore store =
                               AntxrUIPlugin.getDefault().getPreferenceStore();
            RGB rgb = PreferenceConverter.getColor(store,
                                   IPreferencesConstants.PREFIX_COLOR + aName);
            if (rgb != null) {
                color = new Color(Display.getCurrent(), rgb);
            } else {
                color = Display.getCurrent().getSystemColor(
                                                    SWT.COLOR_LIST_FOREGROUND);
                AntxrUIPlugin.logErrorMessage("Undefined color '" +
                                              aName + "'");
            }
            fColorTable.put(aName, color);
        }
        return color;
    }

    /**
     * Release all of the color resources held onto by the color provider.
     */
    public void dispose() {
        Iterator colors = fColorTable.values().iterator();
        while (colors.hasNext()) {
             ((Color)colors.next()).dispose();
        }
    }
}
