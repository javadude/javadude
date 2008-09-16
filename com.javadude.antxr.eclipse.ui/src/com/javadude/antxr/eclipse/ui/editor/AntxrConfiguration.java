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
package com.javadude.antxr.eclipse.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.ui.javaeditor.IClassFileEditorInput;
import org.eclipse.jdt.internal.ui.javaeditor.ICompilationUnitDocumentProvider;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

import com.javadude.antxr.eclipse.ui.AntxrColorProvider;
import com.javadude.antxr.eclipse.ui.IColorConstants;
import com.javadude.antxr.eclipse.ui.editor.text.AutoIndentStrategy;
import com.javadude.antxr.eclipse.ui.editor.text.DoubleClickStrategy;
import com.javadude.antxr.eclipse.ui.editor.text.NonRuleBasedDamagerRepairer;
import com.javadude.antxr.eclipse.ui.editor.text.PartitionScanner;

/**
 * Configure an editor for ANTXR grammars
 */
@SuppressWarnings("restriction")
public class AntxrConfiguration extends TextSourceViewerConfiguration {

    private final AbstractDecoratedTextEditor fEditor;

    /**
     * Create the instance
     * @param anEditor the target editor
     */
    public AntxrConfiguration(AbstractDecoratedTextEditor anEditor) {
        fEditor = anEditor;
    }

    /** {@inheritDoc} */
    public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
        if (contentType.equals(IDocument.DEFAULT_CONTENT_TYPE)) {
            return new IAutoEditStrategy[] {new AutoIndentStrategy()};
        }
        return new IAutoEditStrategy[] {new DefaultIndentLineAutoEditStrategy()};
    }

    /** {@inheritDoc} */
    public ITextDoubleClickStrategy getDoubleClickStrategy(
                                  ISourceViewer aViewer, String aContentType) {
        return new DoubleClickStrategy();
    }

    /** {@inheritDoc} */
    public String[] getConfiguredContentTypes(ISourceViewer aSourceViewer) {
        return PartitionScanner.PARTITION_TYPES;
    }

    /** {@inheritDoc} */
    public IPresentationReconciler getPresentationReconciler(
                                                       ISourceViewer aViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();

        AntxrColorProvider cp = EditorEnvironment.getColorProvider();

        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
                                           EditorEnvironment.getCodeScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(
                       new TextAttribute(cp.getColor(IColorConstants.STRING)));
        reconciler.setDamager(ndr, PartitionScanner.STRING);
        reconciler.setRepairer(ndr, PartitionScanner.STRING);

        ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(
                                        cp.getColor(IColorConstants.COMMENT)));
        reconciler.setDamager(ndr, PartitionScanner.SINGLE_LINE_COMMENT);
        reconciler.setRepairer(ndr, PartitionScanner.SINGLE_LINE_COMMENT);

        ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(
                                        cp.getColor(IColorConstants.COMMENT)));
        reconciler.setDamager(ndr, PartitionScanner.MULTI_LINE_COMMENT);
        reconciler.setRepairer(ndr, PartitionScanner.MULTI_LINE_COMMENT);

        ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(
                                    cp.getColor(IColorConstants.DOC_COMMENT)));
        reconciler.setDamager(ndr, PartitionScanner.JAVA_DOC);
        reconciler.setRepairer(ndr, PartitionScanner.JAVA_DOC);

        return reconciler;
    }

    /** {@inheritDoc} */
    public IReconciler getReconciler(ISourceViewer aSourceViewer) {
        IReconciler reconciler;
        if (fEditor != null && fEditor instanceof AntxrEditor) {
            reconciler = new MonoReconciler(((AntxrEditor)
                                     fEditor).getReconcilingStrategy(), false);
        } else {
            reconciler = null;
        }
        return reconciler;
    }

    /** {@inheritDoc} */
    public IContentAssistant getContentAssistant(ISourceViewer aSourceViewer) {
        ContentAssistant assistant;
        if (fEditor != null && fEditor instanceof AntxrEditor) {
            assistant = new ContentAssistant();
            assistant.setContentAssistProcessor(new AntxrCompletionProcessor(
                        (AntxrEditor)fEditor), IDocument.DEFAULT_CONTENT_TYPE);
            assistant.enableAutoInsert(true);
        } else {
            assistant = null;
        }
        return assistant;
    }

    /** {@inheritDoc} */
    public IAnnotationHover getAnnotationHover(ISourceViewer aSourceViewer) {
        return new AntxrAnnotationHover();
    }

    /** {@inheritDoc} */
    public ITextHover getTextHover(ISourceViewer aSourceViewer,
                                    String aContentType) {
        ITextHover hover;
        if (aContentType.equals(IDocument.DEFAULT_CONTENT_TYPE) &&
                           fEditor != null && fEditor instanceof AntxrEditor) {
            hover = new AntxrTextHover((AntxrEditor)fEditor);
        } else {
            hover = null;
        }
        return hover;
    }

    /** {@inheritDoc} */
    public String[] getDefaultPrefixes(ISourceViewer aSourceViewer,
                                        String aContentType) {
        return new String[] { "//", "" };
    }

    // copied from JavaSourceViewerConfiguration and tweaked
    /** {@inheritDoc} */
    public String[] getIndentPrefixes(ISourceViewer sourceViewer,
            String contentType) {
        List<String> prefixes = new ArrayList<String>();
        int tabWidth=0;
        boolean useSpaces;

        IJavaProject project = getProject();
        String tabSize;
        if (project != null) {
            tabSize = project.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, true);
            useSpaces = JavaCore.SPACE.equals(project.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, true));
        }
        else {
            tabSize = JavaCore.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE);
            useSpaces = JavaCore.SPACE.equals(JavaCore.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR));
        }
        try {
            tabWidth = Integer.parseInt(tabSize);
        }
        catch (NumberFormatException e) {
            tabWidth = 4;
        }

        // prefix[0] is either '\t' or ' ' x tabWidth, depending on useSpaces
        for (int i= 0; i <= tabWidth; i++) {
            StringBuffer prefix= new StringBuffer();

            if (useSpaces) {
                for (int j= 0; j + i < tabWidth; j++) {
	                prefix.append(' ');
                }

                if (i != 0) {
	                prefix.append('\t');
                }
            } else {
                for (int j= 0; j < i; j++) {
	                prefix.append(' ');
                }

                if (i != tabWidth) {
	                prefix.append('\t');
                }
            }

            prefixes.add(prefix.toString());
        }

        prefixes.add(""); //$NON-NLS-1$

        return prefixes.toArray(new String[prefixes.size()]);
    }

    // copied from JavaSourceViewerConfiguration and tweaked
    private IJavaProject getProject() {
        if (fEditor == null) {
	        return null;
        }

        IJavaElement element= null;
        IEditorInput input= fEditor.getEditorInput();
        IDocumentProvider provider= fEditor.getDocumentProvider();
        if (provider instanceof ICompilationUnitDocumentProvider) {
            ICompilationUnitDocumentProvider cudp= (ICompilationUnitDocumentProvider) provider;
            element= cudp.getWorkingCopy(input);
        } else if (input instanceof IClassFileEditorInput) {
            IClassFileEditorInput cfei= (IClassFileEditorInput) input;
            element= cfei.getClassFile();
        }

        if (element == null) {
	        return null;
        }

        return element.getJavaProject();
    }
}
