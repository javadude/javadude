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

import com.javadude.antxr.eclipse.core.AntxrNature;
import com.javadude.antxr.eclipse.core.builder.AntxrBuilder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

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
