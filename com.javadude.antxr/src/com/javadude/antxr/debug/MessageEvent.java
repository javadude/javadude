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

public class MessageEvent extends Event {
	private static final long serialVersionUID = 1L;
	private String text;
	public static int WARNING = 0;
	public static int ERROR = 1;


	public MessageEvent(Object source) {
		super(source);
	}
	public MessageEvent(Object source, int type, String text) {
		super(source);
		setValues(type,text);
	}
	public String getText() {
		return text;
	}
	void setText(String text) {
		this.text = text;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int type, String text) {
		super.setValues(type);
		setText(text);
	}
	@Override
    public String toString() {
		return "ParserMessageEvent [" +
		       (getType()==WARNING?"warning,":"error,") +
		       getText() + "]";
	}
}
