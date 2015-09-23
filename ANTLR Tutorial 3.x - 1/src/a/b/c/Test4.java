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
import org.antlr.runtime.tree.CommonTreeNodeStream;

import a.b.c.Sample4Parser.evaluator_return;

public class Test4 {
	public static void main(String[] args) throws RecognitionException {
		CharStream stream =
			new ANTLRStringStream("x := 4; y := 2 + 3; 3  * (-x + y) * 3");
		Sample4Lexer lexer = new Sample4Lexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		Sample4Parser parser = new Sample4Parser(tokenStream);

//		parser.setDebugListener(new TraceDebugEventListener(new CommonTreeAdaptor()));

		evaluator_return evaluator = parser.evaluator();
		System.out.println(evaluator.tree.toStringTree());
		CommonTreeNodeStream nodeStream = new CommonTreeNodeStream(evaluator.tree);
		EvaluatorWalker walker = new EvaluatorWalker(nodeStream);
		int result = walker.evaluator();
		System.out.println("ok - result = " + result);
	}
}
