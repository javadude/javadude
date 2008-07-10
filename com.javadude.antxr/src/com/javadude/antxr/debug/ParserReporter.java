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

public class ParserReporter extends Tracer implements ParserListener {


	public void parserConsume(ParserTokenEvent e) {
		System.out.println(indent+e);
	}
	public void parserLA(ParserTokenEvent e) {
		System.out.println(indent+e);
	}
	public void parserMatch(ParserMatchEvent e) {
		System.out.println(indent+e);
	}
	public void parserMatchNot(ParserMatchEvent e) {
		System.out.println(indent+e);
	}
	public void parserMismatch(ParserMatchEvent e) {
		System.out.println(indent+e);
	}
	public void parserMismatchNot(ParserMatchEvent e) {
		System.out.println(indent+e);
	}
	public void reportError(MessageEvent e) {
		System.out.println(indent+e);
	}
	public void reportWarning(MessageEvent e) {
		System.out.println(indent+e);
	}
	public void semanticPredicateEvaluated(SemanticPredicateEvent e) {
		System.out.println(indent+e);
	}
	public void syntacticPredicateFailed(SyntacticPredicateEvent e) {
		System.out.println(indent+e);
	}
	public void syntacticPredicateStarted(SyntacticPredicateEvent e) {
		System.out.println(indent+e);
	}
	public void syntacticPredicateSucceeded(SyntacticPredicateEvent e) {
		System.out.println(indent+e);
	}
}
