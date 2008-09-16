/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.dependencies.editparts;

import org.eclipse.draw2d.AbstractBorder;
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
		this(MyRaisedBorder.DEFAULT_INSETS);
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
		Rectangle r = AbstractBorder.getPaintRectangle(figure, argInsets);
		r.resize(-1, -1);
		g.drawLine(r.x, r.y, r.right(), r.y);
		g.drawLine(r.x, r.y, r.x, r.bottom());
		g.setForegroundColor(ColorConstants.buttonDarker);
		g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
		g.drawLine(r.right(), r.y, r.right(), r.bottom());
	}
}
