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

public class Test1 {
	public static void main(String[] args) throws RecognitionException {
		CharStream stream =
			new ANTLRStringStream("program XLSample1 =\r\n" +
					"/*\r\n" +
					"	constant one : Integer := 1;\r\n" +
					"	constant two : Integer := 2 * 3;\r\n" +
					"	var x,         y, z : Integer := 42;\r\n" +
					"*/\r\n" +
					"\r\n" +
					"	procedure foo() =\r\n" +
					"		var x : Integer := 2;\r\n" +
					"	begin\r\n" +
					"	end foo.\r\n" +
					"	procedure fee(y : Integer) =\r\n" +
					"		var x : Integer := 2;\r\n" +
					"	begin\r\n" +
					"	end fee.\r\n" +
					"	function fie(y : Integer) : Integer =\r\n" +
					"		var x : Integer := 2;\r\n" +
					"	begin\r\n" +
					"		return y;\r\n" +
					"	end fie.\r\n" +
					"begin\r\n" +
					"end XLSample1.");
		SampleLexer lexer = new SampleLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		SampleParser parser = new SampleParser(tokenStream);
		parser.program();
		System.out.println("ok");
	}
}
