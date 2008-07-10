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

public class ParserMatchEvent extends GuessingEvent {
	private static final long serialVersionUID = 1L;
	// NOTE: for a mismatch on type STRING, the "text" is used as the lookahead
	//       value.  Normally "value" is this
	public static int TOKEN=0;
	public static int BITSET=1;
	public static int CHAR=2;
	public static int CHAR_BITSET=3;
	public static int STRING=4;
	public static int CHAR_RANGE=5;
	private boolean inverse;
	private boolean matched;
	private Object target;
	private int value;
	private String text;


	public ParserMatchEvent(Object source) {
		super(source);
	}
	public ParserMatchEvent(Object source, int type,
	                        int value, Object target, String text, int guessing,
	                        boolean inverse, boolean matched) {
		super(source);
		setValues(type,value,target,text,guessing,inverse,matched);
	}
	public Object getTarget() {
		return target;
	}
	public String getText() {
		return text;
	}
	public int getValue() {
		return value;
	}
	public boolean isInverse() {
		return inverse;
	}
	public boolean isMatched() {
		return matched;
	}
	void setInverse(boolean inverse) {
		this.inverse = inverse;
	}
	void setMatched(boolean matched) {
		this.matched = matched;
	}
	void setTarget(Object target) {
		this.target = target;
	}
	void setText(String text) {
		this.text = text;
	}
	void setValue(int value) {
		this.value = value;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int type, int value, Object target, String text, int guessing, boolean inverse, boolean matched) {
		super.setValues(type, guessing);
		setValue(value);
		setTarget(target);
		setInverse(inverse);
		setMatched(matched);
		setText(text);
	}
	@Override
    public String toString() {
		return "ParserMatchEvent [" +
		       (isMatched()?"ok,":"bad,") +
		       (isInverse()?"NOT ":"") +
		       (getType()==TOKEN?"token,":"bitset,") +
		       getValue() + "," + getTarget() + "," + getGuessing() + "]";
	}
}
