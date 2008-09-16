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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.swt.widgets.Display;

import com.javadude.antxr.DefaultFileLineFormatter;
import com.javadude.antxr.FileLineFormatter;
import com.javadude.antxr.eclipse.core.parser.AntxrLexer;
import com.javadude.antxr.eclipse.core.parser.AntxrParser;
import com.javadude.antxr.eclipse.core.parser.Hierarchy;
import com.javadude.antxr.eclipse.core.parser.ISegment;
import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

/**
 * Reconciler strategy which parses the whole editor's content (a ANTXR
 * grammar) on a document change.
 */
public class AntxrReconcilingStrategy implements IReconcilingStrategy {
	private AntxrEditor fEditor;
    private Hierarchy fHierarchy;
	private String fError;

	/**
	 * Create an instance
	 * @param anEditor the editor to reconcile
	 */
	public AntxrReconcilingStrategy(AntxrEditor anEditor) {
		fEditor = anEditor;
		fHierarchy = new Hierarchy("<empty>");
	}

	public void setDocument(IDocument aDocument) {
		parse();
	}

	public void reconcile(DirtyRegion aDirtyRegion, IRegion aRegion) {
		parse();
	}

	public void reconcile(IRegion aPartition) {
		parse();
	}

	private void parse() {
		FileLineFormatter.setFormatter(new DefaultFileLineFormatter());
		Reader reader = new StringReader(fEditor.getDocument().get());
		AntxrParser parser = new AntxrParser(new AntxrLexer(reader));
    	Hierarchy hierarchy = null;
        try {
			hierarchy = parser.grammarFile(null);

			// If exception occured then display error message
			Exception e = hierarchy.getException();
			if (e != null) {
				fError = e.toString();
			} else {
				fError = "";
			}
		} catch (Exception e) {
			fError = "";
			AntxrUIPlugin.log(e);
        } finally {
        	try {
				reader.close();
        	} catch (IOException e) {
        		AntxrUIPlugin.log(e);
        	}
        }

		// Replace saved hierarchy with the new parse tree
		synchronized (this) {
        	if (hierarchy != null) {
				fHierarchy = hierarchy;
        	} else {
        		fHierarchy = new Hierarchy("<empty>");
        	}
		}

		// Update outline view and display error message in status line
		Display.getDefault().syncExec(new Runnable() {
			public void run(){
				fEditor.updateOutlinePage();
				fEditor.displayErrorMessage(fError);
			}
		});
	}

	/**
	 * Returns root elements of current parse tree.
	 * @return the root elements
	 */
    public Object[] getRootElements() {
        return fHierarchy.getChildren();
    }

	/**
	 * Returns root node of current parse tree.
	 * @return the segment
	 */
    public ISegment getRootSegment() {
        return fHierarchy;
    }
}
