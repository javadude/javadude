/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
 *   Scott Stanchfield - Modifications for XML Parsing
 *******************************************************************************/
package com.javadude.antxr;

public class DefaultFileLineFormatter extends FileLineFormatter {
    @Override
    public String getFormatString(String fileName, int line, int column) {
        StringBuffer buf = new StringBuffer();

        if (fileName != null) {
            buf.append(fileName + ":");
        }

        if (line != -1) {
            if (fileName == null) {
                buf.append("line ");
            }

            buf.append(line);

            if (column != -1) {
                buf.append(":" + column);
            }

            buf.append(":");
        }

        buf.append(" ");

        return buf.toString();
    }
}
