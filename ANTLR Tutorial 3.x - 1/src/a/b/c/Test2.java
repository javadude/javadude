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

public class Test2 {
	public static void main(String[] args) throws RecognitionException {
		CharStream stream =
			new ANTLRStringStream("3  * (2 + 4) * 3");
		Sample2Lexer lexer = new Sample2Lexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		Sample2Parser parser = new Sample2Parser(tokenStream);
		int result = parser.evaluator();
		System.out.println("ok - result is " + result);
	}
}
