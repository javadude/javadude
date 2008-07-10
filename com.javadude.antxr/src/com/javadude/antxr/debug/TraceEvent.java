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

public class TraceEvent extends GuessingEvent {
	private static final long serialVersionUID = 1L;
	private int ruleNum;
	private int data;
	public static int ENTER=0;
	public static int EXIT=1;
	public static int DONE_PARSING=2;


	public TraceEvent(Object source) {
		super(source);
	}
	public TraceEvent(Object source, int type, int ruleNum, int guessing, int data) {
		super(source);
		setValues(type, ruleNum, guessing, data);
	}
	public int getData() {
		return data;
	}
	public int getRuleNum() {
		return ruleNum;
	}
	void setData(int data) {
		this.data = data;
	}
	void setRuleNum(int ruleNum) {
		this.ruleNum = ruleNum;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	 void setValues(int type, int ruleNum, int guessing, int data) {
		super.setValues(type, guessing);
		setRuleNum(ruleNum);
		setData(data);
	}
	@Override
    public String toString() {
		return "ParserTraceEvent [" +
		       (getType()==ENTER?"enter,":"exit,") +
		       getRuleNum() + "," + getGuessing() +"]";
	}
}
