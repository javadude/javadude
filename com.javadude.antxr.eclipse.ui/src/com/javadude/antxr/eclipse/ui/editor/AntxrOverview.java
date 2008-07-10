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
package com.javadude.antxr.eclipse.ui.editor;

import java.io.Reader;

import com.javadude.antxr.eclipse.core.parser.AntxrOverviewParser;
import com.javadude.antxr.eclipse.ui.editor.text.PartitionScanner;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

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
