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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.javadude.antxr.eclipse.core.AntxrNature;
import com.javadude.antxr.eclipse.core.builder.AntxrBuilder;

/**
 * Decorates the text for generated files
 */
public class AntxrLabelDecorator extends LabelProvider
                                  implements ILabelDecorator {
    /** {@inheritDoc} */
    public void dispose()  {
        super.dispose();
    }

    /** {@inheritDoc} */
    public Image decorateImage(Image anImage, Object anElement)  {
        return anImage;
    }

    /** {@inheritDoc} */
    public String decorateText(String aText, Object anElement)  {
        if (anElement instanceof IFile) {
            String grammar = getGrammarProperty(anElement);
            if (grammar != null) {
                StringBuffer buf = new StringBuffer(aText);
                buf.append("  <");
                buf.append(grammar);
                buf.append(">");
                aText = buf.toString();
            }
        }
        if (anElement instanceof IJavaProject) {
            anElement = ((IJavaProject) anElement).getProject();
        }
        if (anElement instanceof IProject) {
            if (AntxrUIPlugin.getUtil().hasNature((IProject) anElement, AntxrNature.NATURE_ID)) {
                aText += "  <antxr>";
            }
        }
        return aText;
    }

    private String getGrammarProperty(Object anElement) {
        String grammar = null;
        if (anElement instanceof IResource) {
            try {
                if (((IResource)anElement).exists()) {
	                grammar = ((IResource)anElement).getPersistentProperty(
                            AntxrBuilder.GRAMMAR_ECLIPSE_PROPERTY);
                } else {
	                grammar = "(resource not found)";
                }
            } catch (CoreException e) {
                AntxrUIPlugin.log(e);
            }
        }
        return grammar;
    }
}
