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
 *
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *   Ric Klaren <klaren@cs.utwente.nl>
 *******************************************************************************/
package com.javadude.antxr.debug;

public class InputBufferEvent extends Event {
	private static final long serialVersionUID = 1L;
	char c;
	int lookaheadAmount; // amount of lookahead
	public static final int CONSUME = 0;
	public static final int LA = 1;
	public static final int MARK = 2;
	public static final int REWIND = 3;


/**
 * CharBufferEvent constructor comment.
 * @param source java.lang.Object
 */
public InputBufferEvent(Object source) {
	super(source);
}
/**
 * CharBufferEvent constructor comment.
 * @param source java.lang.Object
 */
public InputBufferEvent(Object source, int type, char c, int lookaheadAmount) {
	super(source);
	setValues(type, c, lookaheadAmount);
}
	public char getChar() {
		return c;
	}
	public int getLookaheadAmount() {
		return lookaheadAmount;
	}
	void setChar(char c) {
		this.c = c;
	}
	void setLookaheadAmount(int la) {
		this.lookaheadAmount = la;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int type, char c, int la) {
		super.setValues(type);
		setChar(c);
		setLookaheadAmount(la);
	}
	@Override
    public String toString() {
		return "CharBufferEvent [" +
			(getType()==CONSUME?"CONSUME, ":"LA, ")+
		getChar() + "," + getLookaheadAmount() + "]";
	}
}
