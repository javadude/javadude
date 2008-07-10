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

public class ParserTokenEvent extends Event {
	private static final long serialVersionUID = 1L;
	private int value;
	private int amount;
	public static int LA=0;
	public static int CONSUME=1;


	public ParserTokenEvent(Object source) {
		super(source);
	}
	public ParserTokenEvent(Object source, int type,
	                        int amount, int value) {
		super(source);
		setValues(type,amount,value);
	}
	public int getAmount() {
		return amount;
	}
	public int getValue() {
		return value;
	}
	void setAmount(int amount) {
		this.amount = amount;
	}
	void setValue(int value) {
		this.value = value;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int type, int amount, int value) {
		super.setValues(type);
		setAmount(amount);
		setValue(value);
	}
	@Override
    public String toString() {
		if (getType()==LA) {
            return "ParserTokenEvent [LA," + getAmount() + "," +
			       getValue() + "]";
        }
		return "ParserTokenEvent [consume,1," +
		       getValue() + "]";
	}
}
