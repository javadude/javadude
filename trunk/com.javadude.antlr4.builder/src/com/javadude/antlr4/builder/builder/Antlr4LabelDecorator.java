package com.javadude.antlr4.builder.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.javadude.antlr4.builder.Activator;

/**
 * Decorates the text for generated files
 */
public class Antlr4LabelDecorator extends LabelProvider implements ILabelDecorator {
    public void dispose()  {
        super.dispose();
    }

    public Image decorateImage(Image anImage, Object anElement)  {
        return anImage;
    }

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
            if (Activator.getUtil().hasNature((IProject) anElement, Antlr4Nature.NATURE_ID)) {
                aText += "  <antlr4>";
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
                            Antlr4Builder.SOURCE_GRAMMAR_PROPERTY);
                } else {
	                grammar = "(grammar property not found)";
                }
            } catch (CoreException e) {
                Activator.getUtil().error(200, "Cannot decorate " + anElement, e);
            }
        }
        return grammar;
    }
}
