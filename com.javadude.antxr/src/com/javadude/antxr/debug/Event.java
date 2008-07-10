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

import java.util.EventObject;

public abstract class Event extends EventObject {
	private static final long serialVersionUID = 1L;
	private int type;


	public Event(Object source) {
		super(source);
	}
	public Event(Object source, int type) {
		super(source);
		setType(type);
	}
	public int getType() {
		return type;
	}
	void setType(int type) {
		this.type = type;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int type) {
		setType(type);
	}
}
