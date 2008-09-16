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
package com.javadude.antxr.eclipse.ui.editor.outline;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.javadude.antxr.eclipse.core.parser.AbstractModel;
import com.javadude.antxr.eclipse.core.parser.Grammar;
import com.javadude.antxr.eclipse.core.parser.Rule;
import com.javadude.antxr.eclipse.ui.AntxrUIPluginImages;

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
