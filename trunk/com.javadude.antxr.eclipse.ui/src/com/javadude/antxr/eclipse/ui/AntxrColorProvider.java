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
                             IPreferencesConstants.COLOR_DEFAULT, RGB_DEFAULT);
        PreferenceConverter.setDefault(aStore,
                             IPreferencesConstants.COLOR_KEYWORD, RGB_KEYWORD);
        PreferenceConverter.setDefault(aStore,
                               IPreferencesConstants.COLOR_STRING, RGB_STRING);
        PreferenceConverter.setDefault(aStore,
                             IPreferencesConstants.COLOR_COMMENT, RGB_COMMENT);
        PreferenceConverter.setDefault(aStore,
                     IPreferencesConstants.COLOR_DOC_COMMENT, RGB_DOC_COMMENT);
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
