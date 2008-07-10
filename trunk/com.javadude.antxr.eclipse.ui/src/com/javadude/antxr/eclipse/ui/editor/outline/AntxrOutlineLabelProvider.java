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
package com.javadude.antxr.eclipse.ui.editor.outline;

import java.util.HashMap;
import java.util.Iterator;

import com.javadude.antxr.eclipse.core.parser.AbstractModel;
import com.javadude.antxr.eclipse.core.parser.Grammar;
import com.javadude.antxr.eclipse.core.parser.Rule;
import com.javadude.antxr.eclipse.ui.AntxrUIPluginImages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Renderer for the outline view
 */
public class AntxrOutlineLabelProvider extends LabelProvider {
    private HashMap<ImageDescriptor, Image> fImageCache = new HashMap<ImageDescriptor, Image>();

    /** {@inheritDoc} */
    public Image getImage(Object anElement) {
        ImageDescriptor descriptor;
        if (anElement instanceof Grammar) {
            descriptor = AntxrUIPluginImages.DESC_OBJS_CLASS;
        } else if (anElement instanceof Rule) {
            switch (((Rule)anElement).getVisibility()) {
                case Rule.PRIVATE :
                    descriptor = AntxrUIPluginImages.DESC_MISC_PRIVATE;
                    break;

                case Rule.PROTECTED :
                    descriptor = AntxrUIPluginImages.DESC_MISC_PROTECTED;
                    break;

                default :
                    descriptor = AntxrUIPluginImages.DESC_MISC_PUBLIC;
                    break;
            }
        } else {
            descriptor = AntxrUIPluginImages.DESC_MISC_DEFAULT;
        }

        // obtain the cached image corresponding to the descriptor
        Image image = fImageCache.get(descriptor);
        if (image == null) {
            image = descriptor.createImage();
            fImageCache.put(descriptor, image);
        }
        return image;
    }

    /** {@inheritDoc} */
    public String getText(Object anElement) {
        if (anElement instanceof AbstractModel) {
            return ((AbstractModel)anElement).getName();
        }
        return "<Noname>";
    }

    /** {@inheritDoc} */
    public void dispose() {
        for (Iterator images = fImageCache.values().iterator();
                                                         images.hasNext(); ) {
            ((Image)images.next()).dispose();
        }
        fImageCache.clear();
    }
}
