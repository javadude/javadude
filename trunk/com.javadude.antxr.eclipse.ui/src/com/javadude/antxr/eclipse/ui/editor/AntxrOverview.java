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

import java.io.Reader;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.javadude.antxr.eclipse.core.parser.AntxrOverviewParser;
import com.javadude.antxr.eclipse.ui.editor.text.PartitionScanner;

/**
 * Read-only text editor displaying ANTXR grammar overview (only terminals and
 * non-terminals; no actions, comments and type information).
 */
public class AntxrOverview extends SourceViewer {

    /**
     * Create an instance of AntxrOverview.
     * @param aParent
     */
    public AntxrOverview(Composite aParent) {
        super(aParent, null, SWT.V_SCROLL | SWT.H_SCROLL);

        EditorEnvironment.connect();

        // Configure source viewer
        configure(new AntxrConfiguration(null));
        getTextWidget().setEditable(false);
        getTextWidget().setFont(JFaceResources.getFontRegistry().get(
                                                    JFaceResources.TEXT_FONT));
        // Create document with attached partitioner
        IDocument document = new Document();
        IDocumentPartitioner partitioner = new FastPartitioner(
                     new PartitionScanner(), PartitionScanner.PARTITION_TYPES);
        partitioner.connect(document);
        document.setDocumentPartitioner(partitioner);

        // Attach document to source viewer AFTER configuring source viewer
        // Otherwise syntax highlighting will not work
        setDocument(document);
    }

    protected void handleDispose() {
        EditorEnvironment.disconnect();
    }

    /**
     * @return is save on close needed?
     */
    public boolean isSaveOnCloseNeeded() {
        return false;
    }

    /**
     * Parses given reader for an ANTXR grammar overview and sets this source
     * viewer's document to the parsed text.
     * @param aReader
     */
    public void show(Reader aReader) {
        getDocument().set(new AntxrOverviewParser().parse(aReader));
    }
}
