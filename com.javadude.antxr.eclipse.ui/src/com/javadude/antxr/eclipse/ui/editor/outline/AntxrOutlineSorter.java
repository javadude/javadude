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

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.javadude.antxr.eclipse.core.parser.Block;
import com.javadude.antxr.eclipse.core.parser.Grammar;

/**
 * Sorts entries in the outline view
 */
public class AntxrOutlineSorter extends ViewerSorter {

	/** block in the outline */
    public static final int BLOCK = 0;
	/** grammar in the outline */
    public static final int GRAMMAR = 1;
	/** rule in the outline */
    public static final int RULE = 2;

	/** {@inheritDoc} */
	public int category(Object anElement) {
	    int category;
	    if (anElement instanceof Block) {
	        category = AntxrOutlineSorter.BLOCK;
	    } else if (anElement instanceof Grammar) {
	        category = AntxrOutlineSorter.GRAMMAR;
	    } else {
	        category = AntxrOutlineSorter.RULE;
	    }
	    return category;
	}

	/** {@inheritDoc} */
 	public int compare(Viewer aViewer, Object anObject1, Object anObject2) {
 	    int compare;
		int cat1 = category(anObject1);
		int cat2 = category(anObject2);
		if (cat1 != cat2 || cat1 == AntxrOutlineSorter.BLOCK) {
		    compare = 0;
		} else {
			ILabelProvider lprov = (ILabelProvider)
								  ((ContentViewer)aViewer).getLabelProvider();
			compare = collator.compare(lprov.getText(anObject1),
									   lprov.getText(anObject2));
		}
		return compare;
	}
}
