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

import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.MarkerAnnotation;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

/**
 * Determines marker for the given line and formates the according message.
 */
public class AntxrAnnotationHover implements IAnnotationHover {

	/**
	 * @see org.eclipse.jface.text.source.IAnnotationHover#getHoverInfo(org.eclipse.jface.text.source.ISourceViewer, int)
	 */
	public String getHoverInfo(ISourceViewer aViewer, int aLine) {
		String info = null;
		IMarker marker = getMarkerForLine(aViewer, aLine);
		if (marker != null) {
			String message = marker.getAttribute(IMarker.MESSAGE, (String)null);
			if (message != null && message.trim().length() > 0) {
				info = message.trim();
			}
		}
		return info;
	}

	/**
	 * Returns one marker which includes the ruler's line of activity.
	 * @param aViewer the viewer
	 * @param aLine the current line
	 * @return the marker
	 */
	protected IMarker getMarkerForLine(ISourceViewer aViewer, int aLine) {
		IMarker marker = null;
		IAnnotationModel model = aViewer.getAnnotationModel();
		if (model != null) {
			Iterator e = model.getAnnotationIterator();
			while (e.hasNext()) {
				Object o = e.next();
				if (o instanceof MarkerAnnotation) {
					MarkerAnnotation a = (MarkerAnnotation)o;
					if (compareRulerLine(model.getPosition(a),
										 aViewer.getDocument(), aLine) != 0) {
						marker = a.getMarker();
					}
				}
			}
		}
		return marker;
	}

	/**
	 * Returns distance of given line to specified position (1 = same line,
	 * 2 = included in given position, 0 = not related).
	 * @param aPosition the position in the document
	 * @param aDocument the document
	 * @param aLine the line
	 * @return the distance
	 */
	protected int compareRulerLine(Position aPosition, IDocument aDocument,
									int aLine) {
		int distance = 0;
		if (aPosition.getOffset() > -1 && aPosition.getLength() > -1) {
			try {
				int markerLine = aDocument.getLineOfOffset(
														aPosition.getOffset());
				if (aLine == markerLine) {
					distance = 1;
				} else if (markerLine <= aLine && aLine <=
							  aDocument.getLineOfOffset(aPosition.getOffset() +
													  aPosition.getLength())) {
					distance = 2;
				}
			} catch (BadLocationException e) {
				AntxrUIPlugin.log(e);
			}
		}
		return distance;
	}
}
