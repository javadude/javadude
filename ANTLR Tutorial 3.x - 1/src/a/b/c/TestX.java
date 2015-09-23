/*******************************************************************************
 * Copyright (c) 2009 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package a.b.c;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import a.b.c.TextFilterGrammarParser.content_return;

public class TestX {
	public static void main(String[] args) throws RecognitionException {
		test("foo");
		test("not foo");
		test("not not");
		test("not not and not or and not and");
		test("not not and not and not and");
	}
	public static void test(String expr) throws RecognitionException {
		System.out.println(expr);
		CharStream stream = new ANTLRStringStream(expr);
		TextFilterGrammarLexer lexer = new TextFilterGrammarLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		TextFilterGrammarParser parser = new TextFilterGrammarParser(tokenStream);
		content_return content = parser.content();
		System.out.println(content.tree.toStringTree());
		System.out.println("ok");
	}
}
