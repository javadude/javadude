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

import com.javadude.antxr.eclipse.core.parser.Block;
import com.javadude.antxr.eclipse.core.parser.Grammar;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

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
	        category = BLOCK;
	    } else if (anElement instanceof Grammar) {
	        category = GRAMMAR;
	    } else {
	        category = RULE;
	    }
	    return category;
	}
    
	/** {@inheritDoc} */
 	public int compare(Viewer aViewer, Object anObject1, Object anObject2) {
 	    int compare;
		int cat1 = category(anObject1);
		int cat2 = category(anObject2);
		if (cat1 != cat2 || cat1 == BLOCK) {
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
