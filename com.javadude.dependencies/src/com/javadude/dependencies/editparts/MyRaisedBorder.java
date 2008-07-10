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
package com.javadude.dependencies.editparts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * I copied this from the GEF framework. They use it for palletes
 */
public class MyRaisedBorder extends MarginBorder {

	private static final Insets DEFAULT_INSETS = new Insets(5, 5, 5, 5);

	/**
	 * @see org.eclipse.draw2d.Border#getInsets(IFigure)
	 */
	@Override
    public Insets getInsets(IFigure figure) {
		return insets;
	}

	public MyRaisedBorder() {
		this(DEFAULT_INSETS);
	}

	public MyRaisedBorder(Insets insets) {
		super(insets);
	}

	public MyRaisedBorder(int t, int l, int b, int r) {
		super(t, l, b, r);
	}

	@Override
    public boolean isOpaque() {
		return true;
	}

	/**
	 * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
	 */
	@Override
    public void paint(IFigure figure, Graphics g, Insets argInsets) {
		g.setLineStyle(Graphics.LINE_SOLID);
		g.setLineWidth(1);
		g.setForegroundColor(ColorConstants.buttonLightest);
		Rectangle r = getPaintRectangle(figure, argInsets);
		r.resize(-1, -1);
		g.drawLine(r.x, r.y, r.right(), r.y);
		g.drawLine(r.x, r.y, r.x, r.bottom());
		g.setForegroundColor(ColorConstants.buttonDarker);
		g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
		g.drawLine(r.right(), r.y, r.right(), r.bottom());
	}
}
